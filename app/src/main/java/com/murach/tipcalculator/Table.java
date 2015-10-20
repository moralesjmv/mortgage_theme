package com.murach.tipcalculator;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;

public class Table extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);

        TableLayout table = (TableLayout)findViewById(R.id.mortagetable);

// get data from main activity
        int months;
        double loan, interest_rate, payment, interest, principal;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        months = (int)bundle.getDouble("months");
        interest_rate = bundle.getDouble("rate");
        loan = bundle.getDouble("loan");

// calculate payment
        payment = ((interest_rate/12.0) * loan * Math.pow((1+interest_rate/12.0),months)) / ((Math.pow((1+interest_rate/12.0),months)) - 1);

//format currency
        DecimalFormat currency = new DecimalFormat("$###,###.00");
//currency.getCurrency();

//fill up table with mortgage data
        for (int x = 0; x < months; x++) {
            TextView tv_month = new TextView(this);
            TextView tv_payment = new TextView(this);
            TextView tv_interest = new TextView(this);
            TextView tv_principal = new TextView(this);
            TextView tv_remaining = new TextView(this);
            tv_month.setGravity(Gravity.CENTER);
            tv_payment.setGravity(Gravity.CENTER);
            tv_interest.setGravity(Gravity.CENTER);
            tv_principal.setGravity(Gravity.CENTER);
            tv_remaining.setGravity(Gravity.CENTER);
            TableRow row = new TableRow(this);

            interest = loan*(interest_rate/12.0);
            principal = payment - interest;
            loan -= principal;

            tv_month.setText(Integer.toString(x+1));
            tv_payment.setText(currency.format(payment));
            tv_interest.setText(currency.format(interest));
            tv_principal.setText(currency.format(principal));
            tv_remaining.setText(currency.format(loan));

            row.addView(tv_month);
            row.addView(tv_payment);
            row.addView(tv_principal);
            row.addView(tv_interest);
            row.addView(tv_remaining);

            row.setGravity(Gravity.CENTER);

            table.addView(row);
        }

    }

}
