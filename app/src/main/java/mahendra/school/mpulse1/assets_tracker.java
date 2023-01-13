package mahendra.school.mpulse1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceView;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

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

public class assets_tracker extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener{

    long fileSize;
    ImageView imageToUpload;
    String encodedString="";
    GridView listView;
    ArrayList list = new ArrayList();
    ArrayList<String> filenamelist = new ArrayList();
    CustomAdapter customAdapter;
    ArrayList Lproduct = new ArrayList();
    ArrayList Lstatus = new ArrayList();
    Spinner spin,spin2;

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
    AutoCompleteTextView actv,eassets_id;

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = url_interface.url+"emp_asset_tracker/getAssetsIds";
    String json_string3="";
    ArrayList Lassets_list = new ArrayList();
    ArrayList Ltotal_assets_list = new ArrayList();
    String product_name="";
    int count = 0;

    String text1="",text2="",serial_no="",assest_id="",
            assigned_to="",location="",custodian="TechTeam",encodedString1="";

    private CameraSource mCameraSource;
    private SurfaceView mSurfaceView;
    private static final int RC_HANDLE_CAMERA_PERM = 2;
    Bitmap bmmg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_tracker);
        new backgroundworker().execute();

        eassets_id = (AutoCompleteTextView)assets_tracker.this.findViewById(R.id.editText77);

        mSurfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        mSurfaceView.setVisibility(View.GONE);

        ImageView scanner = (ImageView)findViewById(R.id.imageView23);

        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    startCameraSource();
            }
        });

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

                EditText eserialno = findViewById(R.id.editText);
                EditText eassigned_to = findViewById(R.id.editText8);

                serial_no = eserialno.getText().toString();
                assest_id = eassets_id.getText().toString();
                assigned_to = eassigned_to.getText().toString();
                location = actv.getText().toString();

                if(serial_no.equals(""))
                    eserialno.setError("Please fill");
                if(assest_id.equals(""))
                    eassets_id.setError("Please fill");

                if(Lassets_list.contains(assest_id)){
                if(((!serial_no.equals(""))&&(!assest_id.equals("")))&&((!assigned_to.equals(""))||(!actv.equals("")))){

                    if(!location.equals("")){
                        if(!Lcode.contains(location)){
                            actv.setError("Please Fill The Valid Location");
                        }
                    }

                            progressDialog = new ProgressDialog(assets_tracker.this);
                            progressDialog.setMessage("Please Wait...!!!");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(false);
                            new backgroundworker2().execute();

                }}



                for (String z : filenamelist){
                    encodedString1 += z + "   ";
                }
                encodedString1.trim();


            }
        });
    }

    private void startCameraSource() {

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 300);
        //Create the TextRecognizer


//        if (!textRecognizer.isOperational()) {
//            Log.w("", "Detector dependencies not loaded yet");
//        } else {
//
//            //Initialize camerasource to use high resolution and set Autofocus on.
//            mCameraSource = new CameraSource.Builder(getApplicationContext(), textRecognizer)
//                    .setFacing(CameraSource.CAMERA_FACING_BACK)
//                    .setRequestedPreviewSize(1280, 1024)
//                    .setAutoFocusEnabled(true)
//                    .setRequestedFps(2.0f)
//                    .build();
//
//            /**
//             * Add call back to SurfaceView and check if camera permission is granted.
//             * If permission is granted we can start our cameraSource and pass it to surfaceView
//             */
//            mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
//                @Override
//                public void surfaceCreated(SurfaceHolder holder) {
//                    try {
//
//                        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
//                                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//
//                            ActivityCompat.requestPermissions(assets_tracker.this,
//                                    new String[]{Manifest.permission.CAMERA},
//                                    RC_HANDLE_CAMERA_PERM);
//                            return;
//                        }
//                        mCameraSource.start(mSurfaceView.getHolder());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//
//                @Override
//                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//                }
//
//                /**
//                 * Release resources for cameraSource
//                 */
//                @Override
//                public void surfaceDestroyed(SurfaceHolder holder) {
//                    mCameraSource.stop();
//                }
//            });
//
//            //Set the TextRecognizer's Processor.
//            textRecognizer.setProcessor(new Detector.Processor<TextBlock>() {
//                @Override
//                public void release() {
//                }
//
//                /**
//                 * Detect all the text from camera using TextBlock and the values into a stringBuilder
//                 * which will then be set to the textView.
//                 * */
//                @Override
//                public void receiveDetections(Detector.Detections<TextBlock> detections) {
//                    final SparseArray<TextBlock> items = detections.getDetectedItems();
//                    if (items.size() != 0 ){
//                        eassets_id.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                StringBuilder stringBuilder = new StringBuilder();
//                                for(int i=0;i<items.size();i++){
//                                    TextBlock item = items.valueAt(i);
//                                    stringBuilder.append(item.getValue());
//                                    stringBuilder.append("\n");
//                                }
//                                eassets_id.setText(stringBuilder.toString());
//                            }
//                        });
//                    }
//                }
//            });
//        }
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

        if (requestCode == 300 && resultCode == Activity.RESULT_OK)
        {
            bmmg = (Bitmap) data.getExtras().get("data");
            final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            Frame imageFrame = new Frame.Builder()

                    .setBitmap(bmmg)                 // your image bitmap
                    .build();

            String imageText = "";


            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);

            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                imageText = textBlock.getValue();                   // return string
                eassets_id.setText(imageText);
            }
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId() == R.id.spinner)
        {
            text1 = spin.getSelectedItem().toString();
            product_name = spin.getSelectedItem().toString();
            progressDialog = new ProgressDialog(assets_tracker.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            new backgroundworker3().execute();
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
                        (assets_tracker.this,android.R.layout.select_dialog_item,Lcode);
                actv =  (AutoCompleteTextView)findViewById(R.id.editText9);
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
                final SessionMaintance status=new SessionMaintance(assets_tracker.this);
                String post_data = URLEncoder.encode("branch_id","UTF-8")+"="+URLEncoder.encode(status.get_branch_id(),"UTF-8")+"&"
                        +URLEncoder.encode("product_type","UTF-8")+"="+ URLEncoder.encode(text1,"UTF-8")+"&"
                        +URLEncoder.encode("min_serial_no","UTF-8")+"="+ URLEncoder.encode(serial_no,"UTF-8")+"&"
                        +URLEncoder.encode("asset_ids","UTF-8")+"="+ URLEncoder.encode(assest_id,"UTF-8")+"&"
                        +URLEncoder.encode("assigned_to","UTF-8")+"="+ URLEncoder.encode(assigned_to,"UTF-8")+"&"
                        +URLEncoder.encode("location_floor","UTF-8")+"="+ URLEncoder.encode(location,"UTF-8")+"&"
                        +URLEncoder.encode("custodian","UTF-8")+"="+ URLEncoder.encode(custodian,"UTF-8")+"&"
                        +URLEncoder.encode("asset_status","UTF-8")+"="+ URLEncoder.encode(text2,"UTF-8")+"&"
                        +URLEncoder.encode("updated_by","UTF-8")+"="+ URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("images","UTF-8")+"="+ URLEncoder.encode(encodedString1,"UTF-8")+"&"
                        +URLEncoder.encode("asset_tracker_id","UTF-8")+"="+ URLEncoder.encode("0","UTF-8");
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
                Toast.makeText(assets_tracker.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class backgroundworker3 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url3);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                Lassets_list.clear();
                sb3=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("product_name","UTF-8")+"="+URLEncoder.encode(product_name,"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string3=bufferedReader.readLine())!=null)
                {
                    sb3.append(json_string3+"\n");
                    Log.d("json_string",""+json_string3);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb3.toString());
                return sb3.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string3 = result;
            progressDialog.dismiss();
            int count=0;
            try {
                JSONArray jsonArray = new JSONArray(json_string3);
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    Lassets_list.add(jo.getString("asset_ids"));
                    Ltotal_assets_list.add(jo.getString("asset_ids"));

                    count++;
                }

                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                        (assets_tracker.this,android.R.layout.select_dialog_item,Lassets_list);
                eassets_id.setThreshold(1);
                eassets_id.setAdapter(adapter1);

            } catch (JSONException e) {
                e.printStackTrace();
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
