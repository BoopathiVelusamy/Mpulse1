package mahendra.school.mpulse1;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.chrisbanes.photoview.PhotoView;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class upload_id_card extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url = url_interface.url+"profile/idCardInsert";
    String json_string="";

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"join_report/MobileInsertProfile";
    String json_string2="";

    ProgressDialog progressDialog;

    Button take_photo_btn, remove_bg_btn, save_btn;
    ImageView take_img, remove_bg_img;
    PhotoView take_img1;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;
    final int CAMERA_CAPTURE = 1;
    private static final int GALLERY_REQUEST = 300;
    public static final int PICK_IMAGE = 100;
    String cameraPermission[];
    String storagePermission[];
    Bitmap thumbnail;
    Bitmap resizedBitmap;
    String dirpath;
    File f,f1;
    String encImage="";

    String emp_id_s="",wid_s="";
    TextView dob,bg,add,name,id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_id_card);

        remove_bg_btn = findViewById(R.id.btn_removebg);
        save_btn = findViewById(R.id.save_btn);
        take_img = findViewById(R.id.img_takeimg);
        remove_bg_img = findViewById(R.id.img_removebgimg);
        take_img1 = findViewById(R.id.img_takeimg1);

        Intent intent = getIntent();

        dob = findViewById(R.id.dob_t);
        bg = findViewById(R.id.bg_t);
        add = findViewById(R.id.add_t);
        name = findViewById(R.id.name_t);
        id = findViewById(R.id.id_t);

        dob.setText(intent.getStringExtra("dob"));
        bg.setText(intent.getStringExtra("bg"));
        add.setText(intent.getStringExtra("add"));
        String temp = intent.getStringExtra("name");
        name.setText(temp);
        id.setText(intent.getStringExtra("emp_id"));

        emp_id_s = intent.getStringExtra("emp_id");
        wid_s = intent.getStringExtra("wid");


        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};




        remove_bg_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showImagePicDialog();

            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    imageToPDF();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void imageToPDF() throws FileNotFoundException {

        LinearLayout relativeLayout = (LinearLayout) findViewById(R.id.savepdf_layout);
        // convert view group to bitmap
        relativeLayout.setDrawingCacheEnabled(true);
        relativeLayout.buildDrawingCache();
        Bitmap bm = relativeLayout.getDrawingCache();
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);



        //Id card front page name
        f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "image.jpg");
        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        LinearLayout relativeLayout1 = (LinearLayout) findViewById(R.id.savepdf1_layout);
        // convert view group to bitmap
        relativeLayout1.setDrawingCacheEnabled(true);
        relativeLayout1.buildDrawingCache();
        Bitmap bm1 = relativeLayout1.getDrawingCache();
        Intent share1 = new Intent(Intent.ACTION_SEND);
        share1.setType("image/jpeg");
        ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
        bm1.compress(Bitmap.CompressFormat.JPEG, 100, bytes1);

        //Id card back page name
        f1 = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "image1.jpg");
        try {
            f1.createNewFile();
            FileOutputStream fo1 = new FileOutputStream(f1);
            fo1.write(bytes1.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            Document document = new Document();
            dirpath  = android.os.Environment.getExternalStorageDirectory().toString();

            //PDF name
            PdfWriter.getInstance(document, new FileOutputStream(dirpath + "/"+id.getText().toString()+"-"+name.getText().toString()+".pdf")); //  Change pdf's name.
            document.open();
            Image img = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
            document.newPage();
            Image img1 = Image.getInstance(Environment.getExternalStorageDirectory() + File.separator + "image1.jpg");
            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / img.getWidth()) * 100;
            img.scalePercent(scaler);
            img.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(img);

            img1.scalePercent(scaler);
            img1.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);
            document.add(img1);
            document.close();
            Toast.makeText(this, "PDF Generated successfully!..", Toast.LENGTH_SHORT).show();
            progressDialog = new ProgressDialog(upload_id_card.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            new backgroundworker().execute();
        } catch (Exception e) {

        }
    }

    private void showImagePicDialog() {
        String options[] = {"Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if (which == 0) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }

            }
        });
        builder.create().show();
    }

    // checking storage permissions
    private Boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // Requesting  gallery permission
    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST);
    }

    // checking camera permissions
    private Boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    // Requesting camera permission
    private void requestCameraPermission() {
        requestPermissions(cameraPermission, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    private void pickFromGallery() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), GALLERY_REQUEST);

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
                final SessionMaintance status=new SessionMaintance(upload_id_card.this);
                String post_data = URLEncoder.encode("branch_id","UTF-8")+"="+URLEncoder.encode(status.get_branch_id(),"UTF-8")+"&"
                        +URLEncoder.encode("emp_id","UTF-8")+"="+ URLEncoder.encode(emp_id_s,"UTF-8")+"&"
                        +URLEncoder.encode("walkin_id","UTF-8")+"="+ URLEncoder.encode(wid_s,"UTF-8")+"&"
                        +URLEncoder.encode("issued_on","UTF-8")+"="+ URLEncoder.encode("","UTF-8")+"&"
                        +URLEncoder.encode("created_by","UTF-8")+"="+ URLEncoder.encode(status.get_field_id(),"UTF-8");
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
                if(json_string.equals("1")){
                    progressDialog = new ProgressDialog(upload_id_card.this);
                    progressDialog.setMessage("Please Wait...!!!");
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    new backgroundworker2().execute();
                }
            }catch (Exception e){

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
                final SessionMaintance status=new SessionMaintance(upload_id_card.this);
                String post_data = URLEncoder.encode("crop_readed_image_data_join","UTF-8")+"="+URLEncoder.encode(encImage,"UTF-8")+"&"
                        +URLEncoder.encode("walkin_id","UTF-8")+"="+ URLEncoder.encode(wid_s,"UTF-8");
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
            int count=0;
            try {
                if(json_string2.equals("1")){
                    Intent intent =new Intent(upload_id_card.this,id_card_section.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }catch (Exception e){

            }
        }
    }

    private void pickFromCamera() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_CAPTURE && resultCode == Activity.RESULT_OK) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            take_img1.setImageBitmap(thumbnail);
            resizedBitmap = (Bitmap) data.getExtras().get("data");
        }
        else if(requestCode == GALLERY_REQUEST && resultCode == RESULT_OK){
            Uri selectedimage =data.getData();
            take_img1.setImageURI(selectedimage);
            try {
                final InputStream imageStream = getContentResolver().openInputStream(selectedimage);
                final Bitmap bm = BitmapFactory.decodeStream(imageStream);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                byte[] b = baos.toByteArray();
                encImage = "data:image/jpeg;base64,"+Base64.encodeToString(b, Base64.DEFAULT);
            }catch (Exception e){

            }

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        }else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                take_img1.setImageURI(result.getUri());
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                resizedBitmap = ((BitmapDrawable)take_img1.getDrawable()).getBitmap();
            }
        }
    }

}