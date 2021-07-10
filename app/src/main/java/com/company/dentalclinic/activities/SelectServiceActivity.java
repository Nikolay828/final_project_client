package com.company.dentalclinic.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.company.dentalclinic.R;
import com.company.dentalclinic.adapters.ServicesAdapter;
import com.company.dentalclinic.utils.Service;

import java.util.ArrayList;
import java.util.List;

public class SelectServiceActivity extends AppCompatActivity {
    RecyclerView recyclerViewServices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_service);
        recyclerViewServices = findViewById(R.id.recyclerViewServices);

        List<Service> services = new ArrayList<>();
        services.add(new Service("чистка зубов",300,1));
        services.add(new Service("удаление зуба",100,2));
        services.add(new Service("лечение кариеса",1000,3));
        ServicesAdapter adapter = new ServicesAdapter(this,services);
        recyclerViewServices.setAdapter(adapter);
        recyclerViewServices.setLayoutManager(new LinearLayoutManager(this));
    }
}