package mahendra.school.mpulse1;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

public class profile extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"profile/mobile_profile";
    ProgressDialog progressDialog;
    String json_string="";
    JSONObject jsonObject,jsonObject1;
    ImageView img1;
    String post_data="";
    String sname="";
    String sid ="";
    String sdesignation="-";
    String sblood_group ="";
    String sjoining_date="" ;
    String semail_id="" ;
    String sgender ="";
    String sd_o_b ="";
    String smobile="";
    String smarital_status="",sfile="";
    CardView cel;



    String typer="DF";
    String tbr,twl,tem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SessionMaintance sessionMaintance = new SessionMaintance(profile.this);
        if(sessionMaintance.get_token().equals("")||sessionMaintance.get_token().equals(null)){
            Intent intent = new Intent(profile.this,sign_out.class);
            startActivity(intent);
        }

        Intent intent = getIntent();
        if(intent!=null) {
            try {
                typer = intent.getStringExtra("typer");
                tbr = intent.getStringExtra("branch_id");
                twl = intent.getStringExtra("walkin_id");
                tem = intent.getStringExtra("emp_id");

            } catch (Exception e) {
            }
        }else{
            typer = "DF";
        }
        getSupportActionBar().setTitle("PROFILE");

        img1 = findViewById(R.id.imageView);
        cel = findViewById(R.id.el);
        cel.setVisibility(View.GONE);


        new backgroundworker().execute();
        progressDialog = new ProgressDialog(profile.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        findViewById(R.id.salary_slip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this,salary_slip.class);
                startActivity(intent);
            }
        });



        CardView csign_out = findViewById(R.id.sign_out);
        csign_out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent =new Intent(profile.this,sign_out.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(profile.this);
                builder.setTitle("LOGOUT");
                builder.setMessage("Do You Want To LOGOUT ?").setIcon(R.mipmap.logo);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(intent);
                        }
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                android.app.AlertDialog dialog = builder.create();
                dialog.show();


            }
        });

        findViewById(R.id.myatten).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this,transport_attendance.class);
                intent.putExtra("TYPE","myatt");
                if (typer != null) {
                    intent.putExtra("emp_id",tem);
                    intent.putExtra("walkin_id",twl);
                    intent.putExtra("branch_id",tbr);
                    intent.putExtra("typer","newer");
                }
                startActivity(intent);
            }
        });

        findViewById(R.id.tatten).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(profile.this,transport_attendance.class);
                intent.putExtra("TYPE","tatt");
                if (typer != null) {
                    intent.putExtra("emp_id",tem);
                    intent.putExtra("walkin_id",twl);
                    intent.putExtra("branch_id",tbr);
                    intent.putExtra("typer","newer");
                }
                startActivity(intent);
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

                //Toast.makeText(profile.this, ""+typer, Toast.LENGTH_SHORT).show();
                try {
                    final SessionMaintance status = new SessionMaintance(profile.this);
                    if (typer == null) {

                        post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id(), "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(status.get_username(), "UTF-8") + "&"
                                + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(status.get_branch_id(), "UTF-8") + "&"
                                + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(status.get_token(), "UTF-8")+ "&"
                                + URLEncoder.encode("self", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
                    }else{
                        findViewById(R.id.salary_slip).setVisibility(View.GONE);
                        findViewById(R.id.sign_out).setVisibility(View.GONE);
                        post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(twl, "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(tem, "UTF-8") + "&"
                                + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(tbr, "UTF-8")+ "&"
                                + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(status.get_token(), "UTF-8")+ "&"
                                + URLEncoder.encode("self", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
                    }

                    if(post_data.equals("")){
                        findViewById(R.id.salary_slip).setVisibility(View.GONE);
                        findViewById(R.id.sign_out).setVisibility(View.GONE);
                        post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(twl, "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(tem, "UTF-8") + "&"
                                + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(tbr, "UTF-8")+ "&"
                                + URLEncoder.encode("token", "UTF-8") + "=" + URLEncoder.encode(status.get_token(), "UTF-8")+ "&"
                                + URLEncoder.encode("self", "UTF-8") + "=" + URLEncoder.encode("0", "UTF-8");
                    }
                }catch (Exception e){

                }
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
            progressDialog.dismiss();
            int count = 0;
            if(json_string.equals("Token Mismatch")){
                final Intent intent =new Intent(profile.this,sign_out.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

                final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(profile.this);
                builder.setTitle("LOGOUT");
                builder.setMessage("Token Mismatch Logout Please ").setIcon(R.mipmap.logo);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            startActivity(intent);
                        }
                    }
                });



                android.app.AlertDialog dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
            }else{
                try {
                    jsonObject = new JSONObject(json_string);
                    jsonObject1 = jsonObject.getJSONObject("personal");
                    sname = jsonObject1.getString("name")+jsonObject1.getString("last_name");
                    sid = jsonObject1.getString("emp_id");
                    sdesignation = jsonObject1.getString("designation_name");
                    sblood_group = jsonObject1.getString("blood_group");
                    sjoining_date = jsonObject1.getString("date_of_join");
                    semail_id = jsonObject1.getString("email_id");
                    sgender = jsonObject1.getString("gender");
                    sd_o_b = jsonObject1.getString("certificate_dob");
                    smobile = jsonObject1.getString("mobile_no");
                    smarital_status = jsonObject1.getString("marital_status");
                    //Log.d("GGGGGGG",""+jsonObject.getJSONObject("salary_details"));
                    sfile = jsonObject1.getString("file");
                    try {
                        String[] temper = sfile.split(",");
                        String file_blob_data = temper[1].toString().trim();
                        byte[] decodedString = Base64.decode(file_blob_data, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        img1.setImageBitmap(decodedByte);
                    }catch (Exception e){


                    }
                    if(sdesignation.equals("")||sdesignation.equals(" ")) {
                        sdesignation = "-";
                    }



                    Log.d("QWASZXCV",""+jsonObject1.getString("marital_status"));


                    TextView name = findViewById(R.id.textView15);
                    TextView employee_id = findViewById(R.id.textView16);
                    TextView department = findViewById(R.id.textView17);
                    TextView blood_group = findViewById(R.id.textView32);
                    TextView doj = findViewById(R.id.textView3332);
                    TextView mail = findViewById(R.id.textView33);
                    TextView dob = findViewById(R.id.textView37);
                    TextView gender = findViewById(R.id.textView35);
                    TextView mobile = findViewById(R.id.textView39);
                    TextView martial_status = findViewById(R.id.textView53);

                    final SessionMaintance status=new SessionMaintance(profile.this);
                    name.setText(sname);
                    department.setText(sdesignation);
                    employee_id.setText(sid);
                    blood_group.setText("Blood Group : "+sblood_group);
                    dob.setText(sd_o_b);
                    mail.setText(semail_id);
                    mobile.setText(smobile);
                    gender.setText(sgender);
                    martial_status.setText(smarital_status);
                    doj.setText("Joining Date : "+sjoining_date);



                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }


}
