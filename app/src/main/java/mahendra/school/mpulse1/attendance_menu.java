package mahendra.school.mpulse1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class attendance_menu extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"index/mobile_notification";
    ProgressDialog progressDialog;
    String json_string="";
    JSONArray jsonArray;
    List<String> Lname = new ArrayList<>();
    List<String> Lemp_id = new ArrayList<>();
    Map<String,String> emp_id_map = new HashMap<>();
    TextView notify_count;

    private PopupWindow mPopupWindow2;
    ConstraintLayout mRelativeLayout2;
    LayoutInflater inflater2;
    View customView2;
    ListView listView;
    CustomAdapter customAdapter;

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"index/Mt_attd_mobile_notification";
    String json_string2="";
    TextView notify_count2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_menu);

        notify_count = findViewById(R.id.imageView1119);
        notify_count2 = findViewById(R.id.imageView11119);
        new backgroundworker().execute();
        progressDialog = new ProgressDialog(attendance_menu.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        getSupportActionBar().setTitle("ATTENDANCE MENUS");

        inflater2 = (LayoutInflater) attendance_menu.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        customView2 = inflater2.inflate(R.layout.custom_layout_dialog,null);

        findViewById(R.id.attendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(attendance_menu.this,attendance.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.pendingattendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show3();
            }
        });

        findViewById(R.id.mtrackerattendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(attendance_menu.this,mtracker_attendance.class);
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
                final SessionMaintance status=new SessionMaintance(attendance_menu.this);
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
            progressDialog.dismiss();
            int count = 0;
            try {
                jsonArray = new JSONArray(json_string);
                Log.d("ASASASAS",""+jsonArray.length());
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    Lname.add(jo.getString("name").toUpperCase()+" "+jo.getString("last_name").toUpperCase());
                    Lemp_id.add(jo.getString("emp_id"));
                    emp_id_map.put(jo.getString("name").toUpperCase()+" "+jo.getString("last_name").toUpperCase(),jo.getString("emp_id"));
                    count++;
                }
                notify_count.setText(String.valueOf(count));
                new backgroundworker2().execute();
                progressDialog = new ProgressDialog(attendance_menu.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
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
                final SessionMaintance status=new SessionMaintance(attendance_menu.this);
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
            progressDialog.dismiss();
            int count2 = 0;
                notify_count2.setText(String.valueOf(json_string2));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                Intent intent = new Intent(this, sign_out.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                Intent intent2 = new Intent(this,profile.class);
                startActivity(intent2);
                return true;


            default:return super.onOptionsItemSelected(item);
        }
    }

    public void show3(){
        mRelativeLayout2 = (ConstraintLayout) findViewById(R.id.attendance_menu);
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

        listView = (ListView) customView2.findViewById(R.id.listView);
        listView.setDivider(null);
        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(attendance_menu.this,Lemp_id.get(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(attendance_menu.this,pending_attendance.class);
                intent.putExtra("emp_id_sub",Lemp_id.get(position));
                startActivity(intent);
                mPopupWindow2.dismiss();
            }
        });


        mPopupWindow2.setAnimationStyle(R.style.popup_window_animation);
        mPopupWindow2.showAtLocation(customView2, Gravity.CENTER, 0, 0);
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lemp_id.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_layout2, null);

            TextView name = (TextView)view.findViewById(R.id.textView18);
            TextView emp_id = (TextView)view.findViewById(R.id.textView3);

            name.setText(Lname.get(i));
            emp_id.setText(Lemp_id.get(i));

            return view;
        }
    }
}
