package com.kepler.respartidores01;

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
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;
import com.kepler.respartidores01.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    public ArrayList<Pedidos> lpeA=new ArrayList<>();
    Set<String> setD = new HashSet<>();
    Set<String> setN = new HashSet<>();
    Set<String> setC = new HashSet<>();
    Set<String> setV = new HashSet<>();
    Set<String> setVa = new HashSet<>();

    ArrayList<String> listD;
    ArrayList<String> listN;
    ArrayList<String> listC;
    ArrayList<String> listV;

    private boolean actualposition = true;
    double longitudorigen;
    double latitudorigen;
    private LocationManager locationManager;
    private Marker mMarker;
    Geocoder coder;
    Geocoder coddirmap;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode, strdirecccion, struser,
    strbranch;
    List<Address> address= new ArrayList<>();
    ArrayList<LatLng> puntosdireccion = new ArrayList<>();
    int clave=0;

    String direclis, nombrelist =null;
    String value=null;
    String dircort=null;
    String fol=null;
    Double latcrta = 0.0;
    Double longcort=0.0;
    Boolean bandera =false;
    ArrayList<Marker> identmarkers = null;
    Pedidos pedentregado = null;
    int identificador = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        preference= this.getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        strcodBra = preference.getString("codBra", "null");
        StrServer = preference.getString("Server", "null");
        struser= preference.getString("user","");
        strpass=preference.getString("pass","");
        strbranch=preference.getString("branch","");
        strcode=preference.getString("code","");

         setD = preference.getStringSet("Direcciones",null);
         setN = preference.getStringSet("Nombres",null);
        setC = preference.getStringSet("Clientes",null);


        listD = new ArrayList<String>(setD);
        listN=new ArrayList<String>(setN);
        listC=new ArrayList<String>(setC);


        direclis =null;
        nombrelist =null;

        Bundle parametros = this.getIntent().getExtras();

        direclis= parametros.getString("directlista");
        nombrelist=parametros.getString("nombre_direccion");


            for(int j=0; j<listN.size(); j++){
            lpeA.add(new Pedidos("",listC.get(j),"",listN.get(j),"","","",listD.get(j),""));
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (gpsActived() == true) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

                        mMap.setMyLocationEnabled(true);
                        LocationListener locationListener = new LocationListener() {
                            @Override
                            public void onLocationChanged(Location location) {
                                coddirmap = new Geocoder(MapsActivity.this);
                                if (getApplicationContext() != null) {
                                    if(direclis==null) {
                                        LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                                        latitudorigen = location.getLatitude();
                                        longitudorigen = location.getLongitude();

                                        if (mMarker != null) {
                                            mMarker.remove();
                                        }
                                        mMarker = mMap.addMarker(new MarkerOptions().position(miUbicacion));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                                        CameraPosition cameraPositio = new CameraPosition.Builder()
                                                .target(miUbicacion)
                                                .zoom(17)
                                                .bearing(90)
                                                .build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositio));

                                        obtdistanc();
                                        while (bandera==true){
                                            funcion();
                                            bandera=false;
                                        }

                                    }
                                    else {
                                        Double dircelisLAT;
                                        Double dircelisLONG;
                                        try {
                                            address = coddirmap.getFromLocationName(direclis, 2);
                                            Address direx;
                                            direx=address.get(0);
                                            dircelisLAT = direx.getLatitude();
                                            dircelisLONG = direx.getLongitude();

                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }

                                        LatLng marcadr = new LatLng(dircelisLAT, dircelisLONG);
                                        mMarker = mMap.addMarker(new MarkerOptions().position(marcadr).title(nombrelist));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(marcadr));
                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(marcadr)
                                                .zoom(17)
                                                .bearing(90)
                                                .build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                    }
                                    direclis=null;
                                }
                            }

                        };
                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 2000, locationListener);

                    }
                } else {
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(this);
                    alerta.setMessage("").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    });

                    android.app.AlertDialog titulo = alerta.create();
                    titulo.setTitle("Permisos no otorgados");
                    titulo.show();
                }
            }
        } else {
            showAlertDialogNoGPS();
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                    mMap.setMyLocationEnabled(true);
                    mMap.getUiSettings().setZoomControlsEnabled(true);

                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        //OBTENER LAS LATITUDES Y LONGITUDES DE LA DIRECCION
        coder = new Geocoder(MapsActivity.this);
        for(int i = 0; i< listD.size(); i++) {
            try {
                address = coder.getFromLocationName(listD.get(i), 3);
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                puntosdireccion.add(new LatLng(location.getLatitude(), location.getLongitude()));

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        //Dibuja todos los marcadores en el mapa
        for (int i = 0; i < puntosdireccion.size(); i++) {
            identmarkers.add(i,mMap.addMarker(new MarkerOptions().position(puntosdireccion.get(i)).title(listN.get(i))));
        }

        if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {

            } else {
                ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        } else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setRotateGesturesEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);

            LocationManager locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    coddirmap = new Geocoder(MapsActivity.this);
                    if (getApplicationContext() != null) {
                        if (direclis == null) {
                            LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                            latitudorigen = location.getLatitude();
                            longitudorigen = location.getLongitude();

                            if (mMarker != null) {
                                mMarker.remove();
                            }

                            mMarker = mMap.addMarker(new MarkerOptions().position(miUbicacion));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                            CameraPosition cameraPositio = new CameraPosition.Builder()
                                    .target(miUbicacion)
                                    .zoom(17)
                                    .bearing(90)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositio));

                            obtdistanc();
                            while (bandera==true){
                                funcion();
                                bandera=false;
                            }

                        } else {
                            Double dircelisLAT;
                            Double dircelisLONG;
                            try {
                                address = coddirmap.getFromLocationName(direclis, 2);
                                Address direx;
                                direx = address.get(0);
                                dircelisLAT = direx.getLatitude();
                                dircelisLONG = direx.getLongitude();

                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            LatLng marcadr = new LatLng(dircelisLAT, dircelisLONG);
                            mMarker = mMap.addMarker(new MarkerOptions().position(marcadr).title(nombrelist));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(marcadr));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(marcadr)
                                    .zoom(17)
                                    .bearing(90)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                        }
                        direclis = null;

                    }

                }


            };
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 3000, locationListener);

        }
//        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
//            @Override
//            public void onMyLocationChange(@NonNull Location location) {
//
//            }
//
//        });
    }

    public void funcion() {

        setVa = preference.getStringSet("Values",null);
        //int h= setVa.size();
        listV=new ArrayList<String>(setVa);

        int Valor = Integer.parseInt(listV.get(0));
        lpeA.set(0,new Pedidos("",listC.get(0),"",listN.get(0),"","","",listD.get(0),listV.get(0)));
        dircort=lpeA.get(0).direccion;
        fol=lpeA.get(0).Folio;

        for (int i = 1; i < listV.size(); i++) {
            if (Valor < Integer.parseInt((listV.get(i)))) {

            } else {
                Valor = Integer.parseInt(listV.get(i));;
                lpeA.set(i,new Pedidos("",listC.get(i),"",listN.get(i),"","","",listD.get(i),listV.get(i)));
                dircort=lpeA.get(i).direccion;
                fol=lpeA.get(i).Folio;

            }
        }

        //OBTENER LAS LATITUDES Y LONGITUDES DE LA DIRECCION
        coder = new Geocoder(MapsActivity.this);
            try {
                address = coder.getFromLocationName(dircort, 3);
                Address locationcrt = address.get(0);
               latcrta= locationcrt.getLatitude();
                longcort=locationcrt.getLongitude();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudorigen + "," + longitudorigen + "&destination=" + latcrta +"," + longcort + "&key=AIzaSyAOjhQhJdgBE8AtwovY0_2reTUniizC5xI";

        RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jso = new JSONObject(response);
                    trazarRuta(jso);

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

    public void obtdistanc() {

        for (int i = 0; i <puntosdireccion.size(); i++) {

            LatLng nuevo = puntosdireccion.get(i);
            String url="https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudorigen + "," + longitudorigen + "&destination=" + nuevo.latitude + "," + nuevo.longitude + "&key=AIzaSyAOjhQhJdgBE8AtwovY0_2reTUniizC5xI";

            StringRequest stringRequesttt = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jso = new JSONObject(response);
                        value(jso);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    clave++;
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            });
            Volley .newRequestQueue(this).add(stringRequesttt);

        }


    }

    public void value(JSONObject jso){
      JSONObject routes;
      JSONObject legs;
      JSONObject distance;

        try {
            routes = jso.getJSONArray("routes").getJSONObject(0);
            legs = routes.getJSONArray("legs").getJSONObject(0);
            distance= legs.getJSONObject("distance");
            value = distance.getString("value");
            setV.add(value);
            editor.putStringSet("Values",setV);
            editor.commit();
            editor.apply();
            bandera=true;

        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                        mMap.addPolyline(new PolylineOptions().addAll(list).color(Color.RED).width(6));
                    }
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean gpsActived() {
        boolean isActive = false;
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isActive = true;
        }
        return isActive;
    }

    private void showAlertDialogNoGPS() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Porfavor Activa tu ubicacion para poder continuar")
                .setPositiveButton("Configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), SETTINGS_REQUEST_CODE);
                    }
                }).create().show();
    }

    public void entregado(View v) {
        TextView entregarfolio;
        entregarfolio=findViewById(R.id.foliipaqentrega);

        for (int i=0; i<=lpeA.size(); i++){
            if(fol==lpeA.get(i).getFolio()){
                pedentregado =new Pedidos(lpeA.get(i).getSucursal(),lpeA.get(i).getCliente(),lpeA.get(i).getNumpaq(),lpeA.get(i).getNombre(),lpeA.get(i).getTelefonouno(),lpeA.get(i).getTelefonodos(),lpeA.get(i).getFolio(),lpeA.get(i).getDireccion(),lpeA.get(i).getDistance());
            }
        }
      AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        entregarfolio.setText(fol);
        builder.setView(inflater.inflate(R.layout.recibio_, null))
               .create().show();

        Button acaepar=findViewById(R.id.btentre);

        acaepar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String recibio;
                EditText rec;
                rec=findViewById(R.id.quien_recibio);
                recibio=rec.getText().toString();

                editor.putString("folioentregado",fol);
                editor.putString("entregoSucursal",pedentregado.getSucursal());
                editor.putString("entregoCliente",pedentregado.getCliente());
                editor.putString("entregoNumpaq",pedentregado.getNumpaq());
                editor.putString("entregoNombre",pedentregado.getNombre());
                editor.putString("entregonumteluno",pedentregado.getTelefonouno());
                editor.putString("entregonumteldos",pedentregado.getTelefonodos());
                editor.putString("entregoFolio",pedentregado.getFolio());
                editor.putString("entregoDirec",pedentregado.getDireccion());
                editor.putString("recibio",recibio);
                editor.commit();

            }
        });

    }

    public void pendiente(View vi){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.diseno_pendiente, null))
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                }).create().show();
    }
}