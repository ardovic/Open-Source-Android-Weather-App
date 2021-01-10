package com.ardovic.weatherappprototype;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

public class InternetConnectivityHelper {

    private Context context;

    public InternetConnectivityHelper(Context context) {
        this.context = context;
    }

    public boolean checkDeviceConnectedToInternet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (networkCapabilities != null) {
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                    return true;
                if (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI))
                    return true;
                return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET);
            } else return false;

        } else {
            NetworkInfo network = connectivityManager.getActiveNetworkInfo();
            return network!=null && network.isConnected();
        }
    }
}
