package com.kepler.respartidores01;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.kepler.respartidores01.databinding.ActivityMapsSoloBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsSolo extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;
    private GoogleMap mMap2;
    private Marker mMarker;
    private Marker mMarkerepartidor;
    JSONObject routes;
    AlertDialog dialog = null;
    String tiempo = "";
    JSONObject legs;
    JSONObject distance;
    int tiempoval = 0;
    JSONObject duration;
    Geocoder coddirmap;
    String value = "", text = "";
    List<Address> address = new ArrayList<>();
    String nombrequienrecibio = "", comentarioentre;
    double Latitud = 0, Longitud = 0;
    private SharedPreferences preference;
    String mensajes;
    private SharedPreferences.Editor editor;
    AlertDialog.Builder builder;
    ArrayList<String> values = new ArrayList<>();
    private ActivityMapsSoloBinding binding;
    String  strpass, StrServer, strcodBra, strcode, struser, strbranch, strname, strlname;
    String direclis = "", nombre_direccion = "", clave_cliente, clave_direccion, folioparaentregar, Telefono = "";
    double latitudclien, longitudclien;
    String estatus, comentariog;
    ImageButton bntwaze;

    TextView Vendedortxt, Clientetxt, Direcciontxt;
    int contador = 0;
    int contador2 = 0;
    Polyline line;
    String Empresa, smsCamino;
    AlertDialog.Builder builder6;
    AlertDialog dialog6 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        preference = this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        strcodBra = preference.getString("codBra", "null");
        StrServer = preference.getString("Server", "null");
        struser = preference.getString("user", "");
        strpass = preference.getString("pass", "");
        strname = preference.getString("name", "null");
        strlname = preference.getString("lname", "null");
        strbranch = preference.getString("branch", "");
        strcode = preference.getString("code", "");
        bntwaze = findViewById(R.id.bntwaze);

        switch (StrServer) {
            case "http://jacve.dyndns.org:9085":
                Empresa = "JACVE";
                break;
            case "http://sprautomotive.servehttp.com:9085":
                Empresa = "VIPLA";
                break;
            case "http://cecra.ath.cx:9085":
                Empresa = "CECRA";
                break;
            case "http://guvi.ath.cx:9085":
                Empresa = "GUVI";
                break;
            case "http://cedistabasco.ddns.net:9085":
                Empresa = "PRESSA";
                break;
            case "http://autodis.ath.cx:9085":
                Empresa = "AUTODIS";
                break;

            case "http://sprautomotive.servehttp.com:9090":
                Empresa = "RODATECH";
                break;
            case "http://sprautomotive.servehttp.com:9095":
                Empresa = "PARTECH";
                break;
            case "http://sprautomotive.servehttp.com:9080":
                Empresa = "TG";
                break;
            case "http://autotop.ath.cx:9090":
                Empresa = "AUTOTOP";
                break;
            case "http://autotop.ath.cx:9085":
                Empresa = "TOTALCAR";
                break;
            case "http://autotop.ath.cx:9080":
                Empresa = "PRUEBA";
                break;
            default:
                break;
        }


        binding = ActivityMapsSoloBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapasolo);
        mapFragment.getMapAsync(this);

        Bundle parametros = this.getIntent().getExtras();

        direclis = parametros.getString("directlista");
        nombre_direccion = parametros.getString("nombre_direccion");
        clave_cliente = parametros.getString("clave_cliente");
        latitudclien = parametros.getDouble("latitud");
        longitudclien = parametros.getDouble("longitud");
        clave_direccion = parametros.getString("clave_direccion");
        folioparaentregar = parametros.getString("folios");
        Telefono = parametros.getString("Telefono");


        Vendedortxt = findViewById(R.id.txtVendedor);
        Clientetxt = findViewById(R.id.txtCliente);
        Direcciontxt = findViewById(R.id.txtDireccion);


        builder6 = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pantallacargacarrito, null);
        builder6.setView(dialogView);
        dialog6 = builder6.create();
        builder6.setCancelable(false);
        dialog6.show();


    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        if (ActivityCompat.checkSelfPermission(MapsSolo.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsSolo.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(MapsSolo.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {

            mMap = googleMap;
            mMap2 = googleMap;

            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);


            LocationManager locationManager = (LocationManager) MapsSolo.this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (getApplicationContext() != null) {

                        LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                        Latitud = location.getLatitude();
                        Longitud = location.getLongitude();

                        if (mMarkerepartidor != null) {
                            mMarkerepartidor.remove();
                        }

                        if (contador == 0) {
                            mMarkerepartidor = mMap.addMarker(new MarkerOptions().position(miUbicacion).title(strname + strlname).icon(BitmapDescriptorFactory.fromResource(R.drawable.repartidor2)));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(miUbicacion)
                                    .zoom(17)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            contador++;
                        } else {
                            mMarkerepartidor = mMap.addMarker(new MarkerOptions().position(miUbicacion).title(strname + strlname).icon(BitmapDescriptorFactory.fromResource(R.drawable.repartidor2)));

                        }

                        if (latitudclien != 0.0 && longitudclien != 0.0) {
                            LatLng cliente = new LatLng(Double.valueOf(latitudclien), Double.valueOf(longitudclien));
                            mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(nombre_direccion).icon(BitmapDescriptorFactory.fromResource(R.drawable.local2)));


                            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Latitud + "," + Longitud + "&destination=" + latitudclien + "," + longitudclien + "&key=AIzaSyC6u34cq2ZCBGyTicAa__hScUwcN01zVXE";
                            RequestQueue queue = Volley.newRequestQueue(MapsSolo.this);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jso = new JSONObject(response);
                                        trazarRuta(jso);

                                        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Latitud + "," + Longitud + "&destination=" + latitudclien + "," + longitudclien + "&key=AIzaSyC6u34cq2ZCBGyTicAa__hScUwcN01zVXE";
                                        ingresarDatos(url);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            });
                            queue.add(stringRequest);

                        } else {
                            coddirmap = new Geocoder(MapsSolo.this);
                            Double dircelisLAT;
                            Double dircelisLONG;
                            try {
                                coddirmap.getFromLocationName(direclis, 2);
                                address = coddirmap.getFromLocationName(direclis, 2);
                                Address direx;
                                direx = address.get(0);
                                dircelisLAT = direx.getLatitude();
                                dircelisLONG = direx.getLongitude();
                                LatLng cliente = new LatLng(dircelisLAT, dircelisLONG);
                                mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(nombre_direccion).icon(BitmapDescriptorFactory.fromResource(R.drawable.local2)));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Latitud + "," + Longitud + "&destination=" + dircelisLAT + "," + dircelisLONG + "&key=AIzaSyC6u34cq2ZCBGyTicAa__hScUwcN01zVXE";
                            RequestQueue queue = Volley.newRequestQueue(MapsSolo.this);
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        JSONObject jso = new JSONObject(response);
                                        trazarRuta(jso);

                                        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Latitud + "," + Longitud + "&destination=" + dircelisLAT + "," + dircelisLONG + "&key=AIzaSyC6u34cq2ZCBGyTicAa__hScUwcN01zVXE";
                                        ingresarDatos(url);

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }


                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError volleyError) {

                                }
                            });
                            queue.add(stringRequest);
                        }


                    }

                }

            };


            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        }

    }

    public void entregadoalone(View v) {
        builder = new AlertDialog.Builder(this);
        LayoutInflater inflaterentrega = getLayoutInflater();
        View dialogViewww = inflaterentrega.inflate(R.layout.recibio_, null);
        builder.setView(dialogViewww);
        EditText recibio = dialogViewww.findViewById(R.id.quien_recibio);
        EditText comentario = dialogViewww.findViewById(R.id.id_comentario);
        Button enttegar = dialogViewww.findViewById(R.id.btentre);
        enttegar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estatus = "E";


              //  if (tiempoval > 10) {
                    if (!recibio.getText().toString().equals("") && !comentario.getText().toString().equals("")) {

                        editor.putString("recibio", recibio.getText().toString());
                        editor.putString("comentario", comentario.getText().toString());
                        editor.commit();
                        editor.apply();

                        actualizarfirma();


                    } else {
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsSolo.this);
                        alerta.setMessage("Escriba quien recibio y un comentario porfavor").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        android.app.AlertDialog titulo = alerta.create();
                        titulo.setTitle("Faltan casillas por rellenar");
                        titulo.show();
                    }


              /*  }else{
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsSolo .this);
                    alerta.setMessage("Estas muy lejos de la zona de entrega").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    android.app.AlertDialog titulo = alerta.create();
                    titulo.setTitle("¡Estas muy lejos!");
                    titulo.show();
                }*/

            }
        });

        dialog = builder.create();
        dialog.show();

    }


    private void actualizarfirma() {
        nombrequienrecibio = preference.getString("recibio", "");
        comentarioentre = preference.getString("comentario", "");
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String url = StrServer + "/recibeR";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(response);

                    mensajes = jsonObject.getString("Repartidores");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                android.app.AlertDialog.Builder alerta = new AlertDialog.Builder(MapsSolo.this);
                alerta.setMessage(mensajes + " con folio: " + folioparaentregar).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                        if (!Telefono.equals("")) {
                            smsCamino = Empresa + " agradece su preferencia.\n" +
                                    "" + nombre_direccion + ", Su pedido con el folio " + folioparaentregar + " ha sido entregado por el Repartidor " + strname + " " + strlname + " a " + nombrequienrecibio + ". \n" +
                                    "Le deseamos un excelente día.";

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(Telefono, null, smsCamino, null, null);
                        }

/*
                        if (latitudclien == 0.0 && longitudclien == 0.0) {


                            android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getApplicationContext());
                            alerta = new android.app.AlertDialog.Builder(getApplicationContext());
                            alerta.setMessage("¿Deseas entregar este pedido?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    saveLoca();

                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();

                                }
                            });
                            android.app.AlertDialog titulo = alerta.create();
                            titulo = alerta.create();
                            titulo.setTitle("¿Realizar la entrega?");
                            titulo.show();





                        } else {*/
                            Intent regresar = new Intent(MapsSolo.this, Principal.class);
                            startActivity(regresar);
                            finish();
                        //}


                    }
                });

                android.app.AlertDialog titulo = alerta.create();
                titulo.setTitle("");
                titulo.show();

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsSolo.this);
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
                params.put("folio", folioparaentregar);
                params.put("recibe", nombrequienrecibio);
                params.put("status", estatus);
                params.put("comentario", comentarioentre);
                params.put("hora", currentTime);
                return params;
            }
        };
        Volley.newRequestQueue(MapsSolo.this).add(postRequest);
    }

    public void ingresarDatos(String url) {
        StringRequest stringRequesttt = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jso = new JSONObject(response);
                    routes = jso.getJSONArray("routes").getJSONObject(0);
                    legs = routes.getJSONArray("legs").getJSONObject(0);
                    distance = legs.getJSONObject("distance");
                    value = distance.getString("value");


                    duration = legs.getJSONObject("duration");
                    tiempo = duration.getString("text");
                    tiempoval = duration.getInt("value");

                    routes = jso.getJSONArray("routes").getJSONObject(0);
                    legs = routes.getJSONArray("legs").getJSONObject(0);
                    distance = legs.getJSONObject("distance");
                    text = distance.getString("text");

                    Clientetxt.setText(tiempo + " (" + text + ")");
                    Vendedortxt.setText(nombre_direccion);
                    Direcciontxt.setText(direclis);

                    if (contador2 == 0) {
                        if (Integer.parseInt(value) < 10) {
                            android.app.AlertDialog.Builder alerta = new AlertDialog.Builder(MapsSolo.this);
                            alerta.setMessage("Estas llegando a la ubicacion de tu cliente este pendiente").setCancelable(false).setIcon(R.drawable.icon_advertencia).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    contador2++;

                                }
                            });

                            android.app.AlertDialog titulo = alerta.create();
                            titulo.setTitle("Aviso");
                            titulo.show();
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog6.dismiss();
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsSolo.this);
                alerta.setMessage(volleyError.getMessage().toString()).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                android.app.AlertDialog titulo = alerta.create();
                titulo.setTitle("Error");
                titulo.show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequesttt);
    }

    public void saveLoca() {

        String url = StrServer + "/asignarcor";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    JSONObject jsonObject = new JSONObject(response);


                    Intent regresar = new Intent(MapsSolo.this, Principal.class);
                    startActivity(regresar);
                    finish();


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsSolo.this);
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
                params.put("cliente", clave_cliente);
                params.put("direccion", clave_direccion);
                params.put("latitud", String.valueOf(Latitud));
                params.put("longitud", String.valueOf(Longitud));


                return params;
            }
        };
        Volley.newRequestQueue(MapsSolo.this).add(postRequest);

    }


    private void trazarRuta(JSONObject jso) {

        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {
            jRoutes = jso.getJSONArray("routes");
            for (int i = 0; i < jRoutes.length(); i++) {

                jLegs = ((JSONObject) (jRoutes.get(i))).getJSONArray("legs");

                for (int j = 0; j < jLegs.length(); j++) {

                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    for (int k = 0; k < jSteps.length(); k++) {


                        String polyline = "" + ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        Log.i("end", "" + polyline);
                        List<LatLng> list = PolyUtil.decode(polyline);
                        //

//Add line to map
                        line = mMap2.addPolyline(new PolylineOptions()
                                .addAll(list)
                                .width(6)
                                .color(Color.RED));


//Remove the same line from map


                    }


                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void actualizarfirma2() {


        String url = StrServer + "/recibeR";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    mensajes = jsonObject.getString("Repartidores");

                    Intent regresa = new Intent(MapsSolo.this, Principal.class);
                    startActivity(regresa);
                    finish();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsSolo.this);
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
                params.put("folio", folioparaentregar);
                params.put("recibe", nombrequienrecibio);
                params.put("status", estatus);
                params.put("comentario", comentarioentre);
                return params;
            }
        };
        Volley.newRequestQueue(MapsSolo.this).add(postRequest);
    }




    public void pendientemaps(View v) {
        comentariog = null;
        estatus = null;

        builder = new AlertDialog.Builder(MapsSolo.this);
        LayoutInflater inflatependi = getLayoutInflater();
        View dialogVie = inflatependi.inflate(R.layout.diseno_pendiente, null);
        builder.setView(dialogVie);

        EditText compendiente = dialogVie.findViewById(R.id.motivo_pendiente);
        Button pendiente = dialogVie.findViewById(R.id.btn_pendientemot);

        pendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                estatus = "P";
                compendiente.getText().toString();

                if (!compendiente.getText().toString().equals("")) {

                    comentarioentre = compendiente.getText().toString();

                    actualizarfirma2();
                    dialog.dismiss();
                } else {
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsSolo.this);
                    alerta.setMessage("Escriba un comentario porfavor").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    android.app.AlertDialog titulo = alerta.create();
                    titulo.setTitle("Faltan casillas por rellenar");
                    titulo.show();
                }

            }
        });

        dialog = builder.create();
        dialog.show();

    }
}