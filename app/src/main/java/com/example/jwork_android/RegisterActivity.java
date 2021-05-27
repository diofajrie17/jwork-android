package com.example.jwork_android;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegisterName, etRegisterEmail, etRegisterPassword;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTransparentStatusBarOnly(RegisterActivity.this);
        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException ignored){};
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterName  = findViewById(R.id.etRegisterName);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String regexEmail = "^([\\w\\&\\_~]+\\.{0,1})+@[\\w][\\w\\-](\\.[\\w\\-]+)+$";
                String regexPassword = "^(?=.\\d)(?=.[a-z])(?=.*[A-Z]).{6,}$";

                String name = etRegisterName.getText().toString();
                String email = etRegisterEmail.getText().toString();
                String password = etRegisterPassword.getText().toString();

                if(TextUtils.isEmpty(email)){
                    etRegisterEmail.setError("Email is Required.");
                    btnRegister.setEnabled(true);
                    return;
                }

                if(TextUtils.isEmpty(name)){
                    etRegisterName.setError("Name is Required.");
                    btnRegister.setEnabled(true);
                    return;
                }

                if(!Pattern.matches(regexEmail, email)){
                    etRegisterPassword.setError("Email not valid.");
                    btnRegister.setEnabled(true);
                    return;
                }

                if(!Pattern.matches(regexPassword, password)){
                    etRegisterPassword.setError("Password not valid.");
                    btnRegister.setEnabled(true);
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    etRegisterPassword.setError("Password is Required.");
                    btnRegister.setEnabled(true);
                    return;
                }

                if(password.length() < 6){
                    etRegisterPassword.setError("Password must be > 6 characters");
                    btnRegister.setEnabled(true);
                    return;
                }

                else{
                    btnRegister.setEnabled(false);
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if(jsonObject != null){
                                Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(name, email, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);

            }
        });
    }

    public void setTransparentStatusBarOnly(Activity activity) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        activity.getWindow().setStatusBarColor(Color.TRANSPARENT);
        // this lines ensure only the status-bar to become transparent without affecting the nav-bar
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }
}