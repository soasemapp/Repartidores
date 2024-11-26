package com.kepler.respartidores01.ui.slideshow;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kepler.respartidores01.AdapeterDetallefac;
import com.kepler.respartidores01.Adapterentregados;
import com.kepler.respartidores01.Mdestallefac;
import com.kepler.respartidores01.PedidosEntregados;
import com.kepler.respartidores01.R;
import com.kepler.respartidores01.databinding.FragmentSlideshowBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SlideshowFragment extends Fragment {
    ListView listapacentregados;
    ListView lisdf;
    TextView txtpu,txtpt;
    ArrayList<PedidosEntregados> lpE = new ArrayList<>();

    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strcodBra, StrServer, strbranch, struser, strpass, strcode;
    TextView fol, cli, nom, npac, tun, tdos, di;
    AlertDialog.Builder builder;
    AlertDialog dialog = null;
    String DFfolio, DFsucursal, DFcliente, DFnombre;
    String producto, descripcion, cantidad, entregorc;
    String entregosucu, entregofolio, entregonombre, entregocliente, entregonumpaq, entregoteluno, entregonumdos, entregodirec, entregodis,preciounitario="",preciototal="";;

    String fechaselec=null;
    private String fec;
    private SwipeRefreshLayout refreshLayout;
    public ArrayList<Mdestallefac> ldf=new ArrayList<>();

    Button fechas;
    Boolean banderafecha=false;
    Adapterentregados miAdaptador = null;
    TextView fechatex;
    String Sucursal,Folios,Nombres;
    android.app.AlertDialog.Builder builder6;
    android.app.AlertDialog dialog6 = null;


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

        fechatex=getView().findViewById(R.id.fechatext);

        //para obtener la fecha actual

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH) ;
        int day = c.get(Calendar.DAY_OF_MONTH);

        int mes= month + 1;
        String fecha= year + "-" + mes + "-"+day;

        SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
        Date dates;
        try {
            dates = new Date(sdff.parse(fecha).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        fechaselec=sdff.format(dates);

        lpE.clear();
        builder6 = new android.app.AlertDialog.Builder(getActivity());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.pantallacarga, null);

        LeerWs();

       String nommes= meeses(mes);

        String nomfec= day +" de " +nommes+ " de "+ year;
        fechatex.setText(nomfec);


        fechas=getView().findViewById(R.id.btm_fecha);

        fechas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Le pasamos lo que haya en las globales
                DatePickerDialog dialogoFecha = new DatePickerDialog(getContext(), listenerDeDatePicker,  year, month,  day);
                dialogoFecha.show();
            }
        });


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

        static final int DURACION = 5 * 1000; // 4 segundo de carga

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



            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH) ;
            int day = c.get(Calendar.DAY_OF_MONTH);

            int mes= month + 1;
            String fecha= year + "-" + mes + "-"+day;

            SimpleDateFormat sdff = new SimpleDateFormat("yyyy-MM-dd");
            Date dates;
            try {
                dates = new Date(sdff.parse(fecha).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            fechaselec=sdff.format(dates);

            lpE.clear();

            LeerWs();

            String nommes= meeses(mes);

            String nomfec= day +" de " +nommes+ " de "+ year;
            fechatex.setText(nomfec);

            // Parar la animación del indicador
            refreshLayout.setRefreshing(false);
        }

    }

    public void LeerWs(){
        lpE.clear();
        listapacentregados = (ListView)getView().findViewById(R.id.listaentregados);

        String url =StrServer+"/pEntregados";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;
                    String Nombre, telun, teld, folio, recibio, direccion, sucursal, cliente, fecha, horas,Comentarios;

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
                            fecha = jitems.getString("k_Fecha");
                            horas = jitems.getString("k_Hora");
                            Comentarios =jitems.getString("k_Comentario");
                                lpE.add(new PedidosEntregados(sucursal, cliente, "", Nombre, telun, teld, folio, direccion, recibio, fecha, horas,Comentarios));

                        }
                    }else{
                        if(lpE.size()==0) {
                            android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getContext());
                            alerta.setMessage("").setCancelable(false).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                    if(miAdaptador!=null) {
                                        miAdaptador.notifyDataSetChanged();
                                    }
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
                     miAdaptador = new Adapterentregados(getActivity(), R.layout.diseno_entregados, lpE);
                    listapacentregados.setAdapter(miAdaptador);

                    banderafecha=false;
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


                                                            Sucursal =lpE.get(position).getSucursal();
                                                            Folios =lpE.get(position).getFolio();
                                                            Nombres=lpE.get(position).getCliente();


                                                    detalleFactura(Sucursal,Folios,Nombres);
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
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getActivity());
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
                params.put("fecha",fechaselec);
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(postRequest);
    }

    private void detalleFactura(String Sucursal,String Folios,String Nombres){
        int pos;
        ldf.clear();




        String url =StrServer+"/detallefac";
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
                        if(StrServer.equals("http://autotop.ath.cx:9090") || StrServer.equals("http://autotop.ath.cx:9085") || StrServer.equals("http://autotop.ath.cx:9080") ) {
                            preciounitario = jitems.getString("k_preciou");
                            preciototal = jitems.getString("k_preciototal");
                        }
                        ldf.add(new Mdestallefac(producto,descripcion,cantidad,preciounitario,preciototal));
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                builder = new AlertDialog.Builder(getContext());
                LayoutInflater inflater = getLayoutInflater();
                View dialogVieww = inflater.inflate(R.layout.ver_mas_df, null);


               // lisdff=dialogVieww.findViewById(R.id.lis_detfac);
                lisdf = dialogVieww.findViewById(R.id.lis_detfac);

                txtpu=(TextView)dialogVieww.findViewById(R.id.txvpru);
                txtpt=(TextView)dialogVieww.findViewById(R.id.txvprt);
                if(StrServer.equals("http://autotop.ath.cx:9090") || StrServer.equals("http://autotop.ath.cx:9085") || StrServer.equals("http://autotop.ath.cx:9080") ) {
                    txtpu.setVisibility(View.VISIBLE);
                    txtpt.setVisibility(View.VISIBLE);
                }
                AdapeterDetallefac miAdaptador = new AdapeterDetallefac(getActivity(), R.layout.disenodetfac, ldf,StrServer);
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
                        android.app.AlertDialog.Builder alerta = new android.app.AlertDialog.Builder(getActivity());
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
                params.put("cliente",Nombres);
                params.put("folio",Folios);
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

    public DatePickerDialog.OnDateSetListener listenerDeDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int anio, int mes, int diaDelMes) {

            int y=   view.getYear();
            int m=  view.getMonth() ;
            int d=  view.getDayOfMonth();

            int mess= m+1;

            String fecha= y + "-" +mess + "-"+d;
            Date date;

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                date = new Date(sdf.parse(fecha).getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }

            fec=sdf.format(date);

            fechaselec=fec;

            LeerWs();

            String nommes= meeses(mess);

            String nomfec= d +" de " +nommes+ " de "+ y;
            fechatex.setText(nomfec);
        }
    };

    public String meeses(int valor){
        String nombremes = null;
        switch (valor){

            case 1:
                nombremes="Ene.";
                break;
            case 2:
                nombremes="Feb.";
                break;
            case 3:
                nombremes="Mar.";
                break;
            case 4:
                nombremes="Abr.";
                break;
            case 5:
                nombremes="May.";
                break;
            case 6:
                nombremes="Jun.";
                break;
            case 7:
                nombremes="Jul.";
                break;
            case 8:
                nombremes="Agto.";
                break;
            case 9:
                nombremes="Sept.";
                break;
            case 10:
                nombremes="Oct.";
                break;
            case 11:
                nombremes="Nov.";
                break;
            case 12:
                nombremes="Dic.";
                break;
        }
        return nombremes;
    }

}