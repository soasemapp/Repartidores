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
import android.view.View;
import android.view.WindowManager;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, ActivityCompat.OnRequestPermissionsResultCallback {
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private final static int LOCATION_REQUEST_CODE = 1;
    private final static int SETTINGS_REQUEST_CODE = 2;
    private boolean actualposition = true;
    double longitudorigen;
    double latitudorigen;
    double Latitud = 0, Longitud = 0;
    private LocationManager locationManager;
    private Marker mMarker;
    Geocoder coder;
    ArrayList<LatLng> puntos = new ArrayList<LatLng>();

    Location location;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode, strdirecccion;
    List<Address> address;
    LatLng puntosdireccion = null;
    Address direcc;
    String dis;
    int diI;
    ArrayList<ClienteSandG> ClientesDis = new ArrayList<>();
    int di = 0;
    int clave=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        strcodBra = preference.getString("codBra", "null");
        StrServer = preference.getString("Server", "null");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    //Consulta los folios para obtener las direcciones


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (gpsActived() == true) {
            if (requestCode == LOCATION_REQUEST_CODE) {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(MapsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                        mMap.setMyLocationEnabled(true);

                    }
                } else {
                    Toast.makeText(this, "Permisos no otorgados", Toast.LENGTH_LONG).show();
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

            puntos.add(new LatLng(Latitud = 23.17487650092318, Longitud = -102.8778950998468));
            puntos.add(new LatLng(Latitud = 23.172535411343105, Longitud = -102.87830236229395));
            puntos.add(new LatLng(Latitud = 23.170988833356812, Longitud = -102.86944172670869));
            puntos.add(new LatLng(Latitud = 23.183933670029337, Longitud = -102.8636071452571));


            LocationManager locationManager = (LocationManager) MapsActivity.this.getSystemService(Context.LOCATION_SERVICE);
            LocationListener locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    if (getApplicationContext() != null) {

                        LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());

                        latitudorigen = location.getLatitude();
                        longitudorigen = location.getLongitude();

                        if (mMarker != null) {
                            mMarker.remove();
                        }
                        mMarker = mMap.addMarker(new MarkerOptions().position(miUbicacion).icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_maprepartidor)));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                        CameraPosition cameraPosition = new CameraPosition.Builder()
                                .target(miUbicacion)
                                .zoom(17)
                                .bearing(90)
                                .build();
                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                        entreado();


                    }

                }

            };

            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 2000, locationListener);

        }


        coder = new Geocoder(this);
        try {
//            address = coder.getFromLocationName("99000, Av. Enrique Estrada 521, Centro, Fresnillo, Zac.", 3);
//            address = coder.getFromLocationName("Francisco García Salinas 510, Centro, 98097 Fresnillo, Zac.", 3);
            address = coder.getFromLocationName(strdirecccion, 3);

            direcc = address.get(0);
            direcc.getLatitude();
            direcc.getLongitude();

            puntosdireccion = new LatLng(direcc.getLatitude(), direcc.getLongitude());

        } catch (Exception ex) {

            ex.printStackTrace();
        }
        //Array de diferentes puntos en el map

        //puntos.add(puntosdireccion);

        //Dibuja todos los marcadores en el mapa
        for (int i = 0; i < puntos.size(); i++) {
            mMarker = mMap.addMarker(new MarkerOptions().position(puntos.get(i)).title("Cliente").draggable(true));
        }
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(@NonNull Location location) {

                if (actualposition) {

                }
            }
        });

    }


//    public void funcion(View vi) {
//        int Valor = ClientesDis.get(0).getDistancia();
//        for (int i = 1; i < ClientesDis.size(); i++) {
//            if (Valor<ClientesDis.get(i).Distancia){
//
//            }else{
//                Valor = ClientesDis.get(i).getDistancia();
//            }
//
//
//        }








    public void entreado() {
        for (int i = 0; i < puntos.size(); i++) {

            LatLng nuevo = puntos.get(i);

            String url =//"https://maps.googleapis.com/maps/api/directions/json?origin=23.172281128854596,%20-102.8708630775893&destination=23.172977863806704,%20-102.871723141316&key=AIzaSyAOjhQhJdgBE8AtwovY0_2reTUniizC5xI";
                    "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudorigen + "," + longitudorigen + "&destination=" + nuevo.latitude + "," + nuevo.longitude + "&key=AIzaSyAOjhQhJdgBE8AtwovY0_2reTUniizC5xI";


            RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jso = new JSONObject(response);

clave++;
                        try {
                            jso = jso.getJSONArray("routes").getJSONObject(0);
                            jso = jso.getJSONArray("legs").getJSONObject(0);
                            jso = jso.getJSONObject("distance");

                            // Toast.makeText(MapsActivity.this, dis , Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


//                                       trazarRuta(jso);
//                                       // Log.i("JsonRuta", " " + response);
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

}