package com.example.android.pets;

/**
 * Created by Odalys on 18/11/2017.
 */

public class PetsList {
    private Integer ID;
    private String nombre;
    private String raza;
    private Integer peso;
    private Integer sexo;

    public PetsList(Integer ID, String nombre, String raza, Integer sexo, Integer peso) {
        this.ID = ID;
        this.nombre = nombre;
        this.raza = raza;
        this.peso = peso;
        this.sexo = sexo;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Integer getPeso() {
        return peso;
    }

    public void setPeso(Integer peso) {
        this.peso = peso;
    }

    public Integer getSexo() {
        return sexo;
    }

    public void setSexo(Integer sexo) {
        this.sexo = sexo;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }
}
