package mahendra.school.mpulse1;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class employee_assets_tracker extends AppCompatActivity {

    private CameraSource mCameraSource;
    private SurfaceView mSurfaceView;
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    EditText emp_id;
    AutoCompleteTextView actv;
    Button submit;
    ImageView addbutton;
    LinearLayout linearLayout;

    ArrayList<String> spinner_list = new ArrayList<>();

    ArrayList<String> spinner_data_list = new ArrayList<>();
    ArrayList<String> asset_data_list = new ArrayList<>();
    ArrayList<String> serialno_data_list = new ArrayList<>();

    HashMap<String,String> spinner_data_map = new HashMap<>();
    HashMap<String,String> asset_data_map = new HashMap<>();
    HashMap<String,String> serialno_data_map = new HashMap<>();

    ArrayList<Service_Serialize> serviceSerializeArrayList = new ArrayList<>();

    String spinner_selected_item;

    String spinner_selected_data,asset_selected_data,serialno_selected_data;

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"emp_asset_tracker/MobileInsertEmp_Assets";
    String json_string2="";
    ProgressDialog progressDialog;

    StringBuffer sb = new StringBuffer();
    String json_url = url_interface.url+"asset_tracker/MobileCommonAssets";
    String json_string="";

    StringBuffer sb3 = new StringBuffer();
    String json_url3 = url_interface.url+"emp_asset_tracker/getAssetsIds";
    String json_string3="";
    ArrayList Lassets_list = new ArrayList();
    ArrayList Ltotal_assets_list = new ArrayList();


    ArrayList Lasset_location_code_id = new ArrayList();
    ArrayList Lbranch = new ArrayList();
    ArrayList Lcode = new ArrayList();

    ArrayAdapter<String> adapter;

    String emper_id="",location="";

    String product_name="",working_in="";

    int count = 0;

    Button full_day,halfer_day;

    View descriptionview;

    int countermax = -1;

    Bitmap bmmg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_assets_tracker);

        actv = (AutoCompleteTextView)findViewById(R.id.et_location);
        emp_id = (EditText)findViewById(R.id.et_empid);
        addbutton = (ImageView) findViewById(R.id.addbutton);
        submit = (Button)findViewById(R.id.buttonsubmit);
        linearLayout = (LinearLayout)findViewById(R.id.layout_list);

        actv.setVisibility(View.GONE);

        full_day = findViewById(R.id.button4);
        halfer_day = findViewById(R.id.button5);

        full_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full_day.setBackgroundResource(R.drawable.vertical_divider2);
                full_day.setTextColor(Color.WHITE);
                halfer_day.setBackgroundResource(R.drawable.vertical_divider1);
                halfer_day.setTextColor(Color.parseColor("#6200EA"));
                working_in = "Office";
                actv.setVisibility(View.VISIBLE);
            }
        });

        halfer_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                full_day.setBackgroundResource(R.drawable.vertical_divider1);
                full_day.setTextColor(Color.parseColor("#6200EA"));
                halfer_day.setBackgroundResource(R.drawable.vertical_divider2);
                halfer_day.setTextColor(Color.WHITE);
                working_in = "Home";
                actv.setVisibility(View.GONE);

            }
        });


        spinner_list.add("CPU");
        spinner_list.add("Monitor");
        spinner_list.add("Mouse");
        spinner_list.add("Keyboard");
        spinner_list.add("Laptop");

        SessionMaintance sessionMaintance = new SessionMaintance(employee_assets_tracker.this);
        emp_id.setText(sessionMaintance.get_username());

        addview();

        addbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addview();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkValid()){
                    emper_id = emp_id.getText().toString();
                    location = actv.getText().toString();
                    //Toast.makeText(employee_assets_tracker.this,"Success",Toast.LENGTH_LONG).show();
                    if(working_in.length()!=0) {
                        if ((!location.equals("") && emper_id.equals("")) || (location.equals("") && !emper_id.equals("")) || (!location.equals("") && !emper_id.equals(""))) {
                            progressDialog = new ProgressDialog(employee_assets_tracker.this);
                            progressDialog.setMessage("Please Wait...!!!");
                            progressDialog.show();
                            progressDialog.setCanceledOnTouchOutside(false);
                            new backgroundworker2().execute();
                        }
                    }

                }else {
                    Toast.makeText(employee_assets_tracker.this,"Please Enter all details",Toast.LENGTH_LONG).show();
                }
            }
        });

        new backgroundworker().execute();
    }

    private void addview(){

        countermax++;
        descriptionview = getLayoutInflater().inflate(R.layout.row_item,null,false);
        final Spinner spinner =(Spinner)descriptionview.findViewById(R.id.spinnerdata);
        final AutoCompleteTextView ass_id = (AutoCompleteTextView)descriptionview.findViewById(R.id.et_assetid);
        EditText serial_no = (EditText)descriptionview.findViewById(R.id.et_serialno);
        final ImageView remove = (ImageView)descriptionview.findViewById(R.id.removeitem);
        ImageView scanner = (ImageView)descriptionview.findViewById(R.id.imageView23);
        mSurfaceView = (SurfaceView)descriptionview.findViewById(R.id.surfaceView);
        mSurfaceView.setVisibility(View.GONE);
        remove.setTag(countermax);


        scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCameraSource();
            }
        });

        final ArrayAdapter spinneradapter = new ArrayAdapter(employee_assets_tracker.this,android.R.layout.simple_spinner_item,spinner_list);
        spinneradapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinneradapter);



        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ass_id.setText("");
                ArrayAdapter<String> adapter1 = new ArrayAdapter<String>
                        (employee_assets_tracker.this,android.R.layout.select_dialog_item,Lassets_list);
                ass_id.setThreshold(1);
                ass_id.setAdapter(adapter1);
                spinner_selected_item = spinner_list.get(position);
                product_name = spinner.getSelectedItem().toString();
                progressDialog = new ProgressDialog(employee_assets_tracker.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                new backgroundworker3().execute();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        linearLayout.addView(descriptionview);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int c = (int) remove.getTag();
                Toast.makeText(employee_assets_tracker.this, ""+c, Toast.LENGTH_SHORT).show();
                try{
                linearLayout.removeViewAt(c);}
                catch (Exception e){
                    linearLayout.removeViewAt(linearLayout.getChildCount()-1);
                }

            }
        });

    }


    private void startCameraSource() {

        //Create the TextRecognizer
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 300);

        final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

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
//                            ActivityCompat.requestPermissions(employee_assets_tracker.this,
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
//                    final AutoCompleteTextView ass_id = (AutoCompleteTextView)descriptionview.findViewById(R.id.et_assetid);
//                    if (items.size() != 0 ){
//                        ass_id.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                StringBuilder stringBuilder = new StringBuilder();
//                                for(int i=0;i<items.size();i++){
//                                    TextBlock item = items.valueAt(i);
//                                    stringBuilder.append(item.getValue());
//                                    stringBuilder.append("\n");
//                                }
//                                ass_id.setText(stringBuilder.toString());
//                            }
//                        });
//                    }
//                }
//            });
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 300 && resultCode == Activity.RESULT_OK)
        {
            bmmg = (Bitmap) data.getExtras().get("data");
            final TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
            Frame imageFrame = new Frame.Builder()

                    .setBitmap(bmmg)                 // your image bitmap
                    .build();

            String imageText = "";


            SparseArray<TextBlock> textBlocks = textRecognizer.detect(imageFrame);
            final AutoCompleteTextView ass_id = (AutoCompleteTextView)descriptionview.findViewById(R.id.et_assetid);
            for (int i = 0; i < textBlocks.size(); i++) {
                TextBlock textBlock = textBlocks.get(textBlocks.keyAt(i));
                imageText = textBlock.getValue();                   // return string
                ass_id.setText(imageText);
            }
        }

    }


    public boolean checkValid(){

        boolean result = true;

        for(int i=0; i<linearLayout.getChildCount();i++){
            View descriptionview =linearLayout.getChildAt(i);
            final Spinner spinner =(Spinner)descriptionview.findViewById(R.id.spinnerdata);
            AutoCompleteTextView ass_id = (AutoCompleteTextView)descriptionview.findViewById(R.id.et_assetid);
            EditText serial_no = (EditText)descriptionview.findViewById(R.id.et_serialno);

            Service_Serialize service_serialize = new Service_Serialize();
            if(Ltotal_assets_list.contains(ass_id.getText().toString())||(ass_id.getText().length()==0)) {
                if (spinner.getSelectedItem().toString().length()!= 0 && (!ass_id.getText().toString().equals("") && serial_no.getText().toString().equals("")
                        || (ass_id.getText().toString().equals("") && !serial_no.getText().toString().equals("")) || (!ass_id.getText().toString().equals("") && !serial_no.getText().toString().equals("")))
                ) {
                    service_serialize.setDataspinner(spinner.getSelectedItem().toString());
                    service_serialize.setAssetid(ass_id.getText().toString());
                    service_serialize.setSerialno(serial_no.getText().toString());
                } else {
                    Toast.makeText(this, "Please Fill Either Serial Number Or Asset Id", Toast.LENGTH_SHORT).show();
                    result = false;
                    break;
                }
            }else{
                ass_id.setError("Not a valid Asset's id");
                result = false;
                break;
            }

            serviceSerializeArrayList.add(service_serialize);
            spinner_data_map.put(String.valueOf(i),service_serialize.getDataspinner());
            asset_data_map.put(String.valueOf(i),service_serialize.getAssetid());
            serialno_data_map.put(String.valueOf(i),service_serialize.getSerialno());

            JSONObject jsonObject = new JSONObject(spinner_data_map);
            spinner_selected_data = jsonObject.toString();
            Log.d("SPINNERDATA",spinner_selected_data);

            JSONObject jsonObject1 = new JSONObject(asset_data_map);
            asset_selected_data = jsonObject1.toString();
            Log.d("ASSETDATA",asset_selected_data);

            JSONObject jsonObject2 = new JSONObject(serialno_data_map);
            serialno_selected_data = jsonObject2.toString();
            Log.d("SERIALDATA",serialno_selected_data);
        }



        return result;
    }

    public class Service_Serialize implements Serializable {

        public String dataspinner;
        public String assetid;
        public String serialno;

        public String getDataspinner() {
            return dataspinner;
        }

        public void setDataspinner(String dataspinner) {
            this.dataspinner = dataspinner;
        }

        public String getAssetid() {
            return assetid;
        }

        public void setAssetid(String assetid) {
            this.assetid = assetid;
        }

        public String getSerialno() {
            return serialno;
        }

        public void setSerialno(String serialno) {
            this.serialno = serialno;
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
                        (employee_assets_tracker.this,android.R.layout.select_dialog_item,Lcode);
                actv.setThreshold(1);
                actv.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
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
                final SessionMaintance status=new SessionMaintance(employee_assets_tracker.this);
                String post_data = URLEncoder.encode("location","UTF-8")+"="+URLEncoder.encode(location,"UTF-8")+"&"
                        +URLEncoder.encode("emp_id","UTF-8")+"="+ URLEncoder.encode(emper_id,"UTF-8")+"&"
                        +URLEncoder.encode("product","UTF-8")+"="+ URLEncoder.encode(spinner_selected_data,"UTF-8")+"&"
                        +URLEncoder.encode("asset_id","UTF-8")+"="+ URLEncoder.encode(asset_selected_data,"UTF-8")+"&"
                        +URLEncoder.encode("serial_number","UTF-8")+"="+ URLEncoder.encode(serialno_selected_data,"UTF-8")+"&"
                        +URLEncoder.encode("branch_id","UTF-8")+"="+ URLEncoder.encode(status.get_branch_id(),"UTF-8")+"&"
                        +URLEncoder.encode("working_in","UTF-8")+"="+ URLEncoder.encode(working_in,"UTF-8");
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
            if(result.equals("1")){
                Toast.makeText(employee_assets_tracker.this, "Successfully Inserted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(employee_assets_tracker.this,employee_assets_tracker.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }
}
