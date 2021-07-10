package com.company.dentalclinic.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.company.dentalclinic.R;
import com.company.dentalclinic.utils.Service;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ServicesAdapter extends RecyclerView.Adapter<ServicesAdapter.ServicesViewHolder> {

    List<Service> services;
    Context context;

    public ServicesAdapter(Context context, List<Service> services) {
        this.services = services;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public ServicesViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(
                R.layout.services_container,
                parent,
                false
        );
        return new ServicesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ServicesAdapter.ServicesViewHolder holder, int position) {
        holder.setData(services.get(position));
        holder.itemService.setOnClickListener((view) -> {
            Intent intent = new Intent();
            intent.putExtra("selectedServices", services.get(position));
            ((Activity) context).setResult(Activity.RESULT_OK, intent);
            ((Activity) context).finish();
        });
    }

    @Override
    public int getItemCount() {
        return services.size();
    }

    public class ServicesViewHolder extends RecyclerView.ViewHolder {
        private TextView textServiceName, textDuration, textPrice;
        private ConstraintLayout itemService;

        public ServicesViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            textServiceName = itemView.findViewById(R.id.textServiceName);
            textDuration = itemView.findViewById(R.id.textDuration);
            textPrice = itemView.findViewById(R.id.textPrice);
            itemService = itemView.findViewById(R.id.itemService);
        }


        @SuppressLint("SetTextI18n")
        void setData(Service service) {
            textServiceName.setText(service.getName());
            textDuration.setText("Длительность:" + service.getDuration() + "ч.");
            textPrice.setText(service.getPrice() + "грн");
        }

    }
}
