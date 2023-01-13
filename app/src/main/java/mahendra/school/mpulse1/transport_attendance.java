package mahendra.school.mpulse1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
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

public class transport_attendance extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {

    StringBuffer sb = new StringBuffer();
    String json_url = url_interface.url+"profile/mobile_attd_transport";
    String json_string="";
    ProgressDialog progressDialog;
    List<String> month = new ArrayList<>();
    List<String> atten_mark = new ArrayList<>();
    Spinner spin;
    int days=0;
    ListView listView;
    CustomAdapter customAdapter;
    Intent intent;

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"profile/MobilegetAttd";
    String json_string2="";
    String selected_month="";
    String str1="";
    String post_data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transport_attendance);

        intent = getIntent();
        if(intent.getStringExtra("TYPE").equals("myatt")){
            json_url2 = url_interface.url+"profile/MobilegetAttd";
            getSupportActionBar().setTitle("MY ATTENDANCE");
        }else  if(intent.getStringExtra("TYPE").equals("tatt")){
            json_url2 = url_interface.url+"profile/MobilegetAttdTransport";
            getSupportActionBar().setTitle("TRANSPORT ATTENDANCE");
        }
        spin = (Spinner) findViewById(R.id.spinner8);
        spin.setOnItemSelectedListener(this);

        listView=(ListView)findViewById(R.id.listview);

        progressDialog = new ProgressDialog(transport_attendance.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selected_month = spin.getSelectedItem().toString();

        days =0;
        atten_mark.clear();
        listView.setAdapter(null);

        progressDialog = new ProgressDialog(transport_attendance.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker2().execute();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class backgroundworker extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url);
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

                try {
                    String asd = intent.getStringExtra("typer");
                    if (asd == null) {
                        final SessionMaintance status = new SessionMaintance(transport_attendance.this);
                        post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id(), "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(status.get_user_id(), "UTF-8") + "&"
                                + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(status.get_branch_id(), "UTF-8");
                    }else if(asd.equals("newer")){
                        //findViewById(R.id.salary_slip).setVisibility(View.GONE);
                        post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(intent.getStringExtra("walkin_id"), "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(intent.getStringExtra("emp_id"), "UTF-8") + "&"
                                + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(intent.getStringExtra("branch_id"), "UTF-8");
                    }
                    bufferedWriter.write(post_data);
                    Log.d("PostData",""+post_data);
                }catch (Exception e){
                    Log.d("aSSDDDd",""+e);
                  //  Toast.makeText(transport_attendance.this, ""+e, Toast.LENGTH_SHORT).show();

                }


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
            int count=0;
            try {
                JSONObject jsonObject = new JSONObject(json_string);
                String str = jsonObject.getString("attd_month");

                //-------------
                if(intent.getStringExtra("TYPE").equals("myatt")){
                    str1 = jsonObject.getString("attd");
                }else  if(intent.getStringExtra("TYPE").equals("tatt")){
                    str1 = jsonObject.getString("transport_attd");
                }
                //-------------


                days = Integer.valueOf(jsonObject.getString("last_day"));

                JSONArray jsonArray = new JSONArray(str);
                while(count<jsonArray.length()){
                    JSONObject jo = jsonArray.getJSONObject(count);
                    month.add(jo.getString("month"));
                    count++;
                }

                ArrayAdapter aa = new ArrayAdapter(transport_attendance.this,android.R.layout.simple_spinner_item,month);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);

                if(str1.equals("null")||str1.equals("")){
                    Toast.makeText(transport_attendance.this, "No Records Found", Toast.LENGTH_SHORT).show();
                }else {
                    String str2 = str1.substring(1,str1.length()-1);
                    String[] str3 = str2.split(",");
                    for(int i=0;i<days;i++){
                    atten_mark.add(str3[i]);
                    }
                customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);
                }
                Log.d("ASWERTY",""+str1);
            }catch (Exception e){

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
                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                //final SessionMaintance status=new SessionMaintance(transport_attendance.this);

                try {
                    String asd = intent.getStringExtra("typer");
                    if (asd == null) {
                        final SessionMaintance status = new SessionMaintance(transport_attendance.this);
                        post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id(), "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(status.get_username(), "UTF-8") + "&"
                                + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(status.get_branch_id(), "UTF-8")+"&"
                                +URLEncoder.encode("month","UTF-8")+"="+ URLEncoder.encode(selected_month,"UTF-8");
                    }else if(asd.equals("newer")){
                        //findViewById(R.id.salary_slip).setVisibility(View.GONE);
                        post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(intent.getStringExtra("walkin_id"), "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(intent.getStringExtra("emp_id"), "UTF-8") + "&"
                                + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(intent.getStringExtra("branch_id"), "UTF-8")+"&"
                                +URLEncoder.encode("month","UTF-8")+"="+ URLEncoder.encode(selected_month,"UTF-8");
                    }
                    bufferedWriter.write(post_data);
                    Log.d("PostData",""+post_data);
                }catch (Exception e){
                    Log.d("aSSDDDd",""+e);
                    //  Toast.makeText(transport_attendance.this, ""+e, Toast.LENGTH_SHORT).show();

                }
                
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
            progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(json_string2);
                days = Integer.valueOf(jsonObject.getString("last_day"));
                //-------------
                if(intent.getStringExtra("TYPE").equals("myatt")){
                    str1 = jsonObject.getString("attd");
                }else  if(intent.getStringExtra("TYPE").equals("tatt")){
                    str1 = jsonObject.getString("transport_attd");
                }
                //-------------

                if(str1.equals("null")||str1.equals("")){
                    Toast.makeText(transport_attendance.this, "No Records Found", Toast.LENGTH_SHORT).show();
                }else {
                    String str2 = str1.substring(1,str1.length()-1);
                    String[] str3 = str2.split(",");
                    for(int i=0;i<days;i++){
                        atten_mark.add(str3[i]);
                    }
                    customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);
                }

            }catch (Exception e){

            }

        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return days;
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
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.custom_layout10, null);

            TextView day = view.findViewById(R.id.textView18);
            TextView markday = view.findViewById(R.id.textView3);
            day.setText(atten_mark.get(i).split(":")[0].substring(2,atten_mark.get(i).split(":")[0].length()-1)+"-"+selected_month);
            markday.setText(atten_mark.get(i).split(":")[1].substring(1,atten_mark.get(i).split(":")[1].length()-1));
            return view;
        }
    }
}