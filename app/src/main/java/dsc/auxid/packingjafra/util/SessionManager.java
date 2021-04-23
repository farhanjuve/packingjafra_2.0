package dsc.auxid.packingjafra.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "DHL_Jafra_Packing";

    // Base URL (Server's IP Address)
    public static final String BASE_URL = "BASE_URL";

    public static final String IMAGE_URL = "IMAGE_URL";

    public static final boolean isUploadImage = true;

    // Anomaly status array
    public static final String ANOMALY_ARRAY = "";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    /**
     * Set Base URL
     * */
    public void setBaseUrl(String baseUrl){
        // Storing base_url in pref
        editor.putString(BASE_URL, baseUrl);
        editor.commit();
    }

    /**
     * Get server's ip address
     * */
    public String getBaseUrl(){
        String baseUrl = pref.getString(BASE_URL,"https://dots.dhl.com/dsc-aux/");
        return baseUrl;
    }

    public void setImageUrl(String imageUrl) {
        editor.putString(IMAGE_URL,imageUrl);
        editor.commit();
    }

    public String getImageUrl(){
        String imageUrl = pref.getString(IMAGE_URL, "https://dots.dhl.com/dsc-aux/");
        return imageUrl;
    }

    public void setUploadImage(boolean isUpload){
        editor.putBoolean("IS_UPLOAD", isUpload);
        editor.commit();
    }

    public boolean getIsUploadImage(){
        boolean isUpload = pref.getBoolean("IS_UPLOAD", true);
        return isUpload;

    }

}