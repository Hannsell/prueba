package com.consultinghm.prueba.model;

public class Profesiones {
    private String id;
    private String profesion;

    @Override
    public String toString() {
        return this.profesion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }
}