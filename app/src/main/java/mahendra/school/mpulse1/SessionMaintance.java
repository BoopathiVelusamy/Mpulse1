package mahendra.school.mpulse1;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Santhosh.0983 on 4/24/2018.
 */

public class SessionMaintance {
    Context ctx;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    SessionMaintance(Context ctx)
    {
        this.ctx=ctx;
        prefs=ctx.getSharedPreferences("myapp", Context.MODE_PRIVATE);
        editor=prefs.edit();
    }

    public void set_user_id(String user_id){
        editor.putString("user_id",user_id);
        editor.commit();
    }

    public String get_user_id(){ return prefs.getString("user_id","");}

    public void set_user_token(String token){
        editor.putString("token",token);
        editor.commit();
    }

    public String get_token(){ return prefs.getString("token","");}


    public void set_username(String username){
        editor.putString("username",username);
        editor.commit();
    }

    public String get_username(){ return prefs.getString("username","");}

    public void set_user_type(String user_type){
        editor.putString("user_type",user_type);
        editor.commit();
    }

    public String get_user_type(){ return prefs.getString("user_type","");}


    public void  putfirebasetoken(String firebase_token)
    {
        editor.putString("firebase_token",firebase_token);
        editor.commit();
    }

    public String get_firebase_token()
    {
        return prefs.getString("firebase_token","");
    }

    public void set_field_id(String field_id){
        editor.putString("field_id",field_id);
        editor.commit();
    }

    public String get_field_id(){ return prefs.getString("field_id","");}

    public void set_email(String email){
        editor.putString("email",email);
        editor.commit();
    }

    public String get_email(){ return prefs.getString("email","");}

    public void set_mobile(String mobile){
        editor.putString("mobile",mobile);
        editor.commit();
    }

    public String get_mobile(){ return prefs.getString("mobile","");}

    public void set_branch_id(String branch_id){
        editor.putString("branch_id",branch_id);
        editor.commit();
    }

    public String get_branch_id(){ return prefs.getString("branch_id","");}

    public void set_role_name(String role_name){
        editor.putString("role_name",role_name);
        editor.commit();
    }

    public String get_role_name(){ return prefs.getString("role_name","");}



}
