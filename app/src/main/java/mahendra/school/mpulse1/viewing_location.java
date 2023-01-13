package mahendra.school.mpulse1;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class viewing_location extends AppCompatActivity implements OnMapReadyCallback {

    GoogleMap gmap;
    MarkerOptions[] markerOptions;
    List<String> lat = new ArrayList<>();
    List<String> log = new ArrayList<>();
    List<String> info = new ArrayList<>();
    Map<String,String> hmap = new HashMap<>();

    StringBuffer sb = new StringBuffer();
    String json_url = url_interface.url+"login/MobileGetLoc";
    String json_string="";
    AutoCompleteTextView autocomplete;
    String latlong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewing_location);
        markerOptions = new MarkerOptions[1000000];
        new backgroundworker().execute();
        autocomplete = (AutoCompleteTextView)
                findViewById(R.id.editTextTextPersonName);

        findViewById(R.id.imageView25).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latlong = hmap.get(autocomplete.getText().toString());
                Log.d("ASDERF",""+latlong);
                update();
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
                log.clear();lat.clear();info.clear();
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(viewing_location.this);
                String post_data = "";
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
//            progressDialog.dismiss();
            int count=0;
            try{
                JSONArray jsonArray = new JSONArray(json_string);
                while (count<jsonArray.length()){
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    lat.add(jsonObject.getString("lat"));
                    log.add(jsonObject.getString("lon"));
                    info.add(jsonObject.getString("name")+" "+jsonObject.getString("emp_id"));
                    hmap.put(jsonObject.getString("name")+" "+jsonObject.getString("emp_id"),jsonObject.getString("lat")+","+
                            jsonObject.getString("lon"));
                    count++;
                }
            }catch (Exception e){

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>
                    (viewing_location.this,android.R.layout.select_dialog_item, info);
            autocomplete.setThreshold(1);
            autocomplete.setAdapter(adapter);
            SupportMapFragment supportMapFragment = (SupportMapFragment)
                    getSupportFragmentManager().findFragmentById(R.id.google_map);
            supportMapFragment.getMapAsync(viewing_location.this);

        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gmap = googleMap;
        for(int i=0;i<lat.size();i++){
            markerOptions[i] = new MarkerOptions();
            try {
                LatLng loc = new LatLng(Double.valueOf(lat.get(i)), Double.valueOf(log.get(i)));
                markerOptions[i].position(loc);
                markerOptions[i].title(info.get(i));
                gmap.addMarker(markerOptions[i]);
            }catch (Exception e){

            }
        }
        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(11.6340984,78.1504799),10));
//        MarkerOptions markerOptions = new MarkerOptions();
//        LatLng loc = new LatLng(11.6340984,78.1504799);
//        markerOptions.position(loc);
//        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc,10));
//        gmap.addMarker(markerOptions);
    }

    public void update(){

        String uri = String.format(Locale.ENGLISH, "geo:%f,%f", Float.valueOf(latlong.split(",")[0]),
                Float.valueOf(latlong.split(",")[1]));
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);

    }

}