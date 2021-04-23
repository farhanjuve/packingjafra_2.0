package dsc.auxid.packingjafra;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dsc.auxid.packingjafra.util.ImageAdapter;
import dsc.auxid.packingjafra.util.MyAdapter;
import dsc.auxid.packingjafra.util.ProductPhoto;
import dsc.auxid.packingjafra.util.SessionManager;
import dsc.auxid.packingjafra.util.URLs;
import dsc.auxid.packingjafra.util.VolleyMultipartImageRequest;

//https://www.androidauthority.com/how-to-build-an-image-gallery-app-718976/
public class CameraConfirmActivity extends AppCompatActivity {
    private SessionManager _sessionManager;
    private Uri uriFileName;

    private static final int CAMERA_PHOTO = 111;
    private static final int CAMERA_REQUEST = 1888;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;

    private static final int REQUEST_CAMERA = 100;

    private ArrayList<ProductPhoto> productPhotos = new ArrayList<ProductPhoto>();
    private   RecyclerView recyclerView;

    private Button takePhoto, btnSubmit;


    private String launchNum, shipmentNo;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_confirm);
        _sessionManager = new SessionManager(getApplicationContext());

        Intent intent = getIntent();
        launchNum = intent.getStringExtra("launch_num");
        shipmentNo = intent.getStringExtra("shipment_id");

        takePhoto = findViewById(R.id.btnTakePiture);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                captureImage();
            }
        });

        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog = new ProgressDialog(CameraConfirmActivity.this);
                progressDialog.setMessage("Uploading, please wait...");
                progressDialog.show();

                // update status
                StringRequest stringRequest = new StringRequest(
                    Request.Method.POST,
                    _sessionManager.getBaseUrl() + URLs.confirmPacking,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject objResponse = new JSONObject(response);

                                if(!_sessionManager.getIsUploadImage()) {
                                    Intent newIntent = new Intent(getApplicationContext(), MainScreenActivity.class);
                                    newIntent.putExtra("wave_number", launchNum);
                                    startActivity(newIntent);
                                }
                                Log.d("TAG", "BERHASIH UPDATE" + URLs.confirmPacking);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                            if (error instanceof NetworkError) {
                                Toast.makeText(getApplicationContext(),
                                    "Oops. Cannot connect to server, check your network!",
                                    Toast.LENGTH_LONG).show();
                            } else if (error instanceof ServerError) {
                            } else if (error instanceof AuthFailureError) {
                            } else if (error instanceof ParseError) {
                            } else if (error instanceof NoConnectionError) {
                                Toast.makeText(getApplicationContext(),
                                    "Oops. Cannot connect to server, check your connection!",
                                    Toast.LENGTH_LONG).show();
                            } else if (error instanceof TimeoutError) {
                                Toast.makeText(getApplicationContext(),
                                    "Oops. Timeout error!",
                                    Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                ){
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        return super.getHeaders();
                    }

                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        final Map<String, String> mParams = new ArrayMap<String, String>();
                        mParams.put("launch_num", launchNum);
                        mParams.put("do",shipmentNo);
                        return mParams;
                    }
                };

                RequestQueue requestQueue = Volley.newRequestQueue(CameraConfirmActivity.this);
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(60000,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(stringRequest);

                // upload photo
                if(_sessionManager.getIsUploadImage()) {
                    VolleyMultipartImageRequest postImages = new VolleyMultipartImageRequest(
                        Request.Method.POST,
                        _sessionManager.getImageUrl() + URLs.submitPhotos,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    progressDialog.dismiss();

                                    JSONObject objResponse = response;
                                    String responseMsg = objResponse.getJSONObject("meta").getString("message").trim();
                                    if(objResponse.getJSONObject("meta").getInt("code")==200) {
                                        Toast.makeText(getBaseContext(), responseMsg, Toast.LENGTH_LONG).show();

                                        Intent newIntent = new Intent(getApplicationContext(), MainScreenActivity.class);
                                        newIntent.putExtra("wave_number", launchNum);
                                        startActivity(newIntent);

                                    }
                                }catch (JSONException je) {
                                    je.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                                if (error instanceof NetworkError) {
                                    Toast.makeText(getApplicationContext(),
                                        "Oops. Cannot connect to server, check your network!",
                                        Toast.LENGTH_LONG).show();
                                } else if (error instanceof ServerError) {
                                } else if (error instanceof AuthFailureError) {
                                } else if (error instanceof ParseError) {
                                } else if (error instanceof NoConnectionError) {
                                    Toast.makeText(getApplicationContext(),
                                        "Oops. Cannot connect to server, check your connection!",
                                        Toast.LENGTH_LONG).show();
                                } else if (error instanceof TimeoutError) {
                                    Toast.makeText(getApplicationContext(),
                                        "Oops. Timeout error!",
                                        Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    ) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            final Map<String, String> mParams = new ArrayMap<String, String>();
                            mParams.put("launch_num", launchNum);
                            mParams.put("do",shipmentNo);

                            return mParams;
                        }

                        @Override
                        protected Map<String, ArrayList<DataPart>> getByteData() throws AuthFailureError {
                            Map<String, VolleyMultipartImageRequest.DataPart> params = new HashMap<>();
                            Map<String, ArrayList<DataPart>> imageList = new HashMap<>();
                            ArrayList<DataPart> dataPart = new ArrayList<>();

                            for (ProductPhoto bitmapUpload: productPhotos) {
                                final String fName = bitmapUpload.getFileName();
                                VolleyMultipartImageRequest.DataPart dp = new VolleyMultipartImageRequest.DataPart( fName  , getFileDataFromDrawable(bitmapUpload.getPhoto()), "image/jpeg");
                                dataPart.add(dp);
                            }
                            imageList.put("images[]", dataPart);

                            return imageList;
                        }
                    };

                    requestQueue.add(postImages);
                }
            }
        });

        recyclerView =  findViewById(R.id.imagegallery);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getApplicationContext(),2);
        recyclerView.setLayoutManager(layoutManager);

        MyAdapter adapter = new MyAdapter(getApplicationContext(), productPhotos);
        recyclerView.setAdapter(adapter);


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
        }
    }

    private void captureImage() {
        PackageManager pm  = getPackageManager();
        if( pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "DSC-Packing");

            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("App", "failed to create directory");
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            uriFileName =  Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator +
                    shipmentNo + "_" + timeStamp + ".jpg"));

            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFileName);
            Log.d("TAG", uriFileName.getPath());

            startActivityForResult(cameraIntent, REQUEST_CAMERA );
        }


    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CAMERA)
        {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                captureImage();
            }
            else
            {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK)
        {
            Bitmap photo = null;
            try {
                photo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uriFileName);
                productPhotos.add(new ProductPhoto(photo, uriFileName));

                recyclerView.setAdapter(new MyAdapter(this, productPhotos));



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
     * The method is taking Bitmap as an argument
     * then it will return the byte[] array for the given bitmap
     * and we will send this array to the server
     * here we are using PNG Compression with 80% quality
     * you can give quality between 0 to 100
     * 0 means worse quality
     * 100 means best quality
     * */
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
