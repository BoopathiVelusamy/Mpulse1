package mahendra.school.mpulse1;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import net.gotev.uploadservice.MultipartUploadRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
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
import java.util.UUID;

public class vaccination_certificate extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener,SingleUploadBroadcastReceiver.Delegate{

    private Button buttonChoose;
    private Button buttonUpload;
    public static final String UPLOAD_URL = url_interface.url+"vaccine_certificate/mobileinsertCertificate";
    private int PICK_PDF_REQUEST = 1;
    private static final int STORAGE_PERMISSION_CODE = 123;
    private Uri filePath;

    List<String> temp1 = new ArrayList<>();
    List<String> temp2 = new ArrayList<>();
    List<String> temp3 = new ArrayList<>();
    List<String> temp4 = new ArrayList<>();

    TextView txt71,txt74,txt7231,txt7234;

    List<String> dose = new ArrayList<String>();
    String which_dose="";
    Spinner spin;
    TextView txt76;

    StringBuffer sb = new StringBuffer();
    String json_url = url_interface.url+"vaccine_certificate/mobileCheckExist";
    String json_string="";
    ProgressDialog progressDialog;
    String fst_dose_count="",second_dose_count="";

    ArrayAdapter aa;
    private static final String TAG = "AndroidUploadService";

    private final SingleUploadBroadcastReceiver uploadReceiver =
            new SingleUploadBroadcastReceiver();

    @Override
    protected void onResume() {
        super.onResume();
        uploadReceiver.register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        uploadReceiver.unregister(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_vaccination_certificate);
        getSupportActionBar().setTitle("VACCINATION CERTIFICATE");

        requestStoragePermission();

        txt71 = findViewById(R.id.textView71);
        txt74 = findViewById(R.id.textView74);
        txt7231 = findViewById(R.id.textView7231);
        txt7234 = findViewById(R.id.textView7234);


        dose.add("1st Dose");
        dose.add("2nd Dose");

        //Initializing views
        buttonChoose = (Button) findViewById(R.id.button11);
        buttonUpload = (Button) findViewById(R.id.button12);

        txt76 = findViewById(R.id.textView76);


        buttonChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadMultipart();
            }
        });

        spin = (Spinner) findViewById(R.id.spinner9);
        spin.setOnItemSelectedListener(this);
        aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,dose);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);


        progressDialog = new ProgressDialog(vaccination_certificate.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();
    }

    public void uploadMultipart() {
        //getting name for the image

        progressDialog = new ProgressDialog(vaccination_certificate.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        //getting the actual path of the image
        String path = null;
        try {
            path = FilePath.getUriRealPath(this, filePath);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (path == null) {

            Toast.makeText(this, "Please move your .pdf file to internal storage and retry", Toast.LENGTH_LONG).show();
        } else {
            //Uploading code
            try {
                String uploadId = UUID.randomUUID().toString();
                uploadReceiver.setDelegate(this);
                uploadReceiver.setUploadID(uploadId);
                SessionMaintance status = new SessionMaintance(vaccination_certificate.this);

                //Creating a multi part request
                new MultipartUploadRequest(this, uploadId, UPLOAD_URL)
                        .addFileToUpload(path, "vaccine_certificate") //Adding file
                        .addParameter("vaccine_type", which_dose)
                        .addParameter("walkin_id", status.get_field_id())
                        .addParameter("emp_id", status.get_username())
                        .setMaxRetries(2)
                        .startUpload(); //Starting the upload


            } catch (Exception exc) {
                Toast.makeText(this, exc.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    public void onProgress(int progress) {
        //your implementation
    }

    @Override
    public void onProgress(long uploadedBytes, long totalBytes) {
        //your implementation
    }

    @Override
    public void onError(Exception exception) {
        //your implementation
        Log.d("ASASASA!@##$$s",""+exception);
       progressDialog.dismiss();
    }

    @Override
    public void onCompleted(int serverResponseCode, byte[] serverResponseBody) {
        //your implementation
        Log.d("ASASAS",""+serverResponseBody+"\n\n"+serverResponseCode);
        progressDialog.dismiss();
        Toast.makeText(this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(vaccination_certificate.this,mainmenu.class);
        intent.putExtra("TYPE","z");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        progressDialog = new ProgressDialog(vaccination_certificate.this);
//        progressDialog.setMessage("Please Wait...!!!");
//        progressDialog.show();
//        progressDialog.setCanceledOnTouchOutside(false);
//        new backgroundworker().execute();
    }

    @Override
    public void onCancelled() {
        //your implementation
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_PDF_REQUEST);
    }

    //handling the image chooser activity result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PDF_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            txt76.setText(filePath.getPath());
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }
        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        //Checking the request code of our request
        if (requestCode == STORAGE_PERMISSION_CODE) {

            //If permission is granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Displaying a toast
                Toast.makeText(this, "Permission granted now you can read the storage", Toast.LENGTH_LONG).show();
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(this, "Oops you just denied the permission", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(getApplicationContext(), spin.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        which_dose = spin.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                temp1.clear();temp2.clear();temp3.clear();temp4.clear();
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                SessionMaintance status = new SessionMaintance(vaccination_certificate.this);
                String post_data =URLEncoder.encode("walkin_id","UTF-8")+"="+ URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("emp_id","UTF-8")+"="+ URLEncoder.encode(status.get_username(),"UTF-8");
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
                fst_dose_count = jsonObject.getString("fst_dose_count");
                second_dose_count = jsonObject.getString("sec_dose_count");
                JSONArray jsonArray = new JSONArray(jsonObject.getString("check"));
                while (count < jsonArray.length()){
                    JSONObject jo1 = jsonArray.getJSONObject(count);
                    temp1.add(jo1.getString("approve_status"));
                    temp2.add(jo1.getString("comments"));
                    temp3.add(jo1.getString("certificate"));
                    temp4.add(jo1.getString("dose"));
                    count++;
                }
                //Toast.makeText(vaccination_certificate.this, ""+temp1.size(), Toast.LENGTH_SHORT).show();

                //Toast.makeText(vaccination_certificate.this, ""+fst_dose_count+"\n"+second_dose_count, Toast.LENGTH_SHORT).show();
            }catch (Exception e){

            }




            if(temp1.size()==0){
                findViewById(R.id.doser1).setVisibility(View.GONE);
                findViewById(R.id.doser2).setVisibility(View.GONE);
            }else if(temp1.size()==2){
                findViewById(R.id.doser1).setVisibility(View.VISIBLE);
                findViewById(R.id.doser2).setVisibility(View.VISIBLE);
                if(temp1.get(0).equals("0")) {
                    txt71.setText("-Waiting for Approval");
                }else if(temp1.get(0).equals("1")) {
                    txt71.setText("-Approved");
                }else if(temp1.get(0).equals("2")) {
                    txt71.setText("-Rejected");
                }
                if(temp1.get(1).equals("0")) {
                    txt7231.setText("-Waiting for Approval");
                }else if(temp1.get(1).equals("1")) {
                    txt7231.setText("-Approved");
                }else if(temp1.get(1).equals("2")) {
                    txt7231.setText("-Rejected");
                }
                txt74.setText(temp2.get(0));
                txt7234.setText(temp2.get(1));


                findViewById(R.id.doser1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url_interface.url+"assets/vaccine_certificate/"+temp3.get(0)));
                        startActivity(i);

                    }
                });
                findViewById(R.id.doser2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url_interface.url+"assets/vaccine_certificate/"+temp3.get(1)));
                        startActivity(i);

                    }
                });

            }else if(temp1.size()==1){
                if(temp4.get(0).equals("1st Dose")) {
                    findViewById(R.id.doser1).setVisibility(View.VISIBLE);
                    findViewById(R.id.doser2).setVisibility(View.GONE);
                    if(temp1.get(0).equals("0")) {
                        txt71.setText("-Waiting for Approval");
                    }else if(temp1.get(0).equals("1")) {
                        txt71.setText("-Approved");
                    }else if(temp1.get(0).equals("2")) {
                        txt71.setText("-Rejected");
                    }
                    txt74.setText(temp2.get(0));

                    findViewById(R.id.doser1).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url_interface.url+"assets/vaccine_certificate/"+temp3.get(0)));
                            startActivity(i);

                        }
                    });

                }if(temp4.get(0).equals("2nd Dose")) {
                    findViewById(R.id.doser1).setVisibility(View.GONE);
                    findViewById(R.id.doser2).setVisibility(View.VISIBLE);
                    if(temp1.get(0).equals("0")) {
                        txt7231.setText("-Waiting for Approval");
                    }else if(temp1.get(0).equals("1")) {
                        txt7231.setText("-Approved");
                    }else if(temp1.get(0).equals("2")) {
                        txt7231.setText("-Rejected");
                    }
                    txt7234.setText(temp2.get(0));

                    findViewById(R.id.doser2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setData(Uri.parse(url_interface.url+"assets/vaccine_certificate/"+temp3.get(0)));
                            startActivity(i);

                        }
                    });
                }
            }

            if(fst_dose_count.equals("0")&&second_dose_count.equals("0")){

            }else if(fst_dose_count.equals("1")&&second_dose_count.equals("0")){
                dose.remove(0);
                aa.notifyDataSetChanged();
                spin.setOnItemSelectedListener(vaccination_certificate.this);
                aa = new ArrayAdapter(vaccination_certificate.this,android.R.layout.simple_spinner_item,dose);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);
            }else if(fst_dose_count.equals("0")&&second_dose_count.equals("1")){
                dose.remove(1);
                aa.notifyDataSetChanged();
                spin.setOnItemSelectedListener(vaccination_certificate.this);
                aa = new ArrayAdapter(vaccination_certificate.this,android.R.layout.simple_spinner_item,dose);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);
            }else if(fst_dose_count.equals("1")&&second_dose_count.equals("1")){
                dose.clear();
                aa.notifyDataSetChanged();
                spin.setOnItemSelectedListener(vaccination_certificate.this);
                aa = new ArrayAdapter(vaccination_certificate.this,android.R.layout.simple_spinner_item,dose);
                aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spin.setAdapter(aa);
                findViewById(R.id.button12).setVisibility(View.GONE);
            }

        }
    }
}