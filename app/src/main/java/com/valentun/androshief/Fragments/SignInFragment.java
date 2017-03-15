package com.valentun.androshief.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valentun.androshief.Constants;
import com.valentun.androshief.DTOs.User;
import com.valentun.androshief.MainActivity;
import com.valentun.androshief.R;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import static com.valentun.androshief.Constants.ORANGE;
import static com.valentun.androshief.Support.colorizeButton;
import static com.valentun.androshief.Support.saveAuthData;

/**
 * Created by Valentun on 14.03.2017.
 */

public class SignInFragment extends Fragment {

    private AppCompatEditText inputEmail, inputPassword;
    private AppCompatButton signIn;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_in, container, false);

        inputEmail = (AppCompatEditText) view.findViewById(R.id.sign_in_name);
        inputPassword = (AppCompatEditText) view.findViewById(R.id.sign_in_password);

        signIn = (AppCompatButton) view.findViewById(R.id.sign_in_submit);

        colorizeButton(signIn, ORANGE);

        signIn.setOnClickListener(view -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();

            new SignInTask().execute(email, password);
        });

        return view;
    }


    public class SignInTask extends AsyncTask<String, Void, ResponseEntity<User>> {
        private ProgressDialog progress;
        private String password;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = ProgressDialog.show(getActivity(), "Signing in...",
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

            ResponseEntity<User> response = null;

            try {
                response = restTemplate.postForEntity(Constants.URL.SIGN_IN,
                        null, User.class, headers);

            } catch (org.springframework.web.client.HttpClientErrorException e) {
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
            return response;
        }


        @Override
        protected void onPostExecute(ResponseEntity<User> response) {
            progress.dismiss();

            if (saveAuthData(response, getActivity(), password)) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

}
