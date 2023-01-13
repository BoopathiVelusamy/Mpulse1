package mahendra.school.mpulse1;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.snackbar.Snackbar;

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
import java.util.Locale;
import java.util.Map;

public class holiday_calender extends AppCompatActivity {

    String temp="";
    CompactCalendarView compactCalendar;
    TextView textview_month_view;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatMonth1 = new SimpleDateFormat("MM", Locale.getDefault());


    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"index/holidayEvent";
    ProgressDialog progressDialog;
    String json_string="";
    String event_data;
    List<String> Ldate = new ArrayList<>();
    List<String> Lholiday_name = new ArrayList<>();
    Map<String,String> Lholiday_list = new HashMap<>();
    List<String> list = new ArrayList<>();
    List<String> list1 = new ArrayList<>();
    Event[] eventarray;
    CustomAdapter customAdapter;
    ListView listView;
    int month=0;
    String pass_year="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_holiday_calender);
        getSupportActionBar().hide();
        eventarray = new Event[10000];
        listView = (ListView) findViewById(R.id.listView);
        textview_month_view = findViewById(R.id.textView501);
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH) + 1;
        if (month == 1)
            temp = "JANUARY";
        else if (month == 2)
            temp = "FEBUARY";
        else if (month == 3)
            temp = "MARCH";
        else if (month == 4)
            temp = "APRIL";
        else if (month == 5)
            temp = "MAY";
        else if (month == 6)
            temp = "JUNE";
        else if (month == 7)
            temp = "JULY";
        else if (month == 8)
            temp = "AUGUST";
        else if (month == 9)
            temp = "SEPTEMBER";
        else if (month == 10)
            temp = "OCTOBER";
        else if (month == 11)
            temp = "NOVEMBER";
        else if (month == 12)
            temp = "DECEMBER";

        textview_month_view.setText(temp.toUpperCase() + " - " + String.valueOf(year));
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

        pass_year = String.valueOf(year);


        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(java.util.Date dateClicked) {
                Context context = getApplicationContext();
                List<Event> events = compactCalendar.getEvents(dateClicked);
                try {
                    //   Log.d("EVENTS",""+events.toString());
                    StringBuffer sb = new StringBuffer(events.toString());
                    sb.delete(1,6);
                    sb.deleteCharAt(0);
                    sb.deleteCharAt(sb.length()-1);
                    sb.deleteCharAt(0);
                    sb.deleteCharAt(sb.length()-1);
                    String[] temper = sb.toString().split(",");
                    String[] temper1 = temper[2].toString().split("=");
                    //  Log.d("EVENTS1",""+temper1[1]);
                    event_data = temper1[1];

                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    final ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.holiday_event);
                    Snackbar snackbar1 = Snackbar.make(constraintLayout, "" + event_data.substring(0, event_data.length()), Snackbar.LENGTH_SHORT);
                    snackbar1.show();
                }catch (Exception e){

                }
            }

            @Override
            public void onMonthScroll(java.util.Date firstDayOfNewMonth) {
                textview_month_view.setText(dateFormatMonth.format(firstDayOfNewMonth));

                //Toast.makeText(holiday_calender.this, ""+dateFormatMonth1.format(firstDayOfNewMonth), Toast.LENGTH_SHORT).show();
                list.clear();
                list1.clear();
                for (Map.Entry<String, String> entry : Lholiday_list.entrySet()){
                    String mmm = entry.getKey();
                    String tem[] = mmm.split("-");
                    int yaerer = Integer.valueOf(textview_month_view.getText().toString().split("-")[1].trim());
                    //Log.d("JKJKJK",""+Integer.valueOf(tem[1])+"     "+month+"      "+tem[2]+"   "+yaerer);
                    if((Integer.valueOf(tem[1])==Integer.valueOf(dateFormatMonth1.format(firstDayOfNewMonth))) && (Integer.valueOf(tem[2])==yaerer)){
                        list.add(entry.getValue());
                        list1.add(entry.getKey());
                    }





                }
                customAdapter.notifyDataSetChanged();
                Log.d("HI Here",""+list+"\n\n\n"+list1);

            }

        });


        progressDialog = new ProgressDialog(holiday_calender.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);
        new backgroundworker().execute();
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
                final SessionMaintance status=new SessionMaintance(holiday_calender.this);
                String post_data = URLEncoder.encode("user_type_id","UTF-8")+"="+URLEncoder.encode(status.get_username(),"UTF-8")+ "&"
                        + URLEncoder.encode("year", "UTF-8") + "=" + URLEncoder.encode(pass_year,"UTF-8")+ "&"
                        + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(status.get_branch_id(),"UTF-8");
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
                JSONArray jsonArray = new JSONArray(json_string);
                while(count<jsonArray.length()){
                    JSONObject jsonObject = jsonArray.getJSONObject(count);
                    Ldate.add(jsonObject.getString("datee"));
                    Lholiday_name.add(jsonObject.getString("holiday_name"));
                    Lholiday_list.put(jsonObject.getString("holiday_date"),jsonObject.getString("holiday_name")
                            +"-"+jsonObject.getString("holiday_category"));
                    count++;
                }
                setcalendar();
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setcalendar(){

        Log.d("MPA NOW",""+Lholiday_list);
        for(int i=0;i<Ldate.size();i++){
            long temp = Long.parseLong(Ldate.get(i));
            // Log.d("FGFGFGFG",""+temp);
            eventarray[i] = new Event(Color.RED, temp, Lholiday_name.get(i));
        }
        for(int j=0;j<Ldate.size();j++){
            compactCalendar.addEvent(eventarray[j],true);
        }

        for (Map.Entry<String, String> entry : Lholiday_list.entrySet()){
            String mmm = entry.getKey();
            String tem[] = mmm.split("-");
            int yaerer = Integer.valueOf(textview_month_view.getText().toString().split("-")[1].trim());
            Log.d("JKJKJK",""+Integer.valueOf(tem[1])+"     "+month+"      "+tem[2]+"   "+yaerer);
            if((Integer.valueOf(tem[1])==month) && (Integer.valueOf(tem[2])==yaerer)){
                list.add(entry.getValue());
                list1.add(entry.getKey());
            }





        }
        Log.d("HI Here",""+list+"\n\n\n"+list1);
        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

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

            view = getLayoutInflater().inflate(R.layout.custom_layout199, null);

            CardView cd = (CardView)view.findViewById(R.id.leave_cardview);

            TextView date = (TextView)view.findViewById(R.id.textView18);

            TextView name = (TextView)view.findViewById(R.id.textView3);

            date.setText(list1.get(i));

            name.setText(list.get(i).split("-")[0].toUpperCase().toString());

            if(list.get(i).split("-")[1].toUpperCase().toString().equals("GH")){

                cd.setBackgroundColor(getResources().getColor(R.color.timecolor));
                name.setTextColor(getResources().getColor(R.color.white));
                date.setTextColor(getResources().getColor(R.color.white));

            }else if(list.get(i).split("-")[1].toUpperCase().toString().equals("FL")){

                cd.setBackgroundColor(getResources().getColor(R.color.bd_options_button_bg));
                name.setTextColor(getResources().getColor(R.color.white));
                date.setTextColor(getResources().getColor(R.color.white));

            }


            return view;
        }
    }

}
