package mahendra.school.mpulse1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class employee_list extends AppCompatActivity {

    List<String> Lemp_id = new ArrayList<>();
    List<String> Lwalkin_id = new ArrayList<>();
    List<String> Lbranch_id = new ArrayList<>();
    List<String> Lname = new ArrayList<>();
    ListView listView;
    CustomAdapter customAdapter;
    ProgressDialog progressDialog;

    StringBuffer sb222 = new StringBuffer();
    String json_url222 = url_interface.url+"relieving_list/GetEmpDetailsMobile";
    String json_string222="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_list);
        getSupportActionBar().setTitle("EMPLOYEE LIST");
        listView=(ListView)findViewById(R.id.listView);
        Intent intent = getIntent();

        new backgroundworker66().execute();
        progressDialog = new ProgressDialog(employee_list.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

    }

    public class backgroundworker66 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url222);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb222=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status = new SessionMaintance(employee_list.this);
                String post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id(), "UTF-8") + "&"
                        + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(status.get_username(), "UTF-8") + "&"
                        + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(status.get_branch_id(), "UTF-8");

                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string222=bufferedReader.readLine())!=null)
                {
                    sb222.append(json_string222+"\n");
                    Log.d("json_string",""+json_string222);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb222.toString());
                return sb222.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string222 = result;
            progressDialog.dismiss();
            int count = 0;
            try{
                JSONArray jsonArray = new JSONArray(json_string222);
                while(count<jsonArray.length()){
                    JSONObject jo = jsonArray.getJSONObject(count);
                    Lemp_id.add(jo.getString("emp_id"));
                    Lwalkin_id.add(jo.getString("walkin_id"));
                    Lbranch_id.add(jo.getString("branch_id"));
                    Lname.add(jo.getString("name")+" "+jo.getString("last_name"));
                    count++;

                }
                if(Lemp_id.size()>0){
                    customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);

                }

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(employee_list.this,profile.class);
                        intent.putExtra("typer","newer");
                        intent.putExtra("emp_id",Lemp_id.get(position));
                        intent.putExtra("walkin_id",Lwalkin_id.get(position));
                        intent.putExtra("branch_id",Lbranch_id.get(position));
                        startActivity(intent);
                    }
                });
            }catch (Exception e){

            }

        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lname.size();
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

            view = getLayoutInflater().inflate(R.layout.custom_layout_attendance1, null);

            TextView txt6 = view.findViewById(R.id.textView6);

            TextView txt23 = view.findViewById(R.id.textView23);

            ImageView img31 = view.findViewById(R.id.imageView31);

            txt6.setText(Lemp_id.get(i));

            txt23.setText(Lname.get(i));

            img31.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(employee_list.this,profile.class);
                    intent.putExtra("typer","newer");
                    intent.putExtra("emp_id",Lemp_id.get(i));
                    intent.putExtra("walkin_id",Lwalkin_id.get(i));
                    intent.putExtra("branch_id",Lbranch_id.get(i));
                    startActivity(intent);
                }
            });

            return view;
        }
    }
}