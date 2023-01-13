package mahendra.school.mpulse1;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
import java.util.ArrayList;
import java.util.List;

public class salary_slip extends AppCompatActivity {

    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"profile/mobile_profile";
    ProgressDialog progressDialog;
    String json_string="";
    JSONObject jsonObject,jsonObject1;

    ListView listView,listView2,listView3;

    CustomAdapter customAdapter;
    CustomAdapter2 customAdapter2;
    CustomAdapter3 customAdapter3;

    int count = 0,counter=0;

    List<String> Lleft = new ArrayList<>();
    List<String> Lrigth = new ArrayList<>();


    List<String> Lleft2 = new ArrayList<>();
    List<String> Lrigth2 = new ArrayList<>();


    List<String> Lleft3 = new ArrayList<>();
    List<String> Lrigth3 = new ArrayList<>();

    String arrear_desc = "";

    TextView takehome,txt46,txt47;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_slip);

        SessionMaintance sessionMaintance = new SessionMaintance(salary_slip.this);
        if(sessionMaintance.get_token().equals("")||sessionMaintance.get_token().equals(null)){
            Intent intent = new Intent(salary_slip.this,sign_out.class);
            startActivity(intent);
        }

        listView=(ListView)findViewById(R.id.listView);

        listView2=(ListView)findViewById(R.id.listView2);

        listView3=(ListView)findViewById(R.id.listView3);

        takehome = findViewById(R.id.textView51);

        txt46 = findViewById(R.id.textView46);

        txt47 = findViewById(R.id.textView47);

        new backgroundworker().execute();
        progressDialog = new ProgressDialog(salary_slip.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

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
                final SessionMaintance status=new SessionMaintance(salary_slip.this);
                String post_data = URLEncoder.encode("walkin_id","UTF-8")+"="+URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("emp_id","UTF-8")+"="+ URLEncoder.encode(status.get_username(),"UTF-8")+"&"
                        +URLEncoder.encode("branch_id","UTF-8")+"="+ URLEncoder.encode(status.get_branch_id(),"UTF-8")+"&"
                        +URLEncoder.encode("token","UTF-8")+"="+ URLEncoder.encode(status.get_token(),"UTF-8")+ "&"
                        + URLEncoder.encode("self", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");
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
            try {
                jsonObject = new JSONObject(json_string);
                jsonObject1 = jsonObject.getJSONObject("salary_details");
                TextView oner = (TextView)findViewById(R.id.textView46);

                oner.setText("SALARY SLIP\n" +
                        "FOR THE MONTH OF : " + jsonObject1.getString("salary_month") + "\n" +
                        "Last update time : " + jsonObject1.getString("updated_time"));

                while(count < jsonObject1.length()){
                    if((count>3)){
                        String temp = jsonObject1.names().getString(count);
                        Lleft.add(jsonObject1.names().getString(count));
                        Lrigth.add(jsonObject1.getString(temp));
                        if(temp.equals("Basic_Pay")){
                            counter = count;
                            count = jsonObject1.length();
                            customAdapter = new CustomAdapter();
                            listView.setAdapter(customAdapter);

                        }
                    }
                    count++;
                }


                count=counter;
                int t2=0;
                String temper = "";
                while(count < jsonObject1.length()){

                        String temp = jsonObject1.names().getString(count);
                        int i1 =0;
                        try{
                            i1 = Integer.valueOf(jsonObject1.getString(jsonObject1.names().getString(count)));}
                        catch (Exception e){
                            i1 = 999;
                        }
                        if(i1>0) {

                            if (temp.equals("Night_Shift_Days") || temp.equals("Shift_Allowance_Days") || temp.equals("PL_Encashment_Days") || temp.equals("Extra_Pay_Days")) {
                                //t2 = count-counter-1;
                                temper = jsonObject1.getString(jsonObject1.names().getString(count));
                            }

                            if (temp.equals("Night_Shift_Allowance") || temp.equals("Shift_Allowance") || temp.equals("PL_Encashment") || temp.equals("Extra_Pay")) {
                                Lleft2.add(jsonObject1.names().getString(count) + "( " + temper + " Days's )");
                                Lrigth2.add(jsonObject1.getString(temp));
                            } else if (temp.equals("Night_Shift_Days") || temp.equals("Shift_Allowance_Days") || temp.equals("PL_Encashment_Days") || temp.equals("Extra_Pay_Days")) {
                            } else if(temp.equals("Arrears_Description")){
                                arrear_desc = jsonObject1.getString(temp);
                                if(arrear_desc.equals("")){

                                }else{
                                    txt47.setText("*Arrears/Adjustment Description: "+arrear_desc);
                                }
                            }else{
                                Lleft2.add(jsonObject1.names().getString(count));
                                Lrigth2.add(jsonObject1.getString(temp));
                            }

                        }

                    if (temp.equals("Cumulative_Bonus")) {
                        counter = count;
                        count = jsonObject1.length();
                        customAdapter2 = new CustomAdapter2();
                        listView2.setAdapter(customAdapter2);
                    }
                        count++;
                }

                count=counter+1;

                while(count < jsonObject1.length()){
                    String temp = jsonObject1.names().getString(count);
                    int i1 =0;
                    try{
                        i1 = Integer.valueOf(jsonObject1.getString(jsonObject1.names().getString(count)));}
                    catch (Exception e){
                        i1 = 999;
                    }
                    if(i1>0) {
                        Lleft3.add(jsonObject1.names().getString(count));
                        Lrigth3.add(jsonObject1.getString(temp));

                    }
                    if (temp.equals("Sub_Total_Deduction")) {
                        counter = count;
                        count = jsonObject1.length();
                        customAdapter3 = new CustomAdapter3();
                        listView3.setAdapter(customAdapter3);
                        setListViewHeightBasedOnChildren(listView);
                        setListViewHeightBasedOnChildren(listView2);
                        setListViewHeightBasedOnChildren(listView3);
                        takehome.setText(jsonObject1.getString("Take_Home"));
                    }
                    count++;
                }


            } catch (JSONException e) {
                e.printStackTrace();
                txt46.setText("SALARY SLIP NOT UPDATED FOR THIS MONTH");
                //findViewById(R.id.textView46).setVisibility(View.GONE);
                findViewById(R.id.listView).setVisibility(View.GONE);
                findViewById(R.id.listView2).setVisibility(View.GONE);
                findViewById(R.id.listView3).setVisibility(View.GONE);
                findViewById(R.id.textView43).setVisibility(View.GONE);
                findViewById(R.id.textView47).setVisibility(View.GONE);
                findViewById(R.id.textView48).setVisibility(View.GONE);
                findViewById(R.id.ccd).setVisibility(View.GONE);
                //Toast.makeText(salary_slip.this, "Salary Slip not Updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lleft.size()-5;
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

                view = getLayoutInflater().inflate(R.layout.tablelayout1, null);

                TextView temper1 = (TextView) view.findViewById(R.id.textView19);

                TextView temper2 = (TextView) view.findViewById(R.id.textView44);

                temper1.setText(Lleft.get(i) + " : ");

                temper2.setText(Lrigth.get(i));

            return view;
        }
    }

    public class CustomAdapter2 extends BaseAdapter {


        @Override
        public int getCount() {
            return Lleft2.size()-0;
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

            view = getLayoutInflater().inflate(R.layout.tablelayout, null);

            TextView temper1 = (TextView) view.findViewById(R.id.textView19);

            TextView temper2 = (TextView) view.findViewById(R.id.textView44);

            temper1.setText(Lleft2.get(i) + " : ");

            temper2.setText(Lrigth2.get(i));

            return view;
        }
    }

    public class CustomAdapter3 extends BaseAdapter {


        @Override
        public int getCount() {
            return Lleft3.size()-0;
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

            view = getLayoutInflater().inflate(R.layout.tablelayout, null);

            TextView temper1 = (TextView) view.findViewById(R.id.textView19);

            TextView temper2 = (TextView) view.findViewById(R.id.textView44);

            temper1.setText(Lleft3.get(i) + " : ");

            temper2.setText(Lrigth3.get(i));

            return view;
        }
    }

    public static void setListViewHeightBasedOnChildren
            (ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(),
                View.MeasureSpec.EXACTLY);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0) view.setLayoutParams(new
                    ViewGroup.LayoutParams(desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight + (listView.getDividerHeight()*(listAdapter.getCount()-1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }


}
