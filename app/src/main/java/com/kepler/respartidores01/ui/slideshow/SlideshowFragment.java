package com.kepler.respartidores01.ui.slideshow;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kepler.respartidores01.AdapeterDetallefac;
import com.kepler.respartidores01.Adapterentregados;
import com.kepler.respartidores01.MapsActivity;
import com.kepler.respartidores01.Mdestallefac;
import com.kepler.respartidores01.MiAdaptador;
import com.kepler.respartidores01.Pedidos;
import com.kepler.respartidores01.PedidosEntregados;
import com.kepler.respartidores01.R;
import com.kepler.respartidores01.databinding.FragmentSlideshowBinding;
import com.kepler.respartidores01.ui.gallery.GalleryFragment;
import com.kepler.respartidores01.ui.gallery.GalleryViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlideshowFragment extends Fragment {
    ListView listapacentregados;
    ListView lisdf;
    RecyclerView lisdff;
    ArrayList<PedidosEntregados> lpE=new ArrayList<>();

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strfolio, strnombre, strtelefono1, strtelefono2, strsucursal;
    String strcodBra, StrServer, strbranch, struser, strpass, strcode;
    TextView fol,cli,nom,npac,tun,tdos,di;
    AlertDialog.Builder builder;
    AlertDialog dialog = null;
    String DFfolio, DFsucursal, DFcliente, DFnombre;
    String producto, descripcion, cantidad, entregorc;
    String entregosucu, entregofolio, entregonombre, entregocliente, entregonumpaq, entregoteluno, entregonumdos, entregodirec, entregodis;

    private SwipeRefreshLayout refreshLayout;
    public ArrayList<Mdestallefac> ldf=new ArrayList<>();


    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        preference= getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        strcodBra = preference.getString("codBra", "null");
        StrServer = preference.getString("Server", "null");
        strbranch=preference.getString("branch","");
        struser= preference.getString("user","");
        strpass=preference.getString("pass","");
        strcode=preference.getString("code","");

        //entregado y es quien lo recibio
        entregorc= preference.getString("recibio","");
        entregosucu= preference.getString("entregoSucursal","");
        entregocliente= preference.getString("entregoCliente","");
        entregonombre= preference.getString("entregoNombre","");
        entregoteluno= preference.getString("entregonumteluno","");
        entregonumdos= preference.getString("entregonumteldos","");
        entregofolio= preference.getString("entregoFolio","");
        entregodirec= preference.getString("entregoDirec","");
        entregonumpaq= preference.getString("entregoNumpaq","");

        lpE.clear();
        LeerWs();

        refreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipeRefresh);

// Iniciar la tarea asíncrona al revelar el indicador
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        new HackingBackgroundTaskken().execute();
                        //refreshLayout.setRefreshing(false);
                    }

                }
        );
        refreshLayout.setColorSchemeResources(R.color.ColorRojoTenue);

    }

    public class HackingBackgroundTaskken extends AsyncTask<Void, Void, Void> {

        static final int DURACION = 4 * 1000; // 4 segundo de carga

        @Override
        protected Void doInBackground(Void... params) {
            // Simulación de la carga de items
            try {
                Thread.sleep(DURACION);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Retornar en nuevos elementos para el adaptador
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            LeerWs();

            // Parar la animación del indicador
            refreshLayout.setRefreshing(false);
        }

    }

    private void LeerWs(){

        String url =StrServer+"/pEntregados";

        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    String Nombre, telun, teld, folio, recibio, direccion, sucursal, cliente;

                    JSONObject jsonObject = new JSONObject(response);

                    if (response.length() != 6) {
                        jfacturas = jsonObject.getJSONObject("Repartidores");
                        for (int i = 0; i < jfacturas.length(); i++) {
                            jitems = jfacturas.getJSONObject("items" + i);
                            folio = jitems.getString("k_Folio");
                            sucursal=jitems.getString("k_Sucursal");
                            cliente = jitems.getString("k_Cliente");
                            Nombre = jitems.getString("k_Nombre");
                            telun = jitems.getString("k_Telefono1");
                            teld = jitems.getString("k_Telefono2");
                            direccion=jitems.getString("k_Direccion");
                            recibio = jitems.getString("k_recibo");

                            lpE.add(new PedidosEntregados(sucursal,cliente,"",Nombre,telun,teld,folio,direccion,recibio));
                        }
                    }else{
                        if(lpE.size()==0) {
                            android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
                            alerta.setMessage("").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });

                            android.app.AlertDialog titulo = alerta.create();
                            titulo.setTitle("Sin paquetes etregados");
                            titulo.show();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if(lpE.size()!=0){
                    listapacentregados = (ListView)getView().findViewById(R.id.listaentregados);
                    Adapterentregados miAdaptador = new Adapterentregados(getActivity(), R.layout.diseno_entregados, lpE);
                    listapacentregados.setAdapter(miAdaptador);

                    listapacentregados.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            int viewId = view.getId();
                            switch (viewId) {

                                case R.id.btn_detallefac:
                                    builder = new AlertDialog.Builder(getContext());
                                    LayoutInflater inflater = getLayoutInflater();

                                    View dialogView = inflater.inflate(R.layout.detalle_fac, null);
                                    fol = dialogView.findViewById(R.id.id_folio_df);
                                    cli = dialogView.findViewById(R.id.id_cliente_df);
                                    nom = dialogView.findViewById(R.id.id_nombre_df);
                                    npac = dialogView.findViewById(R.id.id_numpaq_df);
                                    tun = dialogView.findViewById(R.id.id_tel1_df);
                                    tdos = dialogView.findViewById(R.id.id_tel2_df);
                                    di = dialogView.findViewById(R.id.id_direccion_df);

                                    fol.setText(lpE.get(position).getFolio());
                                    cli.setText(lpE.get(position).getCliente());
                                    nom.setText(lpE.get(position).getNombre());
                                    npac.setText(lpE.get(position).getNumpaq());
                                    tun.setText(lpE.get(position).getTelefonouno());
                                    tdos.setText(lpE.get(position).getTelefonodos());
                                    di.setText(lpE.get(position).getDireccion());



                                    builder.setView(dialogView)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                }
                                            }).setNegativeButton("Ver mas ", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    editor.putString("Sucursal", lpE.get(position).getSucursal());
                                                    editor.putString("Clientes",lpE.get(position).getCliente());
                                                    editor.putString("Folios",lpE.get(position).getFolio());
                                                    editor.putString("Nombres",lpE.get(position).getNombre());
                                                    editor.putInt("posicion",position);
                                                    editor.commit();
                                                    editor.apply();

                                                    detalleFactura();
                                                }
                                            });
                                    dialog = builder.create();
                                    dialog.show();
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


               // lisdff=dialogVieww.findViewById(R.id.lis_detfac);
                lisdf = dialogVieww.findViewById(R.id.lis_detfac);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}