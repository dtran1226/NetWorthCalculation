package com.example.thuct.networthtracking.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.thuct.networthtracking.R;
import com.example.thuct.networthtracking.utility.Utility;

public class MainActivity extends AppCompatActivity {

    EditText edtName;
    Button btnOK;
    Button btnClear;
    final String SHARED_PREFERENCE_NAME = "MyPref";
    final String SHARED_PREFERENCE_USER_NAME = "userName";
    final String EMPTY_STRING = "";
    final String TOAST_MESSAGE_EMPTY_STRING = "Please enter your name!";
    final String TOAST_MESSAGE_INVALID_USER_NAME = "Your name must contain letters and white spaces only!";
    final String VALID_REGEX_FOR_USER_NAME = "^([a-zA-Z]+\\s)*[a-zA-Z]+$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get all controls from view to handle
        edtName = findViewById(R.id.edt_user_name);
        btnOK = findViewById(R.id.btn_ok);
        btnClear = findViewById(R.id.btn_clear);

        // Validate, then save user name and switch to 'Net Worth Tracking' activity
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = edtName.getText().toString();
                //Validate user name
                if (validUserName(userName)) {
                    // Save user name to SharedPreference
                    Utility.saveStringToSharePref(getApplicationContext(), SHARED_PREFERENCE_NAME, SHARED_PREFERENCE_USER_NAME, edtName.getText().toString());
                    // Switch to 'Net Worth Tracking' activity
                    Intent intent = new Intent(MainActivity.this, NetWorthTrackingActivity.class);
                    startActivity(intent);
                }
            }
        });

        /*
        Clear user name's input text when clicking 'Clear' button
         */
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edtName.setText("");
            }
        });
    }

    /*
    Show a message to let user know if they did not input their name or their name contains numbers/special characters
     */
    private boolean validUserName(String name) {
        String toastMsg = EMPTY_STRING;
        if (name.isEmpty()) {
            toastMsg = TOAST_MESSAGE_EMPTY_STRING;
        } else if (!name.matches(VALID_REGEX_FOR_USER_NAME)) {
            toastMsg = TOAST_MESSAGE_INVALID_USER_NAME;
        }
        if (!EMPTY_STRING.equals(toastMsg)) {
            Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
