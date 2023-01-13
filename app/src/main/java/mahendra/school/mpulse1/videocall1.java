package mahendra.school.mpulse1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class videocall1 extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url;
    String json_string="";

    List<String> info = new ArrayList<>();
    List<String> info1 = new ArrayList<>();
    List<String> info2 = new ArrayList<>();


    ProgressDialog progressDialog;
    ListView listView;
    CustomAdapter customAdapter;
    String b;

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"emp_team_allocation/mobile_emps_notify";
    String json_string2="";
    String post_data;
    int inc = -1;
    List<String> amap = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videocall1);
        Intent intent = getIntent();
        if(intent.getStringExtra("type").equals("REPORT_EMPLOYEE")){
            json_url = url_interface.url+"emp_team_allocation/mobile_reporting_emps";
            try {
                final SessionMaintance status=new SessionMaintance(videocall1.this);
                post_data = URLEncoder.encode("emp_id","UTF-8")+"="+URLEncoder.encode(status.get_username(),"UTF-8")+"&"
                        +URLEncoder.encode("branch_id","UTF-8")+"="+ URLEncoder.encode(status.get_branch_id(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(intent.getStringExtra("type").equals("PROJECT_EMPLOYEE")){
            json_url = url_interface.url+"emp_team_allocation/mobile_emps_processwise";
            try {
                final SessionMaintance status=new SessionMaintance(videocall1.this);
                post_data = URLEncoder.encode("process_id","UTF-8")+"="+URLEncoder.encode(intent.getStringExtra("id"),"UTF-8")+"&"
                        +URLEncoder.encode("branch_id","UTF-8")+"="+ URLEncoder.encode(status.get_branch_id(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else if(intent.getStringExtra("type").equals("ALL_EMPLOYEE")){
            json_url = url_interface.url+"emp_team_allocation/mobile_emps_all";
            try {
                final SessionMaintance status=new SessionMaintance(videocall1.this);
                post_data = URLEncoder.encode("branch_id","UTF-8")+"="+URLEncoder.encode(status.get_branch_id(),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        listView=(ListView)findViewById(R.id.listView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(inc%2==0){

                }else{

                }

            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    b = "";
                    for (String z : info2) {
                        b += z.split("     ")[0].trim() + ",";
                    }
                    b = b.substring(0, b.length() - 1);
                    //Toast.makeText(videocall1.this, "" + b, Toast.LENGTH_SHORT).show();

                    progressDialog = new ProgressDialog(videocall1.this);
                    progressDialog.setMessage("Please Wait...!!!");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    new backgroundworker3().execute();
                }catch (Exception e){

                }
            }
        });
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
                info.clear();
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
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
            int count=0;
            try{
                JSONArray jsonArray = new JSONArray(json_string);
                while (count<jsonArray.length()){
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    info.add(jsonObject.getString("emp_id").trim()+"     "+jsonObject.getString("name").trim().toUpperCase());
                    info1.add(jsonObject.getString("emp_id").trim()+"     "+jsonObject.getString("name").trim().toUpperCase());
                    amap.add(jsonObject.getString("client_token"));
                    count++;
                }
                Log.d("ASAS",""+amap);
            }catch (Exception e){

            }
            customAdapter = new CustomAdapter();
            listView.setAdapter(customAdapter);
        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return info.size();
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
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

                view = getLayoutInflater().inflate(R.layout.custom_layout_video_call, null);

                CardView cardView = (CardView)view.findViewById(R.id.cardView);

                TextView name = (TextView) view.findViewById(R.id.textView19);

                name.setTag("0");

                name.setText(info.get(i));

                if(amap.get(i).equals("null")||amap.get(i).equals("")){

                    view.findViewById(R.id.imageView26).setVisibility(View.INVISIBLE);
                }

                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(name.getTag().equals("0")){
                            if(amap.get(i).equals("null")||amap.get(i).equals("")){

                            }else {

                                name.setTextColor(Color.BLUE);

                                name.setTypeface(null, Typeface.BOLD);

                                info2.add(info.get(i));

                                name.setTag("1");
                            }
                        }else if(name.getTag().equals("1")){
                            if(amap.get(i).equals("null")||amap.get(i).equals("")){

                            }else {

                                name.setTextColor(Color.BLACK);

                                info2.remove(info.get(i));

                                name.setTag("0");

                                name.setTypeface(null, Typeface.NORMAL);
                            }
                        }
                    }
                });

                return view;
        }
    }

    public class backgroundworker3 extends AsyncTask<Void,Void,String> {

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
                final SessionMaintance status=new SessionMaintance(videocall1.this);
                String post_data = URLEncoder.encode("emp_id","UTF-8")+"="+URLEncoder.encode(b,"UTF-8")+"&"
                        +URLEncoder.encode("subject","UTF-8")+"="+ URLEncoder.encode("VIDEO_MEETING","UTF-8")+"&"
                        +URLEncoder.encode("title","UTF-8")+"="+ URLEncoder.encode("TEST","UTF-8");
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
//            Intent intent = new Intent(videocall1.this,video_meeting.class);
//            intent.putExtra("video_name","TEST");
//            startActivity(intent);
        }
    }

}