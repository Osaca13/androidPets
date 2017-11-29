package com.example.android.pets.AdapterRecyclerView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.pets.CatalogActivity;
import com.example.android.pets.EditorActivity;
import com.example.android.pets.PetsList;
import com.example.android.pets.R;

import java.util.ArrayList;

/**
 * Created by Odalys on 18/11/2017.
 */

public class PetAdapter extends RecyclerView.Adapter<PetAdapter.PetViewHolder> {

    ArrayList<PetsList> petsLists;


    public PetAdapter(ArrayList<PetsList> petsLists){
        this.petsLists = petsLists;

    }

    @Override
    public PetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview, parent, false);

        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PetViewHolder petViewHolder, int position) {
        PetsList petsList = petsLists.get(position);
      //  petViewHolder.textViewNumero.setText(String.valueOf(petsList.getID()));
        petViewHolder.textViewName.setText(petsList.getNombre());
        petViewHolder.textViewBreed.setText(petsList.getRaza());
     //   petViewHolder.textViewSexo.setText(String.valueOf(petsList.getSexo()));
     //   petViewHolder.textViewPeso.setText(String.valueOf(petsList.getPeso()));



    }

    @Override
    public long getItemId(int position) {
        petsLists.get(position);
        return super.getItemId(position);
    }

    @Override
    public int getItemCount() {
        return petsLists.size();
    }

    public  static class PetViewHolder extends ViewHolder implements OnClickListener{

        private TextView textViewName;
        private TextView textViewBreed;
        private ImageView imageViewFoto;
        /*private TextView textViewSexo;
        private TextView textViewPeso;
        private TextView textViewNumero;*/

        public PetViewHolder(View itemView) {
            super(itemView);

           // textViewNumero = (TextView) itemView.findViewById(R.id.textViewNumero);
            textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            textViewBreed = (TextView) itemView.findViewById(R.id.textViewBreed);
            /*imageViewFoto = (ImageView) itemView.findViewById(R.id.imageViewFoto);
            textViewSexo = (TextView) itemView.findViewById(R.id.textViewSexo);
            textViewPeso = (TextView) itemView.findViewById(R.id.textViewPeso);*/

        }

        @Override
        public void onClick(View itemView) {
            Context context = itemView.getContext();

            Intent intent = new Intent(context, EditorActivity.class);

            //PetAdapter petAdapter = new PetAdapter(petsLists);
            // petAdapter.getItemId(RecyclerView.NO_POSITION);
            //    intent.putExtra("ID", textViewNumero.getText().toString());

                intent.putExtra("name", textViewName.getText().toString());

                intent.putExtra("raza", textViewBreed.getText().toString());

            //    intent.putExtra("Sexo", textViewSexo.getText().toString());

             //   intent.putExtra("Peso", textViewPeso.getText().toString());


            context.startActivity(intent);

        }
    }

}



