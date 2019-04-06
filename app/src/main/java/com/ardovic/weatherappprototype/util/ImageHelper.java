package com.ardovic.weatherappprototype.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import androidx.annotation.NonNull;

/**
 * @author Polurival on 12.05.2018.
 */
public class ImageHelper {

    public static Bitmap getResizedBitmap(@NonNull Bitmap bmp, int newHeight, int newWidth) {
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        return Bitmap.createBitmap(bmp, 0, 0, width, height,
                matrix, false);
    }
}
