package com.company.dentalclinic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.company.dentalclinic.utils.Doctor;
import com.company.dentalclinic.R;
import com.company.dentalclinic.adapters.OnboardingAdapter;
import com.company.dentalclinic.utils.RequestCode;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.LongFunction;

public class SelectDoctorActivity extends AppCompatActivity {

    private OnboardingAdapter onboardingAdapter;
    private LinearLayout layoutOnboardingIndicators;

    private Socket socket;
    private PrintWriter printWriter;
    private Scanner scanner;

    private Gson gson;

    private List<Doctor> doctors;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);

        connect();



        layoutOnboardingIndicators = findViewById(R.id.layoutOnboardingIndicators);

        setupOnboardingItems();


        gson = new Gson();

        ViewPager2 onboardingViewPager = findViewById(R.id.layoutOnboardingViewPager);
        onboardingViewPager.setAdapter(onboardingAdapter);
        ArrayList<Doctor> onboardingItems = (ArrayList<Doctor>) onboardingAdapter.getOnboardingItems();
        findViewById(R.id.buttonOnboardingAction).setOnClickListener(view -> {
            Doctor selectedDoctors = onboardingItems.get(onboardingViewPager.getCurrentItem());
            Toast.makeText(this,selectedDoctors.getFullNаme() , Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("result",selectedDoctors);
            setResult(RESULT_OK,intent);
            finish();
        });


        setupOnboardingIndicators();
        setCurrentOnboardingIndicator(0);
        onboardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });
    }

    private void connect() {
        new Thread(() -> {
            try {
                socket = new Socket("192.168.1.111", 7999);
                printWriter = new PrintWriter(socket.getOutputStream());

                Log.d("test",printWriter.toString());
                scanner = new Scanner(socket.getInputStream());
//                printWriter.println(RequestCode.KEY_GET_DOCTOR);
//                printWriter.flush();
//                String jsonDoctors = scanner.nextLine();
//                Log.d("test",jsonDoctors);
//                doctors = gson.fromJson(jsonDoctors,ArrayList.class);
//                doctors.get(0).setImage(R.drawable.first_dentist);
//                doctors.get(1).setImage(R.drawable.second_dentist);
//                doctors.get(2).setImage(R.drawable.third_dentist);
                runOnUiThread(() -> Toast.makeText(this, "jdsj", Toast.LENGTH_SHORT).show());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void setupOnboardingIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for (int i  = 0; i < indicators.length; i++){
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnboardingIndicators.addView(indicators[i]);
        }
    }

    private void getDoctorFromDb(){
        new Thread(() -> {

        }).start();
    }

    private void setupOnboardingItems() {
        List<Doctor> onboardingItems = new ArrayList<>();
        Doctor firstDoctor = new Doctor();
        firstDoctor.setFullNаme("Ivan Ivanov");
        firstDoctor.setExperience("5 year experience");
        firstDoctor.setDescription("Lorem ipsum dolor sit amet consectetur adipiscing elit, suspendisse ex nibh augue euismod quis, at leo venenatis ultrices dui proin. Efficitur vulputate ut finibus justo pharetra inceptos nibh hac placerat.");
        firstDoctor.setImage(R.drawable.first_dentist);

        Doctor secondDoctor = new Doctor();
        secondDoctor.setFullNаme("Petr Petrov");
        secondDoctor.setExperience("10 year experience");
        secondDoctor.setDescription("Lorem ipsum dolor sit amet consectetur, adipiscing elit nascetur sodales donec, tortor viverra maximus curae. Nisi leo pretium hendrerit turpis ornare tempus tortor id cursus, justo nec posuere eu platea porta habitant.");
        secondDoctor.setImage(R.drawable.second_dentist);

        Doctor thirdDoctor = new Doctor();
        thirdDoctor.setFullNаme("Elena Petrova");
        thirdDoctor.setExperience("1 year experience");
        thirdDoctor.setDescription("Lorem ipsum dolor sit amet consectetur adipiscing elit risus, nibh dis metus in inceptos elementum vitae gravida et, dictumst finibus curae semper ridiculus potenti malesuada. Ultricies ex ligula felis condimentum fames quam pretium nulla.");
        thirdDoctor.setImage(R.drawable.third_dentist);

        onboardingItems.add(firstDoctor);
        onboardingItems.add(secondDoctor);
        onboardingItems.add(thirdDoctor);



        onboardingAdapter = new OnboardingAdapter(onboardingItems);

    }

    private void setCurrentOnboardingIndicator(int index){
        int childCount = layoutOnboardingIndicators.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutOnboardingIndicators.getChildAt(i);
            if (i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_inactive)
                );
            }
        }
    }

}