package mahendra.school.mpulse1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
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

public class assets_tracker_view extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    EditText eserialno,eassets_id,eassigned_to,ecustodian;
    Spinner spin,spin2;

    long fileSize;
    ImageView imageToUpload;
    String encodedString="";
    GridView listView,listView1;
    ArrayList list = new ArrayList();
    ArrayList<String> filenamelist = new ArrayList();
    CustomAdapter customAdapter;
    CustomAdapter2 customAdapter2;
    ArrayList Lproduct = new ArrayList();
    ArrayList Lstatus = new ArrayList();

    StringBuffer sb = new StringBuffer();
    String json_url = url_interface.url+"asset_tracker/MobileCommonAssets";
    String json_string="";
    ProgressDialog progressDialog;

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"asset_tracker/MobileInsertAssets";
    String json_string2="";

    ArrayList Lasset_location_code_id = new ArrayList();
    ArrayList Lbranch = new ArrayList();
    ArrayList Lcode = new ArrayList();

    ArrayAdapter<String> adapter;
    AutoCompleteTextView actv;

    String text1="",text2="",serial_no="",assest_id="",
            assigned_to="",location="",custodian="TechTeam",encodedString1="",asset_tracker_id="",encodedString2="",encodedString3="";

    List<String> Limages = new ArrayList();
    List<String> Limages1 = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_tracker_view);

        eserialno = findViewById(R.id.editText);
        eassets_id = findViewById(R.id.editText77);
        eassigned_to = findViewById(R.id.editText8);
        ecustodian = findViewById(R.id.editText10);
        actv =  (AutoCompleteTextView)findViewById(R.id.editText9);


        progressDialog = new ProgressDialog(assets_tracker_view.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();

        Lproduct.add("CPU");
        Lproduct.add("Monitor");
        Lproduct.add("Mouse");
        Lproduct.add("Keyboard");
        Lproduct.add("Laptop");

        Lstatus.add("Working");
        Lstatus.add("Needs Repair");
        Lstatus.add("Scrap");

        listView = (GridView) findViewById(R.id.listView);

        customAdapter = new CustomAdapter();

        listView.setAdapter(customAdapter);

        spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Lproduct);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);

        spin2 = (Spinner) findViewById(R.id.spinner3);
        spin2.setOnItemSelectedListener(this);
        ArrayAdapter aa2 = new ArrayAdapter(this,android.R.layout.simple_spinner_item,Lstatus);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin2.setAdapter(aa2);

        findViewById(R.id.imageView15).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImageFromDeviceGallery();
            }
        });

        findViewById(R.id.button6).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                serial_no = eserialno.getText().toString();
                assest_id = eassets_id.getText().toString();
                assigned_to = eassigned_to.getText().toString();
                location = actv.getText().toString();

                if(serial_no.equals(""))
                    eserialno.setError("Please fill");
                if(assest_id.equals(""))
                    eassets_id.setError("Please fill");

                if(((!serial_no.equals(""))&&(!assest_id.equals("")))&&((!assigned_to.equals(""))||(!actv.equals("")))){

                    if(!location.equals("")){
                        if(!Lcode.contains(location)){
                            actv.setError("Please Fill The Valid Location");
                        }
                    }

                    for(String z1 : Limages1){
                        encodedString2 += z1+",";
                    }
                    for(String z2 : Limages){
                        encodedString3 += z2+",";
                    }
                    progressDialog = new ProgressDialog(assets_tracker_view.this);
                    progressDialog.setMessage("Please Wait...!!!");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    new backgroundworker2().execute();

                    Log.d("QSCDERFV",""+Limages1);

                }



                for (String z : filenamelist){
                    encodedString1 += z + "   ";
                }
                encodedString1.trim();


            }
        });

        Intent intenter =getIntent();
        spin.setSelection(aa.getPosition(intenter.getStringExtra("product")));
        spin2.setSelection(aa2.getPosition(intenter.getStringExtra("status")));
        eserialno.setText(intenter.getStringExtra("product_serial_no"));
        eassets_id.setText(intenter.getStringExtra("assets_id"));
        eassigned_to.setText(intenter.getStringExtra("assigned_to"));
        actv.setText(intenter.getStringExtra("location"));
        ecustodian.setText(intenter.getStringExtra("custodian"));
        asset_tracker_id = intenter.getStringExtra("asset_tracker_id");

        String temp1 = intenter.getStringExtra("images");
        int counter =0 ;
        try {
            JSONArray jsonArrayer = new JSONArray(temp1);
            while(counter < jsonArrayer.length()){
                Limages.add((String) jsonArrayer.get(counter));
                Log.d("fgfgfgfgf",""+jsonArrayer.get(counter));
                counter++;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        listView1 = (GridView) findViewById(R.id.listView1);

        customAdapter2 = new CustomAdapter2();

        listView1.setAdapter(customAdapter2);


    }

    private void pickImageFromDeviceGallery() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 200 && resultCode == Activity.RESULT_OK)
        {
            Bitmap myImg = (Bitmap) data.getExtras().get("data");
            list.add(myImg);
            customAdapter.notifyDataSetChanged();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myImg.compress(Bitmap.CompressFormat.JPEG,100 , stream);
            byte[] byte_arr = stream.toByteArray();
            fileSize = byte_arr.length;
            encodedString = Base64.encodeToString(byte_arr, 0);
            Log.d("FFFFFFFFF",""+fileSize+" "+list+"\n"+encodedString);

            filenamelist.add(encodedString);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinner)
        {
            text1 = spin.getSelectedItem().toString();
            //Toast.makeText(this, ""+text1, Toast.LENGTH_SHORT).show();
        }
        else if(parent.getId() == R.id.spinner3)
        {
            text2 = spin2.getSelectedItem().toString();
            //Toast.makeText(this, ""+text2, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return list.size();
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

            view = getLayoutInflater().inflate(R.layout.grid_single3, null);

            imageToUpload = (ImageView) view.findViewById(R.id.imageView20);

            imageToUpload.setImageBitmap((Bitmap) list.get(i));

            return view;
        }
    }

    public class CustomAdapter2 extends BaseAdapter {


        @Override
        public int getCount() {
            return Limages.size();
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

            view = getLayoutInflater().inflate(R.layout.grid_single3, null);

            imageToUpload = (ImageView) view.findViewById(R.id.imageView20);

            Picasso.with(assets_tracker_view.this)
                    .load(url_interface.url+"assets/image/asset_tracker/"+Limages.get(i))
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(imageToUpload);

            imageToUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(assets_tracker_view.this);
                    builder.setTitle("DELETE");
                    builder.setMessage("Do you want to delete this image ?").setIcon(R.mipmap.logo);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            Limages1.add(Limages.get(i));
                            Limages.remove(Limages.get(i));
                          //  Toast.makeText(assets_tracker_view.this, ""+i, Toast.LENGTH_SHORT).show();
                            customAdapter2.notifyDataSetChanged();
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int j) {
                            dialogInterface.dismiss();
                        }
                    });

                    android.app.AlertDialog dialog = builder.create();
                    dialog.getWindow().setLayout(600, 400);
                    dialog.show();

                }
            });

            Log.d("QSCFGTHJ",""+url_interface.url+"assets/image/asset_tracker/"+Limages.get(i));

            return view;
        }
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
            progressDialog.dismiss();
            int count=0;
            try {
                JSONObject jsonObject = new JSONObject(json_string);
                JSONArray jsonArray = jsonObject.getJSONArray("asset_loc_code");
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    Lasset_location_code_id.add(jo.getString("asset_location_code_id"));
                    Lbranch.add(jo.getString("branch"));
                    Lcode.add(jo.getString("code"));
                    count++;
                }
                adapter = new ArrayAdapter<String>
                        (assets_tracker_view.this,android.R.layout.select_dialog_item,Lcode);
                actv.setThreshold(1);
                actv.setAdapter(adapter);

            } catch (JSONException e) {
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
                sb2=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(assets_tracker_view.this);
                String post_data = URLEncoder.encode("branch_id","UTF-8")+"="+URLEncoder.encode(status.get_branch_id(),"UTF-8")+"&"
                        +URLEncoder.encode("product_type","UTF-8")+"="+ URLEncoder.encode(text1,"UTF-8")+"&"
                        +URLEncoder.encode("min_serial_no","UTF-8")+"="+ URLEncoder.encode(serial_no,"UTF-8")+"&"
                        +URLEncoder.encode("asset_ids","UTF-8")+"="+ URLEncoder.encode(assest_id,"UTF-8")+"&"
                        +URLEncoder.encode("assigned_to","UTF-8")+"="+ URLEncoder.encode(assigned_to,"UTF-8")+"&"
                        +URLEncoder.encode("location_floor","UTF-8")+"="+ URLEncoder.encode(location,"UTF-8")+"&"
                        +URLEncoder.encode("custodian","UTF-8")+"="+ URLEncoder.encode(custodian,"UTF-8")+"&"
                        +URLEncoder.encode("asset_status","UTF-8")+"="+ URLEncoder.encode(text2,"UTF-8")+"&"
                        +URLEncoder.encode("updated_by","UTF-8")+"="+ URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("asset_tracker_id","UTF-8")+"="+ URLEncoder.encode(asset_tracker_id,"UTF-8")+"&"
                        +URLEncoder.encode("delete_images","UTF-8")+"="+ URLEncoder.encode(encodedString2,"UTF-8")+"&"
                        +URLEncoder.encode("images","UTF-8")+"="+ URLEncoder.encode(encodedString1,"UTF-8")+"&"
                        +URLEncoder.encode("old_images","UTF-8")+"="+ URLEncoder.encode(encodedString3,"UTF-8");

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
            encodedString1="";
            if(result.equals("1")){
                Toast.makeText(assets_tracker_view.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(assets_tracker_view.this,tracker_details.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu1, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.apps:
                Intent intent = new Intent(this, tracker_details.class);
                startActivity(intent);
                return true;


            default:return super.onOptionsItemSelected(item);
        }
    }
}
