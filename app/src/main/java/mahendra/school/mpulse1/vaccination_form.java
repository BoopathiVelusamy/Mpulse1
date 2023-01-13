package mahendra.school.mpulse1;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.Calendar;

public class vaccination_form extends AppCompatActivity {

    Spinner spinner1,spinner2,spinner3;
    String[] temp1 = { "SELECT","Yes", "No"};
    String[] temp2 = { "Covaxin", "Covishield","Sputnik V"};
    String[] temp3 = { "SELECT","Yes", "No"};
    TextView txt1,txt2,txt3,txt4,txt5,txt6;
    EditText etxt1,etxt2,etxt3,etxt4;
    DatePickerDialog datePickerDialog;

    StringBuffer sb = new StringBuffer();
    String json_url = url_interface.url+"covid_vaccine/mobileCovid_insert";
    String json_string="";
    ProgressDialog progressDialog;
    String are_u_vaccine="";
    String vaccine_name = "";
    String date_of_fstdose = "";
    String vaccine_secdose =  "";
    String due_date_of_secdose ="";
    String actual_date_of_secdose = "";
    String emp_comments = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination_form);
        getSupportActionBar().setTitle("Covid-19 Vaccination");

        spinner1 = findViewById(R.id.spinner5);
        spinner2 = findViewById(R.id.spinner6);
        spinner3 = findViewById(R.id.spinner7);

        txt1 = findViewById(R.id.textView66);
        txt2 = findViewById(R.id.textView67);
        txt3 = findViewById(R.id.textView68);
        txt4 = findViewById(R.id.textView69);
        txt5 = findViewById(R.id.textView711);
        txt6 = findViewById(R.id.textView70);

        etxt1 = findViewById(R.id.editTextTextPersonName3);
        etxt2 = findViewById(R.id.editTextTextPersonName4);
        etxt3 = findViewById(R.id.editTextTextPersonName61);
        etxt4 = findViewById(R.id.editTextTextPersonName5);

        spinner2.setVisibility(View.GONE);
        spinner3.setVisibility(View.GONE);
        txt1.setVisibility(View.GONE);
        txt2.setVisibility(View.GONE);
        txt3.setVisibility(View.GONE);
        txt4.setVisibility(View.GONE);
        txt5.setVisibility(View.GONE);
        txt6.setVisibility(View.GONE);
        etxt1.setVisibility(View.GONE);
        etxt2.setVisibility(View.GONE);
        etxt3.setVisibility(View.GONE);
        etxt4.setVisibility(View.GONE);


        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, temp1);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(aa);

        ArrayAdapter aa1 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, temp2);
        aa1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(aa1);

        ArrayAdapter aa2 = new ArrayAdapter(this, android.R.layout.simple_spinner_item, temp3);
        aa2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(aa2);


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinner1.getSelectedItem().toString().equals("Yes")) {
                    spinner2.setVisibility(View.VISIBLE);
                    spinner3.setVisibility(View.VISIBLE);
                    txt1.setVisibility(View.VISIBLE);
                    txt2.setVisibility(View.VISIBLE);
                    txt3.setVisibility(View.VISIBLE);
                    txt4.setVisibility(View.VISIBLE);
                    txt6.setVisibility(View.VISIBLE);
                    etxt1.setVisibility(View.VISIBLE);
                    etxt2.setVisibility(View.VISIBLE);
                    etxt4.setVisibility(View.VISIBLE);
                } else if (spinner1.getSelectedItem().toString().equals("No")) {
                    spinner2.setVisibility(View.GONE);
                    spinner3.setVisibility(View.GONE);
                    txt1.setVisibility(View.GONE);
                    txt2.setVisibility(View.GONE);
                    txt3.setVisibility(View.GONE);
                    txt4.setVisibility(View.GONE);
                    txt5.setVisibility(View.GONE);
                    txt6.setVisibility(View.GONE);
                    etxt1.setVisibility(View.GONE);
                    etxt2.setVisibility(View.GONE);
                    etxt3.setVisibility(View.GONE);
                    etxt4.setVisibility(View.GONE);
                }
            }


            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }

        });

        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spinner3.getSelectedItem().toString().equals("Yes")) {
                    txt5.setVisibility(View.VISIBLE);
                    etxt3.setVisibility(View.VISIBLE);
                } else if (spinner3.getSelectedItem().toString().equals("No")) {
                    txt5.setVisibility(View.GONE);
                    etxt3.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        etxt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(vaccination_form.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etxt1.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        etxt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(vaccination_form.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etxt2.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        etxt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(vaccination_form.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                etxt3.setText(year + "-"
                                        + (monthOfYear + 1) + "-" + dayOfMonth);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        findViewById(R.id.button10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 are_u_vaccine = spinner1.getSelectedItem().toString();
                if(are_u_vaccine.equals("Yes")){
                     vaccine_name = spinner2.getSelectedItem().toString();
                     date_of_fstdose = etxt1.getText().toString();
                     vaccine_secdose =  spinner3.getSelectedItem().toString();
                    if(vaccine_secdose.equals("Yes")){
                         due_date_of_secdose = etxt2.getText().toString();
                         actual_date_of_secdose = etxt3.getText().toString();
                         emp_comments = etxt4.getText().toString();
                    }else{
                         due_date_of_secdose = etxt2.getText().toString();
                         actual_date_of_secdose = "";
                         emp_comments = etxt4.getText().toString();
                        if(emp_comments.equals("")){
                            etxt4.setError("Please Fill the field");
                            findViewById(R.id.button10).setVisibility(View.GONE);
                        }
                    }
                }else{
                     vaccine_name = "";
                     date_of_fstdose = "";
                     vaccine_secdose =  "";
                     due_date_of_secdose ="";
                     actual_date_of_secdose = "";
                     emp_comments = "";
                }
                progressDialog = new ProgressDialog(vaccination_form.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
                new backgroundworker().execute();
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
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(vaccination_form.this);
                String post_data = URLEncoder.encode("branch_id","UTF-8")+"="+URLEncoder.encode(status.get_branch_id(),"UTF-8")+"&"
                        +URLEncoder.encode("walkin_id","UTF-8")+"="+ URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("are_u_vaccine","UTF-8")+"="+ URLEncoder.encode(are_u_vaccine,"UTF-8")+"&"
                        +URLEncoder.encode("vaccine_name","UTF-8")+"="+ URLEncoder.encode(vaccine_name,"UTF-8")+"&"
                        +URLEncoder.encode("date_of_fstdose","UTF-8")+"="+ URLEncoder.encode(date_of_fstdose,"UTF-8")+"&"
                        +URLEncoder.encode("vaccine_secdose","UTF-8")+"="+ URLEncoder.encode(vaccine_secdose,"UTF-8")+"&"
                        +URLEncoder.encode("due_date_of_secdose","UTF-8")+"="+ URLEncoder.encode(due_date_of_secdose,"UTF-8")+"&"
                        +URLEncoder.encode("actual_date_of_secdose","UTF-8")+"="+ URLEncoder.encode(actual_date_of_secdose,"UTF-8")+"&"
                        +URLEncoder.encode("emp_comments","UTF-8")+"="+ URLEncoder.encode(emp_comments,"UTF-8");
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
            if(result.equals("1")){
                Toast.makeText(vaccination_form.this, "Details Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}