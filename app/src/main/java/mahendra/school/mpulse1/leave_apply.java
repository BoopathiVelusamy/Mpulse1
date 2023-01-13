package mahendra.school.mpulse1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class leave_apply extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String temp="";
    CompactCalendarView compactCalendar;
    TextView textview_month_view;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());


    StringBuffer sb12 = new StringBuffer();
    String json_url12 = "http://eattendance.mahendra.org/e_college/holiday_event.php";
    ProgressDialog progressDialog;
    String json_string12="";
    JSONObject jsonObject12;
    JSONArray jsonArray12;
    String event_data;
    List<String> Ldate = new ArrayList<>();
    List<String> Lholiday_name = new ArrayList<>();
    Event[] eventarray;

    EditText date_of_apply,from_date,to_date;
    DatePickerDialog datePickerDialog;
    final int REQUEST_IMAGE_FROM_GALLERY = 200;
    String filename="";
    long fileSize;
    ImageView imageToUpload;
    String encodedString="";
    TextView txtfilename;
    TextView total_no_of_days;
    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"leave_apply/getLeave_policyMobile";
    String json_string="";
    JSONObject jsonObject,jsonObject1,jsonObject2;
    JSONArray jsonArray;
    String inputString1="";
    String inputString2="";
    float t_d=0;
    String leave_types_from_json="";
    Spinner leave_tpes_spinner;
    String [] levae_type_string_array;
    Button leave_submit;
    float fltemp;
    StringBuffer sb5 = new StringBuffer();
    String json_url5 = url_interface.url+"leave_apply/MobileleaveApply";
    String json_string5="";
    JSONObject jsonObject5;
    JSONArray jsonArray5;

    StringBuffer sb6 = new StringBuffer();
    String json_url6 = url_interface.url+"leave_apply/GetReporting_personMobile";
    String json_string6="";
    JSONObject jsonObject6;
    JSONArray jsonArray6;

    int switch_filter = 0;
    EditText leave_subject,leave_reason,topic,location,accomodation,finance;
    String leave_sub="",leave_res="",leave_type="",leave_day_type="",curr_date="",dhr1="",dhr2="",dsid1="",dsid2="",stopic="",slocation="",saccomadation="",sfinance="";
    List<String> leavers = new ArrayList<>();
    Map<String,String> leavers_split = new HashMap<>();
    String leave_policy_id="";

    List<String> report_person = new ArrayList<>();
    Map<String,String> report_person_map = new HashMap<>();

    EditText efrp,esrp;
    String frp="",srp="";
    Button full_day,halfer_day;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_apply);
        getSupportActionBar().setTitle("LEAVE APPLY");

        efrp = findViewById(R.id.editText55);
        esrp = findViewById(R.id.editText555);

        date_of_apply = findViewById(R.id.editText2);
        from_date = findViewById(R.id.editText3);
        to_date = findViewById(R.id.editText4);
        txtfilename = findViewById(R.id.textView31);
        txtfilename.setVisibility(View.GONE);
        imageToUpload = findViewById(R.id.imageView2);
        total_no_of_days = findViewById(R.id.textView4);
        total_no_of_days.setVisibility(View.GONE);
        leave_subject = findViewById(R.id.editText);
        leave_reason = findViewById(R.id.editText5);

        full_day = findViewById(R.id.button4);
        halfer_day = findViewById(R.id.button5);

        full_day.setVisibility(View.GONE);
        halfer_day.setVisibility(View.GONE);

        full_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    total_no_of_days.setText("1");
                    full_day.setBackgroundResource(R.drawable.vertical_divider2);
                    full_day.setTextColor(Color.WHITE);
                    halfer_day.setBackgroundResource(R.drawable.vertical_divider1);
                    halfer_day.setTextColor(Color.parseColor("#6200EA"));
                    leave_day_type = "1";
            }
        });

        halfer_day.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_no_of_days.setText("0.5");
                full_day.setBackgroundResource(R.drawable.vertical_divider1);
                full_day.setTextColor(Color.parseColor("#6200EA"));
                halfer_day.setBackgroundResource(R.drawable.vertical_divider2);
                halfer_day.setTextColor(Color.WHITE);
                leave_day_type = "0";
            }
        });

        leave_tpes_spinner = findViewById(R.id.spinner);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickImageFromDeviceGallery();
            }
        });


        String date_n = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        date_of_apply.setText(date_n);

        from_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(leave_apply.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                from_date.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        to_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(leave_apply.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                to_date.setText(dayOfMonth + "-"
                                        + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        to_date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculate_no_of_days();
            }
        });

        leave_submit = findViewById(R.id.button);
        leave_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leave_sub = leave_subject.getText().toString();
                leave_res = leave_reason.getText().toString();
                leave_type = leave_tpes_spinner.getSelectedItem().toString();

                curr_date = date_of_apply.getText().toString();

                fltemp = Float.valueOf(total_no_of_days.getText().toString());

                progressDialog = new ProgressDialog(leave_apply.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                new backgroundworker4().execute();
                    }
        });

    }

    public void calculate_no_of_days(){
        levae_type_string_array = null;
        SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy");
        inputString1 = from_date.getText().toString();
        inputString2 = to_date.getText().toString();

        try {
            java.util.Date date1 = myFormat.parse(inputString1);
            java.util.Date date2 = myFormat.parse(inputString2);
            long diff = date2.getTime() - date1.getTime();
            t_d = (float) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+1;
            total_no_of_days.setText(String.valueOf(t_d));
            if(t_d==1){
                full_day.setVisibility(View.VISIBLE);
                halfer_day.setVisibility(View.VISIBLE);
            }else{
                full_day.setVisibility(View.GONE);
                halfer_day.setVisibility(View.GONE);
            }
            leavers.clear();leavers_split.clear();
            new backgroundworker2().execute();
            progressDialog = new ProgressDialog(leave_apply.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pickImageFromDeviceGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_FROM_GALLERY && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            assert cursor != null;
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            String fileNameSegments[] = picturePath.split("/");
            filename = fileNameSegments[fileNameSegments.length - 1];
            txtfilename.setText(filename);
            Bitmap myImg = BitmapFactory.decodeFile(picturePath);
            imageToUpload.setImageBitmap(myImg);
            imageToUpload.setVisibility(View.VISIBLE);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            myImg.compress(Bitmap.CompressFormat.JPEG, 50, stream);
            byte[] byte_arr = stream.toByteArray();
            fileSize = byte_arr.length;
            encodedString = Base64.encodeToString(byte_arr, 0);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        //Toast.makeText(this, ""+leave_tpes_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
        leave_policy_id = leavers_split.get(leave_tpes_spinner.getSelectedItem().toString());
        new backgroundworker6().execute();
        progressDialog = new ProgressDialog(leave_apply.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public class backgroundworker2 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url1);
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
                final SessionMaintance status=new SessionMaintance(leave_apply.this);
                float tempdays = fltemp;
                String post_data = URLEncoder.encode("branch_id","UTF-8")+"="+URLEncoder.encode(status.get_branch_id(),"UTF-8")+"&"
                        +URLEncoder.encode("walkin_id","UTF-8")+"="+ URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("from_date","UTF-8")+"="+ URLEncoder.encode(inputString1,"UTF-8")+"&"
                        +URLEncoder.encode("to_date","UTF-8")+"="+ URLEncoder.encode(inputString2,"UTF-8");
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
            int count = 0;
            try {
                jsonObject = new JSONObject(json_string);
                String error_message = jsonObject.getString("err_message");
                if(error_message.equals("")||error_message.equals(" ")){
                    leave_submit.setEnabled(true);
                }else{
                    leave_submit.setEnabled(false);
                }
                Toast.makeText(leave_apply.this, ""+error_message, Toast.LENGTH_SHORT).show();
                String leave_data = jsonObject.getString("date_leave");
                leave_data = leave_data.substring(1,leave_data.length()-1);
                Log.d("ASDFCVB",""+leave_data);
                String temp[] = leave_data.split(",");
                for(String z : temp){
                    String temper[] = z.split("-");
                    leavers.add(temper[1]);
                    leavers_split.put(temper[1],temper[0].substring(1,temper[0].length()).trim());
                }
                addItemsOnSpinner1();
            } catch (JSONException e) {
                e.printStackTrace();
                final ScrollView coordinatorLayout = (ScrollView) findViewById(R.id.leave_apply);
                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "You Already Applied The Leave", Snackbar.LENGTH_LONG);
                Toast.makeText(leave_apply.this, "You Already Applied The Leave", Toast.LENGTH_SHORT).show();
                snackbar1.show();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addItemsOnSpinner1(){
        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, leavers);
        leave_tpes_spinner.setAdapter(dataAdapter3);
        leave_tpes_spinner.setOnItemSelectedListener(this);
    }

    public class backgroundworker4 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url5);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb5=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(leave_apply.this);
                String post_data =URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id(), "UTF-8") + "&"
                        + URLEncoder.encode("apply_date", "UTF-8") + "=" + URLEncoder.encode(curr_date, "UTF-8")+ "&"
                        + URLEncoder.encode("leave_subject", "UTF-8") + "=" + URLEncoder.encode(leave_sub, "UTF-8")+ "&"
                        + URLEncoder.encode("from_date", "UTF-8") + "=" + URLEncoder.encode(inputString1, "UTF-8") + "&"
                        + URLEncoder.encode("to_date", "UTF-8") + "=" + URLEncoder.encode(inputString2, "UTF-8") + "&"
                        + URLEncoder.encode("total_days", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(fltemp), "UTF-8") + "&"
                        + URLEncoder.encode("leave_type", "UTF-8") + "=" + URLEncoder.encode(leavers_split.get(leave_type), "UTF-8") + "&"
                        + URLEncoder.encode("leave_day_type", "UTF-8") + "=" + URLEncoder.encode(leave_day_type, "UTF-8") + "&"
                        + URLEncoder.encode("reason", "UTF-8") + "=" + URLEncoder.encode(leave_res, "UTF-8") + "&"
                        + URLEncoder.encode("fstapp", "UTF-8") + "=" + URLEncoder.encode(report_person_map.get(report_person.get(0)), "UTF-8") + "&"
                        + URLEncoder.encode("lstapp", "UTF-8") + "=" + URLEncoder.encode(report_person_map.get(report_person.get(1)), "UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string5=bufferedReader.readLine())!=null)
                {
                    sb5.append(json_string5+"\n");
                    Log.d("GGG",""+json_string5);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG12345",""+sb5);
                return sb5.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string5 = result;
            progressDialog.dismiss();
            if(json_string5.equals("1")){
                Toast.makeText(leave_apply.this, "Successfully Applied Leave", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(leave_apply.this,leave_summary.class);
                startActivity(intent);
            }
        }
    }

    public class backgroundworker6 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url6);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb6=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(leave_apply.this);
                String post_data =URLEncoder.encode("branch_id","UTF-8")+"="+URLEncoder.encode(status.get_branch_id(),"UTF-8")+"&"
                        +URLEncoder.encode("walkin_id","UTF-8")+"="+ URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("leave_policy_id","UTF-8")+"="+ URLEncoder.encode(leave_policy_id,"UTF-8");
                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string6=bufferedReader.readLine())!=null)
                {
                    sb6.append(json_string6+"\n");
                    Log.d("GGG",""+json_string6);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG123",""+sb6);
                return sb6.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string6 = result;
            progressDialog.dismiss();
            int count =0;
            try {
                jsonArray6 = new JSONArray(json_string6);
                while (count < jsonArray6.length()) {
                    JSONObject jo = jsonArray6.getJSONObject(count);
                    report_person.add(jo.getString("name")+" "+jo.getString("last_name"));
                    report_person_map.put(jo.getString("name")+" "+jo.getString("last_name"),jo.getString("walkin_id"));
                    count++;
                }
                efrp.setText(report_person.get(0));
                esrp.setText(report_person.get(1));
                Log.d("HIDA",""+report_person_map);
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setcalendar(){
        for(int i=0;i<Ldate.size();i++){
            long temp = Long.parseLong(Ldate.get(i));
            Log.d("FGFGFGFG",""+temp);
            eventarray[i] = new Event(Color.RED, temp, Lholiday_name.get(i));
        }
        for(int j=0;j<Ldate.size();j++){
            compactCalendar.addEvent(eventarray[j],true);
        }

    }
}
