package com.kepler.respartidores01;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public static class spinner extends AppCompatActivity {
        @Override
        protected void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            Spinner sempresas= findViewById(R.id.spiner1);
            ArrayList<String> lista = new ArrayList<String>();
            lista.add("Autodis");
            lista.add("Vipla");
            lista.add("Cecra");

           ArrayAdapter<String> spinerArray = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, lista);
           spinerArray.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
           sempresas.setAdapter(spinerArray);



        }
    }
}

