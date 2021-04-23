package dsc.auxid.packingjafra;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import dsc.auxid.packingjafra.util.SessionManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SettingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
  // TODO: Rename parameter arguments, choose names that match
  // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public View inflatedView;

  private EditText txtDataServer, txtImageServer;
  private CheckBox chkUploadPicture;
  private Button btnSave, btnCancel;
  private  SessionManager _sessionManager;

  private Fragment fragment;
  public FragmentTransaction ft;

  public SettingFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment SettingFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static SettingFragment newInstance(String param1, String param2) {
    SettingFragment fragment = new SettingFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    inflatedView = inflater.inflate(R.layout.fragment_setting, container, false);
    _sessionManager = new SessionManager(getContext());


    txtDataServer = inflatedView.findViewById(R.id.txtServerData);
    txtImageServer = inflatedView.findViewById(R.id.txtServerImage);
    chkUploadPicture = inflatedView.findViewById(R.id.chkUploadImage);

    try {
      txtDataServer.setHint(_sessionManager.getBaseUrl());
      txtImageServer.setHint(_sessionManager.getImageUrl());
      chkUploadPicture.setChecked(_sessionManager.getIsUploadImage());

    } catch (Exception e) {

    }

    btnSave = inflatedView.findViewById(R.id.btnSaveSetting);
    btnCancel = inflatedView.findViewById(R.id.btnCancelSetting);

    btnSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        if(!txtDataServer.getText().toString().isEmpty()) {
          _sessionManager.setBaseUrl(txtDataServer.getText().toString());
        }
        if(!txtImageServer.getText().toString().isEmpty()) {
          _sessionManager.setImageUrl(txtImageServer.getText().toString());
        }

        _sessionManager.setUploadImage(chkUploadPicture.isChecked());

        fragment = new ScanWaveFragment();
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();
      }
    });

    btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        fragment = new ScanWaveFragment();
        ft = getActivity().getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
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
