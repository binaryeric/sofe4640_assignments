package com.example.emicalculator;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.emicalculator.databinding.FragmentSecondBinding;
import com.example.emicalculator.databinding.FragmentThirdBinding;


public class ThirdFragment extends Fragment {

    // view
    private FragmentThirdBinding binding;

    // TODO: Rename and change types of parameters
    private int payment_terms_param;

    public ThirdFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // Load the arguments
        super.onCreate(savedInstanceState);

        // Get the arguments from the other fragment
        if (getArguments() != null) {
            payment_terms_param = getArguments().getInt("payment_terms");
        } else {
            payment_terms_param = 12;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentThirdBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent calculator = new Intent(getActivity(), Calculator.class);

                double loan = Double.parseDouble(binding.loanValue.getText().toString());
                double interest = Double.parseDouble(binding.interestRate.getText().toString());

                // Create bundle to pass data
                Bundle bundle = new Bundle();
                bundle.putInt("payment_terms", payment_terms_param);
                bundle.putDouble("loan_value",  loan);
                bundle.putDouble("interest_rate", interest);

                calculator.putExtras(bundle);
                startActivity(calculator);
            }
        });
    }
}