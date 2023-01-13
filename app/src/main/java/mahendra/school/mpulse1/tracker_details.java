package mahendra.school.mpulse1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

public class tracker_details extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"asset_tracker/getTrackerDetails";
    ProgressDialog progressDialog;
    String json_string="";

    ListView listView;
    CustomAdapter customAdapter;

    List<String> Lasset_tracker_id = new ArrayList<>();
    List<String> Lproduct = new ArrayList<>();
    List<String> LMIN = new ArrayList<>();
    List<String> LID = new ArrayList<>();
    List<String> LAssigned = new ArrayList<>();
    List<String> LLocation = new ArrayList<>();
    List<String> LCustodian = new ArrayList<>();
    List<String> LStatus = new ArrayList<>();
    List<String> Limages = new ArrayList<>();

    String assets_tracker_id = "";

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"asset_tracker/DeleteTrackerDetails";
    String json_string2="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker_details);

        listView=(ListView)findViewById(R.id.listView);

        progressDialog = new ProgressDialog(tracker_details.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();

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
                Lasset_tracker_id.clear();Lproduct.clear();LMIN.clear();LID.clear();LAssigned.clear();
                LLocation.clear();LCustodian.clear();LStatus.clear();Limages.clear();
                assets_tracker_id = "";
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(tracker_details.this);
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
                JSONArray jsonArray = new JSONArray(json_string);
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    Lasset_tracker_id.add(jo.getString("asset_tracker_id"));
                    Lproduct.add(jo.getString("product_type"));
                    LMIN.add(jo.getString("min_serial_no"));
                    LID.add(jo.getString("asset_ids"));
                    LAssigned.add(jo.getString("assigned_to"));
                    LLocation.add(jo.getString("location_floor"));
                    LCustodian.add(jo.getString("custodian"));
                    LStatus.add(jo.getString("asset_status"));
                    Limages.add(jo.getString("images"));
                    count++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if(Lasset_tracker_id.size()>0) {
                customAdapter = new CustomAdapter();
                listView.setAdapter(customAdapter);
            }

        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lproduct.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_layout_asset_tracker, null);

            TextView txt1 = (TextView)view.findViewById(R.id.textView19);
            TextView txt2 = (TextView)view.findViewById(R.id.textView29);
            TextView txt3 = (TextView)view.findViewById(R.id.textView30);
            TextView txt4 = (TextView)view.findViewById(R.id.textView40);

            ImageView delete_assets = (ImageView)view.findViewById(R.id.imageView5);

            ImageView assets_image_view = (ImageView)view.findViewById(R.id.imageView21);

            delete_assets.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(tracker_details.this, ""+Lasset_tracker_id.get(i), Toast.LENGTH_SHORT).show();
                    assets_tracker_id = Lasset_tracker_id.get(i);
                    progressDialog = new ProgressDialog(tracker_details.this);
                    progressDialog.setMessage("Please Wait...!!!");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    new backgroundworker2().execute();
                }
            });

            assets_image_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(tracker_details.this, ""+Lasset_tracker_id.get(i), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(tracker_details.this,assets_tracker_view.class);
                    intent.putExtra("asset_tracker_id",Lasset_tracker_id.get(i));
                    intent.putExtra("product",Lproduct.get(i));
                    intent.putExtra("status",LStatus.get(i));
                    intent.putExtra("product_serial_no",LMIN.get(i));
                    intent.putExtra("assets_id",LID.get(i));
                    intent.putExtra("assigned_to",LAssigned.get(i));
                    intent.putExtra("location",LLocation.get(i));
                    intent.putExtra("custodian",LCustodian.get(i));
                    intent.putExtra("images",Limages.get(i));
                    startActivity(intent);

                }
            });


            txt1.setText(Lproduct.get(i));
            txt2.setText("MIN : "+LMIN.get(i)+" - "+"ID : "+LID.get(i));
            txt3.setText("Assigned to : "+LAssigned.get(i)+" - "+"Location : "+LLocation.get(i));
            txt4.setText("Custodian : "+LCustodian.get(i)+" - "+"Status : "+LStatus.get(i));


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
                String post_data = URLEncoder.encode("tracker_id","UTF-8")+"="+URLEncoder.encode(assets_tracker_id,"UTF-8");
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
            progressDialog = new ProgressDialog(tracker_details.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            listView.setAdapter(null);
            new backgroundworker().execute();

        }
    }

}
