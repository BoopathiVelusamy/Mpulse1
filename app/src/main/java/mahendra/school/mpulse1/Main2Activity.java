package mahendra.school.mpulse1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    private PopupWindow mPopupWindow2;
    ConstraintLayout mRelativeLayout2;
    LayoutInflater inflater2;
    View customView2;


    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"profile/leave_list";
    ProgressDialog progressDialog;
    String json_string="";
    JSONArray jsonArray;
    ArrayList<String> leave_Name = new ArrayList<>();
    Map<String,String> counter = new HashMap<>();
    ListView listView;
    CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);


        getSupportActionBar().setTitle("LEAVE MENUS");

        new backgroundworker().execute();
        progressDialog = new ProgressDialog(Main2Activity.this);
        progressDialog.setMessage("Please Wait...!!!");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        inflater2 = (LayoutInflater) Main2Activity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        customView2 = inflater2.inflate(R.layout.custom_layout_dialog1,null);

        findViewById(R.id.leave_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show3();
            }
        });

        findViewById(R.id.leave_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,leave_apply.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.leave_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Main2Activity.this,leave_summary.class);
                startActivity(intent);
            }
        });


    }

    public void show3(){
        mRelativeLayout2 = (ConstraintLayout) findViewById(R.id.mainmenu);
        mPopupWindow2 = new PopupWindow(
                customView2,
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                true
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow2.setElevation(5.0f);
        }

        mPopupWindow2.setOutsideTouchable(false);
        mPopupWindow2.setFocusable(true);
        mPopupWindow2.update();

        listView = (ListView) customView2.findViewById(R.id.listView);

        ImageView close_button = (ImageView) customView2.findViewById(R.id.imageView4);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow2.dismiss();
            }
        });

        //listView.setDivider(null);
        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);


        mPopupWindow2.setAnimationStyle(R.style.popup_window_animation);
        mPopupWindow2.showAtLocation(customView2, Gravity.CENTER, 0, 0);
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
                final SessionMaintance status=new SessionMaintance(Main2Activity.this);
                String post_data = URLEncoder.encode("walkin_id","UTF-8")+"="+URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
                        +URLEncoder.encode("emp_id","UTF-8")+"="+ URLEncoder.encode(status.get_user_id(),"UTF-8")+"&"
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
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string = result;
            progressDialog.dismiss();
            int count = 0;
            try {
                jsonArray = new JSONArray(json_string);

                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    leave_Name.add(jo.getString("present_full_name"));

                    Float tem1 = Float.valueOf(jo.getString("total"));
                    Float tem2 = Float.valueOf(jo.getString("last_total"));
                    Float tem3 = Float.valueOf(jo.getString("taken"));
                    Float tem = (tem1 + tem2) - tem3;
                        counter.put(jo.getString("present_full_name"), String.valueOf(tem));
                        tem = 0.0f;
                        tem1 = 0.0f;
                        tem2 = 0.0f;
                        tem3 = 0.0f;
                        count++;


                }

                Log.d("QWERTYUIOP",""+jsonArray.length()+"\n\n"+counter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return leave_Name.size();
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
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.custom_layout1, null);

            CardView card = (CardView) view.findViewById(R.id.leave_cardview);

            if (i % 2 == 1) {
                card.setBackgroundColor(Color.parseColor("#DBDBDB"));
            } else {
                card.setBackgroundColor(Color.parseColor("#DBDBDB"));
            }

            TextView leave_name = (TextView)view.findViewById(R.id.textView18);
            TextView leave_count = (TextView)view.findViewById(R.id.textView3);
            leave_name.setText(leave_Name.get(i));
            leave_count.setText(counter.get(leave_Name.get(i)));
            return view;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.logout:
                Intent intent = new Intent(this, sign_out.class);
                startActivity(intent);
                return true;

            case R.id.profile:
                Intent intent2 = new Intent(this,profile.class);
                startActivity(intent2);
                return true;


            default:return super.onOptionsItemSelected(item);
        }
    }
}
