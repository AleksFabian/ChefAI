package com.example.hci_finalproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class StartUpActivity extends  AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_up);
    }
    public void toLoginActivity(View v){
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }
    public void toSignUpActivity(View v){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }
}
