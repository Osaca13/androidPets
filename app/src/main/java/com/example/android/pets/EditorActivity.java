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

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.pets.data.PetContract;
import com.example.android.pets.data.PetContract.PetEntry;
import com.example.android.pets.data.PetDbHelper;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    private PetDbHelper mDbHelper;

    /** EditText field to enter the pet's name */
    private EditText mNameEditText;

    /** EditText field to enter the pet's breed */
    private EditText mBreedEditText;

    /** EditText field to enter the pet's weight */
    private EditText mWeightEditText;

    /** EditText field to enter the pet's gender */
    private Spinner mGenderSpinner;
    private String ID0;

    /**
     * Gender of the pet. The possible valid values are in the PetContract.java file:
     * {@link PetEntry#GENDER_UNKNOWN}, {@link PetEntry#GENDER_MALE}, or
     * {@link PetEntry#GENDER_FEMALE}.
     */
    private int mGender = PetEntry.GENDER_UNKNOWN;
    public int numeroDatos;
    ArrayAdapter genderSpinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mNameEditText = (EditText) findViewById(R.id.edit_pet_name);
        mBreedEditText = (EditText) findViewById(R.id.edit_pet_breed);
        mWeightEditText = (EditText) findViewById(R.id.edit_pet_weight);
        mGenderSpinner = (Spinner) findViewById(R.id.spinner_gender);

        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri == null){
            setTitle("Add a pet");
            setupSpinner();
        }else {
            setTitle(getString(R.string.titulo_pet));
            Bundle bundle = intent.getExtras();
            ID0 = bundle.getString("id");
            mNameEditText.setText(bundle.getString("nombre"));
            mBreedEditText.setText(bundle.getString("raza"));
            String texto = bundle.getString("Sexo");

            if (texto != null) {
                mGender = Integer.parseInt(texto);
            }
            changeSpinner(mGender);

            mWeightEditText.setText(String.valueOf(bundle.getString("Peso")));


        }


    }

    private void changeSpinner(int i){

        genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mGenderSpinner.setAdapter(genderSpinnerAdapter);
        mGenderSpinner.setSelection(i);
    }

    /**
     * Setup the dropdown spinner that allows the user to select the gender of the pet.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        genderSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_gender_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGenderSpinner.setAdapter(genderSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.gender_male))) {
                        mGender = PetEntry.GENDER_MALE;
                    } else if (selection.equals(getString(R.string.gender_female))) {
                        mGender = PetEntry.GENDER_FEMALE;
                    } else {
                        mGender = PetEntry.GENDER_UNKNOWN;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGender = PetEntry.GENDER_UNKNOWN;
            }


        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                insertPet();
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_modify:
                modifyPet();
                // Do nothing for now
                return true;
            case R.id.action_delete:

                deletePet();
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)


                NavUtils.navigateUpFromSameTask(this);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deletePet() {

        String[] selectionArgs = { "Toto"};

        Integer index = getContentResolver().delete(PetEntry.CONTENT_URI, PetEntry.COLUMN_PET_NAME, selectionArgs );

        Toast.makeText(this, "Filas borradas"+String.valueOf(index), Toast.LENGTH_SHORT).show();

    }

    private void modifyPet(){

        AlertDialog alertDialog = new AlertDialog.Builder(EditorActivity.this).create();
        alertDialog.setTitle("Actualizar");
        alertDialog.setMessage("¿Desea cambiar los datos?");
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "si",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        recogerDatos();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "no",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private void recogerDatos(){
          Uri uri;
          Integer seleccion;


        //Integer idnumber = indice ;

        ContentValues contentValues = new ContentValues();
        String name = mNameEditText.getText().toString();
        String raza = mBreedEditText.getText().toString();
        String sexo = mGenderSpinner.getSelectedItem().toString();
        switch (sexo){
            default:
                seleccion = 0;
                break;
            case "Female":
                seleccion = 2;
                break;
            case "Male":
                seleccion = 1;
                break;
        }
        String peso = mWeightEditText.getText().toString();
        uri = ContentUris.withAppendedId(PetEntry.CONTENT_URI, Integer.parseInt(ID0));

        contentValues.put(PetEntry.COLUMN_PET_NAME, name );
        contentValues.put(PetEntry.COLUMN_PET_BREED, raza);
        contentValues.put(PetEntry.COLUMN_PET_GENDER, seleccion);
        contentValues.put(PetEntry.COLUMN_PET_WEIGHT, Integer.parseInt(peso));

        //AlertDialog.BUTTON_POSITIVE == 0)
        try {
            Integer indexx = getContentResolver().update(uri, contentValues, null, null);
            Toast.makeText(this, "Actualizado Pets ID =" +String.valueOf(ID0), Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            Toast.makeText(this, "Error en ID =" +String.valueOf(ID0), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void insertPet(){
        int seleccion = 0;


        Toast.makeText(this, "se incluira en la base de datos", Toast.LENGTH_SHORT).show();
        ContentValues contentValues = new ContentValues();

       // if (contentValues.getAsString(PetEntry.COLUMN_PET_NAME) != null) {
            Log.v("Pet name", mNameEditText.getText().toString());
            contentValues.put(PetEntry.COLUMN_PET_NAME, mNameEditText.getText().toString());
      //  }else{
      //      throw new IllegalArgumentException("Pet requires a name");
      //  }

      //  if (contentValues.getAsString(PetEntry.COLUMN_PET_BREED) != null) {
        Log.v("Pet raza", mBreedEditText.getText().toString());
            contentValues.put(PetEntry.COLUMN_PET_BREED, mBreedEditText.getText().toString() );
    //}
      //  else{
      //      throw new IllegalArgumentException("Pet requires a breed");
       // }

        switch (mGender){
            case PetEntry.GENDER_UNKNOWN:
                seleccion = 0;
                break;
            case PetEntry.GENDER_FEMALE:
                seleccion = 2;
                break;
            case PetEntry.GENDER_MALE:
                seleccion = 1;
                break;
        }
        contentValues.put(PetEntry.COLUMN_PET_GENDER, seleccion );

        contentValues.put(PetEntry.COLUMN_PET_WEIGHT, mWeightEditText.getText().toString() );

        Uri uri = getContentResolver().insert(PetEntry.CONTENT_URI, contentValues);
        Toast.makeText(this, String.valueOf(ContentUris.parseId(uri)), Toast.LENGTH_SHORT).show();


    }

   /* public void colocarDatos(){
        Intent intent = getIntent();
        // Bundle bundleI = intent.getExtras("name");

        data = intent.getExtras();

        if (data != null){
            String texto0 = data.getString("ID");
            indice = Integer.parseInt(texto0);

            String texto1 = data.getString("name");
            mNameEditText.setText(texto1);
            String texto2 = data.getString("raza");
            mBreedEditText.setText(texto2);
            String texto3 = data.getString("Peso");
            mWeightEditText.setText(texto3);
            String texto4 = data.getString("Sexo");
            if (texto4 == "UNKNOWN"){
                mGenderSpinner.setSelection(0);
            }
           if (texto4 == "Macho"){
               mGenderSpinner.setSelection(1);
           }
           if (texto4 == "Hembra"){
               mGenderSpinner.setSelection(2);
           }
        }
        return;

    }*/

    @Override
    protected void onStart() {
        super.onStart();

    }
}