                             package mahendra.school.mpulse1;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Santhosh.0983 on 5/8/2018.
 */

public class firebase_token extends FirebaseInstanceIdService {

    String recent_token;

    // Getting the Recent Token
    @Override
    public void onTokenRefresh() {
        SessionMaintance status=new SessionMaintance(this);
        recent_token= FirebaseInstanceId.getInstance().getToken();
        Log.d("RECENT_TOKEN",""+recent_token);
        status.putfirebasetoken(recent_token);
    }
}
