package com.consultinghm.prueba.interfaces;

import com.consultinghm.prueba.model.Municipios;

import java.util.ArrayList;

public interface VolleyCallbacksMunicipios{
    void onSuccess(ArrayList<Municipios> data);
    void onSuccess(String txt);
}