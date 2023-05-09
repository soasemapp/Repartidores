package com.kepler.respartidores01.ui.gallery;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kepler.respartidores01.AdapeterDetallefac;
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
    private FragmentGalleryBinding binding;
    ListView lista;
    ListView lisdf;
    int j;
    public ArrayList<Pedidos> lpeA=new ArrayList<>();
    public ArrayList<Mdestallefac> ldf=new ArrayList<>();

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strfolio, strnombre, strtelefono1, strtelefono2, strsucursal, struser, strcode, strbranch, strpass;
    String strcodBra, StrServer, escfolio, escnombre, escdireccion, escnumun, escnumdos =null;
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

    String DFfolio, DFsucursal, DFcliente, DFnombre;

    String producto, descripcion, cantidad, entregorc, entregofolio;


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

        entregorc= preference.getString("recibio","");
        entregofolio= preference.getString("entregoFolio","");


        LeerWs();

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
                            int v= lpeA.size();
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

                if(entregofolio!="") {
                    for (int i = 0; i < lpeA.size(); i++) {
                        if(entregofolio==lpeA.get(i).getFolio()){
                            lpeA.remove(i);
                        }
                    }
                }


                if (escfolio.length() == 7) {
                    lpeA.add(new Pedidos("", "", "", escnombre, escnumun, escnumdos, escfolio, escdireccion,""));
                }
//                Set<Pedidos>hashSet= new HashSet(lpeA);
//                lpeA.clear();
//                lpeA.addAll(hashSet);

                //crea la lista
                if (lpeA.size() != 0) {

                    lista = (ListView) getView().findViewById(R.id.listaporentregar);
                    MiAdaptador miAdaptador = new MiAdaptador(getActivity(), R.layout.diseno_item, lpeA);
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
}