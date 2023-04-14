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
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
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
    double Latitud=0, Longitud=0;
    private LocationManager locationManager;
    private Marker mMarker;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode;
    Location location;
    List<Address> address;
    LatLng puntosdireccion = null;
    Address direcc;
    String dire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        strcodBra = preference.getString("codBra", "null");
        StrServer = preference.getString("Server", "null");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        //LeerWs();
    }
    private void LeerWs(){
        String url =StrServer+"/consulfac";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject=jsonObject.getJSONObject("Repartidores");
                    jsonObject.getString("k_Folio");
                    jsonObject.getString("k_Clave");
                    jsonObject.getString("k_Nombre");
                    jsonObject.getString("k_Numero1");
                    jsonObject.getString("k_Numero2");
                    jsonObject.getString("k_Direccion");

                    Toast.makeText(MapsActivity.this, response, Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        },
                new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("user","jared");
                header.put("pass","jared");
                return header;
            }
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap params = new HashMap();
                params.put("sucursal","01");
                params.put("folio","0000629");
                return params;
            }
        };
        Volley .newRequestQueue(this).add(postRequest);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (gpsActived()==true) {
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
        }else{
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
        }
        else {
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }

        //Metodo para Obtener las coordenadas por la direccion
        Geocoder coder = new Geocoder(this);

        try {
            address = coder.getFromLocationName("Aguascalientes 462-484, Quinta del Cobre, 99090 Fresnillo, Zac.", 3);
            direcc = address.get(0);
            direcc.getLatitude();
            direcc.getLongitude();

            puntosdireccion = new LatLng(direcc.getLatitude(), direcc.getLongitude() );

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        //Array de diferentes puntos en el map
        List<LatLng> puntos=new ArrayList<LatLng>();
        puntos.add(puntosdireccion);
        puntos.add(new LatLng(Latitud=23.17487650092318, Longitud=-102.8778950998468));
        puntos.add(new LatLng(Latitud=23.172535411343105, Longitud=-102.87830236229395));

        //Dibuja todos los marcadores en el mapa
        for (int i = 0; i < puntos.size(); i++) {
            mMarker = mMap.addMarker(new MarkerOptions().position(puntos.get(i)).title("Cliente").draggable(true));
        }
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
               @Override
               public void onMyLocationChange(@NonNull Location location) {
                   if (actualposition) {
                       latitudorigen = location.getLatitude();
                       longitudorigen = location.getLongitude();
                       actualposition = false;
                       String url =//"https://maps.googleapis.com/maps/api/directions/json?origin=23.172281128854596,%20-102.8708630775893&destination=23.172977863806704,%20-102.871723141316&key=AIzaSyAOjhQhJdgBE8AtwovY0_2reTUniizC5xI";
                               "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudorigen + "," + longitudorigen + "&destination=" + Latitud + "," + Longitud + "&key=AIzaSyAOjhQhJdgBE8AtwovY0_2reTUniizC5xI";
                       LatLng Origen = new LatLng(latitudorigen,longitudorigen );
                       LatLng miCliente = new LatLng(Latitud,Longitud);
                       mMap.addMarker(new MarkerOptions().position(Origen).title( "Origen").icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_maprepartidor)));
                       mMap.addMarker(new MarkerOptions().position(miCliente).title("Direccion Cliente"));
                       CameraPosition cameraPosition = new CameraPosition.Builder()
                               .target(new LatLng(latitudorigen, longitudorigen))
                               .zoom(17)
                               .bearing(90)
                               .build();
                       mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                       RequestQueue queue = Volley.newRequestQueue(MapsActivity.this);
                       StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                           @Override
                           public void onResponse(String response) {
                               try {
                                   JSONObject jso = new JSONObject(response);
                                   trazarRuta(jso);
                                   Log.i("JsonRuta", " " + response);
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
           });
    }
    public void rutacorta(){

        //ArrayList<String> hola= new ArrayList<>();
        for(int i=0; i<address.size(); i++){
           direcc= address.get(i);
           Latitud=direcc.getLatitude();
           Longitud=direcc.getLongitude();
        }

    }

    public void entreado(View vi){
      rutacorta();
    }

    //Para obtener la distania de cada direccion
    public void  obtenerdistancia() {

        String ruta = "https://maps.googleapis.com/maps/api/directions/json?origin=" + latitudorigen + "," + longitudorigen + "&destination=" + Latitud + "," + Longitud + "&key=AIzaSyAOjhQhJdgBE8AtwovY0_2reTUniizC5xI";
        StringRequest trutac = new StringRequest(Request.Method.GET, ruta, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonObject=jsonObject.getJSONArray("routes").getJSONObject(0);
                    jsonObject=jsonObject.getJSONArray("legs").getJSONObject(0);
                    jsonObject= jsonObject.getJSONObject("distance");
                    dire= jsonObject.getString("text");

                  Toast.makeText(MapsActivity.this, dire, Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MapsActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
      Volley .newRequestQueue(this).add(trutac);
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