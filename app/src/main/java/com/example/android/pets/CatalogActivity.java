/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.pets;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.pets.AdapterCursor.PetCursorAdapter;
import com.example.android.pets.data.PetContract.PetEntry;

import java.util.ArrayList;

import android.content.CursorLoader;


/**
 * Displays list of pets that were entered and stored in the app.
 */
public class CatalogActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{



    private Cursor cursor = null;

    private ListView listViewPet;
    private PetCursorAdapter petCursorAdapter;
    private static final int PET_LOADER = 0;
   // private LoaderCallbacks<D> callback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });


        listViewPet = (ListView) findViewById(R.id.listViewPets);


        petCursorAdapter = new PetCursorAdapter(this, cursor);
        listViewPet.setAdapter(petCursorAdapter);
        listViewPet.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                Uri uri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, id);
                intent.setData(uri);

                Cursor cursor = (Cursor) listViewPet.getItemAtPosition(position);

                intent.putExtra("id", cursor.getString(cursor.getColumnIndex(PetEntry._ID)));
                intent.putExtra("nombre", cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_NAME)));
                intent.putExtra("raza", cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_BREED)));
                intent.putExtra("Sexo", cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_GENDER)));
                intent.putExtra("Peso", cursor.getString(cursor.getColumnIndex(PetEntry.COLUMN_PET_WEIGHT)));


                startActivity(intent);

            }
        });

        getLoaderManager().initLoader(PET_LOADER, null, this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Insert dummy data" menu option
            case R.id.action_insert_dummy_data:
                insertPet();
                return true;
            // Respond to a click on the "Delete all entries" menu option
            case R.id.action_delete_all_entries:
                deletePet();
                Log.v("Borrado", "borrado correctamente");
                // Do nothing for now
                return true;
            case R.id.close_activity:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void insertPet() {
        // Create a ContentValues object where column names are the keys,
        // and Toto's pet attributes are the values.
        ContentValues values = new ContentValues();
        values.put(PetEntry.COLUMN_PET_NAME, "Roro");
        values.put(PetEntry.COLUMN_PET_BREED, "labrador");
        values.put(PetEntry.COLUMN_PET_GENDER, PetEntry.GENDER_UNKNOWN);
        values.put(PetEntry.COLUMN_PET_WEIGHT, 15);


        getContentResolver().insert(PetEntry.CONTENT_URI, values);

        }



    private void deletePet() {

        //String selection = PetEntry.COLUMN_PET_NAME + "=?";
        //String[] selectionArgs = { "5"};

        Uri uri = PetEntry.CONTENT_URI;
        Integer index = getContentResolver().delete(uri, null, null );
        Toast.makeText(this, "Filas borradas: "+String.valueOf(index), Toast.LENGTH_SHORT).show();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String[] projection = {
                PetEntry._ID,
                PetEntry.COLUMN_PET_NAME,
                PetEntry.COLUMN_PET_BREED,
                PetEntry.COLUMN_PET_GENDER,
                PetEntry.COLUMN_PET_WEIGHT
        };

        return new CursorLoader(this,
                PetEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);

       }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        petCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        petCursorAdapter.swapCursor(null);

    }
}
