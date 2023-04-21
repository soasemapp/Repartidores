package com.kepler.respartidores01;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.kepler.respartidores01.databinding.ActivityPrincipalBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Principal extends AppCompatActivity implements AdapterView.OnItemClickListener{
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPrincipalBinding binding;
    public EditText textfolio;
    public ArrayList<String> folios= new ArrayList<>();
    public String usa;
   public ArrayList<Pedidos> lpeA=new ArrayList<>();
    TextView namerepa;
    private SharedPreferences preference;
    ArrayList<ClienteSandG> ClientesDis = new ArrayList<>();
    private SharedPreferences.Editor editor;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode;

    AlertDialog.Builder builder;
    AlertDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarPrincipal.toolbar);
        binding.appBarPrincipal.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.mapsActivity)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        //AgregarFolios();

        //ACCESO A ESCRIBIR O ESCANEAR EL FOLIO DESDE UN DIALOGO
        binding.appBarPrincipal.toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_scanner) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                LayoutInflater inflater = this.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog, null))
                        .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // sign in the user ...
                            }
                        }).create().show();
                return true;
            }
            return super.onOptionsItemSelected(item);
        });

        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();
        strcodBra = preference.getString("branch", null);
        strcode = preference.getString("code", null);
        StrServer = preference.getString("Server", "null");
        //namerepa=findViewById(R.id.txtmenuname);
    }

    //ESCANEAR LOS FOLIOS
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result= IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result !=null){

            if(result.getContents()==null){
                Toast.makeText(this, "Lectura Cancelada", Toast.LENGTH_SHORT).show();
            }else{
               // folios.add(result.getContents());
                Toast.makeText(this, result.getContents(), Toast.LENGTH_SHORT).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);
        return true;
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void respuesta(View view) {

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.escribirfolio, null);
        builder.setView(dialogView).setTitle("Introduce Folio")
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        dialog = builder.create();
        dialog.show();
        textfolio = (EditText) dialogView.findViewById(R.id.cajatextfolio);
    }


    public void respuestascanner(View view){

        IntentIntegrator integrador= new IntentIntegrator(Principal.this);
        integrador.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrador.setPrompt("Lector");
        integrador.setCameraId(0);
        integrador.setBeepEnabled(true);
        integrador.setBarcodeImageEnabled(true);
        integrador.initiateScan();
    }



    private void LeerWs() {
        String url = StrServer + "/consulfac";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    String Clave,Nombre,Direccion;
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.length()>0) {
                        jsonObject = jsonObject.getJSONObject("Repartidores");
                        jsonObject.getString("k_Folio");
                        Clave = jsonObject.getString("k_Clave");
                        Nombre = jsonObject.getString("k_Nombre");
                        jsonObject.getString("k_Numero1");
                        jsonObject.getString("k_Numero2");
                        Direccion = jsonObject.getString("k_Direccion");
                        ClientesDis.add(new ClienteSandG(Clave, Nombre, Direccion));
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(Principal.this);
                        alerta.setMessage("Folio registrado con exito").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        android.app.AlertDialog titulo = alerta.create();
                        titulo.setTitle("Registro realizado");
                        titulo.show();


                    }else{
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(Principal.this);
                        alerta.setMessage("No se encontro folio").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        android.app.AlertDialog titulo = alerta.create();
                        titulo.setTitle("No existe");
                        titulo.show();
                    }


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Principal.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("user", "jared");
                header.put("pass", "jared");
                return header;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap params = new HashMap();
                params.put("sucursal", strcodBra);
                params.put("folio", usa);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }
  public void guardarfolio(View v){
        usa=textfolio.getText().toString();
        if(!usa.equals("")){
           LeerWs();
            Toast.makeText(this, "Guardado", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Ingresa el folio porfavor", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}