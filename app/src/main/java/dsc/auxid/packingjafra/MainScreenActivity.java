package dsc.auxid.packingjafra;

import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dsc.auxid.packingjafra.util.SessionManager;

public class MainScreenActivity extends AppCompatActivity
implements ScanWaveFragment.OnFragmentInteractionListener, InputOrderFragment.OnFragmentInteractionListener, SettingFragment.OnFragmentInteractionListener{
    public Fragment fragment;
    public FragmentTransaction ft;
    public Bundle bundle;
    SessionManager m_session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        String waveNumber = getIntent().getStringExtra("wave_number");
        String shipmentId = getIntent().getStringExtra("shipment_id");

        if(waveNumber != null && shipmentId != null ) {
            Bundle bundle = new Bundle();
            bundle.putString("wave_number", waveNumber);
            bundle.putString("shipment_id", shipmentId);

            fragment = new InputOrderFragment();
            fragment.setArguments(bundle);
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();

        } else if(waveNumber != null) {

            Bundle bundle = new Bundle();
            bundle.putString("wave_number", waveNumber);

            fragment = new InputOrderFragment();
            fragment.setArguments(bundle);
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();

        } else {
            fragment = new ScanWaveFragment();
            //fragment = new SettingFragment();
            ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
