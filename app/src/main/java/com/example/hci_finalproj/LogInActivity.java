package com.example.hci_finalproj;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LogInActivity extends AppCompatActivity{
    public AccountDatabase accountDatabase;
    EditText loginEmail;
    EditText loginPassword;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in);

        accountDatabase = new AccountDatabase(this);
    }

    public void toHomeScreen(View v){
        loginEmail = findViewById(R.id.login_email_et);
        loginPassword = findViewById(R.id.login_password_et);

        String email = loginEmail.getText().toString();
        String password = loginPassword.getText().toString();

        if (email.isEmpty()) {
            loginEmail.setError("Email is required");
            return;
        }

        if (password.isEmpty()) {
            loginPassword.setError("Password is required");
            return;
        }

        boolean isValidAccount = validate(email, password);

        if (isValidAccount) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("email", email);
            intent.putExtra("password", password);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean validate(String email, String password){
        SQLiteDatabase db = accountDatabase.getReadableDatabase();

        String selection = AccountDatabase.COLUMN_EMAIL + " = ? AND " + AccountDatabase.COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {email, password};

        Cursor cursor = db.query(AccountDatabase.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        boolean isValidUser = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        db.close();

        return isValidUser;
    }

    public void finishActivity(View view) {
        super.finish();
    }
}
