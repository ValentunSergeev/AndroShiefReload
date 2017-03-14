package com.valentun.androshief.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.valentun.androshief.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.valentun.androshief.Constants.ORANGE;
import static com.valentun.androshief.Support.colorizeButton;

/**
 * Created by Valentun on 14.03.2017.
 */

public class RegisterFragment extends Fragment {
    private AppCompatEditText inputEmail, inputPassword, inputName;
    private CircleImageView inputImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);


        inputImage = (CircleImageView) view.findViewById(R.id.register_image);

        inputEmail = (AppCompatEditText) view.findViewById(R.id.register_email);
        inputPassword = (AppCompatEditText) view.findViewById(R.id.register_password);
        inputName = (AppCompatEditText) view.findViewById(R.id.register_name);

        AppCompatButton register = (AppCompatButton) view.findViewById(R.id.register_submit);

        colorizeButton(register, ORANGE);

        return view;
    }

}
