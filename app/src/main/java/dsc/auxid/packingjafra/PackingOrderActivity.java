package dsc.auxid.packingjafra;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dsc.auxid.packingjafra.util.PackDetailAdapter;
import dsc.auxid.packingjafra.util.PackDetail;
import dsc.auxid.packingjafra.util.SessionManager;
import dsc.auxid.packingjafra.util.URLs;

public class PackingOrderActivity extends AppCompatActivity
implements  ScanWaveFragment.OnFragmentInteractionListener, InputOrderFragment.OnFragmentInteractionListener{

    public Fragment fragment;
    public FragmentTransaction ft;

    private String shipmentId, launchNum;
    private PackDetailAdapter customAdapter;
    private ListView listPacking;
    private Button btnConfirm, btnCancel;

    private TextView txtWave, txtDO, txtCity, txtTotalQty, txtPriority,packQty;

    private List<PackDetail> packDetails = new ArrayList<PackDetail>();
    private int totalQty = 0;
    private SessionManager _sessionManager;
    private TextView txtCustomerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_order);
        _sessionManager= new SessionManager(getApplicationContext());

        shipmentId = getIntent().getStringExtra("shipment_id");
        launchNum = getIntent().getStringExtra("launch_num");

        listPacking = findViewById(R.id.orderList);

        txtWave = findViewById(R.id.txtWave);
        txtDO = findViewById(R.id.txtDO);
        txtCustomerName = findViewById(R.id.txtCustomerName);
        txtCity = findViewById(R.id.txtCity);
        txtTotalQty = findViewById(R.id.txtTotalQty);
        txtPriority = findViewById(R.id.txtPriority);
        packQty = findViewById(R.id.packQty);

        final Map<String, String> mParams = new ArrayMap<String, String>();
        mParams.put("launch_num",getIntent().getStringExtra("launch_num"));
        mParams.put("do_number",getIntent().getStringExtra("shipment_id"));

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
            _sessionManager.getBaseUrl() + URLs.getShipmentDetail,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objResponse = new JSONObject(response);
                            if(objResponse.getJSONObject("meta").getInt("code")==200) {

                                // set shipment info
                                //objResponse.getJSONObject("data").get String("shipment_id")
                                txtWave.setText(objResponse.getJSONObject("data").getString("launch_num"));
                                txtDO.setText(objResponse.getJSONObject("data").getString("shipment_id"));
                                txtCity.setText(objResponse.getJSONObject("data").getString("ship_to_city"));
                                txtTotalQty.setText(objResponse.getJSONObject("data").getString("total_qty"));
                                txtPriority.setText(objResponse.getJSONObject("data").getString("priority"));
                                txtCustomerName.setText(objResponse.getJSONObject("data").getString("ship_to_name"));


                                totalQty = objResponse.getJSONObject("data").getInt("total_qty");

                                //set detail
                                for (int i=0; i< objResponse.getJSONObject("data").getJSONArray("items").length();i++) {

                                    JSONObject item = objResponse.getJSONObject("data").getJSONArray("items").getJSONObject(i);

                                    PackDetail pd = new PackDetail();
                                    pd.setSequence(i+1);
                                    pd.setItemCode(item.getString("item"));
                                    pd.setItemDesc(item.getString("item_desc"));
                                    pd.setItemQuantity((item.getInt("item_qty")));

                                    packDetails.add(pd);
                                }

                                customAdapter = new PackDetailAdapter(getApplicationContext(), packDetails);
                                listPacking.setAdapter(customAdapter);
                                customAdapter.notifyDataSetChanged();

                                listPacking.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TAG", "Position: " + position);

                                        PackDetail pack = packDetails.get(position);
                                        pack.setPack(!pack.isPack());

                                        int totalPacked = 0;
                                        for (PackDetail pd: packDetails) {
                                            if(pd.isPack()){
                                                totalPacked = totalPacked + pd.getItemQuantity();
                                            }
                                        }
                                        packQty.setText(Integer.toString(totalPacked));
                                        customAdapter.notifyDataSetChanged();
                                        btnConfirm.setEnabled((totalQty == totalPacked && totalPacked > 0));
                                    }
                                });

                            } else if(objResponse.getJSONObject("meta").getInt("code")==404) {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return super.getHeaders();
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                return mParams;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);


        btnConfirm = findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CameraConfirmActivity.class);
                intent.putExtra("launch_num", launchNum);
                intent.putExtra("shipment_id", shipmentId);

                startActivity(intent);
            }
        });

        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(PackingOrderActivity.this, MainScreenActivity.class);
                intent.putExtra("shipment_id", shipmentId);
                intent.putExtra("wave_number", launchNum);

                finish();
                startActivity(intent);

                /*
                fragment = new InputOrderFragment();
                fragment.setArguments(bundle);
                ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.addToBackStack(null);
                ft.commit();
                */

            }
        });
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
