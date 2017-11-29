package com.example.android.pets.AdapterCursor;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.android.pets.R;
import com.example.android.pets.data.PetContract;

/**
 * Created by Odalys on 22/11/2017.
 */

public class PetCursorAdapter extends CursorAdapter {

    public PetCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
        TextView textViewBreed = (TextView) view.findViewById(R.id.textViewBreed);
       TextView textViewSexo = (TextView) view.findViewById(R.id.textViewSexo);
        TextView textViewPeso = (TextView) view.findViewById(R.id.textViewPeso);
       /* TextView textViewID = (TextView) view.findViewById(R.id.textViewNumero);*/

        //int idColumnIndex = cursor.getColumnIndex(PetContract.PetEntry._ID);
        int nameColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_NAME);
        int breedColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_BREED);
        int genderColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_GENDER);
        int weightColumnIndex = cursor.getColumnIndex(PetContract.PetEntry.COLUMN_PET_WEIGHT);


      //  String idPet = String.valueOf(cursor.getInt(idColumnIndex));
        String namePet = cursor.getString(nameColumnIndex);
        String breedPet = cursor.getString(breedColumnIndex);
        String genderPet = "Unknown";
        switch (cursor.getInt(genderColumnIndex)){
            case 0:
                genderPet = "Unknown";
                break;
            case 1:
                genderPet = "Male";
                break;
            case 2:
                genderPet = "Female";
                break;
         }


        String weightPet = String.valueOf(cursor.getInt(weightColumnIndex));

       /* textViewID.setText(idPet);*/
        textViewName.setText(namePet);
        textViewBreed.setText(breedPet);
        textViewSexo.setText(genderPet);
        textViewPeso.setText(weightPet);

    }
}
