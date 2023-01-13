package mahendra.school.mpulse1;

import static android.content.ContentValues.TAG;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class mainmenu extends AppCompatActivity {


    int leave_app1;
    int leave_app2;
    private PopupWindow mPopupWindow21;
    ScrollView mRelativeLayout21;
    LayoutInflater inflater21;
    View customView21;

    StringBuffer sb222 = new StringBuffer();
    String json_url222 = url_interface.url+"relieving_list/GetEmpDetailsMobile";
    String json_string222="";

    StringBuffer sb2222 = new StringBuffer();
    String json_url2222 = url_interface.url+"index/menu_detailsMobile";
    String json_string2222="";

    StringBuffer sb11 = new StringBuffer();
    String json_url11 = url_interface.url+"profile/leave_list";
    String json_string11="";
    JSONArray jsonArray11;
    ArrayList<String> leave_Name = new ArrayList<>();
    Map<String,String> counter = new HashMap<>();
    ListView listView1;
    CustomAdapter2 customAdapter1;

    StringBuffer sb = new StringBuffer();
    String json_url1 = url_interface.url+"index/mobile_notification";
    ProgressDialog progressDialog;
    String json_string="";
    JSONArray jsonArray;
    List<String> Lname = new ArrayList<>();
    List<String> Lemp_id = new ArrayList<>();
    Map<String,String> emp_id_map = new HashMap<>();
    TextView notify_count;

    private PopupWindow mPopupWindow2;
    ScrollView mRelativeLayout2;
    LayoutInflater inflater2;
    View customView2;
    ListView listView;
    CustomAdapter customAdapter;
    StringBuffer sb2 = new StringBuffer();
    String json_url2 = url_interface.url+"index/Mt_attd_mobile_notification";
    String json_string2="";
    TextView notify_count2;
    int counter2 =0,counter1=0,counter3=0,counter4=0,counter5=0;
    String currentVersion="",onlineVersion="";
    ImageView imageView1,imageView2,imageView3,imageView4,imageView5;
    Intent mServiceIntent;
    private SensorService mSensorService;
    Context ctx;
    public Context getCtx() {
        return ctx;
    }
    StringBuffer sb22 = new StringBuffer();
    String json_url22 = url_interface.url+"Autorun_leavepolicy/mobile_notification";
    String fcm_token;
    String json_string22="";
    Intent oldintent;
    String penattcount,mtrackcount;

    StringBuffer sb33 = new StringBuffer();
    String json_url33 = url_interface.url+"index/combineApi";
    String json_string33="";
    TextView approv_txt;


    InstallStateUpdatedListener installStateUpdatedListener;
    private AppUpdateManager mAppUpdateManager;
    private static final int RC_APP_UPDATE = 11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainmenu);

        ctx = this;

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancelAll();

        imageView1 = findViewById(R.id.imageView10);
        imageView2 = findViewById(R.id.imageView11);
        imageView3 = findViewById(R.id.imageView9996);
        imageView4 = findViewById(R.id.imageView999996);
        imageView5 = findViewById(R.id.imageView29);

        approv_txt = findViewById(R.id.textView11111);





        findViewById(R.id.cel).setVisibility(View.GONE);
        findViewById(R.id.mtrackerattendance).setVisibility(View.GONE);
        findViewById(R.id.pendingattendance).setVisibility(View.GONE);
        findViewById(R.id.attendance1).setVisibility(View.GONE);
        //findViewById(R.id.id_card).setVisibility(View.GONE);


        getSupportActionBar().setTitle("MAIN MENU");

        oldintent = getIntent();

        if(oldintent.getStringExtra("TYPE").equals("x")){

        }else if(oldintent.getStringExtra("TYPE").equals("y")){

//            penattcount = oldintent.getStringExtra("pendingattendancecount");
//            mtrackcount = oldintent.getStringExtra("mtrackerattendancecount");
//
//            if(penattcount.equals("0")){
//                findViewById(R.id.pendingattendance).setVisibility(View.GONE);
//            }else{
//                findViewById(R.id.pendingattendance).setVisibility(View.VISIBLE);
//            }
//            if(mtrackcount.equals("0")){
//                findViewById(R.id.mtrackerattendance).setVisibility(View.GONE);
//            }else {
//                findViewById(R.id.mtrackerattendance).setVisibility(View.VISIBLE);
//            }
        }

        findViewById(R.id.id_card).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,id_card_section.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.cel).setVisibility(View.GONE);

        findViewById(R.id.cel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,employee_list.class);
                startActivity(intent);
            }
        });


        inflater21 = (LayoutInflater) mainmenu.this.getSystemService(LAYOUT_INFLATER_SERVICE);

        customView21 = inflater21.inflate(R.layout.custom_layout_dialog1,null);

        findViewById(R.id.leave_policy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new backgroundworker3().execute();
                progressDialog = new ProgressDialog(mainmenu.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
            }
        });

//        findViewById(R.id.vaccination).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mainmenu.this,vaccination_form.class);
//                startActivity(intent);
//            }
//        });

        findViewById(R.id.all_employee).setVisibility(View.GONE);
        findViewById(R.id.report_employee).setVisibility(View.GONE);
        findViewById(R.id.video_call).setVisibility(View.GONE);
        findViewById(R.id.project_employee).setVisibility(View.GONE);
        findViewById(R.id.vaccine_form).setVisibility(View.GONE);
        findViewById(R.id.vaccine_certificate).setVisibility(View.GONE);

        findViewById(R.id.leave_apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,leave_apply.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.leave_summary).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,leave_summary.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.assets_tracker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,assets_tracker.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.employe_assets_tracker).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,employee_assets_tracker.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,viewing_location.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.leave_policy).setVisibility(View.GONE);
        findViewById(R.id.leave_apply).setVisibility(View.GONE);
        findViewById(R.id.leave_summary).setVisibility(View.GONE);

        findViewById(R.id.leave_approval1).setVisibility(View.GONE);
        findViewById(R.id.leave_approval2).setVisibility(View.GONE);

        findViewById(R.id.assets_tracker).setVisibility(View.GONE);
        findViewById(R.id.employe_assets_tracker).setVisibility(View.GONE);



        notify_count = findViewById(R.id.imageView1119);
        notify_count2 = findViewById(R.id.imageView11119);

        if(oldintent.getStringExtra("TYPE").equals("x")) {
            new backgroundworker333().execute();
            progressDialog = new ProgressDialog(mainmenu.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }else if(oldintent.getStringExtra("TYPE").equals("y")){
            new backgroundworker333().execute();
            progressDialog = new ProgressDialog(mainmenu.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }

        inflater2 = (LayoutInflater) mainmenu.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        customView2 = inflater2.inflate(R.layout.custom_layout_dialog,null);

        findViewById(R.id.leave_menus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.imageView10).performClick();
            }
        });

        findViewById(R.id.attendance1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                findViewById(R.id.imageView11).performClick();
            }
        });



        findViewById(R.id.imageView10).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter2%2==0) {
                    imageView1.setImageResource(R.drawable.ic_baseline_remove_24);
                    findViewById(R.id.leave_policy).setVisibility(View.VISIBLE);
                    findViewById(R.id.leave_apply).setVisibility(View.VISIBLE);
                    findViewById(R.id.leave_summary).setVisibility(View.VISIBLE);
                }else{
                    imageView1.setImageResource(R.drawable.ic_add_black_24dp);
                    findViewById(R.id.leave_policy).setVisibility(View.GONE);
                    findViewById(R.id.leave_apply).setVisibility(View.GONE);
                    findViewById(R.id.leave_summary).setVisibility(View.GONE);
                }
                counter2++;
            }
        });

        findViewById(R.id.imageView11).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter1%2==0) {
                    imageView2.setImageResource(R.drawable.ic_baseline_remove_24);
                    findViewById(R.id.leave_approval1).setVisibility(View.VISIBLE);
                    findViewById(R.id.leave_approval2).setVisibility(View.VISIBLE);
                    checkermac();
                }else{
                    imageView2.setImageResource(R.drawable.ic_add_black_24dp);
                    findViewById(R.id.leave_approval1).setVisibility(View.GONE);
                    findViewById(R.id.leave_approval2).setVisibility(View.GONE);
                }
                counter1++;
            }
        });



//        findViewById(R.id.leave_menus).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mainmenu.this,Main2Activity.class);
//                startActivity(intent);
//            }
//        });

        findViewById(R.id.attendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,attendance.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.profile).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,profile.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.leave_approval1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,leave_approval.class);
                intent.putExtra("approval_type","first");
                startActivity(intent);
            }
        });

        findViewById(R.id.leave_approval2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,leave_approval.class);
                intent.putExtra("approval_type","second");
                startActivity(intent);
            }
        });


        findViewById(R.id.mtrackerattendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,mtracker_attendance.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.pendingattendance).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new backgroundworker().execute();
                progressDialog = new ProgressDialog(mainmenu.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
            }
        });

        findViewById(R.id.project_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,projectlist.class);
                startActivity(intent);
            }
        });

        try {
            currentVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e) {
            e.printStackTrace();
        }

        final SessionMaintance status=new SessionMaintance(mainmenu.this);
        String temp = status.get_role_name();
        //Toast.makeText(mainmenu.this, ""+temp, Toast.LENGTH_SHORT).show();
        List<String> abc = new ArrayList<>();
        try{
            String[] temp1 = temp.split("//");
            for(String z : temp1){
                abc.add(z);
            }
            if(abc.contains("TECH")){
                findViewById(R.id.asseter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(counter3%2==0) {
                            imageView3.setImageResource(R.drawable.ic_baseline_remove_24);
                            findViewById(R.id.assets_tracker).setVisibility(View.VISIBLE);
                            findViewById(R.id.employe_assets_tracker).setVisibility(View.VISIBLE);
                        }else{
                            imageView3.setImageResource(R.drawable.ic_add_black_24dp);
                            findViewById(R.id.assets_tracker).setVisibility(View.GONE);
                            findViewById(R.id.employe_assets_tracker).setVisibility(View.GONE);
                        }
                        counter3++;
                    }
                });
            }else{
                findViewById(R.id.asseter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(counter3%2==0) {
                            imageView3.setImageResource(R.drawable.ic_baseline_remove_24);
                            findViewById(R.id.employe_assets_tracker).setVisibility(View.VISIBLE);
                        }else{
                            imageView3.setImageResource(R.drawable.ic_add_black_24dp);
                            findViewById(R.id.employe_assets_tracker).setVisibility(View.GONE);
                        }
                        counter3++;
                    }
                });
            }
            //Toast.makeText(mainmenu.this, ""+abc, Toast.LENGTH_SHORT).show();
            if(abc.contains("VP")||abc.contains("DGM")||abc.contains("SM")||abc.contains("M")||
                    abc.contains("DM")||abc.contains("ADM")||abc.contains("CENTRE HEAD")){
                findViewById(R.id.map).setVisibility(View.VISIBLE);

            }else{
               findViewById(R.id.map).setVisibility(View.GONE);
            }


//            if(abc.contains("ADMIN")){
//                findViewById(R.id.id_card).setVisibility(View.VISIBLE);
//            }else{ findViewById(R.id.id_card).setVisibility(View.GONE); }

            //Toast.makeText(mainmenu.this, ""+abc, Toast.LENGTH_SHORT).show();

            if(abc.contains("TL")
                    ||abc.contains("TECH")
                    ||abc.contains("ADMIN")
                    ||abc.contains("JTL")){
                    //findViewById(R.id.video_call).setVisibility(View.VISIBLE);
//                    findViewById(R.id.video_call).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(counter4%2==0) {
//                            imageView4.setImageResource(R.drawable.ic_baseline_remove_24);
//                            findViewById(R.id.all_employee).setVisibility(View.GONE);
//                            findViewById(R.id.project_employee).setVisibility(View.GONE);
//                            findViewById(R.id.report_employee).setVisibility(View.VISIBLE);
//                        }else{
//                            imageView4.setImageResource(R.drawable.ic_add_black_24dp);
//                            findViewById(R.id.all_employee).setVisibility(View.GONE);
//                            findViewById(R.id.report_employee).setVisibility(View.GONE);
//                            findViewById(R.id.project_employee).setVisibility(View.GONE);
//                        }
//                        counter4++;
//                    }
//                });

            }else{
                //findViewById(R.id.video_call).setVisibility(View.GONE);
            }

            if(abc.contains("VP")
                    ||abc.contains("HR")
                    ||abc.contains("ER")
                    ||abc.contains("AM")
                    ||abc.contains("SP")
                    ||abc.contains("DGM")||abc.contains("SM")||abc.contains("M")||
                    abc.contains("DM")||abc.contains("ADM")||abc.contains("CENTRE HEAD")){
//                    findViewById(R.id.video_call).setVisibility(View.VISIBLE);
//                    findViewById(R.id.video_call).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if(counter4%2==0) {
//                            imageView4.setImageResource(R.drawable.ic_baseline_remove_24);
//                            findViewById(R.id.project_employee).setVisibility(View.VISIBLE);
//                            findViewById(R.id.all_employee).setVisibility(View.VISIBLE);
//                            findViewById(R.id.report_employee).setVisibility(View.VISIBLE);
//                        }else{
//                            imageView4.setImageResource(R.drawable.ic_add_black_24dp);
//                            findViewById(R.id.project_employee).setVisibility(View.GONE);
//                            findViewById(R.id.all_employee).setVisibility(View.GONE);
//                            findViewById(R.id.report_employee).setVisibility(View.GONE);
//                        }
//                        counter4++;
//                    }
//                });

            }else{
                //findViewById(R.id.video_call).setVisibility(View.GONE);
            }

        }catch (Exception e){
            if(temp.equals("EX")){
                findViewById(R.id.attendance).setVisibility(View.INVISIBLE);
            }
            if(temp.equals("TECH")){
                findViewById(R.id.assets_tracker).setVisibility(View.VISIBLE);
                findViewById(R.id.asseter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(counter3%2==0) {
                            imageView3.setImageResource(R.drawable.ic_baseline_remove_24);
                            findViewById(R.id.assets_tracker).setVisibility(View.VISIBLE);
                            findViewById(R.id.employe_assets_tracker).setVisibility(View.VISIBLE);
                        }else{
                            imageView3.setImageResource(R.drawable.ic_add_black_24dp);
                            findViewById(R.id.assets_tracker).setVisibility(View.GONE);
                            findViewById(R.id.employe_assets_tracker).setVisibility(View.GONE);
                        }
                        counter3++;
                    }
                });
            }else{
                findViewById(R.id.assets_tracker).setVisibility(View.INVISIBLE);
                findViewById(R.id.asseter).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(counter3%2==0) {
                            imageView3.setImageResource(R.drawable.ic_baseline_remove_24);
                            findViewById(R.id.employe_assets_tracker).setVisibility(View.VISIBLE);
                        }else{
                            imageView3.setImageResource(R.drawable.ic_add_black_24dp);
                            findViewById(R.id.employe_assets_tracker).setVisibility(View.GONE);
                        }
                        counter3++;
                    }
                });
            }

            if(temp.equals("VP")||temp.equals("DGM")||temp.equals("SM")||temp.equals("M")||
                    temp.equals("DM")||temp.equals("ADM")||temp.equals("CENTRE HEAD")){
                findViewById(R.id.map).setVisibility(View.VISIBLE);

            }else{
                findViewById(R.id.map).setVisibility(View.GONE);
            }

            if(temp.equals("TL")
                    ||temp.equals("TECH")
                    ||temp.equals("ADMIN")
                    ||temp.equals("JTL")){
                //findViewById(R.id.video_call).setVisibility(View.VISIBLE);
                findViewById(R.id.report_employee).setVisibility(View.VISIBLE);

            }else{
                //findViewById(R.id.video_call).setVisibility(View.GONE);
                findViewById(R.id.report_employee).setVisibility(View.GONE);
            }

//            if(temp.equals("ADMIN")){
//                findViewById(R.id.id_card).setVisibility(View.VISIBLE);
//            }else{ findViewById(R.id.id_card).setVisibility(View.GONE); }

            if(temp.equals("VP")
                    ||temp.equals("HR")
                    ||temp.equals("ER")
                    ||temp.equals("AM")
                    ||temp.equals("SP")
                    ||temp.equals("DGM")||temp.equals("SM")||temp.equals("M")||
                    temp.equals("DM")||temp.equals("ADM")||temp.equals("CENTRE HEAD")){
                //findViewById(R.id.video_call).setVisibility(View.VISIBLE);
//                findViewById(R.id.all_employee).setVisibility(View.VISIBLE);
//                findViewById(R.id.report_employee).setVisibility(View.VISIBLE);
//                findViewById(R.id.project_employee).setVisibility(View.VISIBLE);
            }else{
                //findViewById(R.id.video_call).setVisibility(View.GONE);
//                findViewById(R.id.all_employee).setVisibility(View.GONE);
//                findViewById(R.id.report_employee).setVisibility(View.GONE);
//                findViewById(R.id.project_employee).setVisibility(View.GONE);
            }

        }

        findViewById(R.id.mnwd).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent4 = new Intent(mainmenu.this,documents_add.class);
                startActivity(intent4);
            }
        });

        findViewById(R.id.all_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,videocall1.class);
                intent.putExtra("type","ALL_EMPLOYEE");
                startActivity(intent);
            }
        });

        findViewById(R.id.report_employee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,videocall1.class);
                intent.putExtra("type","REPORT_EMPLOYEE");
                startActivity(intent);
            }
        });

        findViewById(R.id.vaccine_form).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,vaccination_form.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.vaccine_certificate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mainmenu.this,vaccination_certificate.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.imageView29).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(counter5%2==0){
                    imageView5.setImageResource(R.drawable.ic_baseline_remove_24);
                    findViewById(R.id.vaccine_form).setVisibility(View.VISIBLE);
                    findViewById(R.id.vaccine_certificate).setVisibility(View.VISIBLE);

                }else{
                    imageView5.setImageResource(R.drawable.ic_add_black_24dp);
                    findViewById(R.id.vaccine_form).setVisibility(View.GONE);
                    findViewById(R.id.vaccine_certificate).setVisibility(View.GONE);
                }
                counter5++;
            }
        });

        findViewById(R.id.vaccination).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter5%2==0){
                    imageView5.setImageResource(R.drawable.ic_baseline_remove_24);
                    findViewById(R.id.vaccine_form).setVisibility(View.VISIBLE);
                    findViewById(R.id.vaccine_certificate).setVisibility(View.VISIBLE);
                }else{
                    imageView5.setImageResource(R.drawable.ic_add_black_24dp);
                    findViewById(R.id.vaccine_form).setVisibility(View.GONE);
                    findViewById(R.id.vaccine_certificate).setVisibility(View.GONE);
                }
                counter5++;
            }
        });

        try {
            installStateUpdatedListener = new
                    InstallStateUpdatedListener() {
                        @Override
                        public void onStateUpdate(InstallState state) {
                            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                                popupSnackbarForCompleteUpdate();
                            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                                if (mAppUpdateManager != null) {
                                    mAppUpdateManager.unregisterListener(installStateUpdatedListener);
                                }

                            } else {
                                Log.i(TAG, "InstallStateUpdatedListener: state: " + state.installStatus());
                            }
                        }
                    };
        }catch (Exception e){

        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_APP_UPDATE) {
            if (resultCode != RESULT_OK) {
                Log.e(TAG, "onActivityResult: app download failed");
            }
        }
    }

    private void popupSnackbarForCompleteUpdate() {

        Snackbar snackbar =
                Snackbar.make(
                        findViewById(R.id.attendance_menu),
                        "New app is ready!",
                        Snackbar.LENGTH_INDEFINITE);

        snackbar.setAction("Install", view -> {
            if (mAppUpdateManager != null){
                mAppUpdateManager.completeUpdate();
            }
        });


        snackbar.setActionTextColor(getResources().getColor(R.color.red));
        snackbar.show();
    }

    @Override
    protected void onStop() {

        super.onStop();

        if (mAppUpdateManager != null) {
            mAppUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onStart() {

        super.onStart();

        mAppUpdateManager = AppUpdateManagerFactory.create(this);

        mAppUpdateManager.registerListener(installStateUpdatedListener);

        mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {

            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/)){

                try {
                    mAppUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE /*AppUpdateType.IMMEDIATE*/, mainmenu.this, RC_APP_UPDATE);

                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }

            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED){
                //CHECK THIS if AppUpdateType.FLEXIBLE, otherwise you can skip
                popupSnackbarForCompleteUpdate();
            } else {
                Log.e(TAG, "checkForAppUpdateAvailability: something else");
            }
        });
    }

    public class backgroundworker22 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url = null;
            try {
                url = new URL(json_url22);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb22 = new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status = new SessionMaintance(mainmenu.this);
                fcm_token = status.get_firebase_token();
                String post_data = URLEncoder.encode("fcm_token", "UTF-8") + "=" + URLEncoder.encode(fcm_token, "UTF-8") + "&"
                        + URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id().toString(), "UTF-8");
                Log.d("PostData", "" + post_data);
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                while ((json_string22 = bufferedReader.readLine()) != null) {
                    sb22.append(json_string22 + "\n");
                    Log.d("json_string", "" + json_string22);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return sb22.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(mainmenu.this, "Please Check Your Network Connection", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            new backgroundworker6().execute();
            progressDialog = new ProgressDialog(mainmenu.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            Log.d("ASASAS", "" + result);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onDestroy() {
        Intent broadcastIntent = new Intent(this, SensorRestarterBroadcastReceiver.class);
        sendBroadcast(broadcastIntent);
        //stopService(mServiceIntent);
        Log.i("MAINACT", "onDestroy!");
        super.onDestroy();

    }

    public void show4(){
        mRelativeLayout21 = (ScrollView) findViewById(R.id.attendance_menu);
        mPopupWindow21 = new PopupWindow(
                customView21,
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                true
        );
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow21.setElevation(5.0f);
        }

        mPopupWindow21.setOutsideTouchable(false);
        mPopupWindow21.setFocusable(true);
        mPopupWindow21.update();

        listView1 = (ListView) customView21.findViewById(R.id.listView);

        ImageView close_button = (ImageView) customView21.findViewById(R.id.imageView4);

        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow21.dismiss();
            }
        });

        //listView.setDivider(null);
        customAdapter1 = new CustomAdapter2();
        listView1.setAdapter(customAdapter1);


        mPopupWindow21.setAnimationStyle(R.style.popup_window_animation);
        mPopupWindow21.showAtLocation(customView21, Gravity.CENTER, 0, 0);
    }

    public class backgroundworker3 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url11);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                leave_Name.clear();counter.clear();
                sb11=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(mainmenu.this);
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
                while((json_string11=bufferedReader.readLine())!=null)
                {
                    sb11.append(json_string11+"\n");
                    Log.d("json_string",""+json_string11);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb11.toString());
                return sb11.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string11 = result;
            progressDialog.dismiss();
            int count = 0;
            try {
                jsonArray11 = new JSONArray(json_string11);

                while (count < jsonArray11.length()) {
                    JSONObject jo = jsonArray11.getJSONObject(count);
                    leave_Name.add(jo.getString("present_full_name"));

                    Float tem1 = Float.valueOf(jo.getString("total"));
                    Float tem2 = Float.valueOf(jo.getString("last_total"));
                    Float tem3 = Float.valueOf(jo.getString("taken"));
                    Float tem4 = Float.valueOf(jo.getString("encash"));
                    Float tem = (tem1 + tem2) - (tem3+tem4);
                    counter.put(jo.getString("present_full_name"), String.valueOf(tem));
                    tem = 0.0f;
                    tem1 = 0.0f;
                    tem2 = 0.0f;
                    tem3 = 0.0f;
                    tem4 =0.0f;
                    count++;


                }

                Log.d("QWERTYUIOP",""+jsonArray11.length()+"\n\n"+counter);
                show4();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public class CustomAdapter2 extends BaseAdapter {


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
                final SessionMaintance status=new SessionMaintance(mainmenu.this);
                String post_data = URLEncoder.encode("login_emp_id","UTF-8")+"="+URLEncoder.encode(status.get_username(),"UTF-8")+"&"
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
                Log.d("ASASASAS",""+jsonArray.length());
                while (count < jsonArray.length()) {
                    JSONObject jo = jsonArray.getJSONObject(count);
                    Lname.add(jo.getString("name").toUpperCase()+" "+jo.getString("last_name").toUpperCase());
                    Lemp_id.add(jo.getString("emp_id"));
                    emp_id_map.put(jo.getString("name").toUpperCase()+" "+jo.getString("last_name").toUpperCase(),jo.getString("emp_id"));
                    count++;
                }
                notify_count.setText(String.valueOf(count));
                if(notify_count.getText().toString().equals("0")){
                    findViewById(R.id.pendingattendance).setVisibility(View.GONE);
                }else{
                    findViewById(R.id.pendingattendance).setVisibility(View.VISIBLE);
                    show3();
                }
                new backgroundworker2().execute();
                progressDialog = new ProgressDialog(mainmenu.this);
                progressDialog.setMessage("Please Wait...!!!");
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);
            } catch (JSONException e) {
                e.printStackTrace();
            }catch (Exception e) {
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
                sb=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status=new SessionMaintance(mainmenu.this);
                String post_data = URLEncoder.encode("walkin_id","UTF-8")+"="+URLEncoder.encode(status.get_field_id(),"UTF-8")+"&"
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
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string2 = result;
            progressDialog.dismiss();
            int count2 = 0;
            notify_count2.setText(String.valueOf(json_string2));
            if(notify_count2.getText().toString().equals("0")){
                findViewById(R.id.mtrackerattendance).setVisibility(View.GONE);
            }else {
                findViewById(R.id.mtrackerattendance).setVisibility(View.VISIBLE);
            }
            new backgroundworker22().execute();
            progressDialog = new ProgressDialog(mainmenu.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
        }
    }

    public class backgroundworker6 extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            String newVersion = null;

            try {
                Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=mahendra.school.mpulse1&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get();
                if (document != null) {
                    Elements element = document.getElementsContainingOwnText("Current Version");
                    for (Element ele : element) {
                        if (ele.siblingElements() != null) {
                            Elements sibElemets = ele.siblingElements();
                            for (Element sibElemet : sibElemets) {
                                newVersion = sibElemet.text();
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
            Log.d("ASASASAS",""+currentVersion+"   "+newVersion);
            return newVersion;
        }

        @Override
        protected void onPostExecute(String fnonlineVersion) {
            super.onPostExecute(onlineVersion);
            onlineVersion = fnonlineVersion;
            progressDialog.dismiss();
//            if (onlineVersion != null && !onlineVersion.isEmpty()) {
//
//                if (Float.valueOf(currentVersion) < Float.valueOf(onlineVersion)) {
//
//                    final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mainmenu.this);
//                    builder.setCancelable(false);
//                    builder.setTitle("UPDATE");
//                    builder.setMessage("Please Update the App").setIcon(R.mipmap.logo);
//                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialogInterface, int i) {
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                                try {
//                                    Intent viewIntent =
//                                            new Intent("android.intent.action.VIEW",
//                                                    Uri.parse("https://play.google.com/store/apps/details?id=mahendra.school.mpulse1"));
//                                    startActivity(viewIntent);
//                                }catch(Exception e) {
////                                    Toast.makeText(getApplicationContext(),"Unable to Connect Try Again...",
////                                            Toast.LENGTH_LONG).show();
//                                    e.printStackTrace();
//                                }
//                                finishAffinity();
//                                System.exit(0);
//                            }
//                        }
//                    });
//
//                    android.app.AlertDialog dialog = builder.create();
//                    dialog.getWindow().setLayout(600, 400);
//                    dialog.show();
//
//                }
//
//            }

            new backgroundworker66().execute();
            progressDialog = new ProgressDialog(mainmenu.this);
            progressDialog.setMessage("Please Wait...!!!");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);

        }
    }

    public class backgroundworker66 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url222);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb222=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status = new SessionMaintance(mainmenu.this);
                String post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id(), "UTF-8") + "&"
                                + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(status.get_username(), "UTF-8") + "&"
                                + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(status.get_branch_id(), "UTF-8");

                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string222=bufferedReader.readLine())!=null)
                {
                    sb222.append(json_string222+"\n");
                    Log.d("json_string",""+json_string222);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb222.toString());
                return sb222.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string222 = result;
            progressDialog.dismiss();
            int count = 0;
            try {
                JSONArray  jsonArray = new JSONArray(json_string222);
                if(jsonArray.length()>0){
                    findViewById(R.id.cel).setVisibility(View.VISIBLE);
                    findViewById(R.id.cel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mainmenu.this,employee_list.class);
                            intent.putExtra("link",json_string222);
                            startActivity(intent);
                        }
                    });
                }
//                new backgroundworker666().execute();
//                progressDialog = new ProgressDialog(mainmenu.this);
//                progressDialog.setMessage("Please Wait...!!!");
//                progressDialog.show();
//                progressDialog.setCanceledOnTouchOutside(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class backgroundworker333 extends AsyncTask<Void,Void,String> {

        @Override
        protected String doInBackground(Void... voids) {
            URL url= null;
            try {
                url = new URL(json_url33);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                sb33=new StringBuffer();
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                final SessionMaintance status = new SessionMaintance(mainmenu.this);
                String post_data = URLEncoder.encode("walkin_id", "UTF-8") + "=" + URLEncoder.encode(status.get_field_id(), "UTF-8") + "&"
                        + URLEncoder.encode("emp_id", "UTF-8") + "=" + URLEncoder.encode(status.get_username(), "UTF-8") + "&"
                        + URLEncoder.encode("branch_id", "UTF-8") + "=" + URLEncoder.encode(status.get_branch_id(), "UTF-8")+ "&"
                        + URLEncoder.encode("fcm_token", "UTF-8") + "=" + URLEncoder.encode(status.get_firebase_token(), "UTF-8")+"&"
                        +URLEncoder.encode("user_type","UTF-8")+"="+ URLEncoder.encode(status.get_user_type(),"UTF-8");

                bufferedWriter.write(post_data);
                Log.d("PostData",""+post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream=httpURLConnection.getInputStream();
                BufferedReader bufferedReader =new BufferedReader(new InputStreamReader(inputStream));
                while((json_string33=bufferedReader.readLine())!=null)
                {
                    sb33.append(json_string33+"\n");
                    Log.d("json_string",""+json_string33);
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("GGG",""+sb33.toString());
                return sb33.toString().trim();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            json_string33 = result;
            progressDialog.dismiss();
            int count = 0;
            try {
                    JSONObject jsonObject = new JSONObject(result);

                    if(jsonObject.getString("emp_details").equals("0")){
                        findViewById(R.id.cel).setVisibility(View.GONE);
                    }else{
                        findViewById(R.id.cel).setVisibility(View.VISIBLE);
                    }

                    if(jsonObject.getString("attd_notification").equals("0")){
                        findViewById(R.id.pendingattendance).setVisibility(View.GONE);
                    }else{
                        findViewById(R.id.pendingattendance).setVisibility(View.VISIBLE);
                    }

                    if(jsonObject.getString("mt_attd_notification").equals("0")){
                        findViewById(R.id.mtrackerattendance).setVisibility(View.GONE);
                    }else{
                        findViewById(R.id.mtrackerattendance).setVisibility(View.VISIBLE);
                    }

                    leave_app1 = Integer.parseInt(jsonObject.getString("leave_approve1"));

                    leave_app2 = Integer.parseInt(jsonObject.getString("leave_approve2"));

                    if(leave_app1==0&&leave_app2==0){
                        findViewById(R.id.attendance1).setVisibility(View.GONE);
                    }else  if(leave_app1==0&&leave_app2>0){
                        findViewById(R.id.attendance1).setVisibility(View.VISIBLE);
                        approv_txt.setText("LEAVE APPROVAL"+" - 2nd : "+leave_app2);
                    }else  if(leave_app1>0&&leave_app2==0){
                        findViewById(R.id.attendance1).setVisibility(View.VISIBLE);
                        approv_txt.setText("LEAVE APPROVAL"+" - 1st : "+leave_app1);
                    }else  if(leave_app1>0&&leave_app2>0){
                        findViewById(R.id.attendance1).setVisibility(View.VISIBLE);
                        approv_txt.setText("LEAVE APPROVAL"+" - 1st : "+leave_app1+" 2nd : "+leave_app2);
                    }




            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void checkermac(){
        if(leave_app1==0){
            findViewById(R.id.leave_approval1).setVisibility(View.GONE);
        }else{
            findViewById(R.id.leave_approval1).setVisibility(View.VISIBLE);
        }

        if(leave_app2==0){
            findViewById(R.id.leave_approval2).setVisibility(View.GONE);
        }else{
            findViewById(R.id.leave_approval2).setVisibility(View.VISIBLE);
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

            case R.id.location:
                Intent intent3 = new Intent(this,add_location.class);
                startActivity(intent3);
                return true;

                case R.id.hc:
                Intent intent5 = new Intent(this,holiday_calender.class);
                startActivity(intent5);
                return true;


            default:return super.onOptionsItemSelected(item);
        }
    }

    public void show3(){
        mRelativeLayout2 = (ScrollView) findViewById(R.id.attendance_menu);
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
        //listView.setDivider(null);
        customAdapter = new CustomAdapter();
        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(mainmenu.this,Lemp_id.get(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mainmenu.this,pending_attendance.class);
                intent.putExtra("emp_id_sub",Lemp_id.get(position));
                startActivity(intent);
                mPopupWindow2.dismiss();
            }
        });


        mPopupWindow2.setAnimationStyle(R.style.popup_window_animation);
        mPopupWindow2.showAtLocation(customView2, Gravity.CENTER, 0, 0);
    }

    public class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return Lemp_id.size();
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
            view = getLayoutInflater().inflate(R.layout.custom_layout2, null);

            CardView card = (CardView) view.findViewById(R.id.pend_atten_list);

            if (i % 2 == 1) {
                card.setBackgroundColor(Color.parseColor("#ffffff"));
            } else {
                card.setBackgroundColor(Color.parseColor("#DBDBDB"));
            }

            TextView name = (TextView)view.findViewById(R.id.textView18);
            TextView emp_id = (TextView)view.findViewById(R.id.textView3);

            name.setText(Lname.get(i));
            emp_id.setText(Lemp_id.get(i));

            return view;
        }
    }

}
