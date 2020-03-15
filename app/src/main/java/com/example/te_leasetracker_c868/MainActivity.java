package com.example.te_leasetracker_c868;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.te_leasetracker_c868.DB_Entities.User;
import com.example.te_leasetracker_c868.View_Models.UserViewModel;
import com.google.android.material.snackbar.Snackbar;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements CreateUserDialog.CreateUserDialogListener {
    //Used to pass userId of the logged in user to the new activity.
    public static final String EXTRA_USER_ID ="com.example.te_leasetracker_c868.EXTRA_USER_ID";
    public static final String EXTRA_USER_NAME ="com.example.te_leasetracker_c868.EXTRA_USER_NAME";

    //Used for log tracing
    private static final String TAG = MainActivity.class.getSimpleName();

    private UserViewModel userViewModel;

    private EditText et_userName;
    private EditText et_userPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_userName = findViewById(R.id.et_user_name);
        et_userPass = findViewById(R.id.et_user_pass);
        Button btn_login = findViewById(R.id.btn_login);
        TextView tv_createAcct = findViewById(R.id.tv_createAccount);

        //Setting up access to UserViewModel
        userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

/////If the login/create buttons exist, set the on click listeners defined below.
        if(btn_login !=null){
            btn_login.setOnClickListener(loginBtnListener);
        }
        if(tv_createAcct !=null){
            tv_createAcct.setOnClickListener(createAcctBtnListener);
        }

    }

/////Handles functionality of the login button. Does some checks and makes sure un/pw match the db.
    private View.OnClickListener loginBtnListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if(et_userName.getText().toString().equals("")||et_userPass.getText().toString().equals("")){
                    Log.d(TAG, "onClick: Please enter username and password");
                    Toast.makeText(MainActivity.this, "Please enter username and password", Toast.LENGTH_SHORT).show();
                }else {
                    //checkLogin requires running on the background thread as it does some sql queries on the db.
                    //Used an executor to run this portion of the code because of that.
                    Executor executor = Executors.newSingleThreadExecutor();
                    executor.execute(() -> {
                        Looper.prepare();
                        String userName = et_userName.getText().toString();
                        if(userViewModel.checkLogin(userName, et_userPass.getText().toString())){
                            User currentUser = userViewModel.getUserByName(userName);
                            int userId = currentUser.getUser_id();
                            Intent intent = new Intent(MainActivity.this, CarListActivity.class);
                            intent.putExtra(EXTRA_USER_ID, userId);
                            intent.putExtra(EXTRA_USER_NAME, userName);
                            startActivity(intent);
                            Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Invalid Login", Toast.LENGTH_SHORT).show();
                        }
                        Looper.loop();
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, "test catch");
            }
        }
    };

    private View.OnClickListener createAcctBtnListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            openCreateUserDialog();
        }
    };

    public void openCreateUserDialog(){
        CreateUserDialog createUserDialog = new CreateUserDialog();
        createUserDialog.show(getSupportFragmentManager(), "create user dialog");
    }

    @Override
    public void createUser(String un, String pw) {
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Looper.prepare();
            User newUser = new User(un, pw);
            userViewModel.insertUser(newUser);
            Toast.makeText(this, "New user created!", Toast.LENGTH_SHORT).show();
            Looper.loop();
        });
    }
}
