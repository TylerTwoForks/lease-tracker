package com.example.te_leasetracker_c868;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.te_leasetracker_c868.Utility.DateUtil;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_ANNUAL_MILEAGE;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_ID;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_LEASE_START;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_MAKE;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_MODEL;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_NAME;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_START_MILEAGE;

public class CarDetailActivity extends AppCompatActivity {
    private static final String TAG = CarDetailActivity.class.getSimpleName();

    private TextView tv_currentMileageAllowed;
    private TextView tv_variance;
    private EditText et_actualMileage;
    private CardView cv_leaseHealth;

    private LocalDate ldCarLeaseStart;
    private double carAnnualMileage, carStartMileage;

    @SuppressLint("SetTextI18n")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        //Find Views
        TextView tv_leaseStartDate = findViewById(R.id.tv_lease_start);
        TextView tv_annualMileage = findViewById(R.id.tv_annual_mileage);
        tv_currentMileageAllowed = findViewById(R.id.tv_current_mileage_allowed);
        et_actualMileage = findViewById(R.id.et_current_mileage);
        tv_variance = findViewById(R.id.tv_variance);
        Button btn_calc = findViewById(R.id.btn_calculate);
        cv_leaseHealth = findViewById(R.id.cv_lease_health);
        //tv_message = findViewById(R.id.tv_message);

        final Intent data = getIntent();
        int carId = data.getIntExtra(EXTRA_CAR_ID, -1);
        String carName = data.getStringExtra(EXTRA_CAR_NAME);
        String carLeaseStart = data.getStringExtra(EXTRA_CAR_LEASE_START);
        String carMake = data.getStringExtra(EXTRA_CAR_MAKE);
        String carModel = data.getStringExtra(EXTRA_CAR_MODEL);

        //Below variables used for calculations.
        carStartMileage = data.getIntExtra(EXTRA_CAR_START_MILEAGE, -1);
        carAnnualMileage = data.getIntExtra(EXTRA_CAR_ANNUAL_MILEAGE, -1);
        ldCarLeaseStart = DateUtil.stringToDateConverter(carLeaseStart);
        
        //Filling out fields with pre-given data
        tv_leaseStartDate.setText(carLeaseStart);
        tv_annualMileage.setText(String.valueOf(Math.round(carAnnualMileage)));

        double daysIntoLease = daysIntoLease(ldCarLeaseStart);
        double allowedDaily = allowedDailyMileage(carAnnualMileage);
        double currentAllowedMileage = currentAllowedMileage(carStartMileage, allowedDaily, daysIntoLease);
        tv_currentMileageAllowed.setText(Long.toString(Math.round(currentAllowedMileage)));

//        //Using variables to store calculated data
//        double daysIntoLease = daysIntoLease(ldCarLeaseStart);
//        double allowedDailyMileage = allowedDailyMileage(carAnnualMileage);
//        double currentAllowedMileage = currentAllowedMileage(carStartMileage, allowedDailyMileage, daysIntoLease);
//        double variance = variance(currentAllowedMileage, actualMileage);
//
//        tv_leaseStartDate.setText(carLeaseStart);
//        tv_annualMileage.setText((int) carAnnualMileage);
//        tv_currentMileageAllowed.setText(Long.toString(currentAllowedMileage));
        

        btn_calc.setOnClickListener(calcBtnListener);



        //Setting views


        setTitle(carName +": "+carMake+" "+carModel);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private double daysIntoLease(LocalDate leaseStartDate){
        LocalDate today = LocalDate.now();
        Log.i(TAG, "daysIntoLease(LocalDate leaseStartDate): "+ChronoUnit.DAYS.between(leaseStartDate, today));
        return ChronoUnit.DAYS.between(leaseStartDate, today);
    }

    private double allowedDailyMileage(double annualMileage){
        Log.i(TAG, "allowedDailyMileage(double annualMileage): "+Math.round(annualMileage/366));
        return annualMileage/365;
    }

    private double currentAllowedMileage(double startingMileage, double allowedDailyMileage, double daysIntoLease){
        Log.i(TAG, "currentAllowedMileage(int startingMileage, double allowedDailyMileage, double daysIntoLease)1: "+startingMileage);
        Log.i(TAG, "currentAllowedMileage(int startingMileage, double allowedDailyMileage, double daysIntoLease)2: "+allowedDailyMileage);
        Log.i(TAG, "currentAllowedMileage(int startingMileage, double allowedDailyMileage, double daysIntoLease)3: "+daysIntoLease);
        return startingMileage+(allowedDailyMileage*daysIntoLease);
    }

    private double variance(double currentAllowedMileage, double actualCurrentMileage){
        Log.i(TAG, "variance(double currentAllowedMileage, double actualCurrentMileage)1: "+currentAllowedMileage);
        Log.i(TAG, "variance(double currentAllowedMileage, double actualCurrentMileage)2: "+actualCurrentMileage);
        return currentAllowedMileage-actualCurrentMileage;
    }

    private View.OnClickListener calcBtnListener = new View.OnClickListener(){
        @SuppressLint("SetTextI18n")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onClick(View v) {

            String etActMil = et_actualMileage.getText().toString();

            //If you hit calc button without any mileage entered, a toast will pop asking you to enter mileage.
            if(etActMil.isEmpty()){
                Toast.makeText(CarDetailActivity.this, "Please enter your mileage!", Toast.LENGTH_SHORT).show();
            }else{
                //If you did enter mileage, this does the work.
                //InputMethodManager portion minimizes keyboard when "calc" button is clicked if there were numbers in the field.
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                if (inputManager != null) {
                    inputManager.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }

                Long actualMileage = Long.parseLong(et_actualMileage.getText().toString());

                //These do the calculations.  I left log tags below each one if testing is needed in the future.
                //variance requires currentAllowedMileage which requires allowedDailyMileage and daysIntoLease.
                double daysIntoLease = daysIntoLease(ldCarLeaseStart);
                //Log.i(TAG, "daysIntoLease: "+daysIntoLease(ldCarLeaseStart));
                double allowedDailyMileage = allowedDailyMileage(carAnnualMileage);
                //Log.i(TAG, "allowedDailyMileage: "+allowedDailyMileage(carAnnualMileage));
                double currentAllowedMileage = currentAllowedMileage(carStartMileage, allowedDailyMileage, daysIntoLease);
                //Log.i(TAG, "currentAllowedMileage: "+currentAllowedMileage(carStartMileage, allowedDailyMileage, daysIntoLease));
                double variance = variance(currentAllowedMileage, actualMileage);
                //Log.i(TAG, "variance: "+variance(currentAllowedMileage, actualMileage));

                //Here I check to see what the variance is and set background color of the cardview appropriately.
                //Green for over 200 miles under, yellow for between 0 and 200, red for under 0.
                if(variance>100){
                    cv_leaseHealth.setCardBackgroundColor(Color.parseColor("#1faa00"));
                }else if(variance>0&&variance<200){
                    cv_leaseHealth.setCardBackgroundColor(Color.parseColor("#fca103"));
                }else{
                    cv_leaseHealth.setCardBackgroundColor(Color.parseColor("#ff0000"));
                }

                tv_currentMileageAllowed.setText(Long.toString(Math.round(currentAllowedMileage)));
                tv_variance.setText(Long.toString(Math.round(variance)));
            }

        }
    };


}
