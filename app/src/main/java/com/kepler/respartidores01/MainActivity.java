package com.kepler.respartidores01;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebView;
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
import com.bumptech.glide.Glide;

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
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String usua, conta;
    EditText textusu, textcont;
    String usuraio, contrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textusu = findViewById(R.id.txtinUsu);
        textcont =findViewById(R.id.txtinCla);
        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();
        usuraio = preference.getString("user", null);
        contrasena = preference.getString("pass", null);

        verificacion();
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
                    urlEmpresa="http://autodis.ath.cx:9085";

                }else if (selection=="Vipla"){
                    foto.setImageResource(R.drawable.vipla);
                    urlEmpresa="";

                } else if (selection=="Jacve") {
                    foto.setImageResource(R.drawable.jacve);
                    urlEmpresa="http://jacve.dyndns.org:9085";

                } else if (selection=="Cecra") {
                    foto.setImageResource(R.drawable.cecra);
                    urlEmpresa="http://cecra.ath.cx:9085";

                } else if (selection=="Guvi") {
                    foto.setImageResource(R.drawable.guvi);
                    urlEmpresa="http://guvi.ath.cx:9085";

                }else if (selection=="Pressa"){
                    foto.setImageResource(R.drawable.pressa);
                    urlEmpresa="http://cedistabasco.ddns.net:9085";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    public void sendMessage(View view){
        usua = textusu.getText().toString();
        conta= textcont.getText().toString();
        LeerWs();
    }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        }

        private void LeerWs(){
        String url =urlEmpresa+"/loginr";

            StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);

                       if(response.length()>60) {
                           jsonObject = jsonObject.getJSONObject("UserInfo");
                           editor.putString("user", usua);
                           editor.putString("pass", conta);

                           if (jsonObject.getString("k_type").equals("REPAR")) {

                               editor.putString("name", jsonObject.getString("k_name"));
                               editor.putString("lname", jsonObject.getString("k_lname"));
                               editor.putString("type", jsonObject.getString("k_type"));
                               editor.putString("branch", jsonObject.getString("k_branch"));
                               editor.putString("email", jsonObject.getString("k_mail1"));
                               editor.putString("code", jsonObject.getString("k_kcode"));
                               editor.putString("NameBra", jsonObject.getString("k_dscr"));
                               editor.putString("Server", urlEmpresa);
                               editor.commit();

                               Intent inteto = new Intent(MainActivity.this, Principal.class);
                               startActivity(inteto);


                           } else {
                               android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MainActivity.this);
                               alerta.setMessage("Su rol no cuenta con los permisos para acceder").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                   @Override
                                   public void onClick(DialogInterface dialogInterface, int i) {
                                       dialogInterface.cancel();
                                   }
                               });

                               android.app.AlertDialog titulo = alerta.create();
                               titulo.setTitle("Acceso Denegado");
                               titulo.show();
                           }
                       }else{
                           android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MainActivity.this);
                           alerta.setMessage("Usuario y/o contraseña incorrecta").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialogInterface, int i) {
                                   dialogInterface.cancel();
                               }
                           });

                           android.app.AlertDialog titulo = alerta.create();
                           titulo.setTitle("Datos incorrectos");
                           titulo.show();
                       }


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
                    header.put("user",usua);
                    header.put("pass",conta);
                    return header;
                }
            };
            Volley .newRequestQueue(this).add(postRequest);
        }

        public void verificacion(){
        if(usuraio!=null && contrasena!=null){
            Intent inteto = new Intent(this, Principal.class);
            startActivity(inteto);

            textusu.setText(usuraio);
            textcont.setText(contrasena);
        }
    }


    }

