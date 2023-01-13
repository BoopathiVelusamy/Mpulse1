package mahendra.school.mpulse1;

import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

public class video_meeting extends FragmentActivity {
    //private JitsiMeetView view;

//    @Override
//    public void onBackPressed() {
//        JitsiMeetActivityDelegate.onBackPressed();
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.stop();
//        view = new JitsiMeetView(this);
//
//        JitsiMeetConferenceOptions options = new JitsiMeetConferenceOptions.Builder()
//                .setRoom("https://meet.jit.si/TEST")
//                .build();
//        view.join(options);
//        setContentView(view);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        view.dispose();
//        view = null;
//        JitsiMeetActivityDelegate.onHostDestroy(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // JitsiMeetActivityDelegate.onHostResume(this); }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        JitsiMeetActivityDelegate.onHostPause(this); }
//
//    @Override
//    public void requestPermissions(String[] strings, int i, PermissionListener permissionListener) { }
    }
}