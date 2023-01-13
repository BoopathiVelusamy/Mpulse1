package mahendra.school.mpulse1;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.concurrent.Executor;

import javax.crypto.Cipher;


public class splash_screen extends AppCompatActivity {

    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    private Executor executor;

    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_NETWORK_STATE
            ,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.USE_FINGERPRINT};
    int permission_All = 1;

//    private BiometricPrompt biometricPrompt;
//    private BiometricPrompt.PromptInfo promptInfo;

    //-0-----------------------------------------------------
    private KeyStore keyStore;
    // Variable used for storing the key in the Android Keystore container
    private static final String KEY_NAME = "androidHive";
    private Cipher cipher;
    //-----------------------------------------------
    String currentVersion="",onlineVersion="";

    StringBuffer sb22 = new StringBuffer();
    String json_url22 = url_interface.url+"Autorun_leavepolicy/mobile_notification";
    String fcm_token;
    String json_string22="";

    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"index/mobile_notification";
    String json_string="";
    JSONArray jsonArray;

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"index/Mt_attd_mobile_notification";
    String json_string2="";

    Intent nextIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        if(!haspermission(this,permissions))
            ActivityCompat.requestPermissions(this,permissions,permission_All);

        nextIntent = new Intent(splash_screen.this,mainmenu.class);

        executor = ContextCompat.getMainExecutor(this);

        biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(splash_screen.this,"Error Authentication",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                startActivity(nextIntent);
                Toast.makeText(splash_screen.this,"Success Authentication",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(splash_screen.this,"Failure Authentication",Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication")
                .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_WEAK| BiometricManager.Authenticators.DEVICE_CREDENTIAL)
                .setConfirmationRequired(true)
                .build();



        ImageView imageView = findViewById(R.id.imageView6);

        Glide.with(this).load(R.drawable.animation).into(imageView);

        final SessionMaintance status=new SessionMaintance(this);



        if(status.get_username().equals("")) {
            Intent intent = new Intent(splash_screen.this,MainActivity.class);
            intent.putExtra("TYPE","x");
            startActivity(intent);
        }
        else
        {
            nextIntent.putExtra("TYPE","y");
            nextIntent.putExtra("pendingattendancecount","0");
            nextIntent.putExtra("mtrackerattendancecount","0");
            new backgroundworker6().execute();
        }


        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }


//        executor = ContextCompat.getMainExecutor(this);
//        biometricPrompt = new BiometricPrompt(splash_screen.this,
//                executor, new BiometricPrompt.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode,
//                                              @NonNull CharSequence errString) {
//                super.onAuthenticationError(errorCode, errString);
//                Toast.makeText(getApplicationContext(),
//                        "Authentication error: " + errorCode + " "+errString, Toast.LENGTH_SHORT)
//                        .show();
//                Log.d("KLKL",""+errString);
//            }
//
//            @Override
//            public void onAuthenticationSucceeded(
//                    @NonNull BiometricPrompt.AuthenticationResult result) {
//                super.onAuthenticationSucceeded(result);
//                startActivity(nextIntent);
//            }
//
//            @Override
//            public void onAuthenticationFailed() {
//                super.onAuthenticationFailed();
//                Toast.makeText(getApplicationContext(), "Authentication failed",
//                        Toast.LENGTH_SHORT)
//                        .show();
//            }
//        });
//
//        promptInfo = new BiometricPrompt.PromptInfo.Builder()
//                .setTitle("Biometric login for my app")
//                .setSubtitle("Log in using your biometric credential")
//                .setDeviceCredentialAllowed(true)
//                .setConfirmationRequired(true)
//                .build();
//
//        biometricPrompt.authenticate(promptInfo);
        //----------------------------------------------------------------------


    }



    public static boolean haspermission(Context context, String... permissions) {
        if((Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)&&(context!=null)&&(permissions!=null))
        {
            for(String temp : permissions)
                if(ActivityCompat.checkSelfPermission(context,temp)!= PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
        }
        return true;
    }



    public class backgroundworker extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url1);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(splash_screen.this);
                String post_data = URLEncoder.encode("login_emp_id","UTF-8")+"="+URLEncoder.encode(status.get_username(),"UTF-8")+"&"
                        +URLEncoder.encode("branch_id","UTF-8")+"="+ URLEncoder.encode(status.get_branch_id(),"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string=bufferedReader.readLine())!=null)
                {
                    sb.append(json_string+"\n");
                    Log.d("json_string",""+json_string);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb.toString());
                return sb.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string = result;
            int count = 0;
            try {
                jsonArray = new JSONArray(json_string);
                Log.d("ASASASAS",""+jsonArray.length());
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    count++;
                }

                nextIntent.putExtra("pendingattendancecount",String.valueOf(count));

                new backgroundworker2().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class backgroundworker2 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url2);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(splash_screen.this);
                String post_data = URLEncoder.encode("walkin_id","UTF-8")+"="+URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("branch_id","UTF-8")+"="+ URLEncoder.encode(status.get_branch_id(),"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string2=bufferedReader.readLine())!=null)
                {
                    sb2.append(json_string2+"\n");
                    Log.d("json_string",""+json_string2);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb2.toString());
                return sb2.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string2 = result;
            nextIntent.putExtra("mtrackerattendancecount",String.valueOf(json_string2));
            new backgroundworker6().execute();

        }
    }

    public class backgroundworker6 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;



            return "1";
        }

        @Override
        protected void onPostExecute(String fnonlineVersion) {
            super.onPostExecute(onlineVersion);
            final Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    authentication();
                }
            }, 2000);



        }
    }



    public void authentication(){

        BiometricManager biometricManager = BiometricManager.from(this);

        if(biometricManager.canAuthenticate() != BiometricManager.BIOMETRIC_SUCCESS){
            //Toast.makeText(splash_screen.this,"BioMetric not Supported",Toast.LENGTH_SHORT).show();
            startActivity(nextIntent);
            return;
        }

        biometricPrompt.authenticate(promptInfo);

    }




}
