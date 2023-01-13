package mahendra.school.mpulse1;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class documents_add extends AppCompatActivity  implements
        AdapterView.OnItemSelectedListener {

    Uri imageUri;
    String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CHANGE_NETWORK_STATE, Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE
            , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.FOREGROUND_SERVICE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS};
    int permission_All = 1;
    public static final int PICK_IMAGE = 1;
    long fileSize;
    String encodedString = "", encodedString1 = "";
    ArrayList<String> filenamelist = new ArrayList();
    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url + "profile/doc_category";
    ProgressDialog progressDialog;
    String json_string = "";
    List<String> Ldocument_list = new ArrayList<>();
    List<String> Lsub_document_list = new ArrayList<>();
    Spinner spin, spin1;
    String type_of_doc = "", type_of_doc1 = "";
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url + "profile/getDocsMobile";
    String json_string2 = "";
    StringBuffer sb3 = new StringBuffer();
    String json_url3 = url_interface.url + "profile/getDocsIndMobile";
    String json_string3 = "";
    List<String> Lemp_doc_id = new ArrayList<>();
    List<String> Ldoc_category = new ArrayList<>();
    List<String> Ldoc_mandatory = new ArrayList<>();
    List<String> Ldoc_image = new ArrayList<>();
    List<String> Ldoc_approved = new ArrayList<>();
    CustomAdapter customAdapter;
    ListView listView;
    List<String> Lim_id = new ArrayList<>();
    List<String> Lsemester = new ArrayList<>();
    List<String> Lsem_document = new ArrayList<>();
    List<String> Lsem_document_approved = new ArrayList<>();
    final List<ImageView> imageviewlist = new ArrayList<>();
    final List<TextView> textViewList2 = new ArrayList<>();
    final List<TextView> textViewList3 = new ArrayList<>();
    ImageView viewer;
    TextView txter, txter2;
    static int inc = 0;

    Map<String, String> hmap = new HashMap<>();

    StringBuffer sb4 = new StringBuffer();
    String json_url4 = url_interface.url + "profile/insert_MobileDocs";
    String json_string4 = "";
    String post_data = "";
    JSONObject jsonObj;
    JSONArray arrayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_add);
        if (!haspermission(this, permissions))
            ActivityCompat.requestPermissions(this, permissions, permission_All);

        getSupportActionBar().setTitle("UPLOAD DOCUMENTS");
        spin = (Spinner) findViewById(R.id.spinner10);
        spin1 = (Spinner) findViewById(R.id.spinner11);
        spin1.setVisibility(View.GONE);
        spin.setOnItemSelectedListener(this);
        Lsub_document_list.add("Diploma");
        Lsub_document_list.add("UG");
        Lsub_document_list.add("PG");

        listView = (ListView) findViewById(R.id.listView);
        listView.setDivider(null);

        progressDialog = new ProgressDialog(documents_add.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();


        findViewById(R.id.button14).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for (Map.Entry<String, String> entry : hmap.entrySet()) {

                    Log.d("QWERTY", "" + hmap.size() + "  " + entry.getKey());
                }

                if (!type_of_doc.equals("Education")) {
                    SessionMaintance status = new SessionMaintance(documents_add.this);
                    try {
                        post_data = URLEncoder.encode("type_of_doc", "UTF-8") + "=" + URLEncoder.encode(type_of_doc, "UTF-8") + "&"
                                + URLEncoder.encode("doc_details", "UTF-8") + "=" + URLEncoder.encode(new JSONObject(hmap).toString(), "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(status.get_username().toString(), "UTF-8") + "&"
                                + URLEncoder.encode("field_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id().toString(), "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    SessionMaintance status = new SessionMaintance(documents_add.this);
                    try {
                        post_data = URLEncoder.encode("type_of_doc", "UTF-8") + "=" + URLEncoder.encode(type_of_doc, "UTF-8") + "&"
                                + URLEncoder.encode("doc_details", "UTF-8") + "=" + URLEncoder.encode(new JSONObject(hmap).toString(), "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(status.get_username().toString(), "UTF-8") + "&"
                                + URLEncoder.encode("field_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id().toString(), "UTF-8") + "&"
                                + URLEncoder.encode("semester_type", "UTF-8") + "=" + URLEncoder.encode(type_of_doc1, "UTF-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
//                Log.d("ASDFG",""+arrayer.toString());
                progressDialog = new ProgressDialog(documents_add.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                new backgroundworker4().execute();
            }
        });
    }

    public static boolean haspermission(Context context, String... permissions) {
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) && (context != null) && (permissions != null)) {
            for (String temp : permissions)
                if (ActivityCompat.checkSelfPermission(context, temp) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
        }
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        inc = 0;

        Lemp_doc_id.clear();
        Ldoc_category.clear();
        Ldoc_mandatory.clear();
        Ldoc_image.clear();

        listView.setAdapter(null);

        type_of_doc = Ldocument_list.get(position);

        imageviewlist.clear();
        textViewList2.clear();
        textViewList3.clear();

        Ldoc_approved.clear();

        hmap.clear();

        if (type_of_doc.equals("Education")) {

            Lemp_doc_id.clear();
            Ldoc_category.clear();
            Ldoc_mandatory.clear();
            Ldoc_image.clear();

            listView.setAdapter(null);

            imageviewlist.clear();
            textViewList2.clear();
            textViewList3.clear();

            Ldoc_approved.clear();

            hmap.clear();

            spin1.setVisibility(View.VISIBLE);

            ArrayAdapter aa1 = new ArrayAdapter(documents_add.this, android.R.layout.simple_spinner_item, Lsub_document_list);

            aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spin1.setAdapter(aa1);

            spin1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                    //inc =0;

//                    for(String x : Lsemester){
//
//                            Ldoc_category.remove(x);
//                            Ldoc_mandatory.remove("0");
//
//                    }
//                    for(String y : Lsemester){
//
//                        Lemp_doc_id.remove(y);
//                    }
//                    for(String z : Lsem_document){
//                        try {
//                            int index = ((Ldoc_image.size() -Lsem_document.size()) +Lsem_document.indexOf(z));
//                            Ldoc_image.remove(index);
//                        }catch (Exception e){
//
//                        }
//                    }
//                    for(String w : Lsem_document_approved){
//
//                        Ldoc_approved.remove(w);
//                    }

                    inc = 0;

                    Lemp_doc_id.clear();
                    Ldoc_category.clear();
                    Ldoc_mandatory.clear();
                    Ldoc_image.clear();

                    listView.setAdapter(null);

                    imageviewlist.clear();
                    textViewList2.clear();
                    textViewList3.clear();

                    Ldoc_approved.clear();

                    hmap.clear();

                    Lim_id.clear();
                    Lsemester.clear();
                    Lsem_document.clear();
                    Lsem_document_approved.clear();

                    type_of_doc1 = Lsub_document_list.get(position);

                    new backgroundworker2().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                }

            });
        } else {
            spin1.setVisibility(View.GONE);
            progressDialog = new ProgressDialog(documents_add.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            new backgroundworker2().execute();
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class backgroundworker extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(json_url1);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb = new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status = new SessionMaintance(documents_add.this);
                String post_data = URLEncoder.encode("user_type_id", "UTF-8") + "=" + URLEncoder.encode(status.get_username(), "UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData", "" + post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((json_string = bufferedReader.readLine()) != null) {
                    sb.append(json_string + "\n");
                    Log.d("json_string", "" + json_string);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG", "" + sb.toString());
                return sb.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            json_string = result;
            progressDialog.dismiss();
            int count = 0;
            try {
                JSONArray jsonArray = new JSONArray(json_string);
                while (count < jsonArray.length()) {
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    Ldocument_list.add(jsonObject.getString("type"));
                    count++;
                }
                if (Ldocument_list.size() > 0) {
                    ArrayAdapter aa = new ArrayAdapter(documents_add.this, android.R.layout.simple_spinner_item, Ldocument_list);
                    aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(aa);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class backgroundworker2 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(json_url2);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb2 = new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status = new SessionMaintance(documents_add.this);
                String post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id(), "UTF-8") + "&"
                        + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type_of_doc, "UTF-8") + "&"
                        + URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(status.get_username(), "UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData", "" + post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((json_string2 = bufferedReader.readLine()) != null) {
                    sb2.append(json_string2 + "\n");
                    Log.d("json_string", "" + json_string2);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG", "" + sb2.toString());
                return sb2.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            json_string2 = result;
            progressDialog.dismiss();
            int count = 0;
            try {
                JSONArray jsonArray = new JSONArray(json_string2);
                while (count < jsonArray.length()) {
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    if (!jsonObject.getString("category").equals("Individul Marksheet")) {
                        Ldoc_category.add(jsonObject.getString("category").toUpperCase());
                        Lemp_doc_id.add(jsonObject.getString("emp_docs_id"));
                        Ldoc_mandatory.add(jsonObject.getString("mandatory"));
                        Ldoc_image.add(jsonObject.getString("document"));
                        Ldoc_approved.add(jsonObject.getString("doc_status"));

                    }
                    count++;
                }
                if ((Ldoc_category.size() > 0) && (!type_of_doc.equals("Education"))) {
                    imageviewlist.clear();
                    textViewList2.clear();
                    textViewList3.clear();
                    customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);
                } else {
                    new backgroundworker3().execute();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class backgroundworker3 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(json_url3);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb3 = new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status = new SessionMaintance(documents_add.this);
                String post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id(), "UTF-8") + "&"
                        + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(type_of_doc1, "UTF-8") + "&"
                        + URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(status.get_username(), "UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData", "" + post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((json_string3 = bufferedReader.readLine()) != null) {
                    sb3.append(json_string3 + "\n");
                    Log.d("json_string", "" + json_string3);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG", "" + sb3.toString());
                return sb3.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            json_string3 = result;
            //progressDialog.dismiss();
            int count = 0;
            try {
                JSONArray jsonArray = new JSONArray(json_string3);
                while (count < jsonArray.length()) {
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    Lsemester.add("semester_" + jsonObject.getString("semester"));
                    Lim_id.add("semester_" + jsonObject.getString("im_id"));
                    Lsem_document.add(jsonObject.getString("document"));
                    Lsem_document_approved.add("semester-" + jsonObject.getString("doc_status"));
                    count++;
                }
                if (Ldoc_category.size() > 0) {

                    for (String x : Lsemester) {
                        Ldoc_category.add(x);
                        Ldoc_mandatory.add("0");
                    }
                    for (String y : Lsemester) {
                        Lemp_doc_id.add(y);
                    }
                    for (String z : Lsem_document) {
                        Ldoc_image.add(z);
                    }
                    for (String w : Lsem_document_approved) {
                        Ldoc_approved.add(w);
                    }
                    inc = 0;
                    imageviewlist.clear();
                    textViewList2.clear();
                    textViewList3.clear();
                    hmap.clear();
                    customAdapter = new CustomAdapter();
                    listView.setAdapter(customAdapter);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class backgroundworker4 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(json_url4);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb4 = new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status = new SessionMaintance(documents_add.this);
                bufferedWriter.write(post_data);
                Log.d("PostData", "" + post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((json_string4 = bufferedReader.readLine()) != null) {
                    sb4.append(json_string4 + "\n");
                    Log.d("json_string", "" + json_string4);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG", "" + sb4.toString());
                return sb4.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            json_string4 = result;
            if (json_string4.equals("1")) {
                Toast.makeText(documents_add.this, "Your Upload is Success", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        }
    }

    public class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return Lemp_doc_id.size();
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
        public View getView(int i, View view, ViewGroup parent) {


            try {
                inc++;

                if (Ldoc_category.size() >= inc) {

                    view = getLayoutInflater().inflate(R.layout.custom_layout_documents_add, null);

                    TextView textView = (TextView) view.findViewById(R.id.textView78);

                    TextView txt1 = (TextView) view.findViewById(R.id.textView77);

                    TextView txt2 = (TextView) view.findViewById(R.id.textView79);

                    TextView txt3 = (TextView) view.findViewById(R.id.textView80);

                    txt1.setVisibility(View.GONE);

                    txt2.setVisibility(View.GONE);

                    ImageView imageView = (ImageView) view.findViewById(R.id.imageView28);

                    ImageView imageView30 = (ImageView) view.findViewById(R.id.imageView30);

                    imageView30.setVisibility(View.GONE);

                    ImageView button = (ImageView) view.findViewById(R.id.button13);

                    ImageView choose_file = (ImageView) view.findViewById(R.id.imageView32);

                    textView.setText(Ldoc_category.get(i));

                    textViewList2.add(txt2);

                    imageviewlist.add(imageView);

                    txt1.setText(Lemp_doc_id.get(i));

                    textViewList3.add(txt1);

                    if (Ldoc_image.get(i).equals("")) {

                    } else {
                        //Toast.makeText(documents_add.this, ""+i, Toast.LENGTH_SHORT).show();
                        Picasso.with(documents_add.this)
                                .load(Ldoc_image.get(i))
                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                .placeholder(R.drawable.progress_animation)
                                .into(imageView);
                    }

                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String images = Ldoc_image.get(i).toString();
                            image_BottomSheetDialog imageBottomSheetDialog = new image_BottomSheetDialog(documents_add.this, images);
                            imageBottomSheetDialog.show(getSupportFragmentManager(), "Dialog");
                        }
                    });


                    if (Ldoc_approved.get(i).equals("0") || Ldoc_approved.get(i).equals("semester-0")) {
                        txt3.setText("Pending");
                        txt3.setTextColor(ContextCompat.getColor(documents_add.this, R.color.bd_options_button_bg));
                        button.setVisibility(View.GONE);
                        choose_file.setVisibility(View.GONE);
                    } else if (Ldoc_approved.get(i).equals("1") || Ldoc_approved.get(i).equals("semester-1")) {
                        txt3.setText("Approved");
                        txt3.setTextColor(ContextCompat.getColor(documents_add.this, R.color.timecolor));
                        button.setVisibility(View.GONE);
                        choose_file.setVisibility(View.GONE);
                    } else if (Ldoc_approved.get(i).equals("2") || Ldoc_approved.get(i).equals("semester-2")) {
                        txt3.setTextColor(ContextCompat.getColor(documents_add.this, R.color.login_btn_color));
                        txt3.setText("Rejected");
                        button.setVisibility(View.VISIBLE);
                        choose_file.setVisibility(View.GONE);
                    }

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            //Toast.makeText(documents_add.this, "" + imageviewlist.size(), Toast.LENGTH_SHORT).show();

                            viewer = new ImageView(documents_add.this);

                            txter = new TextView(documents_add.this);

                            txter2 = new TextView(documents_add.this);

                            viewer = imageviewlist.get(i);

                            txter = textViewList2.get(i);

                            txter2 = textViewList3.get(i);

                            CropImage.activity()
                                    .setGuidelines(CropImageView.Guidelines.ON)
                                    .start(documents_add.this);


//                            ContentValues values = new ContentValues();
//                            values.put(MediaStore.Images.Media.TITLE, "New Picture");
//                            values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
//                            imageUri = getContentResolver().insert(
//                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//                            intent.putExtra("return-data", true);
//                            startActivityForResult(intent, 200);

                        }
                    });

                    choose_file.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            viewer = new ImageView(documents_add.this);

                            txter = new TextView(documents_add.this);

                            txter2 = new TextView(documents_add.this);

                            viewer = imageviewlist.get(i);

                            txter = textViewList2.get(i);

                            txter2 = textViewList3.get(i);

                            Intent i = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(i, 2);
                        }
                    });

                }
            } catch (Exception e) {

            }
            if (view == null) {
                view = getLayoutInflater().inflate(R.layout.custom_layout_documents_add, null);
            }
            return view;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                try {
                    Bitmap myImg = MediaStore.Images.Media.getBitmap(
                            getContentResolver(), imageUri);
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    myImg.compress(Bitmap.CompressFormat.JPEG, 30, stream);
                    byte[] byte_arr = stream.toByteArray();
                    fileSize = byte_arr.length;
                    encodedString = Base64.encodeToString(byte_arr, 0);
                    filenamelist.add(encodedString);
                    viewer.setImageBitmap(myImg);
                    viewer.setVisibility(View.VISIBLE);
                    txter.setText(encodedString);
                    try {
                        Log.d("ASDFGH", "" + txter2.getText().toString());
                        hmap.put(txter2.getText().toString(), encodedString);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {

        }
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myImg.compress(Bitmap.CompressFormat.JPEG, 70, stream);
            byte[] byte_arr = stream.toByteArray();
            fileSize = byte_arr.length;
            encodedString = Base64.encodeToString(byte_arr, 0);
            filenamelist.add(encodedString);
            viewer.setImageBitmap(myImg);
            viewer.setVisibility(View.VISIBLE);
            txter.setText(encodedString);
            try {
                Log.d("ASDFGH", "" + txter2.getText().toString());
                hmap.put(txter2.getText().toString(), encodedString);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

    /**
     * this function does the crop operation.
     */