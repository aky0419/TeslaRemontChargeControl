package com.example.teslaremontchargecontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.teslaremontchargecontrol.interfaces.TeslaApi;
import com.example.teslaremontchargecontrol.models.TokenRequest;
import com.example.teslaremontchargecontrol.models.TokenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private EditText emailEt, passwordEt;
    private Button loginButton;
    private SharedPreferences shp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEt = findViewById(R.id.emailET);
        passwordEt = findViewById(R.id.passwordET);
        loginButton = findViewById(R.id.loginButton);

        shp = getSharedPreferences("accessToken", MODE_PRIVATE);
        if (shp.getString("token", null) != null && shp.getInt("expireTime", 0) != 0) {

            Log.d(TAG, "onCreate: " + shp.getInt("expireTime", 0));
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }

        loginButton.setOnClickListener(v -> {

            TeslaApi service = RetrofitClient.getClient(getString(R.string.baseUrl)).create(TeslaApi.class);

            final TokenRequest tokenRequest = new TokenRequest();
            tokenRequest.setEmail(emailEt.getText().toString());
            tokenRequest.setPassword(passwordEt.getText().toString());
            tokenRequest.setClient_id("81527cff06843c8634fdc09e8ac0abefb46ac849f38fe1e431c2ef2106796384");
            tokenRequest.setClient_secret("c7257eb71a564034f9419ee651c7d0e5f7aa6bfbd18bafb5c5c033b093bb2fa3");
            tokenRequest.setGrant_type("password");

            Call<TokenResponse> response = service.getTokenAccess(tokenRequest);
            response.enqueue(new Callback<TokenResponse>() {
                @Override
                public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                    int statusCode = response.code();
                    TokenResponse tokenResponse = response.body();
                    Log.d(TAG, "onResponse: " + statusCode + " " + tokenResponse);

                    shp = getSharedPreferences("accessToken", MODE_PRIVATE);
                    shp.edit().putString("token", tokenResponse.getAccessToken()).putInt("expireTime", tokenResponse.getExpiresIn()).apply();


                    if (shp.getString("token",null) != null && shp.getInt("expireTime", 0) != 0) {
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }

                }

                @Override
                public void onFailure(Call<TokenResponse> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());

                }
            });
        });

        checkFieldEmpty();

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkFieldEmpty();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        emailEt.addTextChangedListener(watcher);
        passwordEt.addTextChangedListener(watcher);





    }

    private void checkFieldEmpty() {
        if (emailEt.getText().toString().equals("") || passwordEt.getText().toString().equals("")) {
            loginButton.setEnabled(false);
        } else {
            loginButton.setEnabled(true);
        }
    }
}
