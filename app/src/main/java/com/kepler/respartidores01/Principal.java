package com.kepler.respartidores01;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class Principal extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityPrincipalBinding binding;
    public EditText textfolio, textonumcajas, textocajasescaner;

    public String usaFolio;
        String numcajas="1" ;
   public ArrayList<Pedidos> lpeA=new ArrayList<>();
    TextView namerepa, correorepa;
    private SharedPreferences preference;
    ArrayList<ClienteSandG> ClientesDis = new ArrayList<>();
    private SharedPreferences.Editor editor;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode, strcorreo, struser, strbranch;

    AlertDialog.Builder builder;
    AlertDialog dialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityPrincipalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.appBarPrincipal.toolbar);
        binding.appBarPrincipal.toolbar.setTitleTextColor(Color.WHITE);

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
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.mapsActivity, R.id.cerrarsecion)
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_principal);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);

        NavigationUI.setupWithNavController(navigationView, navController);

        MenuItem menuItem = navigationView.getMenu().getItem(4);

        menuItem.setOnMenuItemClickListener(item ->{
            if (item.getItemId() == R.id.cerrarsecion) {

             editor.remove("user");
             editor.remove("pass");
             editor.commit();
             editor.apply();
                Intent inteto = new Intent(this, MainActivity.class);
                startActivity(inteto);

                return true;
            }
            return super.onOptionsItemSelected(item);
        });



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
        strname=preference.getString("name","");
        strcorreo=preference.getString("email","");
        strbranch = preference.getString("branch", "");

        struser = preference.getString("user", "");
        strpass = preference.getString("pass", "");
    }

    //ESCANEAR LOS FOLIOS
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        IntentResult result= IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if(result !=null){

            if(result.getContents()==null){
                android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(Principal.this);
                alerta.setMessage("").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                android.app.AlertDialog titulo = alerta.create();
                titulo.setTitle("Lectura Cancelada");
                titulo.show();

            }else{
                usaFolio =result.getContents();
                    LeerWs();
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
        namerepa= findViewById(R.id.txtmenuname);
        correorepa=findViewById(R.id.textViewcorreo);
        namerepa.setText(strname);
        namerepa.setTextSize(18);
        correorepa.setText(strcorreo);
        correorepa.setTextSize(15);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void respuesta(View view) {

        builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.escribirfolio, null);
        builder.setView(dialogView).setTitle("Introduce Folio y Numero de cajas")
                .setPositiveButton(R.string.signin, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        dialog = builder.create();
        dialog.show();

        textfolio = (EditText) dialogView.findViewById(R.id.cajatextfolio);
        textonumcajas=(EditText) dialogView.findViewById(R.id.cajatextonumc);


        textfolio.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                usaFolio =textfolio.getText().toString();
                numcajas=textonumcajas.getText().toString();

                if(!usaFolio.equals("")) {
                    if (usaFolio.length() < 7) {
                        int fo = usaFolio.length();
                        switch (fo) {
                            case 1:
                                usaFolio = "000000" + usaFolio;
                                break;
                            case 2:
                                usaFolio = "00000" + usaFolio;
                                break;
                            case 3:
                                usaFolio ="0000" + usaFolio;
                                break;
                            case 4:
                                usaFolio ="000" + usaFolio;
                                break;
                            case 5:
                                usaFolio ="00" + usaFolio;
                                break;
                            case 6:
                                usaFolio = "0" + usaFolio;
                                break;

                            default:
                                break;
                        }
                    }
                    textfolio.setText(usaFolio);
                }else{
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(Principal.this);
                    alerta.setMessage("").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    android.app.AlertDialog titulo = alerta.create();
                    titulo.setTitle("Ingrese el folio porfavor");
                    titulo.show();
                }

                return false;
            }
        });

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
                    String telun, teld, folio;
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.length()>0) {
                        jsonObject = jsonObject.getJSONObject("Repartidores");
                        Clave = jsonObject.getString("k_Clave");
                        folio=jsonObject.getString("k_Folio");
                        Nombre = jsonObject.getString("k_Nombre");
                        telun=jsonObject.getString("k_Numero1");
                        teld=jsonObject.getString("k_Numero2");
                        Direccion = jsonObject.getString("k_Direccion");

                        editor.putString("folioescrito",folio);
                        Clave = jsonObject.getString("k_Clave");
                        editor.putString("Nombreescrito",  Nombre);
                        editor.putString("Num1_escrito", telun);
                        editor.putString("Num2escrito",teld);
                        editor.putString("direccionescrito", Direccion );
                        editor.putString("numc",numcajas);
                        editor.commit();

                        ClientesDis.add(new ClienteSandG(Clave, Nombre, Direccion));
                        lpeA.add(new Pedidos("","", "", Nombre,telun,teld,folio,Direccion,""));

                        insertarfolioesc();

                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(Principal.this);
                        alerta.setMessage("Folio registrado con exito").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                textfolio.setText(null);
                                textonumcajas.setText(null);
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

                        usaFolio="";

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
                header.put("user", struser);
                header.put("pass", strpass);
                return header;
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap params = new HashMap();
                params.put("sucursal", strbranch);
                params.put("folio", usaFolio);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

    //boton del alert para guardar el folio escrito
  public void guardarfolio(View v){

      usaFolio =textfolio.getText().toString();
      numcajas=textonumcajas.getText().toString();

      if(!usaFolio.equals("")) {
          if (usaFolio.length() < 7) {
              int fo = usaFolio.length();
              switch (fo) {
                  case 1:
                      usaFolio = "000000" + usaFolio;
                      break;
                  case 2:
                      usaFolio = "00000" + usaFolio;
                      break;
                  case 3:
                      usaFolio = "0000" + usaFolio;
                      break;
                  case 4:
                      usaFolio = "000" + usaFolio;
                      break;
                  case 5:
                      usaFolio = "00" + usaFolio;
                      break;
                  case 6:
                      usaFolio = "0" + usaFolio;
                      break;

                  default:
                      break;
              }
          }
          textfolio.setText(usaFolio);
      }

        if(!usaFolio.equals("") && !numcajas.equals("")){
           LeerWs();

        }else{
            android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(Principal.this);
            alerta.setMessage("").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            android.app.AlertDialog titulo = alerta.create();
            titulo.setTitle("Ingrese el folio y el numero de cajas porfavor");
            titulo.show();
        }
    }

    private void insertarfolioesc(){

        String url =StrServer+"/registroR";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(response);

                     jsonObject.getString("Repartidores");

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
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("user",struser);
                header.put("pass",strpass);
                return header;
            }
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap params = new HashMap();
                params.put("sucursal",strbranch);
                params.put("folio",usaFolio);
                params.put("repartidor",strcode);
                params.put("numC",numcajas);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }

}