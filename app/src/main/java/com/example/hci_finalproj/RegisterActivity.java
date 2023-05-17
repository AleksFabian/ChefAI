package com.example.hci_finalproj;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    private AccountDatabase accountDatabase;
    EditText userName;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        userName = (EditText) findViewById(R.id.username_et);
        accountDatabase = new AccountDatabase(this);
    }

    public void toLoginActivity(View v){
        registerAcc(getIntent().getStringExtra("email"), getIntent().getStringExtra("password"), userName.getText().toString());
        Intent intent = new Intent(this, LogInActivity.class);
        startActivity(intent);
    }

    public void registerAcc(String email, String password, String username){
        SQLiteDatabase db = accountDatabase.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(AccountDatabase.COLUMN_EMAIL, email);
        values.put(AccountDatabase.COLUMN_PASSWORD, password);
        values.put(AccountDatabase.COLUMN_USERNAME, username);

        db.insert(AccountDatabase.TABLE_NAME, null, values);

        db.close();
    }
    public void finishActivity(View view) {
        super.finish();
    }
}
