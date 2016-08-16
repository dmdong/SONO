package com.manhdong.sono.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Saphiro on 8/9/2016.
 */
public class GetPreference {
    SharedPreferences preferences;
    Context context;
    int mode = Context.MODE_PRIVATE;

    public GetPreference(Context context) {
        this.context = context;
    }

    SharedPreferences getUserPref(){
        preferences = context.getSharedPreferences("Users", mode);
        return preferences;
    }

    public boolean hasThisUser(String username){
        if (username.equals("default")){
            return false;
        }else return getUserPref().contains(username);
    }

    public void savethisUser(String user, String passcode){
        getUserPref().edit().putString(user, passcode).apply();
    };

    public boolean checkUserPass(String user, String passcode){
        boolean isValid = getUserPref().getString(user, "zz").equals(passcode);
        if (isValid){
            return true;
        }else return false;
    }

    SharedPreferences getLatestUserPref(){
        preferences = context.getSharedPreferences("LatestUser", mode);
        return preferences;
    }

    public String getLatestUsername(){
        return getLatestUserPref().getString("LatestUser", "default");
    }

    public void setLatestUser(String user){
        getLatestUserPref().edit().clear().putString("LatestUser", user).apply();
    }

    SharedPreferences getPassPref(){
        preferences = context.getSharedPreferences("Passcode", mode);
        return preferences;
    }

    public boolean getPasscodeOnOff(String user){
        return getPassPref().getBoolean("STATUS"+user, false);
    }

    public void setPasscodeOnOff(boolean status, String user){
        if(status){
            getPassPref().edit().putBoolean("STATUS"+user, false).apply();
        }else {
            getPassPref().edit().putBoolean("STATUS"+user, true).apply();
        }
    }

    SharedPreferences getUserEmailPref(){
        preferences = context.getSharedPreferences("UserEmail", mode);
        return preferences;
    }

    public String getUserEmail(String username){
        return getUserEmailPref().getString(username, "sono@gmail.com");
    }

    public void setUserEmail(String username, String usermail){
        getUserEmailPref().edit().putString(username, usermail).apply();
    }


}
