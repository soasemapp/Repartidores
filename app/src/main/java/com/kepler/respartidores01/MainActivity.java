package com.kepler.respartidores01;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0 ;
    private String[] lista;
    private ArrayAdapter<String> adapter;
    private ImageView foto;
    String urlEmpresa;
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

        //verificacion();
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        spinner();
    }


    private void spinner() {
        sempresas = findViewById(R.id.spinerempresas);
        foto=findViewById(R.id.imageView1);
        //lista = new String[]{"Seleccionar...","AUTOTOP","TOTALCAR",/*"DEMO"*/};
        lista = new String[]{"Seleccionar...","Autodis","Vipla","Jacve", "Cecra", "Guvi", "Pressa","MIGRACION" +""};
         // lista = new String[]{"Seleccionar...","Rodatech","Partech","Shark"};

        adapter= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, lista);
        sempresas.setAdapter(adapter);

//

        sempresas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selection = (String) adapterView.getItemAtPosition(i);
                if(selection=="AUTOTOP"){
                    foto.setImageResource(R.drawable.autotop);
                    urlEmpresa="http://autotop.ath.cx:9090";

                }else if (selection=="TOTALCAR"){
                    foto.setImageResource(R.drawable.totalcar);
                    urlEmpresa="http://autotop.ath.cx:9085";

                } else if (selection=="DEMO") {
                    foto.setImageResource(R.drawable.logo);
                    urlEmpresa="http://autotop.ath.cx:9080";

                } else if(selection=="Rodatech"){
                    foto.setImageResource(R.drawable.rodatech);
                    urlEmpresa="http://sprautomotive.servehttp.com:9090";

                }else if (selection=="Partech"){
                    foto.setImageResource(R.drawable.partech);
                    urlEmpresa="http://sprautomotive.servehttp.com:9095";

                } else if (selection=="Shark") {
                    foto.setImageResource(R.drawable.shark);
                    urlEmpresa="http://sprautomotive.servehttp.com:9080";

                }
                else if(selection=="Autodis"){
                    foto.setImageResource(R.drawable.autodis);
                    urlEmpresa="http://autodis.ath.cx:9085";

                }else if (selection=="Vipla"){
                    foto.setImageResource(R.drawable.vipla);
                    urlEmpresa="http://sprautomotive.servehttp.com:9085";

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
                }else if (selection=="MIGRACION"){
                    foto.setImageResource(R.drawable.pressa);
                    urlEmpresa="http://cedistabasco.ddns.net:9080";
                }else {
                    foto.setImageResource(R.drawable.logo);
                    urlEmpresa="";
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

        if(!urlEmpresa.equals("")){
            LeerWs();
        }else{
            android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MainActivity.this);
            alerta.setMessage("No se ah seleccionado ningun Servidor porfavor seleccione alguno").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            android.app.AlertDialog titulo = alerta.create();
            titulo.setTitle("Seleccione un Servidor");
            titulo.show();
        }

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
                        var tamaño =response.length();
                       if(tamaño>71) {
                           jsonObject = jsonObject.getJSONObject("UserInfo");
                           editor.putString("user", usua);
                           editor.putString("pass", conta);

                           if (jsonObject.getString("k_type").equals("REPAR")) {


                             String Nombre = jsonObject.getString("k_name");
                             String lname= jsonObject.getString("k_lname");
                             String tipo= jsonObject.getString("k_type");
                             String branch=  jsonObject.getString("k_branch");
                             String mail=  jsonObject.getString("k_mail1");
                             String code=  jsonObject.getString("k_kcode");
                             String desc= jsonObject.getString("k_dscr");




                               editor.putString("name", jsonObject.getString("k_name"));
                               editor.putString("lname", jsonObject.getString("k_lname"));
                               editor.putString("type", jsonObject.getString("k_type"));
                               editor.putString("branch", jsonObject.getString("k_branch"));
                               editor.putString("email", jsonObject.getString("k_mail1"));
                               editor.putString("code", jsonObject.getString("k_kcode"));
                               editor.putString("NameBra", jsonObject.getString("k_dscr"));
                               editor.putString("Server", urlEmpresa);
                               editor.commit();

                               Intent inteto = new Intent(MainActivity.this, Splash.class);
                               startActivity(inteto);
                               finish();


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
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MainActivity.this);
                    alerta.setMessage(error.getMessage().toString()).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    android.app.AlertDialog titulo = alerta.create();
                    titulo.setTitle("Error");
                    titulo.show();
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

//        public void verificacion(){
//        if(usuraio!=null && contrasena!=null){
//            Intent inteto = new Intent(this, Splash.class);
//            startActivity(inteto);
//            finish();
//
//            textusu.setText(usuraio);
//            textcont.setText(contrasena);
//        }
//    }


    @Override
    protected void onStart() {
        super.onStart();
        if (preference.contains("user") && preference.contains("pass")) {
            Intent perfil = new Intent(this, Splash.class);
            startActivity(perfil);
            finish();
        }

    }

    }

