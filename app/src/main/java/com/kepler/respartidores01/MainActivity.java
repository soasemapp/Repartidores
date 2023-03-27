package com.kepler.respartidores01;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;



import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private Spinner sempresas;
    private String[] lista;
    private ArrayAdapter<String> adapter;
    private ImageView foto;
    String Usuario,Contraseña,Nombre,Apellidos,urlEmpresa;

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
                    urlEmpresa="autodis.ath.cx";

                }else if (selection=="Vipla"){
                    foto.setImageResource(R.drawable.vipla);
                    urlEmpresa="";

                } else if (selection=="Jacve") {
                    foto.setImageResource(R.drawable.jacve);
                    urlEmpresa="jacve.dyndns.org:9085";

                } else if (selection=="Cecra") {
                    foto.setImageResource(R.drawable.cecra);
                    urlEmpresa="cecra.ath.cx:3380";

                } else if (selection=="Guvi") {
                    foto.setImageResource(R.drawable.guvi);
                    urlEmpresa="jacve.dyndns.org:9085";

                }else if (selection=="Pressa"){
                    foto.setImageResource(R.drawable.pressa);
                    urlEmpresa="cedistabasco.ddns.net";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void sendMessage(View view){
        //LeerWs();
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
            Toast tost= Toast.makeText(this, "Datos incorrectos", Toast.LENGTH_LONG);
            tost.show();
        }
    }
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        }

        private void LeerWs(){
        String url =urlEmpresa;

            StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                        jsonObject.getString("");
                        Toast.makeText(MainActivity.this, response, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap header = new HashMap();
                    header.put("user","jared");
                    header.put("pass","jared");
                    return header;
                }
            };
            Volley .newRequestQueue(this).add(postRequest);
        }



    }

