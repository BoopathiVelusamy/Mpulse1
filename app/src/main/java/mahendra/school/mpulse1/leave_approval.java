package mahendra.school.mpulse1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.List;

public class leave_approval extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"approve_leave_req/GetLeave_req_emp_listMobile";
    JSONObject jsonObject;
    JSONArray jsonArray;
    ProgressDialog progressDialog;
    String json_string="";
    String aprroval_type= "1";

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"approve_leave_req/MobileApproveRequest";
    String json_string2="";


    ListView listView;
    CustomAdapter customAdapter;

    List<String> Lleave_id = new ArrayList<>();
    List<String> Lapply_date = new ArrayList<>();
    List<String> Lleave_subject = new ArrayList<>();
    List<String> Lfrom_date = new ArrayList<>();
    List<String> Lto_date = new ArrayList<>();
    List<String> Ltotal_days = new ArrayList<>();
    List<String> Lleave_type = new ArrayList<>();
    List<String> Lapprove_status = new ArrayList<>();
    List<String> Llstapp_status = new ArrayList<>();
    List<String> Lname = new ArrayList<>();
    List<String> Lemp_id = new ArrayList<>();
    List<String> Ldwalkin_id = new ArrayList<>();


    String leave_id="",statuser="",dwalkin_id="",approval_type_str="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_approval);

        Intent intent = getIntent();

        approval_type_str = intent.getStringExtra("approval_type");

        getSupportActionBar().setTitle("LEAVE APPROVAL");

        listView=(ListView)findViewById(R.id.listView);

        if(approval_type_str.equals("first")){
            Lleave_id.clear();Lapply_date.clear();Lleave_subject.clear();Lfrom_date.clear();Lto_date.clear();Lname.clear();Lemp_id.clear();
            Ltotal_days.clear();Lleave_type.clear();Lapprove_status.clear();Llstapp_status.clear();leave_id="";
            listView.setAdapter(null);
            progressDialog = new ProgressDialog(leave_approval.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            new backgroundworker().execute();
            aprroval_type = "1";
        }else if(approval_type_str.equals("second")){
            Lleave_id.clear();Lapply_date.clear();Lleave_subject.clear();Lfrom_date.clear();Lto_date.clear();
            Ltotal_days.clear();Lleave_type.clear();Lapprove_status.clear();Llstapp_status.clear();leave_id="";Lname.clear();Lemp_id.clear();
            listView.setAdapter(null);
            progressDialog = new ProgressDialog(leave_approval.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            new backgroundworker().execute();
            aprroval_type = "2";
        }

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
                Lleave_id.clear();Lapply_date.clear();Lleave_subject.clear();Lfrom_date.clear();Lto_date.clear();
                Ltotal_days.clear();Lleave_type.clear();Lapprove_status.clear();Llstapp_status.clear();Lname.clear();Lemp_id.clear();
                leave_id="";
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(leave_approval.this);
                String post_data = URLEncoder.encode("level_type","UTF-8")+"="+URLEncoder.encode(aprroval_type,"UTF-8")+"&"
                        +URLEncoder.encode("branch_id","UTF-8")+"="+ URLEncoder.encode(status.get_branch_id(),"UTF-8")+"&"
                        +URLEncoder.encode("walkin_id","UTF-8")+"="+ URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("user_type","UTF-8")+"="+ URLEncoder.encode(status.get_user_type(),"UTF-8");
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
            Log.d("RESULT", "" + result);
            int count=0;
            try {
                jsonObject = new JSONObject(json_string);
                jsonArray = jsonObject.getJSONArray("level_type_result");
                Log.d("ASASASAS",""+jsonArray.length());
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    Lleave_id.add(jo.getString("emp_leave_id"));
                    Lapply_date.add(jo.getString("apply_date"));
                    Lleave_subject.add(jo.getString("reason"));
                    Lfrom_date.add(jo.getString("from_date"));
                    Lto_date.add(jo.getString("to_date"));
                    Ltotal_days.add(jo.getString("total_days"));
                    Lleave_type.add(jo.getString("leave_type"));
                    Lapprove_status.add(jo.getString("approve_status"));
                    Llstapp_status.add(jo.getString("lstapp"));
                    Lname.add(jo.getString("name")+" "+jo.getString("last_name"));
                    Lemp_id.add(jo.getString("emp_id"));
                    Ldwalkin_id.add(jo.getString("walkin_id"));
                    count++;
                }
                if(Lleave_id.size()>0) {
                    customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lapply_date.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_layout_leave_approval, null);

            CardView card = (CardView) view.findViewById(R.id.cardView);


            ImageView leave_cancel = (ImageView)view.findViewById(R.id.imageView5);
            TextView leave_type = (TextView)view.findViewById(R.id.textView14);
            TextView subject = (TextView)view.findViewById(R.id.textView19);
            TextView ftdate = (TextView)view.findViewById(R.id.textView20);
            TextView name_id = (TextView)view.findViewById(R.id.textView202);
            TextView leave_status = (TextView)view.findViewById(R.id.textView21);
            ImageView leave_approve = (ImageView)view.findViewById(R.id.imageView3);

            if(Lapprove_status.get(i).equals("0")) {
                leave_cancel.setVisibility(View.INVISIBLE);
                leave_approve.setVisibility(View.INVISIBLE);
                card.setBackgroundColor(Color.parseColor("#7CFD5353"));
            }
            else if(Lapprove_status.get(i).equals("")) {
                leave_status.setText("W1");
                leave_status.setBackgroundResource(R.drawable.roundedbutton2);
            }
            else if((Lapprove_status.get(i).equals("1"))&&(!Llstapp_status.get(i).equals("0"))&&(approval_type_str.equals("second"))) {
                leave_status.setText("W2");
                leave_status.setBackgroundResource(R.drawable.roundedbutton3);
                leave_cancel.setVisibility(View.VISIBLE);
                leave_approve.setVisibility(View.VISIBLE);
            }
            else if((Lapprove_status.get(i).equals("1"))&&(!Llstapp_status.get(i).equals("0"))&&(approval_type_str.equals("first"))) {
                leave_status.setText("W2");
                leave_status.setBackgroundResource(R.drawable.roundedbutton3);
                leave_cancel.setVisibility(View.VISIBLE);
                leave_approve.setVisibility(View.INVISIBLE);
            }
            else if((Lapprove_status.get(i).equals("1"))&&(Llstapp_status.get(i).equals("0"))) {
                leave_status.setText("GR");
                leave_status.setBackgroundResource(R.drawable.roundedbutton1);
                leave_cancel.setVisibility(View.INVISIBLE);
                leave_approve.setVisibility(View.INVISIBLE);
            }
            else if((Lapprove_status.get(i).equals("2"))&&(!Llstapp_status.get(i).equals("0"))) {
                leave_status.setText("GR");
                leave_status.setBackgroundResource(R.drawable.roundedbutton1);
                leave_cancel.setVisibility(View.INVISIBLE);
                leave_approve.setVisibility(View.INVISIBLE);
            }

            leave_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leave_id = Lleave_id.get(i);
                    dwalkin_id = Ldwalkin_id.get(i);
                    if(aprroval_type.equals("1")){
                        statuser = "10";
                    }else if(aprroval_type.equals("2")){
                        statuser = "10";
                    }
                    //Toast.makeText(leave_approval.this, ""+Lleave_id.get(i), Toast.LENGTH_SHORT).show();
                    progressDialog = new ProgressDialog(leave_approval.this);
                    progressDialog.setMessage("Please Wait...!!!");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    new backgroundworker2().execute();

                }
            });

            leave_approve.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    leave_id = Lleave_id.get(i);
                    dwalkin_id = Ldwalkin_id.get(i);
                    if(aprroval_type.equals("1")){
                        statuser = "1";
                    }else if(aprroval_type.equals("2")){
                        statuser = "2";
                    }
                    //Toast.makeText(leave_approval.this, ""+Lleave_id.get(i), Toast.LENGTH_SHORT).show();
                    progressDialog = new ProgressDialog(leave_approval.this);
                    progressDialog.setMessage("Please Wait...!!!");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    new backgroundworker2().execute();

                }
            });

            ftdate.setText("From : "+Lfrom_date.get(i)+" "+"To : "+Lto_date.get(i));

            subject.setText(Lleave_subject.get(i).substring(0, 1).toUpperCase() + Lleave_subject.get(i).substring(1));

            name_id.setText(Lemp_id.get(i)+" - "+Lname.get(i));

            leave_type.setText(Lleave_type.get(i));

            return view;
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
                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(leave_approval.this);
                String post_data = URLEncoder.encode("req_id","UTF-8")+"="+URLEncoder.encode(leave_id,"UTF-8")+"&"
                        +URLEncoder.encode("walkin_id","UTF-8")+"="+ URLEncoder.encode(dwalkin_id,"UTF-8")+"&"
                        +URLEncoder.encode("status","UTF-8")+"="+ URLEncoder.encode(statuser,"UTF-8")+"&"
                        +URLEncoder.encode("type","UTF-8")+"="+ URLEncoder.encode("mobile","UTF-8");
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
            progressDialog.dismiss();
            progressDialog = new ProgressDialog(leave_approval.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            listView.setAdapter(null);
            new backgroundworker().execute();

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(leave_approval.this,mainmenu.class);
        intent.putExtra("TYPE","x");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
