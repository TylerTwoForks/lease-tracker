package com.example.te_leasetracker_c868.Utility;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.te_leasetracker_c868.CarListActivity;
import com.example.te_leasetracker_c868.DB_Entities.Car;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ReportUtil {
    private static final String TAG = CarListActivity.class.getSimpleName();

    public static void createReport(List<Car> carList) {
        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
        String fileName = "CarLeaseReport.csv";
        String filePath = baseDir + File.separator + fileName;
        Log.i(TAG, "createReport: " + filePath);


        try {
            FileWriter writer = new FileWriter(filePath);
            CSVUtils.writeLine(writer, Arrays.asList("Car Name", "Lease Start", "Car Make",
                    "Car Model", "Starting Mileage", "Annual Mileage"));

            for (Car c : carList) {
                List<String> carDetailList = new ArrayList<>();
                String leaseStartString = DateUtil.dateToString(c.getLease_start());
                String startingMileageString = Integer.toString(c.getStarting_mileage());
                String annualMileageString = Integer.toString(c.getMileage_allowed());

                carDetailList.add(c.getCar_name());         //Car Name
                carDetailList.add(leaseStartString);        //Lease Start
                carDetailList.add(c.getCar_make());         //Car Make
                carDetailList.add(c.getCar_model());        //Car Model
                carDetailList.add(startingMileageString);   //Starting Mileage
                carDetailList.add(annualMileageString);     //Annual Mileage

                CSVUtils.writeLine(writer, carDetailList);

            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }


}
