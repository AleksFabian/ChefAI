package com.example.hci_finalproj;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity{
    EditText signUpEmail;
    EditText signUpPassword;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        signUpEmail = (EditText) findViewById(R.id.email_et);
        signUpPassword = (EditText)  findViewById(R.id.password_et);
    }
    public void toRegisterActivity(View v){
        Intent intent = new Intent(this, RegisterActivity.class);
        intent.putExtra("email", signUpEmail.getText().toString());
        intent.putExtra("password", signUpPassword.getText().toString());
        startActivity(intent);
    }
    public void finishActivity(View view) {
        super.finish();
    }
}
