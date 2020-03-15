package com.example.te_leasetracker_c868;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.te_leasetracker_c868.View_Models.UserViewModel;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CreateUserDialog extends AppCompatDialogFragment {
    private static final String TAG = CreateUserDialog.class.getSimpleName();
    private boolean wantToClose = false;
    private EditText etUserName;
    private EditText etPassword;
    private LinearLayout llUnError;
    private CreateUserDialogListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        UserViewModel userViewModel = ViewModelProviders.of(this).get(UserViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_create_user, null);

        builder.setView(view)
                .setTitle("Create User")
                .setNegativeButton("cancel", (dialog, which) -> {
                })
                .setPositiveButton("ok", (dialog, which) -> {
                });


        etUserName = view.findViewById(R.id.et_add_username);
        etPassword = view.findViewById(R.id.et_add_password);
        llUnError = view.findViewById(R.id.ll_un_error);

        final AlertDialog createUserDialog = builder.create();
        createUserDialog.show();

        //overriding the OK button in our dialog so it only closes if the username doesn't exist.
        //if the username exists in the db, we do not close and we display an error saying the name already exists.
        createUserDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

            String username = etUserName.getText().toString();
            String pass = etPassword.getText().toString();

            Executor executor = Executors.newSingleThreadExecutor();
            executor.execute(() -> {
                Looper.prepare();
                if (!username.equals("") && !pass.equals("")) {
                    if (!userViewModel.checkUser(username)) {
                        listener.createUser(username, pass);
                        wantToClose = true;
                    }
                    if (wantToClose) {
                        createUserDialog.dismiss();
                    } else {
                        llUnError.post(() -> {
                            llUnError.setVisibility(View.VISIBLE);
                        });
                    }
                }
                Looper.loop();
            });


        });

        return createUserDialog;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (CreateUserDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement CreateUserDialogListener");
        }
    }

    public interface CreateUserDialogListener {
        void createUser(String un, String pw);
    }
}
