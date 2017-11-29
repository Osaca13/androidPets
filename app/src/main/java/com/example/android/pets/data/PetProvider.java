package com.example.android.pets.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Odalys on 13/11/2017.
 */

public class PetProvider extends ContentProvider {


    public static final String LOG_TAG = PetProvider.class.getSimpleName();
    private PetDbHelper mDbHelper;
    private SQLiteDatabase db;


    /** URI matcher code for the content URI for the pets table */
    private static final int PETS = 100;

    /** URI matcher code for the content URI for a single pet in the pets table */
    private static final int PET_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {

        sUriMatcher.addURI(PetContract.PetEntry.CONTENT_AUTHORITY, PetContract.PetEntry.PATH_TABLE, 100);
        sUriMatcher.addURI(PetContract.PetEntry.CONTENT_AUTHORITY, PetContract.PetEntry.PATH_TABLE+"/#", 101);

        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.

        // TODO: Add 2 content URIs to URI matcher
    }


    @Override
    public boolean onCreate() {
        mDbHelper = new PetDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] proyeccion, @Nullable String seleccion, @Nullable String[] seleccionArgs, @Nullable String sortOrder) {
        // uri y proyeccion son datos de entrada a este metodo
        db = mDbHelper.getReadableDatabase();
       Cursor cursor = null;
          int match = sUriMatcher.match(uri);

        switch (match){
            case PETS:

                cursor = db.query(PetContract.PetEntry.TABLE_NAME, proyeccion, seleccion, seleccionArgs, null, null, sortOrder);
                break;
            case PET_ID:
                seleccion = PetContract.PetEntry._ID + "=?";
                seleccionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                cursor = db.query(PetContract.PetEntry.TABLE_NAME, proyeccion, seleccion, seleccionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException(" Cannot query unknown" + uri);
        }
        //getContext().getContentResolver().notifyChange(uri, null);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case PETS:
                return PetContract.PetEntry.DIRECTORY_MIME_TYPE;
            case PET_ID:
                return PetContract.PetEntry.ITEM_MIME_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }


    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        db = mDbHelper.getWritableDatabase();

        long newRowId = db.insert(PetContract.PetEntry.TABLE_NAME, null, contentValues);
            if (newRowId == -1) {
                    Log.e(LOG_TAG, "Failed to insert row for " + uri);
                    return null;
            }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, newRowId);

    }



    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        db = mDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Log.v("match: ", String.valueOf(match));
        Integer miint;
        switch (match){
            case PETS:
                 miint = db.delete(PetContract.PetEntry.TABLE_NAME,  selection, selectionArgs);
                Log.v("miint", String.valueOf(miint));
                break;
            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                miint = db.delete(PetContract.PetEntry.TABLE_NAME, selection, selectionArgs);
                Log.v("miint", String.valueOf(miint));
                break;
            default:
                miint = 0;
                //throw new IllegalArgumentException("Delection is nor supported" + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return miint;
    }



    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        db = mDbHelper.getWritableDatabase();
        int miint;
        switch (match){
            case PETS:
                miint = db.update(PetContract.PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;


            case PET_ID:
                selection = PetContract.PetEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                miint= db.update(PetContract.PetEntry.TABLE_NAME, contentValues, selection, selectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Update is nor supported" + uri);


        }

        getContext().getContentResolver().notifyChange(uri, null);
        return miint;
    }

    /*public int updatePet(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs){

        int miint;

        //String name =

       *//* if (contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_NAME)) {
            String name = contentValues.getAsString(PetContract.PetEntry.COLUMN_PET_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Pet requires a name");

            }
        }
        if (contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_GENDER)){
                Integer gender = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_GENDER);
                if (gender == null || !PetContract.PetEntry.isValidGender(gender)){
                    throw new IllegalArgumentException("Pet requires valid gender");

                }
        }
        if (contentValues.containsKey(PetContract.PetEntry.COLUMN_PET_WEIGHT)){
                Integer weight = contentValues.getAsInteger(PetContract.PetEntry.COLUMN_PET_WEIGHT);

                if (weight != null && weight < 0){
                    throw new IllegalArgumentException("Pet requires valid weight");

                }
        }
        if (contentValues.size() == 0){
            return 0;
        }*//*




        Log.v("selection", selection);
        Log.v("selectionArgs", selectionArgs[0]);



        return miint;

    }*/


}
