package com.consultinghm.prueba.interfaces;

import com.consultinghm.prueba.model.Profesiones;

import java.util.ArrayList;

public interface VolleyCallbacksProfesiones{
    void onSuccess(ArrayList<Profesiones> data);
    void onSuccess(String txt);
}