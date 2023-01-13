package mahendra.school.mpulse1;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

public class SensorRestarterBroadcastReceiver extends BroadcastReceiver {

    private SensorService mSensorService;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(SensorRestarterBroadcastReceiver.class.getSimpleName(), "Service Stops! Oooooooooooooppppssssss!!!!");

        //Intent mServiceIntent = new Intent(context, mSensorService.getClass());
        //context.stopService(intent);

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            context.startForegroundService(new Intent(context, SensorService.class));
//        } else {
//            context.startService(new Intent(context, SensorService.class));
//        }

    }

}
