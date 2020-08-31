package com.example.karim.shelter.data;
import com.example.karim.shelter.data.PetContract.PetEntry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class PetDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = PetDbHelper.class.getSimpleName();
    private  Context mcontext;

    /** Name of the database file */
    private static final String DATABASE_NAME = "shelter.db";

    /**
     * Database version. If you change the database schema, you must increment the database version.
     */
    private static final int DATABASE_VERSION = 1;

    /**
     * Constructs a new instance of {@link PetDbHelper}.
     *
     * @param context of the app
     */
    public PetDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mcontext=context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create a String that contains the SQL statement to create the pets table
        String SQL_CREATE_PETS_TABLE =  "CREATE TABLE " + PetEntry.TABLE_NAME + " ("
                + PetEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + PetEntry.COLUMN_PET_NAME + " TEXT NOT NULL, "
                + PetEntry.COLUMN_PET_BREED + " TEXT, "
                + PetEntry.COLUMN_PET_GENDER + " INTEGER NOT NULL, "
                + PetEntry.COLUMN_PET_WEIGHT + " INTEGER NOT NULL DEFAULT 0);";
        db.execSQL(SQL_CREATE_PETS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         db.execSQL("DROP TABLE IF EXISTS "+PetEntry.TABLE_NAME);
    }
    public boolean insertPetData(String petName,String petBreed,int petGender,int petWeight){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, petName);
        values.put(PetEntry.COLUMN_PET_BREED, petBreed);
        values.put(PetEntry.COLUMN_PET_GENDER, petGender);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, petWeight);
        long result =db.insert(PetEntry.TABLE_NAME,null,values);
        db.close();

        if (result==-1){
            Toast.makeText(mcontext,"Error with saving pet",Toast.LENGTH_SHORT).show();
            return  false;
        }else {
            Toast.makeText(mcontext,"Pet saved with id "+result,Toast.LENGTH_SHORT).show();
            return true;
        }
    }
    public Cursor getAllPetData(){
        // Create and/or open a database to read from it
        SQLiteDatabase db = this.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME, null);
        db.close();
        return cursor;
    }
    public Cursor getPetData2(){

        // Create and/or open a database to read from it
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = { PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_WEIGHT };
        String selection = PetEntry.COLUMN_PET_GENDER +"=?";

        String[] selectionArgs = new String[] { String.valueOf(PetEntry.GENDER_FEMALE) };

        Cursor cursor = db.query(PetEntry.TABLE_NAME, projection,
                selection, selectionArgs,
                null, null, null);
        db.close();
        return cursor;
    }
    public Cursor getPetData(String id){
        // Create and/or open a database to read from it
        SQLiteDatabase db = this.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        Cursor cursor = db.rawQuery("SELECT * FROM " + PetEntry.TABLE_NAME+" WHERE "+PetEntry._ID+"=?", new String[]{id});
        db.close();
        return cursor;
    }
    public int deletePetData(String id){
        // Create and/or open a database to delete from it
        SQLiteDatabase db = this.getWritableDatabase();
        int i=db.delete(PetEntry.TABLE_NAME,"ID =?",new String[]{id});
        db.close();
        return i;
    }
    public int deleteAllPetData(){
        // Create and/or open a database to delete from it
        SQLiteDatabase db = this.getWritableDatabase();
        int i=db.delete(PetEntry.TABLE_NAME,null,null);
        db.close();
        return i;
    }
    public boolean updatePetData(String id,String petName,String petBreed,int petGender,int petWeight){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, petName);
        values.put(PetEntry.COLUMN_PET_BREED, petBreed);
        values.put(PetEntry.COLUMN_PET_GENDER, petGender);
        values.put(PetContract.PetEntry.COLUMN_PET_WEIGHT, petWeight);

        long result =db.update(PetEntry.TABLE_NAME,values,"ID =?",new String[]{id});
        db.close();
        if (result>0){
            return  true;
        }else {
            return false;
        }
    }

}
