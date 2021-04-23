package dsc.auxid.packingjafra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.ArrayMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.Map;

import dsc.auxid.packingjafra.util.SessionManager;
import dsc.auxid.packingjafra.util.URLs;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ScanWaveFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScanWaveFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanWaveFragment extends Fragment {
    public View inflatedView;
    private SessionManager _sessionManager;

    private EditText txtWaveNumber;
    private ImageButton btnSetting;

    private Fragment fragment;
    public FragmentTransaction ft;

    private OnFragmentInteractionListener mListener;
    private ProgressDialog progressDialog;

    public ScanWaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        inflatedView = inflater.inflate(R.layout.fragment_scan_wave, container, false);
        _sessionManager  = new SessionManager(getContext());

        txtWaveNumber = inflatedView.findViewById(R.id.txtWaveNumber);
        txtWaveNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)
                        || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_TAB)){

                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setMessage("Loading Data, please wait...");
                    progressDialog.show();

                    final TextView tv = v;
                    final Map<String, String> mParams = new ArrayMap<String, String>();
                    mParams.put("wave_number",v.getText().toString().trim());

                    StringRequest stringRequest = new StringRequest(
                            Request.Method.POST,
                        _sessionManager.getBaseUrl() + URLs.getShipments,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        progressDialog.dismiss();
                                        JSONObject objResponse = new JSONObject(response);
                                        if(objResponse.getJSONObject("meta").getInt("code")==200) {

                                            Bundle bundle = new Bundle();
                                            bundle.putString("wave_number", tv.getText().toString().trim());

                                            fragment = new InputOrderFragment();
                                            fragment.setArguments(bundle);
                                            ft = getActivity().getSupportFragmentManager().beginTransaction();
                                            ft.replace(R.id.content_frame, fragment);
                                            ft.addToBackStack(null);
                                            ft.commit();
                                        } else {
                                            Toast.makeText(getContext(),"Wave Not Found",Toast.LENGTH_LONG).show();
                                            txtWaveNumber.setText("");
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

                    RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                    progressDialog.dismiss();

                }
                return true;
            }
        });

        btnSetting = inflatedView.findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = new SettingFragment();
                ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
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


