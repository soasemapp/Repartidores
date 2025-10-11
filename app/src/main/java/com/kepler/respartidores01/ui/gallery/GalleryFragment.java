package com.kepler.respartidores01.ui.gallery;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.AlertDialog;


import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.kepler.respartidores01.SetAndGet.AdapeterDetallefac;
import com.kepler.respartidores01.Activity.MapsSolo;
import com.kepler.respartidores01.SetAndGet.Mdestallefac;
import com.kepler.respartidores01.Adapter.MiAdaptador;
import com.kepler.respartidores01.SetAndGet.Pedidos;
import com.kepler.respartidores01.Activity.Principal;
import com.kepler.respartidores01.R;
import com.kepler.respartidores01.SetAndGet.SetAndGetListClientes;
import com.kepler.respartidores01.databinding.FragmentGalleryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import java.io.IOException;


public class GalleryFragment extends Fragment {
    MiAdaptador miAdaptador;
    MiAdaptador miAdaptador2;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private FragmentGalleryBinding binding;
    String folioparaentregar;
    ListView lista;
    ListView lista2;
    ListView lisdf;
    TextView txtpu, txtpt;
    int j;
    public ArrayList<Pedidos> lpeA = new ArrayList<>();
    public ArrayList<Mdestallefac> ldf = new ArrayList<>();
    public ArrayList<SetAndGetListClientes> ClientesListas = new ArrayList<>();
    String mensajes;
    Button btn_entregar_todo;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String struser, strcode, strbranch, strpass, strname, strlname, stremail;
    String strcodBra, StrServer, escfolio, escnombre, escdireccion, escnumun, escnumdos, escnumc = null;
    String strscliente = "";

    AlertDialog.Builder builder;
    AlertDialog dialog = null;
    private TextView sucur;
    TextView fol, cli, nom, npac, tun, tdos, di;
    LinearLayout ClientesOcular;
    LinearLayout TodosOcultar;
    Button ButtonCliente, ButtonTodos, ButtonListaClientes;
    Set<String> setD = new HashSet<>();
    Set<String> setN = new HashSet<>();
    Set<String> setC = new HashSet<>();
    String mensaje = "entregoSucursal";

    String DFfolio, DFsucursal, DFcliente, DFnombre;

    String producto, descripcion, cantidad, entregorc, entregofolio, entregodirec, preciounitario = "", preciototal = "";
    String folioconfirma, sucursalonfrima, recibiofir, comentariog;
    private SwipeRefreshLayout refreshLayout;
    private SwipeRefreshLayout refreshLayout2;
    String estatus;
    String smsCamino, Empresa, CONFIGURACION = "";


    String Sucursal, Folios, Nombres;
    android.app.AlertDialog.Builder builder6;
    android.app.AlertDialog dialog6 = null;
    int posicion = 0;

    private LinearLayout loadingLayout;
    private boolean isLoading = false;

    private static final double RADIO_ENTREGA = 25.0; // metros
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private boolean isLocationReady = false;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Inicializar loading layout
        loadingLayout = root.findViewById(R.id.loadingLayout);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        preference = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        // Inicializar ubicación
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        setupLocation();


        strcodBra = preference.getString("codBra", "null");
        StrServer = preference.getString("Server", "null");
        struser = preference.getString("user", "");
        strpass = preference.getString("pass", "");
        strbranch = preference.getString("branch", "");
        strcode = preference.getString("code", "");
        strname = preference.getString("name", "null");
        strlname = preference.getString("lname", "null");
        stremail = preference.getString("email", "null");


        escnombre = preference.getString("Nombreescrito", "null");
        escfolio = preference.getString("folioescrito", "null");
        escnumun = preference.getString("Num1_escrito", "null");
        escnumdos = preference.getString("Num2escrito", "null");
        escdireccion = preference.getString("direccionescrito", "null");
        escnumc = preference.getString("numc", "");

        ButtonTodos = getView().findViewById(R.id.ButtonTodos);
        ButtonCliente = getActivity().findViewById(R.id.ButtonClientes);
        ButtonCliente.setBackgroundColor(Color.RED);
        ButtonTodos.setBackgroundColor(Color.BLACK);
        ClientesOcular = getView().findViewById(R.id.ClienteOcultar);
        TodosOcultar = getView().findViewById(R.id.TodoOcultar);
        ButtonListaClientes = getActivity().findViewById(R.id.listaclientes);
        btn_entregar_todo = getActivity().findViewById(R.id.btn_entregar_todo);
        entregorc = preference.getString("recibio", "");
        entregodirec = preference.getString("entregoDirec", "");
        setD.clear();
        lpeA.clear();
        builder6 = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pantallacarga, null);

        // Cargar datos iniciales
        loadInitialData();


        btn_entregar_todo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ENTREGAR_TODO", "Botón presionado - Cliente seleccionado: " + strscliente);

                if (!strscliente.isEmpty()) {


                   // Toast.makeText(getContext(), "Obteniendo ubicación actual...", Toast.LENGTH_SHORT).show();

                    // Obtener ubicación actual
                    obtenerUbicacionActual(new OnLocationObtainedListener() {
                        @Override
                        public void onLocationObtained(Location location) {
                            if (location == null) {
                                Toast.makeText(getContext(), "No se pudo obtener la ubicación", Toast.LENGTH_LONG).show();
                                return;
                            }

                            currentLocation = location;
                            Log.d("ENTREGAR_TODO",
                                    "Mi ubicación actual: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());

                            // Verificar proximidad (con Geocoder si es necesario)
                            verificarProximidadConGeocoder(new OnProximidadVerificadaListener() {
                                @Override
                                public void onProximidadVerificada(boolean estaCerca) {
                                    Log.d("ENTREGAR_TODO", "Resultado verificación proximidad: " + estaCerca);

                                    if (estaCerca) {
                                        Log.d("ENTREGAR_TODO", "Iniciando proceso de entrega...");
                                        iniciarProcesoEntrega();
                                    }
                                }
                            });
                        }
                    });

                } else {
                    mostrarAlerta("Selecciona un cliente porfavor", "Verifica");
                }
            }
        });



        ButtonListaClientes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] opciones = new String[ClientesListas.size()];

                for (int i = 0; i < ClientesListas.size(); i++) {
                    opciones[i] = ClientesListas.get(i).getClave() + ":" + ClientesListas.get(i).getNombre();
                }


                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
                builder.setTitle("SELECCIONE UN CLIENTE").setIcon(R.drawable.ic_repar);


                builder.setItems(opciones, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        strscliente = ClientesListas.get(which).getClave();
                        ButtonListaClientes.setText(ClientesListas.get(which).getNombre());
                        LeerWs2();


                    }
                });
// create and show the alert dialog
                android.app.AlertDialog dialog = builder.create();
                dialog.show();
            }
        });



//Mensaje de Aviso de del folio va en camino


        //Mensaje de que el folio a sido entregado
        switch (StrServer) {
            case "http://jacve.dyndns.org:9085":
                Empresa = "JACVE";
                break;
            case "http://vipla.ath.cx:9085":
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
            case "http://bpr.ath.cx:9095":
                Empresa = "BPR";
                break;
            case "http://vazquin.ath.cx:9085":
                Empresa = "VAZQUIN";
                break;
            case "http://pesbac.ath.cx:9095":
                Empresa = "PESBAC";
                break;

            default:
                break;
        }


        // Obtener el refreshLayout
        refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefresh);

// Iniciar la tarea asíncrona al revelar el indicador
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new HackingBackgroundTaskk().execute();
                    }

                }
        );

        // Obtener el refreshLayout
        refreshLayout2 = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefresh2);

// Iniciar la tarea asíncrona al revelar el indicador
        refreshLayout2.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new HackingBackgroundTaskk2().execute();
                    }

                }
        );
        refreshLayout.setColorSchemeResources(R.color.ColorRojoTenue);
        ButtonCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientesOcular.setVisibility(View.VISIBLE);
                TodosOcultar.setVisibility(View.GONE);
                ButtonCliente.setBackgroundColor(Color.BLACK);
                ButtonTodos.setBackgroundColor(Color.RED);
            }
        });
        ButtonTodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClientesOcular.setVisibility(View.GONE);
                TodosOcultar.setVisibility(View.VISIBLE);
                ButtonCliente.setBackgroundColor(Color.RED);
                ButtonTodos.setBackgroundColor(Color.BLACK);
            }
        });

    }


    private void setupLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        // No iniciamos actualizaciones continuas aquí
    }

    private void obtenerUbicacionActual(OnLocationObtainedListener listener) {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1001);
            listener.onLocationObtained(null);
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(getActivity(), location -> {
                    if (location != null) {
                        Log.d("LOCATION_OBTENIDA",
                                "Ubicación obtenida: " + location.getLatitude() + ", " + location.getLongitude());
                        listener.onLocationObtained(location);
                    } else {
                        // Si getLastLocation() retorna null, intentamos con requestLocationUpdates una vez
                        obtenerUbicacionFresca(listener);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("LOCATION_ERROR", "Error obteniendo ubicación: " + e.getMessage());
                    listener.onLocationObtained(null);
                });
    }

    private void obtenerUbicacionFresca(OnLocationObtainedListener listener) {
        // Verificar permiso explícitamente
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("PERMISSION", "Permiso de ubicación no concedido");
            listener.onLocationObtained(null);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setNumUpdates(1) // Solo una actualización
                .setInterval(0); // Inmediato

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    Location location = locationResult.getLastLocation();
                    Log.d("LOCATION_FRESCA",
                            "Ubicación fresca: " + location.getLatitude() + ", " + location.getLongitude());
                    listener.onLocationObtained(location);
                } else {
                    listener.onLocationObtained(null);
                }
                // Remover el callback después de obtener la ubicación
                try {
                    fusedLocationClient.removeLocationUpdates(this);
                } catch (SecurityException e) {
                    Log.e("SECURITY_EXCEPTION", "Error removiendo updates: " + e.getMessage());
                }
            }
        };

        try {
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        } catch (SecurityException e) {
            Log.e("SECURITY_EXCEPTION", "Error solicitando ubicación: " + e.getMessage());
            listener.onLocationObtained(null);
            return;
        }

        // Timeout por si no se obtiene ubicación en 10 segundos
        new Handler().postDelayed(() -> {
            try {
                fusedLocationClient.removeLocationUpdates(locationCallback);
            } catch (SecurityException e) {
                Log.e("SECURITY_EXCEPTION", "Error removiendo updates en timeout: " + e.getMessage());
            }
            listener.onLocationObtained(null);
        }, 10000);
    }

    // Interface para el callback de ubicación
    interface OnLocationObtainedListener {
        void onLocationObtained(Location location);
    }


    private void verificarProximidadConGeocoder(OnProximidadVerificadaListener listener) {
        if (currentLocation == null || lpeA.isEmpty() || strscliente.isEmpty()) {
            Toast.makeText(getContext(), "Error: No hay ubicación o datos del cliente", Toast.LENGTH_SHORT).show();
            listener.onProximidadVerificada(false);
            return;
        }

        // Buscar el pedido del cliente seleccionado
        for (Pedidos pedido : lpeA) {
            if (pedido.getCliente().equals(strscliente)) {

                // Si ya tiene coordenadas válidas, usarlas directamente
                if (pedido.getLatitud() != 0.0 && pedido.getLongitud() != 0.0) {
                    boolean estaCerca = calcularDistancia(pedido.getLatitud(), pedido.getLongitud());
                    listener.onProximidadVerificada(estaCerca);
                    return;
                }
                // Si no tiene coordenadas, usar Geocoder con la dirección
                else {
                    Log.d("GEOCODER", "Usando Geocoder para: " + pedido.getDireccion());
                    obtenerCoordenadasDesdeDireccion(pedido.getDireccion(), new OnCoordenadasObtenidasListener() {
                        @Override
                        public void onCoordenadasObtenidas(double latitud, double longitud) {
                            if (latitud != 0.0 && longitud != 0.0) {
                                boolean estaCerca = calcularDistancia(latitud, longitud);
                                listener.onProximidadVerificada(estaCerca);
                            } else {
                                Toast.makeText(getContext(), "No se pudo obtener la ubicación de la dirección", Toast.LENGTH_LONG).show();
                                listener.onProximidadVerificada(false);
                            }
                        }
                    });
                    return;
                }
            }
        }

        Toast.makeText(getContext(), "No se encontró el cliente seleccionado", Toast.LENGTH_SHORT).show();
        listener.onProximidadVerificada(false);
    }

    private boolean calcularDistancia(double latitudCliente, double longitudCliente) {
        try {
            float[] results = new float[1];
            Location.distanceBetween(
                    currentLocation.getLatitude(),
                    currentLocation.getLongitude(),
                    latitudCliente,
                    longitudCliente,
                    results
            );
            float distanciaMetros = results[0];

            Log.d("PROXIMIDAD_CALCULO",
                    "Distancia calculada: " + results[0] + "m" +
                            " - Coordenadas cliente: " + latitudCliente + ", " + longitudCliente +
                            " - Mi ubicación: " + currentLocation.getLatitude() + ", " + currentLocation.getLongitude());

            if (distanciaMetros <= RADIO_ENTREGA) {
                Toast.makeText(getContext(), "✓ Estás en la ubicación correcta", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                /*Toast.makeText(getContext(),
                        String.format("Debes acercarte. Distancia: %.1f metros", results[0]),
                        Toast.LENGTH_LONG).show(); */
                mostrarDialogoDistanciaSimple(distanciaMetros);
                return false;
            }
        } catch (Exception e) {
            Log.e("PROXIMIDAD_CALCULO", "Error calculando distancia: " + e.getMessage());
            Toast.makeText(getContext(), "Error calculando distancia", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // Nuevo interface para el callback
    interface OnProximidadVerificadaListener {
        void onProximidadVerificada(boolean estaCerca);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setupLocation();
            } else {
                Toast.makeText(getContext(), "Permiso de ubicación denegado", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void iniciarProcesoEntrega() {
        comentariog = null;
        estatus = null;

        builder = new AlertDialog.Builder(getContext());
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
                recibio.getText().toString();
                comentario.getText().toString();
                String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                if (!lpeA.get(0).getTelefonodos().equals("")) {

                    smsCamino = Empresa + " agradece su preferencia.\n" +
                            "" + lpeA.get(0).getNombre() + ", Su pedido con el folio " + lpeA.get(0).getFolio() + " ha sido entregado por el Repartidor " + strname + " " + strlname + " a " + recibio + ". \n" +
                            "Le deseamos un excelente día.";

                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(lpeA.get(0).getTelefonodos(), null, smsCamino, null, null);
                }

                if (!recibio.getText().toString().equals("") && !comentario.getText().toString().equals("")) {
                    for (int i = 0; i < lpeA.size(); i++) {
                        String folio = "";
                        String recibiostr = recibio.getText().toString();
                        String comentariostr = comentario.getText().toString();
                        folio = lpeA.get(i).getFolio();


                        actualizarfirmanuevo(estatus, folio, recibiostr, comentariostr, currentTime);
                    }
                    dialog.dismiss();
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
                    alerta.setMessage("El cliente a recibido su pedido").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();

                        }
                    });

                    android.app.AlertDialog titulo = alerta.create();
                    titulo.setTitle("");
                    titulo.show();
                    lpeA.clear();
                    ClientesListas.clear();
                    leerWSCONFIGURACION();

                    strscliente = "";
                    ButtonListaClientes.setText("Selecciona un cliente");

                } else {
                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
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

            }
        });

        dialog = builder.create();
        dialog.show();
    }

    private void mostrarAlerta(String mensaje, String titulo) {
        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
        alerta.setMessage(mensaje).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        android.app.AlertDialog tituloDialog = alerta.create();
        tituloDialog.setTitle(titulo);
        tituloDialog.show();
    }


    private void obtenerCoordenadasDesdeDireccion(String direccion, OnCoordenadasObtenidasListener listener) {
        if (getActivity() == null) return;

        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

        new Thread(() -> {
            try {
                List<Address> addresses = geocoder.getFromLocationName(direccion, 1);
                if (addresses != null && !addresses.isEmpty()) {
                    Address address = addresses.get(0);
                    double latitud = address.getLatitude();
                    double longitud = address.getLongitude();

                    Log.d("GEOCODER", "Coordenadas obtenidas: " + latitud + ", " + longitud + " para: " + direccion);

                    getActivity().runOnUiThread(() -> {
                        listener.onCoordenadasObtenidas(latitud, longitud);
                    });
                } else {
                    Log.d("GEOCODER", "No se encontraron coordenadas para: " + direccion);
                    getActivity().runOnUiThread(() -> {
                        listener.onCoordenadasObtenidas(0.0, 0.0);
                    });
                }
            } catch (IOException e) {
                Log.e("GEOCODER", "Error en Geocoder: " + e.getMessage());
                getActivity().runOnUiThread(() -> {
                    listener.onCoordenadasObtenidas(0.0, 0.0);
                });
            }
        }).start();
    }

    interface OnCoordenadasObtenidasListener {
        void onCoordenadasObtenidas(double latitud, double longitud);
    }


    private void mostrarDialogoDistanciaSimple(float distanciaMetros) {
        if (getActivity() == null) return;

        String distanciaFormateada = formatearDistancia(distanciaMetros);
        String mensaje;

        if (distanciaMetros > 1000) {
            mensaje = String.format("📍 Estás muy lejos\n\nDistancia actual: %s\n\nDebes acercarte a menos de 25 metros para realizar la entrega.", distanciaFormateada);
        } else if (distanciaMetros > 500) {
            mensaje = String.format("📍 Aún estás lejos\n\nDistancia actual: %s\n\nSigue acercándote a la ubicación de entrega.", distanciaFormateada);
        } else if (distanciaMetros > 100) {
            mensaje = String.format("📍 Estás en la zona\n\nDistancia actual: %s\n\nBusca la dirección específica.", distanciaFormateada);
        } else if (distanciaMetros > 50) {
            mensaje = String.format("📍 Estás cerca\n\nDistancia actual: %s\n\n¡Falta poco! Acércate más.", distanciaFormateada);
        } else {
            mensaje = String.format("📍 Casi llegas\n\nDistancia actual: %s\n\nSolo necesitas estar a menos de 25 metros.", distanciaFormateada);
        }

        new AlertDialog.Builder(getContext())
                .setTitle("Ubicación Requerida")
                .setMessage(mensaje)
                .setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setCancelable(false)
                .show();
    }

    private String formatearDistancia(float distanciaMetros) {
        if (distanciaMetros >= 1000) {
            // Mostrar en kilómetros con 1 decimal
            float distanciaKm = distanciaMetros / 1000;
            return String.format(Locale.getDefault(), "%.1f km", distanciaKm);
        } else {
            // Mostrar en metros sin decimales
            return String.format(Locale.getDefault(), "%.0f metros", distanciaMetros);
        }
    }





    private void loadInitialData() {
        if (isLoading) return;
        showLoading(true);
        startLoadingTimeout(); // Añadir timeout
        leerWSCONFIGURACION();

       /* if (isLoading) return;

        showLoading(true);
        leerWSCONFIGURACION();*/
    }

    private void showLoading(boolean show) {
        isLoading = show;

        if (getActivity() != null) {
            getActivity().runOnUiThread(() -> {
                if (show) {
                    loadingLayout.setVisibility(View.VISIBLE);
                    // Bloquear interacción con botones
                    setButtonsEnabled(false);
                } else {
                    loadingLayout.setVisibility(View.GONE);
                    // Habilitar interacción
                    setButtonsEnabled(true);
                }
            });
        }
    }

    private void setButtonsEnabled(boolean enabled) {
        if (getView() != null) {
            ButtonTodos.setEnabled(enabled);
            ButtonCliente.setEnabled(enabled);
            ButtonListaClientes.setEnabled(enabled);
            btn_entregar_todo.setEnabled(enabled);
        }
    }



    public class HackingBackgroundTaskk extends AsyncTask<Void, Void, Void> {

        static final int DURACION = 5 * 1000; // 3 segundos de carga

        @Override
        protected Void doInBackground(Void... params) {
            // Simulación de la carga de items
            try {
                Thread.sleep(DURACION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            showLoading(true); // Mostrar loading durante actualización
            lpeA.clear();
            LeerWs();
            leerWSListaClientes();
            ClientesListas = new ArrayList<>();
            refreshLayout.setRefreshing(false);
        }

    }


    public class HackingBackgroundTaskk2 extends AsyncTask<Void, Void, Void> {

        static final int DURACION = 5 * 1000; // 3 segundos de carga

        @Override
        protected Void doInBackground(Void... params) {
            // Simulación de la carga de items
            try {
                Thread.sleep(DURACION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            lpeA.clear();
            ClientesListas = new ArrayList<>();
            LeerWs2();
            leerWSListaClientes();
            refreshLayout2.setRefreshing(false);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isLoading = false;
        // Limpiar recursos si es necesario
        if (fusedLocationClient != null) {
            // No hay callbacks que remover
        }
    }

    private void leerWSCONFIGURACION() {
        showLoading(true);

        String url = StrServer + "/configuracion";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showLoading(false);
                try {

                    JSONObject jItem;
                    JSONObject jitems;
                    String Repartidores = "";
                    JSONObject jsonObject = new JSONObject(response);

                    jItem = jsonObject.getJSONObject("Item");
                    for (int i = 0; i < jItem.length(); i++) {
                        jitems = jItem.getJSONObject("" + i);
                        CONFIGURACION = jitems.getString("Repartidores");
                    }

                    LeerWs();

                    leerWSListaClientes();

                } catch (JSONException e) {
                    showLoading(false);
                    Toast.makeText(getActivity(), "Error al procesar configuración", Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(getActivity());
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();
                        showLoading(false);
                        handleNetworkError(error);
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();
                header.put("user", struser);
                header.put("pass", strpass);
                return header;
            }

        };
        // Configurar timeout
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000, // 15 segundos
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(getActivity()).add(postRequest);

    }


    private void leerWSListaClientes() {
        showLoading(true);

        String url = StrServer + "/listclientesrepar";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showLoading(false);
                try {

                    JSONObject jItem;
                    JSONObject jitems;
                    String Repartidores = "";
                    String clave, nom;

                    JSONObject jsonObject = new JSONObject(response);

                    if (jsonObject.length() != 0) {
                        jItem = jsonObject.getJSONObject("Repartidores");
                        for (int i = 0; i < jItem.length(); i++) {
                            jitems = jItem.getJSONObject("items" + i);
                            clave = jitems.getString("k_clave");
                            nom = jitems.getString("k_cliente");


                            ClientesListas.add(new SetAndGetListClientes(clave, nom));


                        }
                    }



                } catch (JSONException e) {
                    showLoading(false);
                    Toast.makeText(getActivity(), "Error al procesar clientes", Toast.LENGTH_SHORT).show();
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(getActivity());
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();
                        showLoading(false);
                        handleNetworkError(error);
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

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(getActivity()).add(postRequest);

    }


    public void LeerWs() {
        showLoading(true);

        lpeA.clear();
        setD.clear();
        String url = StrServer + "/consulxEn";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showLoading(false); // OCULTAR LOADING AL FINAL
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    String Nombre, telun, teld, folio, direccion, sucu, cliente, numpaq, comentario, status, direccionclave, Aviso, Hora, Minutos, Pedidos;
                    Double latitud, longitud;
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.length() != 0) {
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
                            Pedidos = jitems.getString("k_Pedido");


                            lpeA.add(new Pedidos(sucu, cliente, numpaq, Nombre, telun, teld, folio, direccion, comentario, status, direccionclave, latitud, longitud, 0, "", 0, "", Aviso, Hora, Minutos, Pedidos));
                            setD.add(direccion);


                        }


                    } else {
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
                        alerta.setMessage("").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                                lpeA.clear();
                                lista = (ListView) getView().findViewById(R.id.listaporentregar);
                                miAdaptador = new MiAdaptador(getActivity(), R.layout.diseno_item, lpeA, CONFIGURACION);
                                lista.setAdapter(miAdaptador);

                            }
                        });

                        android.app.AlertDialog titulo = alerta.create();
                        titulo.setTitle("No hay entregas");
                        titulo.show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //crea la lista
                if (lpeA.size() != 0) {
                    lista = (ListView) getView().findViewById(R.id.listaporentregar);
                    miAdaptador = new MiAdaptador(getActivity(), R.layout.diseno_item, lpeA, CONFIGURACION);
                    lista.setAdapter(miAdaptador);

                    //para el detlle de factura
                    lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int viewId = view.getId();
                            switch (viewId) {

                                case R.id.btn_detallefac:
                                    //Boton Detalles
                                    parent.getItemAtPosition(position);
                                    builder = new AlertDialog.Builder(getContext());
                                    LayoutInflater inflater = getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.detalle_fac, null);

                                    fol = dialogView.findViewById(R.id.id_folio_df);
                                    sucur = dialogView.findViewById(R.id.id_suc_df);
                                    cli = dialogView.findViewById(R.id.id_cliente_df);
                                    nom = dialogView.findViewById(R.id.id_nombre_df);
                                    npac = dialogView.findViewById(R.id.id_numpaq_df);
                                    tun = dialogView.findViewById(R.id.id_tel1_df);
                                    tdos = dialogView.findViewById(R.id.id_tel2_df);
                                    di = dialogView.findViewById(R.id.id_direccion_df);

                                    sucur.setText(lpeA.get(position).getSucursal());
                                    fol.setText(lpeA.get(position).getFolio());
                                    cli.setText(lpeA.get(position).getCliente());
                                    nom.setText(lpeA.get(position).getNombre());
                                    npac.setText(lpeA.get(position).getNumpaq());
                                    tun.setText(lpeA.get(position).getTelefonouno());
                                    tdos.setText(lpeA.get(position).getTelefonodos());
                                    di.setText(lpeA.get(position).getDireccion());


                                    builder.setView(dialogView)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {


                                                }
                                            }).setNegativeButton("ver mas", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    Sucursal = lpeA.get(position).getSucursal();
                                                    Folios = lpeA.get(position).getFolio();
                                                    Nombres = lpeA.get(position).getCliente();


                                                    detalleFactura(Sucursal, Folios, Nombres);
                                                }
                                            });
                                    dialog = builder.create();
                                    dialog.show();
                                    break;

                                case R.id.btn_iramap_lis:
                                   /* String ok = lpeA.get(position).getDireccion();
                                    Bundle extras = new Bundle();
                                    extras.putString("directlista", ok);
                                    extras.putString("nombre_direccion", lpeA.get(position).getNombre());
                                    Intent intent = new Intent(getContext(), MapsActivity.class);
                                    intent.putExtras(extras);
                                    startActivity(intent);*/


                                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getActivity());
                                    alerta.setMessage("¿Deseas ir a realizar esta entrega a este cliente?,\n Se le avisara que iras en camino").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Bundle extras = new Bundle();
                                            folioparaentregar = lpeA.get(position).getFolio();
                                            if (lpeA.get(position).getAviso().equals("N") || lpeA.get(position).getAviso().equals("")) {
                                                if (!lpeA.get(position).getTelefonodos().equals("")) {
                                                    smsCamino = "En " + Empresa + " trabajamos para brindarle un mejor servicio y agradecemos su compra con el Folio " + folioparaentregar + " va en camino por el Repartidor " + strname + " " + strlname + ".";

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(lpeA.get(position).getTelefonodos(), null, smsCamino, null, null);
                                                    Aviso();
                                                }
                                            }


                                            extras.putString("directlista", lpeA.get(position).getDireccion());
                                            extras.putString("nombre_direccion", lpeA.get(position).getNombre());
                                            extras.putString("clave_cliente", lpeA.get(position).getCliente());
                                            extras.putDouble("latitud", lpeA.get(position).getLatitud());
                                            extras.putDouble("longitud", lpeA.get(position).getLongitud());
                                            extras.putString("clave_direccion", lpeA.get(position).getDireccionclave());
                                            extras.putString("folios", lpeA.get(position).getFolio());
                                            extras.putString("Telefono", lpeA.get(position).getTelefonodos());


                                            Intent intent = new Intent(getContext(), MapsSolo.class);
                                            intent.putExtras(extras);
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                    android.app.AlertDialog titulo = alerta.create();
                                    titulo.setTitle("Seleccionar este destino");
                                    titulo.show();

                                    break;

                                case R.id.btnntregar:
                                    alerta = new android.app.AlertDialog.Builder(getActivity());
                                    alerta.setMessage("¿Deseas entregar este pedido?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            comentariog = null;
                                            estatus = null;


                                            builder = new AlertDialog.Builder(getContext());
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
                                                    recibio.getText().toString();
                                                    comentario.getText().toString();

                                                    if (!lpeA.get(position).getTelefonodos().equals("")) {

                                                        smsCamino = Empresa + " agradece su preferencia.\n" +
                                                                "" + lpeA.get(position).getNombre() + ", Su pedido con el folio " + lpeA.get(position).getFolio() + " ha sido entregado por el Repartidor " + strname + " " + strlname + " a " + recibio + ". \n" +
                                                                "Le deseamos un excelente día.";

                                                        SmsManager smsManager = SmsManager.getDefault();
                                                        smsManager.sendTextMessage(lpeA.get(position).getTelefonodos(), null, smsCamino, null, null);
                                                    }
                                                    if (!recibio.getText().toString().equals("") && !comentario.getText().toString().equals("")) {

                                                        editor.putString("recibio", recibio.getText().toString());
                                                        editor.putString("entregoSucursal", lpeA.get(position).getSucursal());
                                                        editor.putString("entregoCliente", lpeA.get(position).getCliente());
                                                        editor.putString("entregoNombre", lpeA.get(position).getNombre());
                                                        editor.putString("entregonumteluno", lpeA.get(position).getTelefonouno());
                                                        editor.putString("entregonumteldos", lpeA.get(position).getTelefonodos());
                                                        editor.putString("entregoFolio", lpeA.get(position).getFolio());
                                                        editor.putString("entregoDirec", lpeA.get(position).getDireccion());
                                                        editor.putString("entregoNumpaq", lpeA.get(position).getNumpaq());
                                                        editor.putString("Comentario", comentario.getText().toString());
                                                        editor.putInt("posicion", position);
                                                        posicion = position;

                                                        editor.commit();
                                                        editor.apply();

                                                        actualizarfirma();
                                                        dialog.dismiss();

                                                    } else {
                                                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
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


                                                }
                                            });

                                            dialog = builder.create();
                                            dialog.show();


                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                    titulo = alerta.create();
                                    titulo.setTitle("¿Realizar la entrega?");
                                    titulo.show();


                                    break;
                                case R.id.btnpendiente:


                                    alerta = new android.app.AlertDialog.Builder(getActivity());
                                    alerta.setMessage("¿Deseas dejar pendiente este pedido?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            comentariog = null;
                                            estatus = null;

                                            builder = new AlertDialog.Builder(getContext());
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

                                                        editor.putString("entregoFolio", lpeA.get(position).getFolio());
                                                        editor.putString("Comentario", compendiente.getText().toString());
                                                        editor.commit();
                                                        editor.apply();

                                                        actualizarfirma();
                                                        dialog.dismiss();

                                                    } else {
                                                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
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
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                    titulo = alerta.create();
                                    titulo.setTitle("¿No estaba el cliente?");
                                    titulo.show();


                                    break;

                                default:
                                    break;
                            }
                        }
                    });

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(getActivity());
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();
                        showLoading(false);
                        handleNetworkError(error);
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

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(getActivity()).add(postRequest);
    }


    private void LeerWs2() {
        showLoading(true);

        lpeA.clear();
        setD.clear();
        String url = StrServer + "/consulxEnclientes";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showLoading(false); // OCULTAR LOADING AL FINAL
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    String Nombre, telun, teld, folio, direccion, sucu, cliente, numpaq, comentario, status, direccionclave, Aviso, Hora, Minutos, Pedidos;
                    Double latitud, longitud;
                    JSONObject jsonObject = new JSONObject(response);


                    if (jsonObject.length() != 0) {
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
                            Pedidos = jitems.getString("k_Pedido");


                            lpeA.add(new Pedidos(sucu, cliente, numpaq, Nombre, telun, teld, folio, direccion, comentario, status, direccionclave, latitud, longitud, 0, "", 0, "", Aviso, Hora, Minutos, Pedidos));
                            setD.add(direccion);


                        }


                    } else {
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
                        alerta.setMessage("").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.cancel();
                                lpeA.clear();
                                lista2 = (ListView) getView().findViewById(R.id.listaporentregar2);
                                miAdaptador2 = new MiAdaptador(getActivity(), R.layout.diseno_item, lpeA, CONFIGURACION);
                                lista2.setAdapter(miAdaptador2);

                            }
                        });

                        android.app.AlertDialog titulo = alerta.create();
                        titulo.setTitle("No hay entregas");
                        titulo.show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                //crea la lista
                if (lpeA.size() != 0) {
                    lista2 = (ListView) getView().findViewById(R.id.listaporentregar2);
                    miAdaptador2 = new MiAdaptador(getActivity(), R.layout.diseno_item, lpeA, CONFIGURACION);
                    lista2.setAdapter(miAdaptador2);

                    //para el detlle de factura
                    lista2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int viewId = view.getId();
                            switch (viewId) {

                                case R.id.btn_detallefac:
                                    //Boton Detalles
                                    parent.getItemAtPosition(position);
                                    builder = new AlertDialog.Builder(getContext());
                                    LayoutInflater inflater = getLayoutInflater();
                                    View dialogView = inflater.inflate(R.layout.detalle_fac, null);

                                    fol = dialogView.findViewById(R.id.id_folio_df);
                                    sucur = dialogView.findViewById(R.id.id_suc_df);
                                    cli = dialogView.findViewById(R.id.id_cliente_df);
                                    nom = dialogView.findViewById(R.id.id_nombre_df);
                                    npac = dialogView.findViewById(R.id.id_numpaq_df);
                                    tun = dialogView.findViewById(R.id.id_tel1_df);
                                    tdos = dialogView.findViewById(R.id.id_tel2_df);
                                    di = dialogView.findViewById(R.id.id_direccion_df);

                                    sucur.setText(lpeA.get(position).getSucursal());
                                    fol.setText(lpeA.get(position).getFolio());
                                    cli.setText(lpeA.get(position).getCliente());
                                    nom.setText(lpeA.get(position).getNombre());
                                    npac.setText(lpeA.get(position).getNumpaq());
                                    tun.setText(lpeA.get(position).getTelefonouno());
                                    tdos.setText(lpeA.get(position).getTelefonodos());
                                    di.setText(lpeA.get(position).getDireccion());


                                    builder.setView(dialogView)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {


                                                }
                                            }).setNegativeButton("ver mas", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    Sucursal = lpeA.get(position).getSucursal();
                                                    Folios = lpeA.get(position).getFolio();
                                                    Nombres = lpeA.get(position).getCliente();


                                                    detalleFactura(Sucursal, Folios, Nombres);
                                                }
                                            });
                                    dialog = builder.create();
                                    dialog.show();
                                    break;

                                case R.id.btn_iramap_lis:
                                   /* String ok = lpeA.get(position).getDireccion();
                                    Bundle extras = new Bundle();
                                    extras.putString("directlista", ok);
                                    extras.putString("nombre_direccion", lpeA.get(position).getNombre());
                                    Intent intent = new Intent(getContext(), MapsActivity.class);
                                    intent.putExtras(extras);
                                    startActivity(intent);*/


                                    android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getActivity());
                                    alerta.setMessage("¿Deseas ir a realizar esta entrega a este cliente?,\n Se le avisara que iras en camino").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Bundle extras = new Bundle();
                                            folioparaentregar = lpeA.get(position).getFolio();
                                            if (lpeA.get(position).getAviso().equals("N") || lpeA.get(position).getAviso().equals("")) {
                                                if (!lpeA.get(position).getTelefonodos().equals("")) {
                                                    smsCamino = "En " + Empresa + " trabajamos para brindarle un mejor servicio y agradecemos su compra con el Folio " + folioparaentregar + " va en camino por el Repartidor " + strname + " " + strlname + ".";

                                                    SmsManager smsManager = SmsManager.getDefault();
                                                    smsManager.sendTextMessage(lpeA.get(position).getTelefonodos(), null, smsCamino, null, null);
                                                    Aviso();
                                                }
                                            }


                                            extras.putString("directlista", lpeA.get(position).getDireccion());
                                            extras.putString("nombre_direccion", lpeA.get(position).getNombre());
                                            extras.putString("clave_cliente", lpeA.get(position).getCliente());
                                            extras.putDouble("latitud", lpeA.get(position).getLatitud());
                                            extras.putDouble("longitud", lpeA.get(position).getLongitud());
                                            extras.putString("clave_direccion", lpeA.get(position).getDireccionclave());
                                            extras.putString("folios", lpeA.get(position).getFolio());
                                            extras.putString("Telefono", lpeA.get(position).getTelefonodos());


                                            Intent intent = new Intent(getContext(), MapsSolo.class);
                                            intent.putExtras(extras);
                                            startActivity(intent);
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                    android.app.AlertDialog titulo = alerta.create();
                                    titulo.setTitle("Seleccionar este destino");
                                    titulo.show();

                                    break;

                                case R.id.btnntregar:
                                    alerta = new android.app.AlertDialog.Builder(getActivity());
                                    alerta.setMessage("¿Deseas entregar este pedido?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            comentariog = null;
                                            estatus = null;


                                            builder = new AlertDialog.Builder(getContext());
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
                                                    recibio.getText().toString();
                                                    comentario.getText().toString();

                                                    if (!lpeA.get(position).getTelefonodos().equals("")) {

                                                        smsCamino = Empresa + " agradece su preferencia.\n" +
                                                                "" + lpeA.get(position).getNombre() + ", Su pedido con el folio " + lpeA.get(position).getFolio() + " ha sido entregado por el Repartidor " + strname + " " + strlname + " a " + recibio + ". \n" +
                                                                "Le deseamos un excelente día.";

                                                        SmsManager smsManager = SmsManager.getDefault();
                                                        smsManager.sendTextMessage(lpeA.get(position).getTelefonodos(), null, smsCamino, null, null);
                                                    }
                                                    if (!recibio.getText().toString().equals("") && !comentario.getText().toString().equals("")) {

                                                        editor.putString("recibio", recibio.getText().toString());
                                                        editor.putString("entregoSucursal", lpeA.get(position).getSucursal());
                                                        editor.putString("entregoCliente", lpeA.get(position).getCliente());
                                                        editor.putString("entregoNombre", lpeA.get(position).getNombre());
                                                        editor.putString("entregonumteluno", lpeA.get(position).getTelefonouno());
                                                        editor.putString("entregonumteldos", lpeA.get(position).getTelefonodos());
                                                        editor.putString("entregoFolio", lpeA.get(position).getFolio());
                                                        editor.putString("entregoDirec", lpeA.get(position).getDireccion());
                                                        editor.putString("entregoNumpaq", lpeA.get(position).getNumpaq());
                                                        editor.putString("Comentario", comentario.getText().toString());
                                                        editor.putInt("posicion", position);
                                                        posicion = position;

                                                        editor.commit();
                                                        editor.apply();

                                                        actualizarfirma();
                                                        dialog.dismiss();

                                                    } else {
                                                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
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


                                                }
                                            });

                                            dialog = builder.create();
                                            dialog.show();
                                        }
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                    titulo = alerta.create();
                                    titulo.setTitle("¿Realizar la entrega?");
                                    titulo.show();


                                    break;
                                case R.id.btnpendiente:


                                    alerta = new android.app.AlertDialog.Builder(getActivity());
                                    alerta.setMessage("¿Deseas dejar pendiente este pedido?").setCancelable(false).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            comentariog = null;
                                            estatus = null;

                                            builder = new AlertDialog.Builder(getContext());
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

                                                        editor.putString("entregoFolio", lpeA.get(position).getFolio());
                                                        editor.putString("Comentario", compendiente.getText().toString());
                                                        editor.commit();
                                                        editor.apply();

                                                        actualizarfirma();
                                                        dialog.dismiss();

                                                    } else {
                                                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
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
                                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();
                                        }
                                    });

                                    titulo = alerta.create();
                                    titulo.setTitle("¿No estaba el cliente?");
                                    titulo.show();


                                    break;

                                default:
                                    break;
                            }
                        }
                    });

                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(getActivity());
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();
                        showLoading(false);
                        handleNetworkError(error);
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
                params.put("clientes", strscliente);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));


        Volley.newRequestQueue(getActivity()).add(postRequest);
    }


    private void Aviso() {

        showLoading(true);

        String url = StrServer + "/Aviso";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showLoading(false); // OCULTAR LOADING AL FINAL
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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(getActivity());
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();
                        showLoading(false);
                        handleNetworkError(error);
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
        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(postRequest);
    }


    private void detalleFactura(String Sucursal, String Folios, String Nombres) {
        showLoading(true);

        int pos;
        ldf.clear();


        String url = StrServer + "/detallefac";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("MissingInflatedId")
            @Override
            public void onResponse(String response) {
                showLoading(false); // OCULTAR LOADING AL FINAL
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(response);

                    jfacturas = jsonObject.getJSONObject("Repartidores");
                    for (int i = 0; i < jfacturas.length(); i++) {
                        jitems = jfacturas.getJSONObject("items" + i);
                        producto = jitems.getString("k_Producto");
                        descripcion = jitems.getString("k_Descripcion");
                        cantidad = jitems.getString("k_Cantidad");
                        if (StrServer.equals("http://autotop.ath.cx:9090") || StrServer.equals("http://autotop.ath.cx:9085") || StrServer.equals("http://autotop.ath.cx:9080")) {
                            preciounitario = jitems.getString("k_preciou");
                            preciototal = jitems.getString("k_preciototal");
                        }

                        ldf.add(new Mdestallefac(producto, descripcion, cantidad, preciounitario, preciototal));
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogVieww = inflater.inflate(R.layout.ver_mas_df, null);

                lisdf = (ListView) dialogVieww.findViewById(R.id.lis_detfac);

                txtpu = (TextView) dialogVieww.findViewById(R.id.txvpru);
                txtpt = (TextView) dialogVieww.findViewById(R.id.txvprt);
                if (StrServer.equals("http://autotop.ath.cx:9090") || StrServer.equals("http://autotop.ath.cx:9085") || StrServer.equals("http://autotop.ath.cx:9080")) {
                    txtpu.setVisibility(View.VISIBLE);
                    txtpt.setVisibility(View.VISIBLE);
                }
                AdapeterDetallefac miAdaptador = new AdapeterDetallefac(getActivity(), R.layout.disenodetfac, ldf, StrServer);
                lisdf.setAdapter(miAdaptador);

                builder.setView(dialogVieww)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        });
                dialog = builder.create();
                dialog.show();
                editor.remove("Clientes");
                editor.remove("Folios");

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(getActivity());
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();
                        showLoading(false);
                        handleNetworkError(error);
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
                params.put("cliente", Nombres);
                params.put("folio", Folios);
                return params;
            }
        };

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                15000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));



        Volley.newRequestQueue(getActivity()).add(postRequest);
    }


    private void actualizarfirma() {
        showLoading(true); // MOSTRAR LOADING DURANTE LA OPERACIÓN


        folioconfirma = preference.getString("entregoFolio", "");
        recibiofir = preference.getString("recibio", "");
        comentariog = preference.getString("Comentario", "");
        int posi = preference.getInt("posicion", 0);

        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        String url = StrServer + "/recibeR";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showLoading(false);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    mensajes = jsonObject.getString("Repartidores");
                    if (!estatus.equals("P")) {
                        lpeA.remove(posicion);
                        miAdaptador = new MiAdaptador(getActivity(), R.layout.diseno_item, lpeA, CONFIGURACION);
                        lista.setAdapter(miAdaptador);
                    } else {
                        Intent intent = new Intent(getContext(), Principal.class);

                        getActivity().overridePendingTransition(0, 0);
                        startActivity(intent);
                        getActivity().overridePendingTransition(0, 0);
                        getActivity().finish();
                    }

                } catch (JSONException e) {
                    showLoading(false);
                    Toast.makeText(getActivity(), "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                    //throw new RuntimeException(e);
                }

                android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
                alerta.setMessage(mensajes).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
//                        lpeA.remove(posi);

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
                        AlertDialog.Builder alerta1 = new AlertDialog.Builder(getActivity());
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
                params.put("folio", folioconfirma);
                params.put("recibe", recibiofir);
                params.put("status", estatus);
                params.put("comentario", comentariog);
                params.put("hora", currentTime);
                return params;
            }
        };
        // Configurar para no cachear
        postRequest.setShouldCache(false);

        // Agregar a la cola
        if (getActivity() != null) {
            Volley.newRequestQueue(getActivity()).add(postRequest);
        }

        //Volley.newRequestQueue(getActivity()).add(postRequest);
    }


    private void actualizarfirmanuevo(String estatus, String folio, String recibiostr, String comentariostr, String currentTime) {


        String url = StrServer + "/recibeR";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showLoading(false); // OCULTAR LOADING
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    mensajes = jsonObject.getString("Repartidores");


                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLoading(false); // OCULTAR LOADING EN ERROR

                        // Manejar error con reintento
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());

                        alerta.setMessage("Error de conexión. ¿Reintentar?")
                                .setCancelable(false)
                                .setPositiveButton("Sí", (dialogInterface, i) -> {
                                    // Reintentar después de 2 segundos
                                    new Handler().postDelayed(() -> {
                                        if (getActivity() != null && !getActivity().isFinishing()) {
                                            actualizarfirma();
                                        }
                                    }, 2000);
                                })
                                .setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                        alerta.show();


                     /*   AlertDialog.Builder alerta1 = new AlertDialog.Builder(getActivity());
                        alerta1.setMessage("Tiempo de espera agotado").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();

                            }
                        });
                        AlertDialog titulo1 = alerta1.create();
                        titulo1.setTitle("Error");
                        titulo1.show();

                      */
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
                params.put("folio", folio);
                params.put("recibe", recibiostr);
                params.put("status", estatus);
                params.put("comentario", comentariostr);
                params.put("hora", currentTime);
                return params;
            }
            @Override
            public RetryPolicy getRetryPolicy() {
                return new DefaultRetryPolicy(
                        15000, // 15 segundos timeout
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            }

        };
        Volley.newRequestQueue(getActivity()).add(postRequest);
    }
    private void handleNetworkError(VolleyError error) {
        if (error instanceof TimeoutError) {
            Toast.makeText(getActivity(), "Señal devil", Toast.LENGTH_SHORT).show();
            Toast.makeText(getActivity(), "Revise su conexión a internet", Toast.LENGTH_LONG).show();
        } else if (error instanceof NoConnectionError) {
            Toast.makeText(getActivity(), "Sin conexión a internet", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(getActivity(), "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("isLoading", isLoading);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            isLoading = savedInstanceState.getBoolean("isLoading", false);
            if (isLoading) {
                showLoading(true);
            }
        }
    }

    private void startLoadingTimeout() {
        new Handler().postDelayed(() -> {
            if (isLoading) {
                showLoading(false);
                Toast.makeText(getActivity(), "Timeout - Verifique su conexión", Toast.LENGTH_SHORT).show();
            }
        }, 30000); // 30 segundos timeout
    }


}