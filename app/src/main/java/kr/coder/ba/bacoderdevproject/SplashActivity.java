package kr.coder.ba.bacoderdevproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    final int STORAGE_PERMISSION_CODE = 22;
    final String TAG = SplashActivity.class.getSimpleName();

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private View mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setTheme(R.style.AppTheme_Dark);
        setContentView(R.layout.activity_splash);
        setPhoneInfo();
        mContentView = findViewById(R.id.fullscreen_content);
        Log.d(TAG, "requestPermission");
        requestPermission();
        // Set up the user interaction to manually show or hide the system UI.
        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
    }

    private boolean login(){
        final String url = getString(R.string.server_address) + "/getPerson.jsp";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id = 0;
                try {
                    id = new JSONObject(response).getInt("id");
                }catch (JSONException e){
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "로그인success", Toast.LENGTH_LONG).show();
                onLoginSuccess(id);
//                if(id > 0) {
//
//                    // 로그인 성공
//                }else {
//                    // 로그인 실패
//                    onLoginSuccess();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "로그인 error", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                SharedPreferences pref = getSharedPreferences(getString(R.string.sharedpreference_name), MODE_PRIVATE);
                param.put("phone", pref.getString(getString(R.string.device_phone_num), null));
                param.put("deviceId", pref.getString(getString(R.string.device_uuid), null));
                return param;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
        return false;
    }
    private void onLoginFail(){

    }
    private void setPhoneInfo() {
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            String mPhoneNumber = tMgr.getLine1Number();
            SharedPreferences pref = getSharedPreferences(getString(R.string.sharedpreference_name), MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString(getString(R.string.device_phone_num), mPhoneNumber.substring(1));

            if (pref.getString(getString(R.string.device_uuid), "").length() == 0) {
                editor.putString(getString(R.string.device_uuid), UUID.randomUUID().toString());
            }
            editor.commit();

        }
    }
    private void onLoginSuccess(int id){
        SharedPreferences pref = getSharedPreferences(getString(R.string.sharedpreference_name), MODE_PRIVATE);
        pref.edit().putInt(getString(R.string.user_id), id).commit();
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == STORAGE_PERMISSION_CODE
                && (grantResults[0]==PackageManager.PERMISSION_GRANTED
                && grantResults[1]==PackageManager.PERMISSION_GRANTED
        )
                ){
            login();
        }else{
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
            }
        }
    }
    private void requestPermission(){
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
                &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                        == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "login");
            login();
            return;
        }
        ActivityCompat.requestPermissions(this,
                new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE } ,
                STORAGE_PERMISSION_CODE);
    }
}
