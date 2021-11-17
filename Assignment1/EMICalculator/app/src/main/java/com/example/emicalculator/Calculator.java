package com.example.emicalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Calculator extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        DecimalFormat money_format = new DecimalFormat("#,###.00");

        // Calculate:
        // Get data
        Bundle bundled = getIntent().getExtras();

        // Loan Value
        double pv = bundled.getDouble("loan_value");
        double ir = bundled.getDouble("interest_rate");
        int terms = bundled.getInt("payment_terms");

        TextView tenure = findViewById(R.id.months_text);
        TextView premium = findViewById(R.id.amount_text_view);
        TextView interest = findViewById(R.id.interest_text);
        TextView loan = findViewById(R.id.loan_text);

        // Give the user some information:
        loan.setText("Loan: $"+money_format.format(pv)+ " CAD");
        interest.setText("Interest Rate: "+String.format("%.2f", ir) + "%");

        // Convert percent
        ir = ir/100;
        // convert to monthly interest rate
        ir = ir/12;

        // Convert years to months
        terms = terms*12;
        tenure.setText("Number of Months: "+Integer.toString(terms));

        // Calcualte the EMI using the PV formula
        double exp = Math.pow((1+ir), terms);;

        double EMI = pv * ( (ir*exp) / (exp-1) );

        // Tell the user there premium
        premium.setText("$ "+ money_format.format(EMI) + " CAD");


        // Home Button
        Button recalc = findViewById(R.id.recalc_btn);
        Intent main_activity = new Intent(this, MainActivity.class);

        // Return to the main activity
        recalc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(main_activity);
            }
        });

    }
}