package mahendra.school.mpulse1;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.telephony.AvailableNetworkInfo.PRIORITY_HIGH;


public class firebase_message extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMessageService";
    Bitmap bitmap;
    PendingIntent pendingIntent;

    String title="",message="",imageUri="";
    Notification notification1;
    RemoteViews remoteViews;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        title=remoteMessage.getData().get("title");
        message=remoteMessage.getData().get("body");
        String imageUri = remoteMessage.getData().get("image");
        bitmap = getBitmapfromUrl(imageUri);
        Log.d("HIHIHIHHIIFIRE",""+imageUri);

        remoteViews = new RemoteViews("mahendra.school.mpulse1",R.layout.custom_layout1);


        if(title.equals("VIDEO_MEETING")){
            Intent intent=new Intent(this,video_meeting.class);
            intent.putExtra("video_name",message);
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.mipmap.mpulselogocolor);
            notificationBuilder.setLargeIcon(bitmap);
//            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
//                    .bigPicture(bitmap));
            //notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText("YOU ARE TO ATTEND THE MPULSE VIDEO CALL");
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setPriority(NotificationManager.IMPORTANCE_HIGH);
            notificationBuilder.setCategory(NotificationCompat.CATEGORY_CALL);
            notificationBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
            notificationBuilder.setCustomContentView(remoteViews);
            notificationBuilder.setCustomBigContentView(remoteViews);
            notificationBuilder.setTicker("Call_Status");
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
            notificationBuilder.setFullScreenIntent(pendingIntent,true);
            getNotification();
        }else {

            Intent intent=new Intent(this,mainmenu.class);
            intent.putExtra("TYPE","z");
            //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
            notificationBuilder.setSmallIcon(R.mipmap.mpulselogocolor);
            notificationBuilder.setLargeIcon(bitmap);
            notificationBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap));
            //notificationBuilder.setContentTitle(title);
            notificationBuilder.setContentText("Pending Attendance - Alert");
            notificationBuilder.setAutoCancel(true);
            notificationBuilder.setFullScreenIntent(pendingIntent,true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, notificationBuilder.build());
            getNotification();
        }
    }

    public Bitmap getBitmapfromUrl(String imageUrl) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            bitmap.compress(Bitmap.CompressFormat.JPEG,50,stream);
            return bitmap;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Notification getNotification() {
        String channel;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            channel = createChannel();
        else {
            channel = "";
        }
        if(title.equals("VIDEO_MEETING")){
            remoteViews = new RemoteViews("mahendra.school.mpulse1",R.layout.custom_layout_dialog1);
            Intent intent=new Intent(this,video_meeting.class);
            intent.putExtra("video_name",message);
            pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel notificationChannel = new NotificationChannel("Incomin call", "Incoming call", NotificationManager.IMPORTANCE_HIGH);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(notificationChannel);
                NotificationCompat.Builder notification = new NotificationCompat.Builder(this, "Incoming Call");
                notification.setContentTitle("HAI");
                notification.setTicker("HIA");
                notification.setContentText("HAI");
                notification.setSmallIcon(R.drawable.logo);
                notification.setDefaults(Notification.DEFAULT_LIGHTS);
                notification.setDefaults(Notification.DEFAULT_SOUND);
                notification.setCategory(NotificationCompat.CATEGORY_CALL);
                notification.setVibrate(null);
                notification.setOngoing(true);
                notification.setFullScreenIntent(pendingIntent, true);
                notification.setStyle(new NotificationCompat.DecoratedCustomViewStyle());
                notification.setCustomContentView(remoteViews);
                notification.setCustomBigContentView(remoteViews);
                startForeground(1124, notification.build());
            }else{
               // NotificationChannel notificationChannel = new NotificationChannel("Incomin call", "Incoming call", NotificationManager.IMPORTANCE_HIGH);

            }
        }else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, channel).setSmallIcon(R.mipmap.mpulselogocolor).setContentTitle("");
            mBuilder.setStyle(new NotificationCompat.BigPictureStyle()
                    .bigPicture(bitmap));
            mBuilder.setLargeIcon(bitmap);
            //mBuilder.setContentTitle(title);
            mBuilder.setContentText("Pending Attendance - Alert");
            mBuilder.setAutoCancel(true);
            mBuilder.setFullScreenIntent(pendingIntent,true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(0, mBuilder.build());

            notification1 = mBuilder
                    .setPriority(PRIORITY_HIGH)
                    .setCategory(Notification.CATEGORY_CALL)
                    .build();
        }

        return notification1;
    }

    @NonNull
    @TargetApi(26)
    private synchronized String createChannel() {
        NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        String name = "snap map fake location";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel mChannel = new NotificationChannel("snap map channel", name, importance);
        mChannel.enableLights(true);
        mChannel.enableVibration(true);
        mChannel.setLightColor(Color.BLUE);
        if (mNotificationManager != null) {
            mNotificationManager.createNotificationChannel(mChannel);
        } else {
            stopSelf();
        }
        return "snap map channel";
    }



}
