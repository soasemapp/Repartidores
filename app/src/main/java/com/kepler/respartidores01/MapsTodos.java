package com.kepler.respartidores01;

import androidx.annotation.NonNull;
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
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.kepler.respartidores01.databinding.ActivityMapsTodosBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsTodos extends FragmentActivity implements OnMapReadyCallback {
    double Latitud = 0, Longitud = 0;
    private GoogleMap mMap;
    private Marker mMarker;
    public ArrayList<Pedidos> Pedidos = new ArrayList<>();
    private Marker mMarkerepartidor;
    private ActivityMapsTodosBinding binding;
    Geocoder coder;
    int contador = 0;
    String mensajes;
    String direclis = "", nombre_direccion = "", clave_cliente, clave_direccion, folioparaentregar;
    int pos1 = 0;
    AlertDialog.Builder builder;
    private SharedPreferences preference;
    List<Address> address = new ArrayList<>();
    private SharedPreferences.Editor editor;
    String  strpass, StrServer, strcodBra, strcode, struser, strbranch, strname, strlname;
    JSONObject routes;
    AlertDialog dialog = null;
    String tiempo = "";
    int tiempoval = 0;
    JSONObject legs;
    JSONObject distance;
    JSONObject duration;
    int value = 0, ContadorAnu = 0;
    Double latcrta = 0.0;
    Double longcort = 0.0;

    TextView Vendedortxt, Clientetxt, Direcciontxt;
    String estatus, comentariog;
    String text;
    int contadoringre = 0;
    String nombrequienrecibio = "", comentarioentre;
    LatLng miUbicacion;
    AlertDialog mDialog;
    ImageButton bntwaze;

    String[] listaDato = null;
    AlertDialog.Builder builder6;
    AlertDialog dialog6 = null;
    String Empresa, smsCamino;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsTodosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
        Vendedortxt = findViewById(R.id.txtVendedor);
        Clientetxt = findViewById(R.id.txtCliente);
        Direcciontxt = findViewById(R.id.txtDireccion);
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

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.maptodos);
        mapFragment.getMapAsync(this);


        builder6 = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pantallacargacarrito, null);
        builder6.setView(dialogView);
        dialog6 = builder6.create();
        builder6.setCancelable(false);
        dialog6.show();


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {

                String nombre = marker.getTitle();
                Toast.makeText(MapsTodos.this, marker.getTitle(), Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        if (ActivityCompat.checkSelfPermission(MapsTodos.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsTodos.this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(MapsTodos.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            mMap.setMyLocationEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);

            LocationManager locationManager = (LocationManager) MapsTodos.this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                    if (getApplicationContext() != null) {
                        miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                        Latitud = location.getLatitude();
                        Longitud = location.getLongitude();

                        if (mMarkerepartidor != null) {
                            mMarkerepartidor.remove();
                        }

                        if (contador == 0) {
                            mMarkerepartidor = mMap.addMarker(new MarkerOptions().position(miUbicacion).title(strname + strlname).icon(BitmapDescriptorFactory.fromResource(R.drawable.repartidor2)));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(miUbicacion).zoom(17).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            Inicio();


                            contador++;
                        } else {
                            mMarkerepartidor = mMap.addMarker(new MarkerOptions().position(miUbicacion).title(strname + strlname).icon(BitmapDescriptorFactory.fromResource(R.drawable.repartidor2)));

                            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Latitud + "," + Longitud + "&destination=" + latcrta + "," + longcort + "&key=AIzaSyC6u34cq2ZCBGyTicAa__hScUwcN01zVXE";
                            ingresarDatos2(url);


                            if (value > 5) {
                                if (ContadorAnu == 0) {
                                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsTodos.this);
                                    alerta.setMessage("Estas llegando a tu destino estate atento a tu alrededor").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                    android.app.AlertDialog titulo = alerta.create();
                                    titulo.setTitle("!Alerta!");
                                    titulo.show();
                                }

                            }


                        }

                    }
                }

            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 3000, locationListener);
        }

    }

    public void direcciones() {


        coder = new Geocoder(MapsTodos.this);
        for (int i = 0; i < Pedidos.size(); i++) {
            try {
                if (Pedidos.get(i).getLatitud() == 0.0 && Pedidos.get(i).getLongitud() == 0.0) {
                    address = coder.getFromLocationName(Pedidos.get(i).getDireccion(), 1);
                    Address resultadoscode = address.get(0);
                    resultadoscode.getLatitude();
                    resultadoscode.getLongitude();
                    Pedidos.get(i).setLatitud(resultadoscode.getLatitude());
                    Pedidos.get(i).setLongitud(resultadoscode.getLongitude());


                    LatLng cliente = new LatLng(Pedidos.get(i).getLatitud(), Pedidos.get(i).getLongitud());
                    if (Pedidos.get(i).getStatus().equals("P")) {
                        mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(Pedidos.get(i).getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconrojo)));
                    } else {
                        mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(Pedidos.get(i).getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconsazul)));
                    }

                } else {
                    Pedidos.get(i).getLatitud();
                    Pedidos.get(i).getLongitud();

                    LatLng cliente = new LatLng(Pedidos.get(i).getLatitud(), Pedidos.get(i).getLongitud());
                    if (Pedidos.get(i).getStatus().equals("P")) {
                        mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(Pedidos.get(i).getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconrojo)));
                    } else {
                        mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(Pedidos.get(i).getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconsazul)));

                    }

                }


            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                for (int i = 0; i < Pedidos.size(); i++) {
                    if (marker.getTitle().equals(Pedidos.get(i).getNombre()) && !Pedidos.get(i).getStatus().equals("A")) {
                        AlertDialog.Builder alerta = new AlertDialog.Builder(MapsTodos.this);
                        alerta.setMessage("La siguiente entrega esta pendiente por entregar: \n" +
                                "Folio: " + Pedidos.get(i).getFolio() + "\n" +
                                "Nombre: " + Pedidos.get(i).getNombre() + "\n" +
                                "Direccion: " + Pedidos.get(i).getDireccion() + ".\n¿Deseas seleccionar este pedido para entregar?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Pedidos.get(pos1).getAviso().equals("N") || Pedidos.get(pos1).getAviso().equals("")) {
                                    if (!Pedidos.get(pos1).getTelefonodos().equals("")) {
                                        smsCamino = "En " + Empresa + " trabajamos para brindarle un mejor servicio y agradecemos su compra con el Folio " + Pedidos.get(pos1).getFolio() + " va en camino por el Repartidor " + strname + " " + strlname + ".";

                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(Pedidos.get(pos1).getTelefonodos(), null, smsCamino, null, null);
                                        Aviso();
                                    }
                                }

                                Inicio2(marker);
                                dialogInterface.cancel();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Estas por cambiar la entrega a:");
                        titulo.show();
                    } else if (marker.getTitle().equals(Pedidos.get(i).getNombre()) && Pedidos.get(i).getStatus().equals("A")) {

                        AlertDialog.Builder alerta = new AlertDialog.Builder(MapsTodos.this);
                        alerta.setMessage("Folio: " + Pedidos.get(i).getFolio() + "\n" +
                                "Nombre: " + Pedidos.get(i).getNombre() + "\n" +
                                "Direccion: " + Pedidos.get(i).getDireccion() + ".\n ¿Deseas seleccionar este pedido para entregar? ").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (Pedidos.get(pos1).getAviso().equals("N") || Pedidos.get(pos1).getAviso().equals("")) {
                                    if (!Pedidos.get(pos1).getTelefonodos().equals("")) {
                                        smsCamino = "En " + Empresa + " trabajamos para brindarle un mejor servicio y agradecemos su compra con el Folio " + Pedidos.get(pos1).getFolio() + " va en camino por el Repartidor " + strname + " " + strlname + ".";

                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(Pedidos.get(pos1).getTelefonodos(), null, smsCamino, null, null);
                                        Aviso();
                                    }
                                }
                                Inicio2(marker);
                                dialogInterface.cancel();
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                        AlertDialog titulo = alerta.create();
                        titulo.setTitle("Estas por cambiar la entrega");
                        titulo.show();
                    }
                }


                return false;
            }
        });
        obtdistanc();
    }


    public void direcciones2(Marker marker) {


        for (int i = 0; i < Pedidos.size(); i++) {
            if (marker.getTitle().equals(Pedidos.get(i).getNombre())) {
                latcrta = Pedidos.get(i).getLatitud();
                longcort = Pedidos.get(i).getLongitud();
                tiempo = Pedidos.get(i).getTiempoValor();
                text = Pedidos.get(i).getDistanciaText();
                nombre_direccion = Pedidos.get(i).getNombre();
                direclis = Pedidos.get(i).getDireccion();
                folioparaentregar = Pedidos.get(i).getFolio();
                clave_direccion = Pedidos.get(i).getDireccionclave();
                clave_cliente = Pedidos.get(i).getCliente();
                pos1 = i;
                mMap.clear();
                mMarkerepartidor = mMap.addMarker(new MarkerOptions().position(miUbicacion).title(strname + strlname).icon(BitmapDescriptorFactory.fromResource(R.drawable.repartidor2)));


                coder = new Geocoder(MapsTodos.this);
                for (i = 0; i < Pedidos.size(); i++) {
                    try {
                        if (Pedidos.get(i).getLatitud() == 0.0 && Pedidos.get(i).getLongitud() == 0.0) {
                            address = coder.getFromLocationName(Pedidos.get(i).getDireccion(), 1);
                            Address resultadoscode = address.get(0);
                            resultadoscode.getLatitude();
                            resultadoscode.getLongitude();
                            Pedidos.get(i).setLatitud(resultadoscode.getLatitude());
                            Pedidos.get(i).setLongitud(resultadoscode.getLongitude());


                            LatLng cliente = new LatLng(Pedidos.get(i).getLatitud(), Pedidos.get(i).getLongitud());
                            if (Pedidos.get(i).getStatus().equals("P")) {
                                mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(Pedidos.get(i).getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconrojo)));
                            } else {
                                mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(Pedidos.get(i).getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconsazul)));
                            }

                        } else {
                            Pedidos.get(i).getLatitud();
                            Pedidos.get(i).getLongitud();

                            LatLng cliente = new LatLng(Pedidos.get(i).getLatitud(), Pedidos.get(i).getLongitud());
                            if (Pedidos.get(i).getStatus().equals("P")) {
                                mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(Pedidos.get(i).getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconrojo)));
                            } else {
                                mMarker = mMap.addMarker(new MarkerOptions().position(cliente).title(Pedidos.get(i).getNombre()).icon(BitmapDescriptorFactory.fromResource(R.drawable.iconsazul)));
                            }

                        }


                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }


                String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Latitud + "," + Longitud + "&destination=" + latcrta + "," + longcort + "&key=AIzaSyC6u34cq2ZCBGyTicAa__hScUwcN01zVXE";

                RequestQueue queue = Volley.newRequestQueue(MapsTodos.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jso = new JSONObject(response);
                            trazarRuta(jso);
                            AlertDialog.Builder alerta = new AlertDialog.Builder(MapsTodos.this);
                            alerta.setMessage("Dirigite con " + nombre_direccion + "" +
                                    "\nDireccion" + direclis + "\nTiempo aproximado:" + tiempo + " (" + text + ")").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            AlertDialog titulo = alerta.create();
                            titulo.setTitle("CAMBIO TU DESTINO");
                            titulo.show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsTodos.this);
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();
                    }
                });
                queue.add(stringRequest);


            }
        }
    }


    public void obtdistanc() {


        for (int i = 0; i < Pedidos.size(); i++) {

            Double LatitudClien, LogitudClient;


            LatitudClien = Pedidos.get(i).getLatitud();
            LogitudClient = Pedidos.get(i).getLongitud();

            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Latitud + "," + Longitud + "&destination=" + LatitudClien + "," + LogitudClient + "&key=AIzaSyC6u34cq2ZCBGyTicAa__hScUwcN01zVXE";
            ingresarDatos(i, url);
        }


    }

    public void ingresarDatos(int i, String url) {

        StringRequest stringRequesttt = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jso = new JSONObject(response);
                    routes = jso.getJSONArray("routes").getJSONObject(0);
                    legs = routes.getJSONArray("legs").getJSONObject(0);
                    distance = legs.getJSONObject("distance");
                    value = distance.getInt("value");
                    text = distance.getString("text");
                    Pedidos.get(i).setDistancia(value);
                    Pedidos.get(i).setDistanciaText(text);

                    duration = legs.getJSONObject("duration");
                    tiempo = duration.getString("text");
                    tiempoval = duration.getInt("value");

                    Pedidos.get(i).setTiempo(tiempoval);
                    Pedidos.get(i).setTiempoValor(tiempo);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                contadoringre++;
                if (contadoringre == Pedidos.size()) {
                    contadoringre = 0;
                    funcion();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsTodos.this);
                alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                AlertDialog titulo1 = alerta1.create();
                titulo1.setTitle("Error");
                titulo1.show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequesttt);

    }


    public void ingresarDatos2(String url) {

        StringRequest stringRequesttt = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {


                    JSONObject jso = new JSONObject(response);
                    routes = jso.getJSONArray("routes").getJSONObject(0);
                    legs = routes.getJSONArray("legs").getJSONObject(0);
                    distance = legs.getJSONObject("distance");
                    value = distance.getInt("value");
                    text = distance.getString("text");


                    duration = legs.getJSONObject("duration");
                    tiempo = duration.getString("text");
                    tiempoval = duration.getInt("value");

                    Clientetxt.setText(tiempo + " (" + text + ")");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsTodos.this);
                alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                AlertDialog titulo1 = alerta1.create();
                titulo1.setTitle("Error");
                titulo1.show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequesttt);

    }


    public void funcion() {

        int Valor = Pedidos.get(0).getDistancia();
        latcrta = Pedidos.get(0).getLatitud();
        longcort = Pedidos.get(0).getLongitud();
        tiempo = Pedidos.get(0).getTiempoValor();
        text = Pedidos.get(0).getDistanciaText();
        nombre_direccion = Pedidos.get(0).getNombre();
        direclis = Pedidos.get(0).getDireccion();
        folioparaentregar = Pedidos.get(0).getFolio();
        clave_direccion = Pedidos.get(0).getDireccionclave();
        clave_cliente = Pedidos.get(0).getCliente();
        pos1 = 0;

        for (int i = 1; i < Pedidos.size(); i++) {

            if (Pedidos.get(i).getDistancia() < Valor && !Pedidos.get(i).getStatus().equals("P")) {
                Valor = Pedidos.get(i).getDistancia();
                latcrta = Pedidos.get(i).getLatitud();
                longcort = Pedidos.get(i).getLongitud();
                tiempo = Pedidos.get(i).getTiempoValor();
                text = Pedidos.get(i).getDistanciaText();
                nombre_direccion = Pedidos.get(i).getNombre();
                direclis = Pedidos.get(i).getDireccion();
                folioparaentregar = Pedidos.get(i).getFolio();
                clave_direccion = Pedidos.get(i).getDireccionclave();
                clave_cliente = Pedidos.get(i).getCliente();
                pos1 = i;
            }
        }


        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + Latitud + "," + Longitud + "&destination=" + latcrta + "," + longcort + "&key=AIzaSyC6u34cq2ZCBGyTicAa__hScUwcN01zVXE";

        RequestQueue queue = Volley.newRequestQueue(MapsTodos.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jso = new JSONObject(response);
                    trazarRuta(jso);


                    if (Pedidos.get(pos1).getAviso().equals("N") || Pedidos.get(pos1).getAviso().equals("")) {
                        if (!Pedidos.get(pos1).getTelefonodos().equals("")) {
                            smsCamino = "En " + Empresa + " trabajamos para brindarle un mejor servicio y agradecemos su compra con el Folio " + Pedidos.get(pos1).getFolio() + " va en camino por el Repartidor " + strname + " " + strlname + ".";

                            SmsManager smsManager = SmsManager.getDefault();
                            smsManager.sendTextMessage(Pedidos.get(pos1).getTelefonodos(), null, smsCamino, null, null);
                            Aviso();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsTodos.this);
                alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                AlertDialog titulo1 = alerta1.create();
                titulo1.setTitle("Error");
                titulo1.show();
            }
        });
        queue.add(stringRequest);

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
                        mMap.addPolyline(new PolylineOptions().addAll(list).width(6).color(Color.RED));

//Remove the same line from map

                    }


                }


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        Clientetxt.setText(tiempo + " (" + text + ")");
        Vendedortxt.setText(nombre_direccion);
        Direcciontxt.setText(direclis);
        dialog6.dismiss();

    }


    public void Inicio() {


        String url = StrServer + "/consulxEn";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    String Nombre, telun, teld, folio, direccion, sucu, cliente, numpaq, comentario, status, direccionclave, Aviso,Hora,Minutos,PedidosHora;
                    Double latitud, longitud;
                    JSONObject jsonObject = new JSONObject(response);
                    int json = response.length();
                    if (json != 6) {
                        jfacturas = jsonObject.getJSONObject("Repartidores");
                        for (int i = 0; i < jfacturas.length(); i++) {
                            jitems = jfacturas.getJSONObject("items" + i);
                            sucu = jitems.getString("k_Sucursal");
                            folio = jitems.getString("k_Folio");
                            cliente = jitems.getString("k_Cliente");
                            Nombre = jitems.getString("k_Nombre");
                            numpaq = jitems.getString("k_nPaquetes");
                            direccion = jitems.getString("k_Direccion");
                            telun = jitems.getString("k_Telefono1");
                            teld = jitems.getString("k_Telefono2");
                            comentario = jitems.getString("k_Comentario");
                            status = jitems.getString("k_Status");
                            direccionclave = jitems.getString("k_direccionCla");
                            latitud = jitems.getDouble("k_Latitud");
                            longitud = jitems.getDouble("k_Longitud");
                            Aviso = jitems.getString("k_Aviso");
                            Hora = jitems.getString("k_Horas");
                            Minutos = jitems.getString("k_Minutos");
                            PedidosHora = jitems.getString("k_Pedido" );
                            Pedidos.add(new Pedidos(sucu, cliente, numpaq, Nombre, telun, teld, folio, direccion, comentario, status, direccionclave, latitud, longitud, 0, "", 0, "", Aviso,Hora,Minutos,PedidosHora));

                        }
                        direcciones();
                    } else {
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsTodos.this);
                        alerta.setMessage("No hay entregas pendientes por realizar").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                                Intent regresa = new Intent(MapsTodos.this, Principal.class);
                                startActivity(regresa);
                                finish();

                            }
                        });

                        android.app.AlertDialog titulo = alerta.create();
                        titulo.setTitle("Entregas");
                        titulo.show();
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsTodos.this);
                alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                AlertDialog titulo1 = alerta1.create();
                titulo1.setTitle("Error");
                titulo1.show();
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
                params.put("id_repartidor", strcode);
                return params;
            }
        };
        Volley.newRequestQueue(MapsTodos.this).add(postRequest);
    }


    public void Inicio2(Marker marker) {
        direcciones2(marker);

    }






    public void entregadoevery(View v) {
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

               // if(tiempoval>10){
                if (!recibio.getText().toString().equals("") && !comentario.getText().toString().equals("")) {

                    editor.putString("recibio", recibio.getText().toString());
                    editor.putString("comentario", comentario.getText().toString());
                    editor.commit();
                    editor.apply();

                    actualizarfirma();


                } else {
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsTodos.this);
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
           /* }else{
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsTodos.this);
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


    public void pendientemaps(View v) {
        comentariog = null;
        estatus = null;

        builder = new AlertDialog.Builder(MapsTodos.this);
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
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsTodos.this);
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

                android.app.AlertDialog.Builder alerta = new AlertDialog.Builder(MapsTodos.this);
                alerta.setMessage(mensajes + " con folio: " + folioparaentregar).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();


                        if (Pedidos.get(pos1).getLatitud() == 0.0 && Pedidos.get(pos1).getLongitud() == 0.0) {

                            android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getApplicationContext());
                            alerta = new android.app.AlertDialog.Builder(getApplicationContext());
                            alerta.setMessage("¿Deseas entregar este pedido?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                   // saveLoca();
                                    if (!Pedidos.get(pos1).getTelefonodos().equals("")) {
                                        smsCamino = Empresa + " agradece su preferencia.\n" +
                                                "" + Pedidos.get(pos1).getNombre() + ", Su pedido con el folio " + Pedidos.get(pos1).getFolio() + " ha sido entregado por el Repartidor " + strname + " " + strlname + " a " + nombrequienrecibio + ". \n" +
                                                "Le deseamos un excelente día.";

                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(Pedidos.get(pos1).getTelefonodos(), null, smsCamino, null, null);
                                    }
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();

                                    if (!Pedidos.get(pos1).getTelefonodos().equals("")) {
                                        smsCamino = Empresa + " agradece su preferencia.\n" +
                                                "" + Pedidos.get(pos1).getNombre() + ", Su pedido con el folio " + Pedidos.get(pos1).getFolio() + " ha sido entregado por el Repartidor " + strname + " " + strlname + " a " + nombrequienrecibio + ". \n" +
                                                "Le deseamos un excelente día.";

                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(Pedidos.get(pos1).getTelefonodos(), null, smsCamino, null, null);
                                    }
                                }
                            });
                            android.app.AlertDialog titulo = alerta.create();
                            titulo = alerta.create();
                            titulo.setTitle("¿Realizar la entrega?");
                            titulo.show();




                        } else {


                            builder6 = new AlertDialog.Builder(MapsTodos.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.pantallacargacarrito, null);
                            builder6.setView(dialogView);
                            dialog6 = builder6.create();
                            builder6.setCancelable(false);
                            dialog6.show();

                            if (!Pedidos.get(pos1).getTelefonodos().equals("")) {
                                smsCamino = Empresa + " agradece su preferencia.\n" +
                                        "" + Pedidos.get(pos1).getNombre() + ", Su pedido con el folio " + Pedidos.get(pos1).getFolio() + " ha sido entregado por el Repartidor " + strname + " " + strlname + " a " + nombrequienrecibio + ". \n" +
                                        "Le deseamos un excelente día.";
                                SmsManager smsManager = SmsManager.getDefault();
                                smsManager.sendTextMessage(Pedidos.get(pos1).getTelefonodos(), null, smsCamino, null, null);
                            }
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog6.dismiss();
                                }
                            }, 5000);


                            Pedidos.clear();
                            listaDato = null;
                            mMap.clear();

                            dialog.dismiss();
                            mMarkerepartidor = mMap.addMarker(new MarkerOptions().position(miUbicacion).title(strname + strlname).icon(BitmapDescriptorFactory.fromResource(R.drawable.repartidor2)));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(miUbicacion).zoom(17).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            Inicio();
                        }


                    }
                });

                android.app.AlertDialog titulo = alerta.create();
                titulo.setTitle("");
                titulo.show();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsTodos.this);
                alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                AlertDialog titulo1 = alerta1.create();
                titulo1.setTitle("Error");
                titulo1.show();
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
        Volley.newRequestQueue(MapsTodos.this).add(postRequest);
    }
    public void btnSite (View View) {

        String url = "https://ul.waze.com/ul?ll="+latcrta+"%2C"+longcort+"&navigate=yes&utm_campaign=default&utm_source=waze_website&utm_medium=lm_share_location";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void actualizarfirma2() {

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String url = StrServer + "/recibeR";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    mensajes = jsonObject.getString("Repartidores");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(MapsTodos.this);
                alerta.setMessage(mensajes).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                      /*  if (Pedidos.get(pos1).getLatitud() == 0.0 && Pedidos.get(pos1).getLongitud() == 0.0) {
                            saveLoca();
                        } else {
*/

                            builder6 = new AlertDialog.Builder(MapsTodos.this);
                            LayoutInflater inflater = getLayoutInflater();
                            View dialogView = inflater.inflate(R.layout.pantallacargacarrito, null);
                            builder6.setView(dialogView);
                            dialog6 = builder6.create();
                            builder6.setCancelable(false);
                            dialog6.show();

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    dialog6.dismiss();
                                }
                            }, 5000);


                            Pedidos.clear();
                            listaDato = null;
                            mMap.clear();

                            dialog.dismiss();
                            mMarkerepartidor = mMap.addMarker(new MarkerOptions().position(miUbicacion).title(strname + strlname).icon(BitmapDescriptorFactory.fromResource(R.drawable.repartidor2)));

                            mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                            CameraPosition cameraPosition = new CameraPosition.Builder().target(miUbicacion).zoom(17).build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                            Inicio();
                       /* }*/

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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsTodos.this);
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();
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
        Volley.newRequestQueue(MapsTodos.this).add(postRequest);
    }

    private void Aviso() {

        String url = StrServer + "/Aviso";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsTodos.this);
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();
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
                return params;
            }
        };
        Volley.newRequestQueue(MapsTodos.this).add(postRequest);
    }


    public void saveLoca() {

        String url = StrServer + "/asignarcor";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Pedidos.clear();
                mMap.clear();
                dialog.dismiss();
                mMarkerepartidor = mMap.addMarker(new MarkerOptions().position(miUbicacion).title(strname + strlname).icon(BitmapDescriptorFactory.fromResource(R.drawable.repartidor2)));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(miUbicacion).zoom(17).build();

                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                builder6 = new AlertDialog.Builder(MapsTodos.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.pantallacargacarrito, null);
                builder6.setView(dialogView);
                dialog6 = builder6.create();
                builder6.setCancelable(false);
                dialog6.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog6.dismiss();
                    }
                }, 5000);


                Inicio();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                AlertDialog.Builder alerta1 = new AlertDialog.Builder(MapsTodos.this);
                alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });
                AlertDialog titulo1 = alerta1.create();
                titulo1.setTitle("Error");
                titulo1.show();
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
        Volley.newRequestQueue(MapsTodos.this).add(postRequest);

    }
}