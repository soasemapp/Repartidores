package com.kepler.respartidores01.ui.gallery;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kepler.respartidores01.MiAdaptador;
import com.kepler.respartidores01.Pedidos;
import com.kepler.respartidores01.R;
import com.kepler.respartidores01.databinding.FragmentGalleryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    ListView lista;
    ArrayList<Pedidos> lpeA=new ArrayList<>();
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strfolio, strnombre, strtelefono1, strtelefono2, strsucursal;
    String strcodBra, StrServer;
    List<String> folios= new ArrayList<>();

    RecyclerView datosServer;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    /*public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diseno_item);
        datosServer= findViewById(R.id.datosConsul);
        GridLayoutManager gl = new GridLayoutManager(this, 1);
        datosServer.setLayoutManager(gl);

    }*/
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
        preference= getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();

        strcodBra = preference.getString("codBra", "null");
        StrServer = preference.getString("Server", "null");
        LeerWs();
        // strfolio = preference.getString("escfolios", "null");

       //generarlista();
        if(lpeA!=null){
            lista = (ListView)getView().findViewById(R.id.listaporentregar);
            MiAdaptador miAdaptador = new MiAdaptador(getActivity(), R.layout.diseno_item, lpeA);
            lista.setAdapter(miAdaptador);
        }else
        {
            GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
            final TextView textView = binding.textGallery;
            galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        }
    }

//    private void generarlista() {
//        Pedidos n1=new Pedidos("sofi","2292882","92992929929292","29292992");
//        lpeA.add(n1);
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }

    private void LeerWs(){
        String url =StrServer+"/consulxEn";
        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jfacturas;
                    JSONObject jitems;

                    String res="{'Repartidores':{'items1':{'k_Folio':'','k_Sucursal':'','k_Nombre':'','k_Telefono1':'','k_Telefono2':''},'items2':{'k_Folio':'0207394','k_Sucursal':'CASAMATRIZ','k_Nombre':'EDGARZAMBRANOTEZMOL','k_Telefono1':'2213680931','k_Telefono2':'2213680931'},'items3':{'k_Folio':'0207394','k_Sucursal':'CASAMATRIZ','k_Nombre':'EDUARDODENICIAROJAS','k_Telefono1':'2221770602','k_Telefono2':''},'items4':{'k_Folio':'0207394','k_Sucursal':'CASAMATRIZ','k_Nombre':'COMERCIALIZADORARUBAMSADECV','k_Telefono1':'2229506756','k_Telefono2':'2225184402'},'items5':{'k_Folio':'0207394','k_Sucursal':'CASAMATRIZ','k_Nombre':'JOSEROBERTOESPINOZARODRIGUEZ','k_Telefono1':'2223503539','k_Telefono2':'2226923992'},'items6':{'k_Folio':'0207394','k_Sucursal':'CASAMATRIZ','k_Nombre':'DIANAGUADALUPEOLGUINHUMANA','k_Telefono1':'2383830461','k_Telefono2':'2381220426'},'items7':{'k_Folio':'0210066','k_Sucursal':'CASAMATRIZ','k_Nombre':'RUBENANRUBIOROSALES','k_Telefono1':'7313512310','k_Telefono2':''},'items8':{'k_Folio':'0210066','k_Sucursal':'CASAMATRIZ','k_Nombre':'MARIADELOURDESRANGELGARCIA','k_Telefono1':'2211858986','k_Telefono2':'2211858986'},'items9':{'k_Folio':'0210066','k_Sucursal':'CASAMATRIZ','k_Nombre':'ARMANDOPOMPOSODEJESUS','k_Telefono1':'2211747950','k_Telefono2':'2225648160'}}}";

                    JSONObject jsonObject = new JSONObject(res);
                    jfacturas=jsonObject.getJSONObject("Repartidores");
                    for(int i=2; i<jfacturas.length(); i++){
                        jitems= jfacturas.getJSONObject("items"+i);
                       folios.add(jitems.getString("k_Folio"));
                       strsucursal= jitems.getString("k_Sucursal");
                       strnombre= jitems.getString("k_Nombre");
                       strtelefono1= jitems.getString("k_Telefono1");
                       strtelefono2= jitems.getString("k_Telefono2");

                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
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
                header.put("user","jared");
                header.put("pass","jared");
                return header;
            }
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap params = new HashMap();
                params.put("sucursal","01");
                params.put("id_repartidor","1");
                return params;
            }
        };
        Volley.newRequestQueue(getActivity()).add(postRequest);
        System.out.println(folios);
}

}