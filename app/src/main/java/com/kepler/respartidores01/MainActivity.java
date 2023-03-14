package com.kepler.respartidores01;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Spinner sempresas;
    private String[] lista;
    private ArrayAdapter<String> adapter;
    private ImageView foto;
    int posici;
    private boolean bandera = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner();
        fotos();

    }

    private void spinner() {
        sempresas = findViewById(R.id.spinerempresas);
        lista = new String[]{"Autodis","Vipla","Jacve", "Cecra", "Guvi", "Pressa"};
        adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lista);
        sempresas.setAdapter(adapter);

    }

    private void fotos(){

    }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }

    }

