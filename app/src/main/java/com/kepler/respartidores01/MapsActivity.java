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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.kepler.respartidores01.databinding.ActivityMapsBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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

    AlertDialog.Builder builder;
   AlertDialog dialog = null;
    String[] listaDato = null;
int controlador=0;
    ArrayList<String> listD=null;
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
    ArrayList<String> values=new ArrayList<>();
    String direclis, nombrelist =null;
    String value=null;
    String dircort=null;
    Double latcrta = 0.0;
    Double longcort=0.0;
    ArrayList<String> urls=new ArrayList<>();
    JSONObject routes;
    JSONObject legs;
    JSONObject distance;


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
//         setN = preference.getStringSet("Nombres",null);
//        setC = preference.getStringSet("Clientes",null);


        if(setD!=null){
        listD = new ArrayList<String>(setD);}
//        listN=new ArrayList<String>(setN);
//        listC=new ArrayList<String>(setC);


        direclis =null;
        nombrelist =null;

        Bundle parametros = this.getIntent().getExtras();

        direclis= parametros.getString("directlista");
        nombrelist=parametros.getString("nombre_direccion");


//            for(int j=0; j<listN.size(); j++){
//            lpeA.add(new Pedidos("",listC.get(j),"",listN.get(j),"","","",listD.get(j),""));
//        }



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
                                        mMarker = mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicacion").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mimarkpin)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                                        CameraPosition cameraPositio = new CameraPosition.Builder()
                                                .target(miUbicacion)
                                                .zoom(17)
                                                .bearing(90)
                                                .build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositio));

                                        if(listD!=null){
                                            obtdistanc();}

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

                                        LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());
                                        latitudorigen = location.getLatitude();
                                        longitudorigen = location.getLongitude();

                                        LatLng marcadr = new LatLng(dircelisLAT, dircelisLONG);

                                        mMarker = mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mimarkpin)));
                                        mMarker = mMap.addMarker(new MarkerOptions().position(marcadr).title(nombrelist).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_markrojo)));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLng(marcadr));
                                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                                .target(marcadr)
                                                .zoom(17)
                                                .bearing(90)
                                                .build();
                                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                                        obtdistanc();
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
            for (int i = 0; i < listD.size(); i++) {
                try {
                    address = coder.getFromLocationName(listD.get(i), 3);
                    Address resultadoscode = address.get(0);
                    resultadoscode.getLatitude();
                    resultadoscode.getLongitude();
                    puntosdireccion.add(i, new LatLng(resultadoscode.getLatitude(), resultadoscode.getLongitude()));

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            //Dibuja todos los marcadores en el mapa
            for (int i = 0; i < puntosdireccion.size(); i++) {
                mMap.addMarker(new MarkerOptions().position(puntosdireccion.get(i)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_markrojo)));
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

                            mMarker = mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicacion").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mimarkpin)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                            CameraPosition cameraPositio = new CameraPosition.Builder()
                                    .target(miUbicacion)
                                    .zoom(17)
                                    .bearing(90)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPositio));

                            if(listD!=null){
                            obtdistanc();}


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

                            latitudorigen = location.getLatitude();
                            longitudorigen = location.getLongitude();

                            LatLng marcadr = new LatLng(dircelisLAT, dircelisLONG);

                            LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                            mMarker = mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicacion").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_mimarkpin)));
                            mMarker = mMap.addMarker(new MarkerOptions().position(marcadr).title(nombrelist).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_markrojo)));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(marcadr));
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(marcadr)
                                    .zoom(17)
                                    .bearing(90)
                                    .build();
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            obtdistanc();

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
//        });
    }

    public void funcion() {

        int Valor = Integer.parseInt(listaDato[0]);
       // lpeA.set(0,new Pedidos("","","","","","","",listD.get(0),listaDato[0]));
        dircort=listD.get(0);
        latcrta=puntosdireccion.get(0).latitude;
        longcort=puntosdireccion.get(0).longitude;

        for (int i = 1; i < listaDato.length; i++) {

            if (Valor < Integer.parseInt(listaDato[i])) {

            } else {
                Valor = Integer.parseInt(listaDato[i]);
               // lpeA.set(i,new Pedidos("","","","","","","",listD.get(i),listaDato[i]));
                dircort=listD.get(i);
                latcrta=puntosdireccion.get(i).latitude;
                longcort=puntosdireccion.get(i).longitude;

            }
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

    public void obtdistanc()
    {
        listaDato = new String[puntosdireccion.size()];

        for (int i = 0; i < puntosdireccion.size(); i++) {
            LatLng nuevo = puntosdireccion.get(i);
            String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudorigen + "," + longitudorigen + "&destination=" + nuevo.latitude + "," + nuevo.longitude + "&key=AIzaSyAOjhQhJdgBE8AtwovY0_2reTUniizC5xI";
            ingresarDatos(i,url);
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

    public void estaentregado(View vi){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.recibio_, null))
                .create().show();

    }

    public void ingresarDatos(int pos, String url)
    {
        StringRequest stringRequesttt = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jso = new JSONObject(response);
                    routes = jso.getJSONArray("routes").getJSONObject(0);
                    legs = routes.getJSONArray("legs").getJSONObject(0);
                    distance = legs.getJSONObject("distance");
                    value = distance.getString("value");
                    values.add(value);
                    listaDato[pos] = value;

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                clave++;
                if (controlador == clave) {
                    funcion();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError)
            {

            }
        });
        Volley.newRequestQueue(this).add(stringRequesttt);
        controlador++;
    }

    public void entregado()
    {
//        builder = new AlertDialog.Builder(this);
//        LayoutInflater inflaterentrega = getLayoutInflater();
//        View dialogViewww = inflaterentrega.inflate(R.layout.recibio_, null);
//        builder.setView(dialogViewww);
//        EditText recibio=dialogViewww.findViewById(R.id.quien_recibio);
//        Button enttegar=dialogViewww.findViewById(R.id.btentre);
//        enttegar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                recibio.getText().toString();
//                //folioconfirma=lpeA.get(position).getFolio();
//                //sucursalonfrima=lpeA.get(position).getSucursal();
//
//                editor.putString("recibio",recibio.getText().toString());
//                editor.putString("entregoSucursal",lpeA.get().getSucursal());
//                editor.putString("entregoCliente",lpeA.get().getCliente());
//                editor.putString("entregoNombre",lpeA.get().getNombre());
//                editor.putString("entregonumteluno",lpeA.get().getTelefonouno());
//                editor.putString("entregonumteldos",lpeA.get().getTelefonodos());
//                editor.putString("entregoFolio",lpeA.get().getFolio());
//                editor.putString("entregoDirec",lpeA.get().getDireccion());
//                editor.putString("entregoNumpaq",lpeA.get().getNumpaq());
//                editor.commit();
//                editor.apply();
//
//            }
//        });
//
//        dialog = builder.create();
//        dialog.show();
    }

}