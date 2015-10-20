// Group Project - Mortgage Calc
// Irving Pena, Jose Castellon, Shimul Chaudhary
// 10/05/2016

package com.murach.tipcalculator;

import java.text.NumberFormat;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.AdapterView.OnItemSelectedListener;

public class MortgageCalculatorActivity extends Activity
        implements OnEditorActionListener, OnClickListener {

    // define variables for the widgets
    private EditText editTextMortgageAmount;
    private EditText editTextDownPayment;
    private EditText editTextMonthlyPayment;
    private SeekBar sbInterestRate;
    private Spinner spinner;
    private RadioGroup rBtnGroup;
    private RadioButton rBtn_Ten;
    private RadioButton rBtn_Fifteen;
    private RadioButton rBtn_Thirty;
    private Button btnCalculate;
    private Button btnSchedule;


    // define the SharedPreferences object
    private SharedPreferences savedValues;

    // define instance variables that should be saved
    private String mAmountString = "";
    private String downPaymentString = "";
    double downPayment = 0;
    private double interestRate = .04;
    private TextView interestRateTextView;
    private double loanAmount = 0;
    private Mortgage myMortgage = new Mortgage();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tip_calculator);

        // get references to the widgets
        editTextMortgageAmount = (EditText) findViewById(R.id.editText_MortgageAmount);
        editTextDownPayment = (EditText) findViewById(R.id.editText_downPayment);
        editTextMonthlyPayment = (EditText) findViewById(R.id.mPayment);
        sbInterestRate = (SeekBar) findViewById(R.id.sbInterestRate);
        interestRateTextView = (TextView) findViewById(R.id.interestRateTextView);
        spinner = (Spinner) findViewById(R.id.bankName);
        btnCalculate = (Button) findViewById(R.id.btnCalculate);
        rBtnGroup = (RadioGroup) findViewById(R.id.rBtnGroup);
        rBtn_Ten = (RadioButton) findViewById(R.id.rBtn_Ten);
        rBtn_Fifteen = (RadioButton) findViewById(R.id.rBtn_Fifteen);
        rBtn_Thirty = (RadioButton) findViewById(R.id.rBtn_Thirty);
        btnSchedule = (Button)findViewById(R.id.btnSchedule);
        interestRateTextView.setText(sbInterestRate.getProgress() + "%");

        // set the listeners
        btnCalculate.setOnClickListener(this);

        //Populate the spinner - code stolen from the Android docs
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.arrayOfBanks, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // Pass values to the Amortization Table activity
        btnSchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MortgageCalculatorActivity.this, Table.class);
                Bundle data = new Bundle();
                data.putDouble("months", myMortgage.getTermInMonths());
                data.putDouble("loan", (myMortgage.getLoanAmount() -myMortgage.getDownPayment()));
                data.putDouble("rate", myMortgage.getMonthlyInterestRate()*12);
                intent.putExtras(data);
                startActivity(intent);
            }
        });

        // get SharedPreferences object
        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

        // OnSeekBarChangeListener - gets the current value from the seekbar and updates the interest rate
        // minimum value for interest rate is 1%

        sbInterestRate.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            int progress = 0;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                    progress = progressValue+1;
                    interestRate = progress / 100.0;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                interestRateTextView.setText(progress + "%");
            }
        });
    }

    @Override
    public void onPause() {
        // save the instance variables       
        Editor editor = savedValues.edit();
        editor.commit();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void calculateAndDisplay() {

        // Sets mortgage amount, with error checking for blank spot
        mAmountString = editTextMortgageAmount.getText().toString();
        if (mAmountString.equals("")) {
            loanAmount = 0;
        } else {
            loanAmount = Float.parseFloat(mAmountString);
        }

    }

    // Listener for TextView will run calculate when info is typed in
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
                actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {
            calculateAndDisplay();
        }
        return false;
    }

    // Executes when "Calculate" button is pressed.
    @Override
    public void onClick(View v) {
        // If ensures all needed variables have been entered before calculations are done
        if (isReadyToCalculate() == true) {
            //sets variables from the editBoxes, seekBar, radio buttons
            NumberFormat currency = NumberFormat.getCurrencyInstance();
            mAmountString = editTextMortgageAmount.getText().toString();
            downPaymentString = editTextDownPayment.getText().toString();
            int loanTermInMonths = getLoanTermInYears() * 12;
            //interestRate = (sbInterestRate.getProgress())/100;
       //     interestRate = Double.parseDouble(interestRateTextView.getText().toString()) / 100;

            myMortgage.setAnnualInterestRate(interestRate);
            downPayment = Double.parseDouble(downPaymentString);
            loanAmount = Double.parseDouble(mAmountString);

            // creates a mortgage obejct
            myMortgage.setLoanAmount(loanAmount);
            myMortgage.setMonthlyInterestRate(interestRate / 12);
            myMortgage.setTermInMonths(loanTermInMonths);
            myMortgage.setDownPayment(downPayment);

            //Does the calculation and prints it
            double monthlyPayment = myMortgage.JoseMethod();
            String stringMonthlyPayment = Double.toString(monthlyPayment);
            editTextMonthlyPayment.setText(currency.format(monthlyPayment));
        }
        else
            editTextMonthlyPayment.setText("");
    }

    // Returns loan term based on radio button pressed
    public int getLoanTermInYears() {
        switch (rBtnGroup.getCheckedRadioButtonId()) {
            case R.id.rBtn_Ten:
                return 10;
            case R.id.rBtn_Fifteen:
                return 15;
            case R.id.rBtn_Thirty:
                return 30;
            default:
                return 0;
        }
    }

    // returns true if any condition exists that would
    //  not allow mortgage to be calculated
    public boolean isReadyToCalculate() {
        if (editTextMortgageAmount.getText().toString().equals("")
                || editTextDownPayment.getText().toString().equals(""))
            return false;
        else if (Double.parseDouble(editTextMortgageAmount.getText().toString())
                < Double.parseDouble(editTextDownPayment.getText().toString()))
            return false;
        else if (getLoanTermInYears() == 0)
            return false;
        else
            return true;
    }
}