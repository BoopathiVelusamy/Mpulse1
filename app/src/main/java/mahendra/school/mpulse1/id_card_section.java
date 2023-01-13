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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class id_card_section extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url = url_interface.url+"profile/idCardCheck";
    String json_string="";
    ProgressDialog progressDialog;

    List<String> Lwalkin_id=new ArrayList<>();
    List<String> Lemp_id=new ArrayList<>();
    List<String> Lname=new ArrayList<>();
    List<String> Ldob=new ArrayList<>();
    List<String> Lblood_group=new ArrayList<>();
    List<String> Laddress=new ArrayList<>();
    List<String> Lissued=new ArrayList<>();

    ListView listView;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_id_card_section);

        listView=(ListView)findViewById(R.id.listView);

        progressDialog = new ProgressDialog(id_card_section.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(Lissued.get(position).equals("null")){
                    Intent intent = new Intent(id_card_section.this, upload_id_card.class);
                    intent.putExtra("emp_id", Lemp_id.get(position));
                    String temp = Lname.get(position);
                    intent.putExtra("name", temp);
                    intent.putExtra("dob", Ldob.get(position));
                    intent.putExtra("bg", Lblood_group.get(position));
                    intent.putExtra("add", Laddress.get(position));
                    intent.putExtra("wid", Lwalkin_id.get(position));
                    startActivity(intent);
                }else {

                }
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
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(id_card_section.this);
                String post_data = URLEncoder.encode("branch_id","UTF-8")+"="+URLEncoder.encode(status.get_branch_id(),"UTF-8");
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
            try {
                JSONArray jsonArray = new JSONArray(json_string);
                while(count<jsonArray.length()){
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    Lemp_id.add(jsonObject.getString("emp_id"));
                    Lname.add(jsonObject.getString("name")+" "+
                            jsonObject.getString("middle_name")+" "+
                            jsonObject.getString("last_name"));

                    Ldob.add(jsonObject.getString("original_dob"));
                    Lblood_group.add(jsonObject.getString("blood_group"));
                    Laddress.add(jsonObject.getString("address"));

                    Lwalkin_id.add(jsonObject.getString("walkin_id"));

                    Lissued.add(jsonObject.getString("issued"));
                    count++;
                }
                if(count>0){
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
        public int getItemViewType(int position) {

            return position;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            view = getLayoutInflater().inflate(R.layout.custom_layout_id_card, null);

            CardView cardView = (CardView)view.findViewById(R.id.cardView);

            TextView name = (TextView) view.findViewById(R.id.textView19);

            name.setText(Lemp_id.get(i)+"-"+Lname.get(i));

            if(Lissued.get(i).equals("null")||Lissued.get(i).equals("")){

                view.findViewById(R.id.imageView26).setVisibility(View.VISIBLE);
            }
            else{
                view.findViewById(R.id.imageView26).setVisibility(View.INVISIBLE);
            }

            return view;
        }
    }
}