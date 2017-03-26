package com.valentun.androshief.Fragments;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valentun.androshief.Constants;
import com.valentun.androshief.DTOs.Register;
import com.valentun.androshief.DTOs.User;
import com.valentun.androshief.MainActivity;
import com.valentun.androshief.R;
import com.valentun.androshief.Utils;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static com.valentun.androshief.Constants.GALLERY_REQUEST;
import static com.valentun.androshief.Constants.ORANGE;
import static com.valentun.androshief.Utils.colorizeButton;
import static com.valentun.androshief.Utils.dismissProgressDialog;
import static com.valentun.androshief.Utils.encodeBitmap;
import static com.valentun.androshief.Utils.getImage;
import static com.valentun.androshief.Utils.saveAuthData;

/**
 * Created by Valentun on 14.03.2017.
 */

public class RegisterFragment extends Fragment {
    private AppCompatEditText inputEmail, inputPassword, inputName;
    private CircleImageView inputImage;
    private Bitmap image, smallImage;
    private AppCompatButton register;
    private View view;
    private Activity activity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_register, container, false);

        activity = getActivity();

        inputImage = (CircleImageView) view.findViewById(R.id.register_image);

        inputEmail = (AppCompatEditText) view.findViewById(R.id.register_email);
        inputPassword = (AppCompatEditText) view.findViewById(R.id.register_password);
        inputName = (AppCompatEditText) view.findViewById(R.id.register_name);

        inputImage.setOnClickListener(view1 -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            RegisterFragment.this.startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
        });

        register = (AppCompatButton) view.findViewById(R.id.register_submit);

        colorizeButton(register, ORANGE);

        register.setOnClickListener(view2 -> {
            String email = inputEmail.getText().toString();
            String password = inputPassword.getText().toString();
            String name = inputName.getText().toString();

            String Base64Img = encodeBitmap(image);
            String Base64SmallImg = encodeBitmap(smallImage);

            new RegisterTask().execute(email, name, password, Base64Img, Base64SmallImg);
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;

        switch (requestCode) {
            case GALLERY_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(activity.getContentResolver(), selectedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    image = getImage(bitmap, activity, 80);
                    smallImage = getImage(bitmap, activity, 30);
                    inputImage.setImageBitmap(image);
                    register.setEnabled(true);
                }
        }
    }

    private class RegisterTask extends AsyncTask<String, Void, ResponseEntity<User>> {
        private ProgressDialog progress;
        private String password;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            progress = ProgressDialog.show(activity, getString(R.string.progress_title),
                    getString(R.string.register_progress_body), true);
        }


        @Override
        protected ResponseEntity<User> doInBackground(String... strings) {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/json");

            password = strings[2];

            // email, name, password, image, smallImage
            Register registerUser = new Register(strings[0], strings[1], password,
                    strings[3], strings[4]);

            ResponseEntity<User> response = null;

            try {
                response = restTemplate.postForEntity(Constants.URL.REGISTER,
                        registerUser, User.class, headers);

            } catch (org.springframework.web.client.HttpClientErrorException e) {
                Snackbar.make(view, e.getMessage(), Snackbar.LENGTH_LONG).show();
            }
            return response;
        }


        @Override
        protected void onPostExecute(ResponseEntity<User> response) {
            dismissProgressDialog(progress);

            if (saveAuthData(response, activity, password)) {
                Utils.sendNewTaskIntent(activity, MainActivity.class);
            }
        }
    }

}
