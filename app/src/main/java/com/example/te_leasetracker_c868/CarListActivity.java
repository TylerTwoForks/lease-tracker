package com.example.te_leasetracker_c868;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.te_leasetracker_c868.Adapters.CarAdapter;
import com.example.te_leasetracker_c868.DB_Entities.Car;
import com.example.te_leasetracker_c868.Utility.DateUtil;
import com.example.te_leasetracker_c868.Utility.ReportUtil;
import com.example.te_leasetracker_c868.View_Models.CarViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_ANNUAL_MILEAGE;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_ID;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_LEASE_START;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_MAKE;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_MODEL;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_NAME;
import static com.example.te_leasetracker_c868.CarAddEditActivity.EXTRA_CAR_START_MILEAGE;
import static com.example.te_leasetracker_c868.MainActivity.EXTRA_USER_ID;
import static com.example.te_leasetracker_c868.MainActivity.EXTRA_USER_NAME;

public class CarListActivity extends AppCompatActivity {
    //Used for log tracing
    private static final String TAG = CarListActivity.class.getSimpleName();

    //Used to track if we are editing or creating a car.
    public static final int EDIT_CAR_REQUEST = 1;
    public static final int ADD_CAR_REQUEST = 2;

    private CarViewModel carViewModel;
    //private UserViewModel userViewModel;
    //private LiveData<List<Car>> userCars;
    private List<Car> userCarsList = new ArrayList<>();


    private CarAdapter carAdapter;
    private AlertDialog alertDialog;
    private RecyclerView carRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);

        final Intent userData = getIntent();
        int userId = userData.getIntExtra(EXTRA_USER_ID, -1);
        String userName = userData.getStringExtra(EXTRA_USER_NAME);
        setTitle(userName + "'s Cars");


/////Setting up recycler view for the cars for the user.
        carRecyclerView = findViewById(R.id.rv_user_car_list);
        carRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        carRecyclerView.setHasFixedSize(true);

/////Connecting adapter to recycler view.
        carAdapter = new CarAdapter();
        carRecyclerView.setAdapter(carAdapter);

/////Gathering list of cars based on userId of the user that logged in and populating the recycler view.
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
        carViewModel.getCarsById(userId).observe(this, new Observer<List<Car>>() {
            @Override
            public void onChanged(@Nullable List<Car> carList) {
                assert carList != null;
                carAdapter.setCarList(carList);
                userCarsList.clear();
                userCarsList.addAll(carList);
                carAdapter.notifyDataSetChanged();
            }
        });

/////Click listener for each car item. This takes you to detail view where you can actually do the calc.
        carAdapter.setOnCarClickListener(new CarAdapter.onCarClickListener() {
            //@RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onCarClick(Car car) {
                String ldLeaseStart = DateUtil.dateToString(car.getLease_start());

                Intent intent = new Intent(CarListActivity.this, CarDetailActivity.class);
                intent.putExtra(EXTRA_CAR_ID, car.getCar_id());
                intent.putExtra(EXTRA_CAR_NAME, car.getCar_name());
                intent.putExtra(EXTRA_CAR_LEASE_START, ldLeaseStart);
                intent.putExtra(EXTRA_CAR_MAKE, car.getCar_make());
                intent.putExtra(EXTRA_CAR_MODEL, car.getCar_model());
                intent.putExtra(EXTRA_CAR_START_MILEAGE, car.getStarting_mileage());
                intent.putExtra(EXTRA_CAR_ANNUAL_MILEAGE, car.getMileage_allowed());
                //Log.i(TAG, "Lease start date: "+car.getLease_start());

                startActivity(intent);
            }

            /////Click listener to edit each car item. This takes you to detail view where you can actually do the calc.
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onEditClick(Car car) {
                Intent intent = new Intent(CarListActivity.this, CarAddEditActivity.class);

                LocalDate ldLeaseStart = car.getLease_start();
                String leaseStartString = DateUtil.dateToString(ldLeaseStart);

                intent.putExtra(EXTRA_CAR_ID, car.getCar_id());
                intent.putExtra(EXTRA_CAR_NAME, car.getCar_name());
                intent.putExtra(EXTRA_CAR_LEASE_START, leaseStartString);
                intent.putExtra(EXTRA_CAR_MAKE, car.getCar_make());
                intent.putExtra(EXTRA_CAR_MODEL, car.getCar_model());
                intent.putExtra(EXTRA_CAR_START_MILEAGE, Integer.toString(car.getStarting_mileage()));
                intent.putExtra(EXTRA_CAR_ANNUAL_MILEAGE, Integer.toString(car.getMileage_allowed()));
                intent.putExtra(EXTRA_USER_ID, car.getFk_user_id());

                startActivityForResult(intent, EDIT_CAR_REQUEST);

            }
        });

///////Handling the FAB to add a new car for this user
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CarListActivity.this, CarAddEditActivity.class);
                intent.putExtra(EXTRA_USER_ID, userId);
                startActivityForResult(intent, ADD_CAR_REQUEST);
            }
        });

////////Handles search bar clickable area
        SearchView searchView = findViewById(R.id.search_bar);
        searchView.setOnClickListener(v -> {
            searchView.setIconified(false);
        });

////////Handles search bar query
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            private List<Car> foundCarList = new ArrayList<>();

            @Override
            public boolean onQueryTextSubmit(String query) {
                for (Car c : userCarsList) {
                    if (c.getCar_name().toLowerCase().contains(query.toLowerCase())) {
                        foundCarList.add(c);
                    }
                }
                if (foundCarList.size() > 0) {
                    carAdapter.setCarList(foundCarList);
                    carAdapter.notifyDataSetChanged();
                    searchView.clearFocus();
                    return false;
                } else {
                    Toast.makeText(CarListActivity.this, "No matches found!", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)) {
                    carViewModel.getCarsById(userId).observe(CarListActivity.this, new Observer<List<Car>>() {
                        @Override
                        public void onChanged(@Nullable List<Car> carList) {
                            assert carList != null;
                            carAdapter.setCarList(carList);
                            carAdapter.notifyDataSetChanged();
                            foundCarList.clear();
                        }
                    });
                }
                return false;
            }
        });

        ReportUtil.verifyStoragePermissions(this);

////////Method coded below to handle swipe to delete functionality. Has an AlertDialog to validate deletion.
        swipeHelper();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_CANCELED) {
            if (data != null) {
                String carName = data.getStringExtra(EXTRA_CAR_NAME);
                String carMake = data.getStringExtra(EXTRA_CAR_MAKE);
                String carModel = data.getStringExtra(EXTRA_CAR_MODEL);
                String leaseStartString = data.getStringExtra(EXTRA_CAR_LEASE_START);
                String startingMileageString = data.getStringExtra(EXTRA_CAR_START_MILEAGE);
                String annualMileageString = data.getStringExtra(EXTRA_CAR_ANNUAL_MILEAGE);
                int userId = data.getIntExtra(EXTRA_USER_ID, -1);
                int carId = data.getIntExtra(EXTRA_CAR_ID, -1);

                //Converting strings above to proper types.
                assert startingMileageString != null;
                int startingMileage = Integer.parseInt(startingMileageString);
                assert annualMileageString != null;
                int annualMileage = Integer.parseInt(annualMileageString);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    LocalDate ldLeaseStart = DateUtil.stringToDateConverter(leaseStartString);
                    if (requestCode == ADD_CAR_REQUEST && resultCode == RESULT_OK) {
                        try {
                            Car car = new Car(carName, ldLeaseStart, carMake, carModel, startingMileage, annualMileage, userId);
                            carViewModel.insertCar(car);
                            Toast.makeText(this, "Car Saved!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (requestCode == EDIT_CAR_REQUEST && resultCode == RESULT_OK) {
                        try {
                            Car car = new Car(carName, ldLeaseStart, carMake, carModel, startingMileage, annualMileage, userId);
                            car.setCar_id(carId);
                            carViewModel.updateCar(car);
                            Toast.makeText(this, "Car Updated!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Date dateLeaseStart = DateUtil.stringToDateConverterOld(leaseStartString);
                }


            }

        }

    }

    private void swipeHelper() {
        //handles swiping car to delete.
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int direction) {
                final Car swipedCar = carAdapter.getCarAt(viewHolder.getAdapterPosition());
                AlertDialog.Builder builder = new AlertDialog.Builder(viewHolder.itemView.getContext())
                        .setMessage("Delete this car?");
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        carViewModel.deleteCar(swipedCar);
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        carAdapter.notifyItemChanged(viewHolder.getAdapterPosition());
                        dialog.cancel();
                    }
                });
                alertDialog = builder.show();
                alertDialog.setCanceledOnTouchOutside(false);
            }
        }).attachToRecyclerView(carRecyclerView);
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.report) {
            int permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ReportUtil.verifyStoragePermissions(this);
            }else{
                ReportUtil.createReport(userCarsList);
            }
        }else if(item.getItemId() == R.id.logOut){
            Intent intent = new Intent(CarListActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
