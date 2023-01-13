package mahendra.school.mpulse1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class projectlist extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url = url_interface.url+"emp_team_allocation/mobile_emps_process";
    String json_string="";
    ProgressDialog progressDialog;
    ListView listView;
    ArrayAdapter<String> customAdapter;
    List<String> info = new ArrayList<>();
    Map<String,String> infor = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projectlist);

        listView=(ListView)findViewById(R.id.listView);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(projectlist.this,videocall1.class);
                intent.putExtra("type","PROJECT_EMPLOYEE");
                intent.putExtra("id",infor.get(info.get(position)));
                startActivity(intent);
                //Toast.makeText(projectlist.this, ""+infor.get(info.get(position)), Toast.LENGTH_SHORT).show();
            }
        });
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
                final SessionMaintance status=new SessionMaintance(projectlist.this);
                String post_data = URLEncoder.encode("walkin_id","UTF-8")+"="+URLEncoder.encode(status.get_field_id(),"UTF-8");
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

                    String abc = jsonObject.getString("process_type")+"-"+
                            jsonObject.getString("level1")+"-"+
                            jsonObject.getString("level2")+"-"+
                            jsonObject.getString("level3")+"-"+
                            jsonObject.getString("level4")+"-"+
                            jsonObject.getString("level5");
                    String abcd = abc.replaceAll("-"," ").toUpperCase();
                    info.add(abcd);
                    infor.put(abcd,jsonObject.getString("process_name_id"));
                    count++;
                }
            }catch (Exception e){

            }
            customAdapter = new ArrayAdapter<String>(projectlist.this, android.R.layout.simple_list_item_1, info){
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    TextView tv = (TextView) view.findViewById(android.R.id.text1);
                    tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
                    return view;
                }
            };
            listView.setAdapter(customAdapter);

        }
    }
}