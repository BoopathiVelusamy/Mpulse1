package mahendra.school.mpulse1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class sign_out extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_out);
        final SessionMaintance status=new SessionMaintance(sign_out.this);
        status.set_user_type("");
        status.set_username("");
        status.set_field_id("");
        status.set_mobile("");
        status.set_email("");
        status.set_user_token("");
        Intent intent =new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
