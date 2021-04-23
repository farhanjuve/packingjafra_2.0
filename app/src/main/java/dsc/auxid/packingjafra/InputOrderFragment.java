package dsc.auxid.packingjafra;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

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
import dsc.auxid.packingjafra.util.ShipmentAdapter;
import dsc.auxid.packingjafra.util.ShipmentInfo;
import dsc.auxid.packingjafra.util.URLs;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InputOrderFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InputOrderFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class InputOrderFragment extends Fragment {

    public Fragment fragment;
    public FragmentTransaction ft;

    private OnFragmentInteractionListener mListener;
    private PackDetailAdapter customAdapter;
    private View inflatedView;
    private ListView listShipments;
    private Button btnNext, btnCancel;
    private EditText txtShipmentNo;

    private String waveNumber;
    private int lastPosition = -1;

    private List<ShipmentInfo> shipments = new ArrayList<ShipmentInfo>();
    private ShipmentAdapter shipmentAdapter;
    private SessionManager _sessionManager;
    SearchView searchView;
    private String ShipmentID;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waveNumber = getArguments().getString("wave_number");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_scan_order, container, false);
        _sessionManager = new SessionManager(getContext());

        listShipments = (ListView) inflatedView.findViewById(R.id.listShipments);

        waveNumber = getArguments().getString("wave_number");

        txtShipmentNo = inflatedView.findViewById(R.id.txtOrderNo);
        txtShipmentNo.setFocusable(true);

        TextView textWaveNo = inflatedView.findViewById(R.id.textWaveNo);
        textWaveNo.setText(waveNumber);

        searchView = inflatedView.findViewById(R.id.searchView);
        //searchView.setText(waveNumber);

        btnCancel = inflatedView.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment fragment = new ScanWaveFragment();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
            }
        });

        btnNext = inflatedView.findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShipmentInfo shipment = null;

                for (ShipmentInfo shipmentInfo : shipments) {
                    if(shipmentInfo.getShipmentId().hashCode() == txtShipmentNo.getText().toString().hashCode()) {
                        shipment = shipmentInfo;
                        break;
                    }
                }

                if(shipment != null) {
                    Toast.makeText(getActivity(), txtShipmentNo.getText().toString(),
                            Toast.LENGTH_LONG).show();
                    //shipment = shipments.get(lastPosition);

                    Intent intent = new Intent(getActivity(), PackingOrderActivity.class);
                    //intent.putExtra("shipment_id", shipment.getShipmentId());
                    intent.putExtra("shipment_id", txtShipmentNo.getText().toString());
                    intent.putExtra("launch_num", waveNumber);
                    Log.d("TAG", "onClick: " + ShipmentID + "and :" + txtShipmentNo.getText().toString());
                    getActivity().finish();
                    startActivity(intent);
                } else if(shipment == null) {
                    Toast.makeText(getActivity(), "Order Number tidak boleh kosong",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        final Map<String, String> mParams = new ArrayMap<String, String>();
        mParams.put("wave_number",waveNumber);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                _sessionManager.getBaseUrl() + URLs.getShipments,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject objResponse = new JSONObject(response);
                            if(objResponse.getJSONObject("meta").getInt("code")==200) {
                                for (int i=0; i< objResponse.getJSONArray("data").length();i++) {

                                    JSONObject jsonShipment = objResponse.getJSONArray("data").getJSONObject(i);

                                    ShipmentInfo shipment  = new ShipmentInfo();
                                    shipment.setNo(i+1);
                                    shipment.setShipmentId(jsonShipment.getString("shipment_id"));
                                    shipment.setShipToName(jsonShipment.getString("ship_to_name"));
                                    shipment.setCity(jsonShipment.getString("ship_to_city"));
                                    shipment.setItem(jsonShipment.getString("item"));
                                    shipment.setQty(jsonShipment.getString("qty"));

                                    shipments.add(shipment);
                                }

                                shipmentAdapter = new ShipmentAdapter(getActivity(), shipments);
                                listShipments.setAdapter(shipmentAdapter);
                                shipmentAdapter.notifyDataSetChanged();

                                /*searchView.addTextChangedListener(new TextWatcher() {

                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                                        return;
                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                        shipmentAdapter.getFilter().filter(s);
                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {

                                    }
                                });
                                */
                                listShipments.setOnItemClickListener( new AdapterView.OnItemClickListener() {

                                    @Override
                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                        Log.d("TAG", "Position: " + String.valueOf(position));
                                        lastPosition = position;
                                        ShipmentID = shipments.get(position).getShipmentId();
                                        txtShipmentNo.setText(shipments.get(position).getShipmentId());
                                    }

                                });

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

        int socketTimeout = 60000;
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(shipments.contains(query)){
                    shipmentAdapter.getFilter().filter(query);
                }else{
                    Toast.makeText(getActivity(), "No Match found for :" + query,Toast.LENGTH_LONG).show();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //    adapter.getFilter().filter(newText);
                return false;
            }
        });

        return inflatedView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
