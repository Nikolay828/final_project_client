package com.company.dentalclinic.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.company.dentalclinic.DatePickerFragment;
import com.company.dentalclinic.utils.Doctor;
import com.company.dentalclinic.R;
import com.company.dentalclinic.utils.Service;
import com.company.dentalclinic.utils.RequestCode;
import com.company.dentalclinic.utils.User;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private ImageView ivSelectDoctors,ivSelectService;

    private Intent intent;

    private TextView tvDoctorData,tvServiceName;

    private Doctor selectedDoctor;

    private Service selectedService;

    private TextView tvSelectDate;

    private TextView tvPriceOfService;

    private TextView tvSignOutOfAccount;

    private Socket socket;
    private PrintWriter printWriter;
    private Scanner scanner;

    private Button btnConfirm;

    private User user;

    private Gson gson;

    private String date;

    private String initialValueService,initialValueDoctor,initialValueDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connect();
        ivSelectDoctors = findViewById(R.id.ivSelectDoctor);
        ivSelectService = findViewById(R.id.ivSelectService);
        tvDoctorData = findViewById(R.id.tvDoctorsData);
        tvServiceName = findViewById(R.id.tvServiceName);
        tvSelectDate = findViewById(R.id.tvSelectDate);
        tvSignOutOfAccount = findViewById(R.id.tvSignOutOfAccount);
        tvPriceOfService = findViewById(R.id.tvPriceOfService);
        btnConfirm = findViewById(R.id.btnConfirm);

        initialValueService = tvServiceName.getText().toString();
        initialValueDoctor = tvDoctorData.getText().toString();
        initialValueDate = tvSelectDate.getText().toString();



        Intent getIntent = getIntent();

        gson = new Gson();

      //  user = (User) getIntent.getSerializableExtra("user");
        SharedPreferences sharedPreferences = getSharedPreferences("isSignIn",MODE_PRIVATE);
        user = gson.fromJson(sharedPreferences.getString("user",""),User.class);

        ivSelectService.setOnClickListener((view) -> {
            intent = new Intent(this, SelectServiceActivity.class);
            startActivityForResult(intent,2);
        });

        ivSelectDoctors.setOnClickListener((view) -> {
            intent = new Intent(this, SelectDoctorActivity.class);
            startActivityForResult(intent, 1);
        });

        tvSelectDate.setOnClickListener((view) -> {
            DialogFragment datePicker = new DatePickerFragment();
            datePicker.show(getSupportFragmentManager(),"date picker");
        });

        tvSignOutOfAccount.setOnClickListener((view) -> {
            SharedPreferences.Editor editor = getSharedPreferences("isSignIn",MODE_PRIVATE).edit();
            editor.putBoolean("isSignIn",false);
            editor.putString("user",null);
            editor.apply();
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        });

        btnConfirm.setOnClickListener((view) -> {
            saveOrder();
        });

    }

    private void saveOrder() {
        Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
        new Thread(() -> {
            if (
                    !tvServiceName.getText().toString().equals(initialValueService) &&
                    !tvDoctorData.getText().toString().equals(initialValueDoctor) &&
                    !tvSelectDate.getText().toString().equals(initialValueDate)
            ) {
                printWriter.println(RequestCode.KEY_SAVE_ORDER);
                printWriter.flush();
                String jsonUser = gson.toJson(user);
                String jsonDoctor = gson.toJson(selectedDoctor);
                String jsonService = gson.toJson(selectedService);
                printWriter.println(jsonUser);
                printWriter.flush();
                printWriter.println(jsonDoctor);
                printWriter.flush();
                printWriter.println(jsonService);
                printWriter.flush();
                printWriter.println(date);
                printWriter.flush();
            } else {
                runOnUiThread(() -> Toast.makeText(this, "Выберите услугу и врача и время", Toast.LENGTH_SHORT).show());
            }
        }).start();

    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK) {
                     selectedDoctor = (Doctor) data.getSerializableExtra("result");
                     tvDoctorData.setText(selectedDoctor.getFullNаme());
                     ivSelectDoctors.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_remove));
                }
                break;
            case 2:
                if(resultCode == RESULT_OK) {
                    selectedService = (Service) data.getSerializableExtra("selectedServices");
                    tvServiceName.setText(selectedService.getName());
                    tvPriceOfService.setText("Цена:" + selectedService.getPrice() + "грн");
                    ivSelectService.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_remove));
                }
        }
    }

    private void connect() {
        new Thread(() -> {
            try {
                socket = new Socket("192.168.1.111", 7999);
                printWriter = new PrintWriter(socket.getOutputStream());
                scanner = new Scanner(socket.getInputStream());
                //runOnUiThread(() -> Toast.makeText(this, "Ты подключился", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH,i2);
        date = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        tvSelectDate.setText(date);
    }
}