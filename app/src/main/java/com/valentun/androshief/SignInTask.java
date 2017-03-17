package com.valentun.androshief;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.valentun.androshief.DTOs.User;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.valentun.androshief.Support.saveAuthData;

/**
 * Created by Valentun on 17.03.2017.
 */

public class SignInTask extends AsyncTask<String, Void, ResponseEntity<User>> {
    private ProgressDialog progress;
    private String password;
    private Activity activity;
    private View container;

    public SignInTask(Activity activity, View container) {
        this.container = container;
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress = ProgressDialog.show(activity, "Signing in...",
                "We are searching your account, please wait a bit...", true);
    }


    @Override
    protected ResponseEntity<User> doInBackground(String... strings) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


        password = strings[1];

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json");
        headers.add("email", strings[0]);
        headers.add("password", password);

        HttpEntity<String> entity = new HttpEntity<>("", headers);

        ResponseEntity<User> response = null;

        try {
            response = restTemplate.exchange(Constants.URL.SIGN_IN, HttpMethod.POST, entity, User.class);

        } catch (org.springframework.web.client.HttpClientErrorException e) {
            Snackbar.make(container, e.getResponseBodyAsString(), Snackbar.LENGTH_LONG).show();
        }
        return response;
    }


    @Override
    protected void onPostExecute(ResponseEntity<User> response) {
        progress.dismiss();

        if (saveAuthData(response, activity, password)) {
            Intent intent = new Intent(activity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
        }
    }
}
