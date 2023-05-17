package com.example.hci_finalproj;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RecipeDatabase extends SQLiteOpenHelper {

    private static final String DB_NAME = "RecipeDB";
    private static final int DB_VERSION = 1;
    public static final String TABLE_NAME = "Recipes";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "recipe_name";
    public static final String COLUMN_RECIPE_PROCEDURE = "recipe";

    public RecipeDatabase(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_RECIPE_NAME + " TEXT NOT NULL,"
                + COLUMN_RECIPE_PROCEDURE + " TEXT NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    String recipeProcedure, recipeName, recipeID;

    public void readInfo(String recipeName){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_RECIPE_PROCEDURE};
        String selection = COLUMN_RECIPE_NAME + " = ? ";
        String[] selectionArgs = {recipeName};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int recipeProcIndex = cursor.getColumnIndexOrThrow(COLUMN_RECIPE_PROCEDURE);
            this.recipeProcedure = cursor.getString(recipeProcIndex);
            this.recipeName = recipeName;
        }

        readId();

        cursor.close();
        db.close();
    }

    public void readId(){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {COLUMN_ID};
        String selection = COLUMN_RECIPE_NAME + " = ? AND " + COLUMN_RECIPE_PROCEDURE + " = ?";
        String[] selectionArgs = {this.recipeName, this.recipeProcedure};

        Cursor cursor = db.query(TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        if (cursor.moveToFirst()) {
            int recipeIdIndex = cursor.getColumnIndexOrThrow(COLUMN_ID);
            this.recipeID = cursor.getString(recipeIdIndex);
        }

        cursor.close();
        db.close();
    }
    public int getRecipeId(){
        return Integer.parseInt(this.recipeID);
    }

    public String getRecipeName() {
        return recipeName;
    }
    public String getRecipeProcedure() {
        return recipeProcedure;
    }
}
