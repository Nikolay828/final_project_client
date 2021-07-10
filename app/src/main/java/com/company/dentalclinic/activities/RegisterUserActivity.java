package com.company.dentalclinic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.company.dentalclinic.R;
import com.company.dentalclinic.utils.RequestCode;
import com.company.dentalclinic.utils.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText etFirstName;
    private EditText etSecondName;
    private EditText etEmail;
    private EditText etEnterPassword;
    private EditText etConfirmPassword;

    private Button btnSingOn;

    private TextView tvSignIn;

    private Socket socket;
    private PrintWriter printWriter;
    private Scanner scanner;

    private Gson gson;

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        initView();
        connect();
        setListeners();

        gson = new Gson();
    }

    private void initView() {
        etFirstName = findViewById(R.id.etFirstName);
        etSecondName = findViewById(R.id.etSecondName);
        etEmail = findViewById(R.id.etEmail);
        etEnterPassword = findViewById(R.id.etEnterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        btnSingOn = findViewById(R.id.btnSignOn);

        tvSignIn = findViewById(R.id.tvSignIn);
    }

    private void connect() {
        new Thread(() -> {
            try {
                socket = new Socket("192.168.1.111",7999);
                printWriter = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
              //  runOnUiThread(() -> Toast.makeText(this, "Ты подключился", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();

            }
        }).start();
    }

    private void setListeners() {
        btnSingOn.setOnClickListener(view -> {
            new Thread(() -> {
                String firstName = etFirstName.getText().toString().trim();
                String secondName = etSecondName.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etEnterPassword.getText().toString().trim();
                String confirmPassword = etConfirmPassword.getText().toString().trim();
                if(password.equals(confirmPassword)){
                    User user = new User(firstName,secondName,email,password);
                    printWriter.println(RequestCode.KEY_SIGN_ON);
                    String jsonUser = gson.toJson(user);
                    printWriter.println(jsonUser);
                    printWriter.flush();
                    runOnUiThread(() -> {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        SharedPreferences.Editor editor = getSharedPreferences("isSignIn",MODE_PRIVATE).edit();
                        editor.putString("user",gson.toJson(user));
                        editor.apply();
                        startActivity(intent);
                        finish();
                    });
                }else {
                    runOnUiThread(() -> {
                        Toast.makeText(this, "Пароль должен совпадать", Toast.LENGTH_SHORT).show();
                    });
                }
            }).start();

        });
        tvSignIn.setOnClickListener(view1 -> {
            Intent intent = new Intent(getApplicationContext(),SignInActivity.class);
            startActivity(intent);
            finish();
        });
    }

}