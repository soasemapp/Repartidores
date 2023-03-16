package com.kepler.respartidores01;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ViewSwitcher;


import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Spinner sempresas;
    private String[] lista;
    private ArrayAdapter<String> adapter;
    private ImageView foto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        spinner();

    }

    private void spinner() {
        sempresas = findViewById(R.id.spinerempresas);
        foto=findViewById(R.id.imageView1);
        lista = new String[]{"Autodis","Vipla","Jacve", "Cecra", "Guvi", "Pressa"};
        adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lista);
        sempresas.setAdapter(adapter);

        sempresas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selection = (String) adapterView.getItemAtPosition(i);

                if(selection=="Autodis"){
                    foto.setImageResource(R.drawable.autodis);
                }else if (selection=="Vipla"){
                    foto.setImageResource(R.drawable.vipla);
                } else if (selection=="Jacve") {


                } else if (selection=="Cecra") {

                } else if (selection=="Guvi") {

                }else{

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void sendMessage(View view){
         sesion();
    }

    public void sesion(){
        EditText textusu = findViewById(R.id.txtinUsu);
        String usua = textusu.getText().toString();
        EditText textcont =findViewById(R.id.txtinCla);
        String conta= textcont.getText().toString();

        if(usua.equals("admin") && conta.equals("admin")){

                Intent inteto= new Intent(this, Principal.class);
                startActivity(inteto);

        }else {
            Toast tost= Toast.makeText(this, "usuaio o contraseña incorrecta", Toast.LENGTH_LONG);
            tost.show();
        }



    }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        }


    }

