package com.pruebaandroid.misterhouse.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.pruebaandroid.misterhouse.R;

/**
 * Created by Andres on 06/09/2016.
 */
public class Utils {

    public static Boolean isConnected(Context context)
    {
        Boolean rtaisConnected  =false;
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnected()){
            rtaisConnected=true;
        }
        return rtaisConnected;
    }
    public static void setsessionToken (Activity activity,String sessionToken)
    {
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(activity.getApplicationContext().getString(R.string.sessionToken), sessionToken);
        editor.commit();

    }


    public static String getsessionToken(Activity activity) {
        String sessionToken;
        SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
        String defaultValue = activity.getApplicationContext().getString(R.string.sessionToken);
        sessionToken = sharedPref.getString(activity.getApplicationContext().getString(R.string.sessionToken), defaultValue);
        return sessionToken;
    }
}
