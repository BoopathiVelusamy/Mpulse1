package mahendra.school.mpulse1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mtracker_attendance extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"emp_attendance/MobileMt_attd";
    JSONObject jsonObject,jsonObject1;
    JSONArray jsonArray,jsonArray1;
    ProgressDialog progressDialog;
    String json_string="";

    static int inc = 0;

    ListView listView;
    CustomAdapter customAdapter;
    List<String> Lshift = new ArrayList<>();
    List<String> Lall_leave_policy_with_applied_leave = new ArrayList<>();
    List<String> Lwalkin_id = new ArrayList<>();
    List<String> Lemp_id = new ArrayList<>();
    List<String> Llast_attd_date = new ArrayList<>();

    List<String> Lname = new ArrayList<>();
    String dashline = "";

    Map<String,String> Lleave_map = new HashMap<>();

    JSONObject json;

    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"emp_attendance/MobileInsertAttd";
    String json_string2="";

    Button submit;

    Map<String,String> attendance_map = new HashMap<>();

    TextView ateen_date ;

    String selected_emp_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mtracker_attendance);

        getSupportActionBar().setTitle("MOV TRACKER ATTENDANCE");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();

        listView=(ListView)findViewById(R.id.listview);
        listView.setDivider(null);

        ateen_date = findViewById(R.id.textView7);

        submit = findViewById(R.id.button2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                json = new JSONObject(attendance_map);

                Log.d("DFGHJ",""+json);

                progressDialog = new ProgressDialog(mtracker_attendance.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                new backgroundworker2().execute();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        inc = 0;
        Lshift.clear();Lall_leave_policy_with_applied_leave.clear();Lwalkin_id.clear();
        Lemp_id.clear();Llast_attd_date.clear();Lname.clear();dashline="";Lleave_map.clear();
        attendance_map.clear();
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
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(mtracker_attendance.this);
                String post_data = URLEncoder.encode("walkin_id","UTF-8")+"="+URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("branch_id","UTF-8")+"="+ URLEncoder.encode(status.get_branch_id(),"UTF-8");
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
            int count=0,count1=0;

            try {
                jsonObject = new JSONObject(json_string);
                JSONObject jo = new JSONObject();
                if(jsonObject.getString("err_message").equals("")||jsonObject.getString("err_message").equals(" ")){
                    try{
                        jsonArray = jsonObject.getJSONArray("emp_details");
                        while (count < jsonArray.length()) {
                            jo = jsonArray.getJSONObject(count);
                            Lshift.add(jo.getString("shift"));
                            Lall_leave_policy_with_applied_leave.add(jo.getString("all_leave_policy_with_applied_leave"));
                            Lwalkin_id.add(jo.getString("walkin_id"));
                            Lemp_id.add(jo.getString("emp_id"));
                            String sourceDate = jo.getString("last_attd_date");  // Start date
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(sdf.parse(sourceDate)); // parsed date and setting to calendar
                            calendar.add(Calendar.DATE, 1);  // number of days to add
                            String destDate = sdf.format(calendar.getTime());  // End date
                            Llast_attd_date.add(destDate);
                            Lname.add(jo.getString("name")+" "+jo.getString("last_name"));
                            jsonObject1 = new JSONObject(Lall_leave_policy_with_applied_leave.get(count));
                            while (count1 < jsonObject1.length()) {
                                String temp = String.valueOf(count1+1);
                                JSONObject jo1 = jsonObject1.getJSONObject(temp);
                                dashline = dashline+jo1.getString("present_name_id")+"%"
                                        +jo1.getString("present_short_name")+"%"
                                        +jo1.getString("present_full_name") +"%"
                                        +jo1.getString("default_selected")+"%"
                                        +jo1.getString("attd_menu_type")+"@";
                                count1++;
                            }
                            Lleave_map.put(jo.getString("emp_id"),dashline);
                            dashline = "";
                            count1=0;
                            count++;
                        }
                        if(Lshift.size()>0) {
                            customAdapter = new CustomAdapter();
                            listView.setAdapter(customAdapter);
                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                    submit.setVisibility(View.VISIBLE);
                    ateen_date.setVisibility(View.VISIBLE);
                    ateen_date.setText("TODAY ATTENDANCE DATE IS : "+Llast_attd_date.get(0));

                }

                else if(jsonObject.getString("err_message").equals("Contct Your Reporting Person")){
                    ateen_date.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                    Toast.makeText(mtracker_attendance.this, "Contct Your Reporting Person", Toast.LENGTH_SHORT).show();
                    ateen_date.setText("Contct Your Reporting Person");
                }else if(jsonObject.getString("err_message").equals("Attendance Close")){
                    ateen_date.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                    Toast.makeText(mtracker_attendance.this, "Attendance Close You Already Put Attendance", Toast.LENGTH_SHORT).show();
                    ateen_date.setText("Attendance Close You Already Put Attendance");
                }else if(jsonObject.getString("err_message").equals("Check Pending attendance")){
                    ateen_date.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.INVISIBLE);
                    Toast.makeText(mtracker_attendance.this, "Check Pending attendance", Toast.LENGTH_SHORT).show();
                    ateen_date.setText("Check Pending attendance");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }


        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lshift.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            inc++;

            if(Lshift.size() >= inc ) {


                view = getLayoutInflater().inflate(R.layout.custom_layout_attendance, null);

                TextView name = (TextView) view.findViewById(R.id.textView6);

                final Spinner leave_spinner = (Spinner) view.findViewById(R.id.spinner2);

                List<String> leaves = new ArrayList<>();

                final Map<String,String> leave_map = new HashMap<>();

                try {

                    String temp1 = Lleave_map.get(Lemp_id.get(i));

                    String[] temp2 = temp1.split("@");

                    String[] temp3;

                    int j = 0;

                    for (j = 0; j < temp2.length; j++) {

                        temp3 = temp2[j].split("%");

                        if(temp3[3].equals("1")){

                            leaves.add(0,temp3[1]);
                        }else{

                            leaves.add(temp3[1]);
                        }


                        leave_map.put(temp3[1],temp3[0]);

                    }


                    Log.d("QWSXCDFV", "" + leave_map + "\n\n");

                    ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(mtracker_attendance.this, android.R.layout.simple_spinner_dropdown_item, leaves);

                    leave_spinner.setAdapter(dataAdapter3);

                    leave_spinner.setAdapter(dataAdapter3);

                    name.setText(Lemp_id.get(i).toUpperCase()+"-"+Lname.get(i).toUpperCase());



                    j = 0;

                    leave_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //Toast.makeText(mtracker_attendance.this, ""+leave_spinner.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                            //Toast.makeText(mtracker_attendance.this, ""+leave_map.get(leave_spinner.getSelectedItem().toString()), Toast.LENGTH_SHORT).show();
                            String[] abc = leave_spinner.getSelectedItem().toString().split("-");

                            attendance_map.put(Lwalkin_id.get(i),abc[0].trim()+"|"+Lshift.get(i)+"|"+Llast_attd_date.get(i)+"|"+Lemp_id.get(i));

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                } catch (Exception e) {
                    Toast.makeText(mtracker_attendance.this, "" + e, Toast.LENGTH_SHORT).show();
                }


            }
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
                final SessionMaintance status=new SessionMaintance(mtracker_attendance.this);
                String post_data = URLEncoder.encode("jsoncode","UTF-8")+"="+URLEncoder.encode(json.toString(),"UTF-8")+"&"
                        +URLEncoder.encode("branch_id","UTF-8")+"="+ URLEncoder.encode(status.get_branch_id(),"UTF-8");
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

                Lshift.clear();Lall_leave_policy_with_applied_leave.clear();Lwalkin_id.clear();Lemp_id.clear();
                Llast_attd_date.clear();Lname.clear();
                dashline = "";
                Lleave_map.clear();
                inc =0;

                progressDialog = new ProgressDialog(mtracker_attendance.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                new backgroundworker().execute();
            }else{
                Intent intent =new Intent(mtracker_attendance.this,attendance_menu.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

        }
    }
}
