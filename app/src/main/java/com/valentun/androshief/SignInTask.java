package com.valentun.androshief;

import android.app.Activity;
import android.app.ProgressDialog;
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

import static com.valentun.androshief.Constants.AUTH_MODE;
import static com.valentun.androshief.Utils.dismissProgressDialog;
import static com.valentun.androshief.Utils.saveAuthData;
import static com.valentun.androshief.Utils.sendNewTaskIntent;

/**
 * Created by Valentun on 17.03.2017.
 */

public class SignInTask extends AsyncTask<String, Void, ResponseEntity<User>> {
    private ProgressDialog progress;
    private String password;
    private Activity activity;
    private View container;
    private AUTH_MODE mode;

    public SignInTask(Activity activity, View container, AUTH_MODE mode) {
        this.container = container;
        this.activity = activity;
        this.mode = mode;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        String body = mode == AUTH_MODE.SIGN_IN ? activity.getString(R.string.sign_in_progress_body)
                                                : activity.getString(R.string.reauthorize_body);
        progress = ProgressDialog.show(activity, activity.getString(R.string.progress_title), body, true);
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
        dismissProgressDialog(progress);

        if (saveAuthData(response, activity, password)) {
            if (mode == AUTH_MODE.SIGN_IN) {
                sendNewTaskIntent(activity, MainActivity.class);
            }
        }
    }
}
