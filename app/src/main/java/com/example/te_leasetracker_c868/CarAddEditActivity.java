package com.example.te_leasetracker_c868;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.te_leasetracker_c868.DB_Entities.Car;
import com.example.te_leasetracker_c868.Utility.DateUtil;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import static com.example.te_leasetracker_c868.MainActivity.EXTRA_USER_ID;

public class CarAddEditActivity extends AppCompatActivity {
    //TAG for log tracing.
    private static final String TAG = CarAddEditActivity.class.getSimpleName();

    //EXTRAS to send information on the selected car to the detail activity.
    public static final String EXTRA_CAR_ID = "com.example.te_leasetracker_c868.EXTRA_CAR_ID";
    public static final String EXTRA_CAR_NAME = "com.example.te_leasetracker_c868.EXTRA_CAR_NAME";
    public static final String EXTRA_CAR_LEASE_START = "com.example.te_leasetracker_c868.EXTRA_CAR_LEASE_START";
    public static final String EXTRA_CAR_MAKE = "com.example.te_leasetracker_c868.EXTRA_CAR_MAKE";
    public static final String EXTRA_CAR_MODEL = "com.example.te_leasetracker_c868.EXTRA_CAR_MODEL";
    public static final String EXTRA_CAR_START_MILEAGE = "com.example.te_leasetracker_c868.EXTRA_CAR_START_MILEAGE";
    public static final String EXTRA_CAR_ANNUAL_MILEAGE = "com.example.te_leasetracker_c868.EXTRA_CAR_MILEAGE_ALLOWED";

    private EditText et_name, et_make, et_model, et_startingMileage, et_annualMileage;
    private TextView tv_leaseStartDate;
    private String dateString;
    private int userId;

    private DatePickerDialog.OnDateSetListener leaseStartDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_add);

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_white);

        Intent intent = getIntent();

        userId = intent.getIntExtra(EXTRA_USER_ID, -1);

        tv_leaseStartDate = findViewById(R.id.tv_lease_start);
        et_name = findViewById(R.id.et_car_name);
        et_make = findViewById(R.id.et_car_make);
        et_model = findViewById(R.id.et_car_model);
        et_startingMileage = findViewById(R.id.et_starting_mileage);
        et_annualMileage = findViewById(R.id.et_annual_mileage);

        if(intent.hasExtra(EXTRA_CAR_ID)){
            setTitle("Edit Car");
            et_name.setText(intent.getStringExtra(EXTRA_CAR_NAME));
            et_make.setText(intent.getStringExtra(EXTRA_CAR_MAKE));
            et_model.setText(intent.getStringExtra(EXTRA_CAR_MODEL));
            tv_leaseStartDate.setText(intent.getStringExtra(EXTRA_CAR_LEASE_START));
            et_startingMileage.setText(intent.getStringExtra(EXTRA_CAR_START_MILEAGE));
            et_annualMileage.setText(intent.getStringExtra(EXTRA_CAR_ANNUAL_MILEAGE));
        }else{
            setTitle("New Car");
        }



///////////////////////Lease Start Date/////////////////////////////
        tv_leaseStartDate.setOnClickListener(v -> {
            if (tv_leaseStartDate.getText().toString().equals("")) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CarAddEditActivity.this, R.style.DatePickerDialogTheme,
                        leaseStartDateSetListener,
                        year, month, day);
                Window window = dialog.getWindow();
                window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            } else {
                String startDateString = tv_leaseStartDate.getText().toString();
                LocalDate startDate = null;
                Date startDateOld = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    startDate = DateUtil.stringToDateConverter(startDateString);
                } else {
                    startDateOld = DateUtil.stringToDateConverterOld(startDateString);
                }
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    int year = startDate.getYear();
                    int month = startDate.getMonthValue() - 1;
                    int day = startDate.getDayOfMonth();
                    DatePickerDialog dialog = new DatePickerDialog(
                            CarAddEditActivity.this, R.style.DatePickerDialogTheme,
                            leaseStartDateSetListener,
                            year, month, day);
                    Window window = dialog.getWindow();
                    window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        leaseStartDateSetListener = (view, year, month, dayOfMonth) -> {
            dateString = DateUtil.dateConverter(year, month, dayOfMonth);
            tv_leaseStartDate.setText(dateString);
            Log.i(TAG, "onDateSet Start: short: " + dateString);

        };

    }



    private void saveNewCar(){
        String carName = et_name.getText().toString();
        String carMake = et_make.getText().toString();
        String carModel = et_model.getText().toString();
        String leaseStart = tv_leaseStartDate.getText().toString();
        String startingMileage = et_startingMileage.getText().toString();
        String annualMileage = et_annualMileage.getText().toString();

        if(carName.trim().isEmpty()||carMake.trim().isEmpty()||
                carModel.trim().isEmpty()||leaseStart.trim().isEmpty()||
                startingMileage.trim().isEmpty()||annualMileage.trim().isEmpty()){
            Toast.makeText(this, "Please enter all requested information.", Toast.LENGTH_SHORT).show();
        }else{
            //Create intent. Add details to intent to send back to CarListActivity to process save.
            Intent data = new Intent();
            data.putExtra(EXTRA_CAR_NAME, carName);
            data.putExtra(EXTRA_CAR_MAKE, carMake);
            data.putExtra(EXTRA_CAR_MODEL, carModel);
            data.putExtra(EXTRA_CAR_LEASE_START, leaseStart);
            data.putExtra(EXTRA_CAR_START_MILEAGE, startingMileage);
            data.putExtra(EXTRA_CAR_ANNUAL_MILEAGE, annualMileage);
            data.putExtra(EXTRA_USER_ID, userId);

            //Check to see if there is a Car ID associated with what we are working on.
            //If there is, we know we're editing an existing car and include it to be sent back as well.
            int carId = getIntent().getIntExtra(EXTRA_CAR_ID, -1);
            if(carId!=-1){
                data.putExtra(EXTRA_CAR_ID, carId);
            }

            setResult(RESULT_OK, data);
            finish();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //using IF statement here because we only have one button in our "menu". The save button.
        //IF we add more buttons in the future, we can use a case statement.
        if (item.getItemId() == R.id.save) {
            saveNewCar();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    

}
