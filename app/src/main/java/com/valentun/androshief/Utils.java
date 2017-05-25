package com.valentun.androshief;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayOutputStream;

import static android.content.Context.MODE_PRIVATE;
import static com.valentun.androshief.Constants.APP_PREFERENCES;
import static com.valentun.androshief.Constants.MAX_QUALITY;

/**
 * Created by Valentun on 14.03.2017.
 */

public class Utils {
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
        return ThumbnailUtils.extractThumbnail(bitmap, (int) (size * scale), (int) (size * scale));
    }

    public static String encodeBitmap(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, MAX_QUALITY, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap decodeBitmap(String stringImage, boolean hasPrefix) {
        if (hasPrefix) stringImage = stringImage.substring(23);
        byte[] decodedBytes = Base64.decode(stringImage, 0);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    public static void sendNewTaskIntent (Activity activity, Class<?> cls){
        Intent intent = new Intent(activity, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    public static void dismissProgressDialog(ProgressDialog dialog) {
        try {
            dialog.dismiss();
        } catch (Exception e) {
            // Do nothing.
        } finally {
            dialog = null;
        }
    }

    public static MultiValueMap<String, String> getAuthHeaders (Activity activity) {
        SharedPreferences sPref = activity.getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE);

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("ACCESS-TOKEN", sPref.getString("ACCESS-TOKEN", ""));
        headers.add("CLIENT", sPref.getString("CLIENT", ""));
        headers.add("UID", sPref.getString("EMAIL", ""));

        return headers;
    }
}
