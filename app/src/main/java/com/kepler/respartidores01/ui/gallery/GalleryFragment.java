package com.kepler.respartidores01.ui.gallery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kepler.respartidores01.AdapeterDetallefac;
import com.kepler.respartidores01.HackingBackgroundTask;
import com.kepler.respartidores01.MapsActivity;
import com.kepler.respartidores01.Mdestallefac;
import com.kepler.respartidores01.MiAdaptador;
import com.kepler.respartidores01.Pedidos;
import com.kepler.respartidores01.R;
import com.kepler.respartidores01.databinding.FragmentGalleryBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GalleryFragment extends Fragment {
    MiAdaptador miAdaptador;
    private FragmentGalleryBinding binding;
    ListView lista;
    ListView lisdf;
    int j;
    public ArrayList<Pedidos> lpeA=new ArrayList<>();
    public ArrayList<Mdestallefac> ldf=new ArrayList<>();
    String mensajes;

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strfolio, strnombre, strtelefono1, strtelefono2, strsucursal, struser, strcode, strbranch, strpass;
    String strcodBra, StrServer, escfolio, escnombre, escdireccion, escnumun, escnumdos, escnumc =null;
    List<String> folios= new ArrayList<>();
    Button detfac;
    AlertDialog.Builder builder;
    AlertDialog dialog = null;
    private TextView sucur;
    TextView fol,cli,nom,npac,tun,tdos,di;
    TextView vmpro, vmcan, vmdes;

    Bundle datosescfolio;
    Set<String> setD = new HashSet<>();
    Set<String> setN = new HashSet<>();
    Set<String> setC = new HashSet<>();
    String mensaje = "entregoSucursal";

    String DFfolio, DFsucursal, DFcliente, DFnombre;

    String producto, descripcion, cantidad, entregorc, entregofolio, entregodirec;
    String folioconfirma, sucursalonfrima, recibiofir;
    private SwipeRefreshLayout refreshLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        preference = getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        strcodBra = preference.getString("codBra", "null");
        StrServer = preference.getString("Server", "null");
        struser = preference.getString("user", "");
        strpass = preference.getString("pass", "");
        strbranch = preference.getString("branch", "");
        strcode = preference.getString("code", "");


        escnombre = preference.getString("Nombreescrito", "null");
        escfolio = preference.getString("folioescrito", "null");
        escnumun = preference.getString("Num1_escrito", "null");
        escnumdos = preference.getString("Num2escrito", "null");
        escdireccion = preference.getString("direccionescrito", "null");
        escnumc=preference.getString("numc","");


        entregorc= preference.getString("recibio","");
        entregodirec=preference.getString("entregoDirec","");

        lpeA.clear();
        LeerWs();

        // Obtener el refreshLayout
        refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefresh);

// Iniciar la tarea asíncrona al revelar el indicador
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new HackingBackgroundTaskk().execute();
                         //refreshLayout.setRefreshing(false);
                    }

                }
        );
        refreshLayout.setColorSchemeResources(R.color.ColorRojoTenue);
       // refreshLayout.setRefreshing(false);
 }

    public class HackingBackgroundTaskk extends AsyncTask<Void, Void, Void> {

        static final int DURACION = 4 * 1000; // 3 segundos de carga

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
            LeerWs();

            refreshLayout.setRefreshing(false);
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    private void LeerWs(){

        String url =StrServer+"/consulxEn";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    String Nombre, telun, teld, folio, direccion, sucu, cliente, numpaq;
                    JSONObject jsonObject = new JSONObject(response);


                    if (response.length() != 6) {
                        jfacturas = jsonObject.getJSONObject("Repartidores");
                        for (int i = 0; i <jfacturas.length(); i++) {
                            jitems = jfacturas.getJSONObject("items" + i);
                            sucu = jitems.getString("k_Sucursal");
                            folio = jitems.getString("k_Folio");
                            cliente = jitems.getString("k_Cliente");
                            Nombre = jitems.getString("k_Nombre");
                            numpaq = jitems.getString("k_nPaquetes");
                            direccion = jitems.getString("k_Direccion");
                            telun = jitems.getString("k_Telefono1");
                            teld = jitems.getString("k_Telefono2");

                            lpeA.add(new Pedidos(sucu, cliente, numpaq, Nombre, telun, teld, folio, direccion,""));
                            setD.add(direccion);
                            setN.add(Nombre);
                            setC.add(cliente);
                            int b= setN.size();
                            editor.putStringSet("Direcciones", setD);
                            editor.putStringSet("Nombres", setN);
                            editor.putStringSet("Clientes", setC);
                            editor.commit();
                            editor.apply();

                        }
                    } else {
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
                        alerta.setMessage("").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
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
                     miAdaptador = new MiAdaptador(getActivity(), R.layout.diseno_item, lpeA);
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
                                                    editor.putString("Sucursal", lpeA.get(position).getSucursal());
                                                    editor.putString("Clientes",lpeA.get(position).getCliente());
                                                    editor.putString("Folios",lpeA.get(position).getFolio());
                                                    editor.putString("Nombres",lpeA.get(position).getNombre());
                                                    editor.putInt("posicion",position);
                                                    editor.commit();
                                                    editor.apply();

                                                    detalleFactura();
                                                }
                                            });
                                    dialog = builder.create();
                                    dialog.show();
                                    break;

                                case R.id.btn_iramap_lis:
                                    String ok = lpeA.get(position).getDireccion();
                                    Bundle extras = new Bundle();
                                    extras.putString("directlista", ok);
                                    extras.putString("nombre_direccion", lpeA.get(position).getNombre());
                                    Intent intent = new Intent(getContext(), MapsActivity.class);
                                    intent.putExtras(extras);
                                    startActivity(intent);

                                    break;

                                case R.id.btnntregar:

                                    builder = new AlertDialog.Builder(getContext());
                                    LayoutInflater inflaterentrega = getLayoutInflater();
                                    View dialogViewww = inflaterentrega.inflate(R.layout.recibio_, null);
                                    builder.setView(dialogViewww);
                                    EditText recibio=dialogViewww.findViewById(R.id.quien_recibio);
                                    Button enttegar=dialogViewww.findViewById(R.id.btentre);
                                    enttegar.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            recibio.getText().toString();

                                            editor.putString("recibio",recibio.getText().toString());
                                            editor.putString("entregoSucursal",lpeA.get(position).getSucursal());
                                            editor.putString("entregoCliente",lpeA.get(position).getCliente());
                                            editor.putString("entregoNombre",lpeA.get(position).getNombre());
                                            editor.putString("entregonumteluno",lpeA.get(position).getTelefonouno());
                                            editor.putString("entregonumteldos",lpeA.get(position).getTelefonodos());
                                            editor.putString("entregoFolio",lpeA.get(position).getFolio());
                                            editor.putString("entregoDirec",lpeA.get(position).getDireccion());
                                            editor.putString("entregoNumpaq",lpeA.get(position).getNumpaq());
                                            editor.commit();
                                            editor.apply();

                                            actualizarfirma();
                                            dialog.dismiss();
                                            lpeA.remove(position);



                                        }
                                    });

                                    dialog = builder.create();
                                    dialog.show();


                                    break;
                                case R.id.btnpendiente:
                                    android.app.AlertDialog.Builder builderepen = new android.app.AlertDialog.Builder(getContext());
                                    LayoutInflater inflaterep = getLayoutInflater();
                                    builderepen.setView(inflaterep.inflate(R.layout.diseno_pendiente, null))
                                            .create().show();
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("id_repartidor",strcode);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(postRequest);
}

    private void detalleFactura(){
        int pos;
        ldf.clear();

        preference= getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        DFsucursal=preference.getString("Sucursal","");
        DFcliente=preference.getString("Clientes","");
        DFfolio=preference.getString("Folios","");
        DFnombre=preference.getString("Nombres","");
        pos=preference.getInt("posicion",0);

        String url =StrServer+"/detafactuR";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(response);

                    jfacturas = jsonObject.getJSONObject("Repartidores");
                    for (int i = 0; i <jfacturas.length(); i++) {
                        jitems = jfacturas.getJSONObject("items"+i);
                        producto = jitems.getString("k_Producto");
                        descripcion = jitems.getString("k_Descripcion");
                        cantidad = jitems.getString("k_Cantidad");
                        ldf.add(new Mdestallefac(producto,descripcion,cantidad));
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogVieww = inflater.inflate(R.layout.ver_mas_df, null);

                lisdf = (ListView) dialogVieww.findViewById(R.id.lis_detfac);
                AdapeterDetallefac miAdaptador = new AdapeterDetallefac(getActivity(), R.layout.disenodetfac, ldf);
                lisdf.setAdapter(miAdaptador);

                builder.setView(dialogVieww)
                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("cliente",DFcliente);
                params.put("folio",DFfolio);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(postRequest);
    }



    private void actualizarfirma(){

        sucursalonfrima=preference.getString("entregoSucursal","");
        folioconfirma=preference.getString("entregoFolio","");
        recibiofir=preference.getString("recibio","");

        String url =StrServer+"/recibeR";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    JSONObject jsonObject = new JSONObject(response);

                    mensajes= jsonObject.getString("Repartidores");

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
                alerta.setMessage(mensajes).setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
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
                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
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
                params.put("folio",folioconfirma);
                params.put("recibe",recibiofir);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(postRequest);
    }

}