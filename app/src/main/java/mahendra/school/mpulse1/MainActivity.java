package mahendra.school.mpulse1;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.material.snackbar.Snackbar;

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
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CHANGE_NETWORK_STATE,Manifest.permission.READ_SMS,Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_NETWORK_STATE
            ,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,
    Manifest.permission.MODIFY_AUDIO_SETTINGS,Manifest.permission.USE_FINGERPRINT};
    int permission_All = 1;


    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"login/mobile_login";
    ProgressDialog progressDialog;
    String json_string="";
    String username="",password="";
    JSONObject jsonObject;
    JSONArray jsonArray;
    String user_id="",field_id="",user_type="",email="",mobile="",jusername="",branch_id="",role_name="";

    private PopupWindow mPopupWindow2;
    ConstraintLayout mRelativeLayout2;
    LayoutInflater inflater2;
    View customView2;

    String a = "",b="";

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"login/mobile_forget";
    String json_string2 = "aaa";

    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        if(!haspermission(this,permissions))
            ActivityCompat.requestPermissions(this,permissions,permission_All);




        Button login_Button = findViewById(R.id.login);
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText eusername = findViewById(R.id.username);
                EditText epassword = findViewById(R.id.password);
                username = eusername.getText().toString();
                password = epassword.getText().toString();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                new backgroundworker().execute();
            }
        });

        inflater2 = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        customView2 = inflater2.inflate(R.layout.custom_layout_leave_reject_reason1,null);



        TextView forget_password = findViewById(R.id.textView);
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show3();
            }
        });


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
                String post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(username,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+ URLEncoder.encode(password,"UTF-8");
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
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string = result;
            progressDialog.dismiss();
            int count = 0;
            try {
                String token = "";
                jsonArray = new JSONArray(json_string);
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    user_id = jo.getString("user_id");
                    field_id = jo.getString("field_id");
                    user_type = jo.getString("user_type");
                    email = jo.getString("email");
                    mobile = jo.getString("mobile");
                    jusername = jo.getString("user_name");
                    branch_id = jo.getString("branch_id");
                    role_name = jo.getString("user_role_name");
                    token = jo.getString("token");
                    Log.d("HIDA",""+username);
                    count++;
                }
               if(count==1){
                    final SessionMaintance status=new SessionMaintance(MainActivity.this);
                    status.set_user_id(user_id);
                    status.set_field_id(field_id);
                    status.set_user_type(user_type);
                    status.set_email(email);
                    status.set_email(mobile);
                    status.set_username(jusername);
                    status.set_branch_id(branch_id);
                    status.set_role_name(role_name);
                    status.set_user_token(token);
                    Intent main_menu = new Intent(MainActivity.this,mainmenu.class);
                   main_menu.putExtra("TYPE","x");

                    startActivity(main_menu);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                if(result.equals("invalid_login")){
                final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.login_activity);
                Snackbar snackbar1 = Snackbar.make(constraintLayout, "Username Or Password is Wrong", Snackbar.LENGTH_LONG);
                Toast.makeText(MainActivity.this, "Username Or Password is Wrong", Toast.LENGTH_SHORT).show();
                snackbar1.show();
                }else if(result.equals("Password_Expires")){
                    final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.login_activity);
                    Snackbar snackbar1 = Snackbar.make(constraintLayout, "Password Expires Change Password in Website", Snackbar.LENGTH_LONG);
                    Toast.makeText(MainActivity.this, "Password Expires Change Password in Website", Toast.LENGTH_SHORT).show();
                    snackbar1.show();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    public void show3(){
        mRelativeLayout2 = (ConstraintLayout) findViewById(R.id.login_activity);
        mPopupWindow2 = new PopupWindow(
                customView2,
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                true
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow2.setElevation(5.0f);
        }

        mPopupWindow2.setOutsideTouchable(false);
        mPopupWindow2.setFocusable(true);
        mPopupWindow2.update();

        Button submit = (Button)customView2.findViewById(R.id.button7);
        final EditText user_id = (EditText) customView2.findViewById(R.id.editText6);
        final EditText d_o_b = (EditText) customView2.findViewById(R.id.editText);

        d_o_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(MainActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                d_o_b.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow2.dismiss();
                a = user_id.getText().toString();
                b = d_o_b.getText().toString();
                progressDialog = new ProgressDialog(MainActivity.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                new backgroundworker1().execute();

            }
        });

        mPopupWindow2.setAnimationStyle(R.style.popup_window_animation);
        mPopupWindow2.showAtLocation(customView2, Gravity.CENTER, 0, 0);
    }

    public class backgroundworker1 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url2);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("emp_id","UTF-8")+"="+URLEncoder.encode(a,"UTF-8")+"&"
                        +URLEncoder.encode("emp_dob","UTF-8")+"="+ URLEncoder.encode(b,"UTF-8");
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
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string2 = result;
            progressDialog.dismiss();
            if(json_string2.equals("1")){
                Toast.makeText(MainActivity.this, "Please Check Your Mail", Toast.LENGTH_SHORT).show();
            }else if(json_string2.equals("1")){
                Toast.makeText(MainActivity.this, "Sorry Plese Check Your Details", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
