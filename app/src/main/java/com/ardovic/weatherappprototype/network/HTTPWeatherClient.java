package com.ardovic.weatherappprototype.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import androidx.annotation.Nullable;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @deprecated - используется retrofit и {@link com.ardovic.weatherappprototype.util.ImageHelper}
 */
public class HTTPWeatherClient {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    private static final String IMG_URL = "http://openweathermap.org/img/w/";
    private static final String API_KEY = "1780541fd97c219bcb6b471152ad65c7";
    @Nullable
    public String getWeatherData(String location) {
        HttpURLConnection con = null ;
        InputStream is = null;
        URL url = null;
        StringBuffer buffer = new StringBuffer();
            try {
                url = new URL(Uri.parse(BASE_URL).buildUpon()
                        .appendQueryParameter("q", location)
                        .appendQueryParameter("APPID", API_KEY)
                        .build().toString());

                con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setDoOutput(true);
                con.connect();

                // Let's read the response
                is = con.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String line = null;
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                    buffer.append("\n");
                }
                is.close();
                con.disconnect();
            } catch (Throwable t) {
                t.printStackTrace();
            } finally {
                try {
                    if (is != null)
                    is.close();
                } catch (Throwable t) {
                }
                try {
                    if (con != null)
                        con.disconnect();
                } catch (Throwable t) {
                }
            }

        return buffer.toString();
    }
    @Nullable
    public static Bitmap getBitmapFromURL(String icon) {
        try {
            java.net.URL url = new java.net.URL(IMG_URL + icon + ".png");
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            return BitmapFactory.decodeStream(input);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap bmp, int newHeight, int newWidth) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    /*
    public byte[] getImage(String code) {
        HttpURLConnection con = null ;
        InputStream is = null;
        try {
            con = (HttpURLConnection) ( new URL(IMG_URL + code)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(true);
            con.connect();

            // Let's read the response
            is = con.getInputStream();
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            while ( is.read(buffer) != -1)
                baos.write(buffer);

            return baos.toByteArray();
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
        finally {
            try { is.close(); } catch(Throwable t) {}
            try { con.disconnect(); } catch(Throwable t) {}
        }

        return null;

    }
    */

}
