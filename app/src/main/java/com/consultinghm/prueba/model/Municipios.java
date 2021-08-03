package com.consultinghm.prueba.model;

public  class Municipios{
    private String id;
    private String municipio;

    @Override
    public String toString() {
        return this.municipio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

}