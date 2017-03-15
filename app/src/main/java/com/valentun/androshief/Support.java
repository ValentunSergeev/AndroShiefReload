package com.valentun.androshief;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.ThumbnailUtils;
import android.support.v7.widget.AppCompatButton;
import android.util.Base64;

import com.valentun.androshief.DTOs.User;

import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;

import static android.content.Context.MODE_PRIVATE;
import static com.valentun.androshief.Constants.APP_PREFERENCES;
import static com.valentun.androshief.Constants.MAX_QUALITY;

/**
 * Created by Valentun on 14.03.2017.
 */

public class Support {
    public static void colorizeButton(AppCompatButton btn, int color) {
        btn.setTextColor(Color.WHITE);
        btn.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
    }

    public static boolean saveAuthData(ResponseEntity<User> response, Activity activity, String password) {
        if (response != null) {
            User user = response.getBody();
            MultiValueMap<String, String> headers = response.getHeaders();

            SharedPreferences sPref = activity.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);
            SharedPreferences.Editor ed = sPref.edit();

            ed.putString("ACCESS-TOKEN", headers.getFirst("access-token"));
            ed.putString("CLIENT", headers.getFirst("client"));

            ed.putString("NAME", user.getData().getName());
            ed.putString("IMAGE", user.getData().getImage());
            ed.putString("EMAIL", user.getData().getUid());
            ed.putString("PASSWORD", password);

            ed.putLong("AUTH_TIME", System.currentTimeMillis());

            ed.commit();

            return true;
        }

        return false;
    }

    public static Bitmap getImage(Bitmap bitmap , Context context, int size) {
        final float scale = context.getResources().getDisplayMetrics().density;
        Bitmap bmp = ThumbnailUtils.extractThumbnail(bitmap, (int) (size * scale), (int) (size * scale));
        return bmp;
    }

    public static String encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, MAX_QUALITY, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }

    public static Bitmap decodeBitMap(String stringImage) {
        byte[] decodedBytes = Base64.decode(stringImage, 0);
        Bitmap bmp = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        return bmp;
    }
}
