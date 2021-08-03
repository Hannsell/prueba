package com.consultinghm.test2;

import androidx.appcompat.app.AppCompatActivity;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import kotlin.text.UStringsKt;

public class MainActivity extends AppCompatActivity {
    private RequestQueue mQueue;
    private TextView mTextViewResult;
    private Spinner spinnerMunicipios;
    private Spinner spinnerProfesiones;
    final String base_url  = "http://192.168.0.12";
    private EditText edtCedula;
    private EditText edtNombre;
    private EditText edtApellidos;
    private RadioButton rbMasculino;
    private RadioButton rbFemenino;
    private DatePicker fechaNacimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(this);
        Button btnAdd = findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
        Button btnDel  = findViewById(R.id.btnDel);
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete();
            }
        });

        Button btnUpdate  = findViewById(R.id.btUpdate);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });


        //formulario

        edtCedula = findViewById(R.id.edtCedula);
        edtNombre= findViewById(R.id.edtNombres);
        edtApellidos = findViewById(R.id.edtApellidos);
        rbMasculino = findViewById(R.id.rbMasculino);
        rbFemenino = findViewById(R.id.rbFemenino);

        spinnerMunicipios = findViewById(R.id.spMunicipios);
        setSpinnerMunicipios();
        spinnerProfesiones = findViewById(R.id.spProfesiones);
        setSpinnerProfesiones();

        fechaNacimiento = findViewById(R.id.fechaNacimiento);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    private  void getMunicipios(String api, VolleyCallbacksMunicipios callback){

        String url = base_url+api;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray( "municipios");
                            ArrayList<Municipios> arr =  new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);
                                Municipios m =  new Municipios();
                                m.setId(data.getString("pk_municipio_id"));
                                m.setMunicipio(data.getString("municipio"));
                                arr.add(m);
                            }
                            callback.onSuccess(arr);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }

    private  void getProfesiones(String api, VolleyCallbacksProfesiones callback){

        String url = base_url+api;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray( "profesiones");
                            ArrayList<Profesiones> arr =  new ArrayList<>();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                Profesiones m =  new Profesiones();
                                m.setId(data.getString("pk_profesion_id"));
                                m.setProfesion(data.getString("profesion"));
                                arr.add(m);
                            }
                             callback.onSuccess(arr);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(request);
    }



    public boolean addProfesional(VolleyCallbacks callback) {

        if ( edtCedula.getText().toString().equals("") ) {
            Toast.makeText(getApplicationContext(),"Cedula no puede estar vacia",Toast.LENGTH_LONG).show();
            return false;
        }
        String url = base_url+"/add";
        JSONObject obj = new JSONObject();
        try {
            obj.put("cedula",edtCedula.getText());
            obj.put("nombres",edtNombre.getText());
            obj.put("apellidos",edtApellidos.getText());
            if (rbMasculino.isChecked()){
                obj.put("sexo","m");
            }else{
                obj.put("sexo","f");
            }
            obj.put("fecha_nacimiento",edtApellidos.getText());
            obj.put("fk_municipio_id", muni.getItem(spinnerMunicipios.getSelectedItemPosition()).getId());
            obj.put("fk_profesion_id",prof.getItem(spinnerProfesiones.getSelectedItemPosition()).getId());
            obj.put("fechaNacimiento", fechaNacimiento.getYear()+"-"+(fechaNacimiento.getMonth()+1)+"-"+fechaNacimiento.getDayOfMonth());
            Log.d("addProfesional", "addProfesional : "+obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "onResponse: "+response);
                        ArrayList<String> arr =  new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray( "success");
                            Log.d("onResponse", "jsonArray: "+jsonArray);
                            JSONObject data = jsonArray.getJSONObject(0);
                            callback.onSuccess(data.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
        return true;
    }

    public void delProfesional(VolleyCallbacks callback) {

        String url = base_url+"/delete";
        JSONObject obj = new JSONObject();
        try {
            obj.put("cedula",edtCedula.getText());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "onResponse: "+response);
                        ArrayList<String> arr =  new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray( "success");
                            Log.d("onResponse", "jsonArray: "+jsonArray);
                            JSONObject data = jsonArray.getJSONObject(0);
                            callback.onSuccess(data.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }


    public boolean updateProfesional(VolleyCallbacks callback) {
        Toast.makeText(getApplicationContext(),"Cedula no puede estar vacia "+edtCedula.getText().toString().equals(""),Toast.LENGTH_LONG).show();

        String url = base_url+"/update";
        JSONObject obj = new JSONObject();
        try {
            obj.put("cedula",edtCedula.getText());
            obj.put("nombres",edtNombre.getText());
            obj.put("apellidos",edtApellidos.getText());
            if (rbMasculino.isChecked()){
                obj.put("sexo","m");
            }else{
                obj.put("sexo","f");
            }
            obj.put("fecha_nacimiento",edtApellidos.getText());
            obj.put("fk_municipio_id", muni.getItem(spinnerMunicipios.getSelectedItemPosition()).getId());
            obj.put("fk_profesion_id",prof.getItem(spinnerProfesiones.getSelectedItemPosition()).getId());
            obj.put("fechaNacimiento", fechaNacimiento.getYear()+"-"+(fechaNacimiento.getMonth()+1)+"-"+fechaNacimiento.getDayOfMonth());
            Log.d("addProfesional", "addProfesional : "+obj);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("onResponse", "onResponse: "+response);
                        ArrayList<String> arr =  new ArrayList<>();
                        try {
                            JSONArray jsonArray = response.getJSONArray( "success");
                            Log.d("onResponse", "jsonArray: "+jsonArray);
                            JSONObject data = jsonArray.getJSONObject(0);
                            callback.onSuccess(data.getString("message"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
        return true;
    }


    public ArrayAdapter<Municipios> muni = null;
    public void setSpinnerMunicipios(){
        getMunicipios("/getmunicipios",new VolleyCallbacksMunicipios(){
            @Override
            public void onSuccess(ArrayList<Municipios> result){
                muni = new ArrayAdapter<Municipios>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,result);
                spinnerMunicipios.setAdapter(muni);
                System.out.println(result); // returns the value of lastInsertId
            }

            @Override
            public void onSuccess(String txt) {
                System.out.println(txt);
            }
        });
    }
    public ArrayAdapter<Profesiones> prof = null;
    public void setSpinnerProfesiones(){
        getProfesiones("/getprofesiones",new VolleyCallbacksProfesiones(){
            @Override
            public void onSuccess(ArrayList<Profesiones> result){
                prof = new ArrayAdapter<Profesiones>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,result);
                spinnerProfesiones.setAdapter(prof);
                System.out.println(result); // returns the value of lastInsertId
            }
            @Override
            public void onSuccess(String txt) {
                System.out.println(txt);
            }
        });
    }
     public void add(){
        addProfesional( new VolleyCallbacks(){
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(String txt) {
                Toast.makeText(getApplicationContext(),txt,2500).show();
                System.out.println(txt);
            }
        });
    }
    public void delete(){
        delProfesional( new VolleyCallbacks(){
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(String txt) {
                Toast.makeText(getApplicationContext(),txt,2500).show();
                System.out.println(txt);
            }
        });
    }

    public void update(){
        updateProfesional( new VolleyCallbacks(){
            @SuppressLint("WrongConstant")
            @Override
            public void onSuccess(String txt) {
                Toast.makeText(getApplicationContext(),txt,2500).show();
                System.out.println(txt);
            }
        });
    }

    public interface VolleyCallbacksMunicipios{
        void onSuccess(ArrayList<Municipios> data);
        void onSuccess(String txt);
    }
    public interface VolleyCallbacksProfesiones{
        void onSuccess(ArrayList<Profesiones> data);
        void onSuccess(String txt);
    }
    public interface VolleyCallbacks{
        void onSuccess(String txt);
    }

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

    public class Profesiones {
        private   String id;
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
}