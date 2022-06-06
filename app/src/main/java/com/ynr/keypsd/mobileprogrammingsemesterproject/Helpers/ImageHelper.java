package com.ynr.keypsd.mobileprogrammingsemesterproject.Helpers;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.ImageView;

import java.net.URLConnection;

public class ImageHelper {

    public static void applyGrayscaleFilterToAllImageViews(ImageView[] imageViews){
        for(ImageView imageView : imageViews)
            applyGrayscaleFilter(imageView);
    }

    public static void applyGrayscaleFilter(ImageView imageView){
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        imageView.setColorFilter(filter);
    }



}
