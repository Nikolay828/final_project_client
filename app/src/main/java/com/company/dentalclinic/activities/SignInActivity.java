package com.company.dentalclinic.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
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

public class SignInActivity extends AppCompatActivity {

    private EditText etGmail;
    private EditText etPassword;

    private Button btnSignIn;

    private TextView tvSignOn;

    private Socket socket;
    private PrintWriter printWriter;
    private Scanner scanner;

    private Gson gson;

    private Intent intent;

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if(userAlreadySignIn()){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
        initView();
        connect();
        setListeners();

        gson = new Gson();
    }

    private void initView() {
        etGmail = findViewById(R.id.etGmail);
        etPassword = findViewById(R.id.etPassword);

        btnSignIn = findViewById(R.id.btnSignIn);

        tvSignOn = findViewById(R.id.tvSingOn);
    }

    private void connect() {
        new Thread(() -> {
            try {
                socket = new Socket("192.168.1.111", 7999);
                printWriter = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
//                runOnUiThread(() -> Toast.makeText(this, "Ты подключился", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setListeners() {
        btnSignIn.setOnClickListener(view -> new Thread(this::signIn).start());

        tvSignOn.setOnClickListener(this::startActivityRegisterUser);
    }

    private void signIn() {
        String email = etGmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        user = new User(email, password);
        printWriter.println(RequestCode.KEY_SIGN_IN);
        String jsonUser = gson.toJson(user);
        printWriter.println(jsonUser);
        printWriter.flush();
        checkServerResponse();
    }


    private void checkServerResponse(){
        boolean flag = scanner.nextBoolean();
        if (flag) {
            runOnUiThread(() -> {
                SharedPreferences.Editor editor = getSharedPreferences("isSignIn",MODE_PRIVATE).edit();
                editor.putBoolean("isSignIn",true);
                editor.putString("user",gson.toJson(user));
                editor.apply();

                intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                finish();
            });
        } else {
            runOnUiThread(() -> Toast.makeText(this, "Что-то пошло не так...", Toast.LENGTH_SHORT).show());
        }
    }

    private void startActivityRegisterUser(View view) {
        intent = new Intent(getApplicationContext(), RegisterUserActivity.class);
        startActivity(intent);
        finish();
    }

    private boolean userAlreadySignIn(){
        SharedPreferences sharedPreferences = getSharedPreferences("isSignIn",MODE_PRIVATE);
        return sharedPreferences.getBoolean("isSignIn",false);
    }

}