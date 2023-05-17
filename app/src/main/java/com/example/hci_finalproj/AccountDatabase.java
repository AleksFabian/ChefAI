package com.example.hci_finalproj;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class AccountDatabase extends SQLiteOpenHelper {
    private static final String DB_NAME = "UserDB";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "users";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_USERNAME = "username";


    public AccountDatabase(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    public AccountDatabase(HomeFragment homeFragment) {
        super(homeFragment.getContext(), DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_EMAIL + " TEXT NOT NULL,"
                + COLUMN_PASSWORD + " TEXT NOT NULL,"
                + COLUMN_USERNAME + " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    String userName, email;
    public void readInfo(String eMail, String pw){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_USERNAME};
        String selection = COLUMN_EMAIL + " = ? AND " + COLUMN_PASSWORD + " = ?";
        String[] selectionArgs = {eMail, pw};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int usernameIndex = cursor.getColumnIndexOrThrow(COLUMN_USERNAME);
            this.userName = cursor.getString(usernameIndex);
            this.email = eMail;
        }

        cursor.close();
        db.close();
    }

    public String getUsername(){
        return userName;
    }

    public String getEmail(){
        return email;
    }
}
