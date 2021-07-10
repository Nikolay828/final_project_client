package com.company.dentalclinic.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.dentalclinic.utils.Doctor;
import com.company.dentalclinic.R;

import java.util.List;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>{
    private List<Doctor> onboardingItems;

    public OnboardingAdapter(List<Doctor> onboardingItems) {
        this.onboardingItems = onboardingItems;
    }

    @NonNull
    @org.jetbrains.annotations.NotNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull @org.jetbrains.annotations.NotNull ViewGroup parent, int viewType) {
        return new OnboardingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_container_onboarding,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull @org.jetbrains.annotations.NotNull OnboardingAdapter.OnboardingViewHolder holder, int position) {
        holder.setOnboardingData(onboardingItems.get(position));
    }

    @Override
    public int getItemCount() {
        return onboardingItems.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder {
        private TextView textFullName;
        private TextView textExperience;
        private TextView textDescription;
        private ImageView imageOnboarding;

        public OnboardingViewHolder(@NonNull View itemView) {
            super(itemView);
            textFullName = itemView.findViewById(R.id.textFullName);
            textExperience = itemView.findViewById(R.id.textExperience);
            textDescription = itemView.findViewById(R.id.textDescription);
            imageOnboarding = itemView.findViewById(R.id.imageOnboarding);
        }

        void setOnboardingData(Doctor onboardingItem) {
            textFullName.setText(onboardingItem.getFullNаme());
            textExperience.setText(onboardingItem.getExperience());
            imageOnboarding.setImageResource(onboardingItem.getImage());
            textDescription.setText(onboardingItem.getDescription());
        }

    }

    public List<Doctor> getOnboardingItems(){
        return onboardingItems;
    }

}
