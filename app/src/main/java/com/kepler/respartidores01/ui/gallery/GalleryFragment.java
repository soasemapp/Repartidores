package com.kepler.respartidores01.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kepler.respartidores01.MainActivity;
import com.kepler.respartidores01.MapsActivity;
import com.kepler.respartidores01.MiAdaptador;
import com.kepler.respartidores01.Pedidos;
import com.kepler.respartidores01.Principal;
import com.kepler.respartidores01.R;
import com.kepler.respartidores01.databinding.FragmentGalleryBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    ListView lista;
    ArrayList<Pedidos> lpeA=new ArrayList<>();
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    String strfolio, strnombre, strtelefono;
    String strcodBra, StrServer;

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

        // strfolio = preference.getString("escfolios", "null");


        // generarlista();
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
        LeerWs();
    }

    /*private void generarlista() {
        Pedidos n1 = new Pedidos(strnombre,strfolio,"MOVIL");
        lpeA.add(n1);

    }*/

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

                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject jfacturas;
                    JSONArray jitems;
                    jfacturas=jsonObject.getJSONObject("Repartidores");
                    for  (int i =0;i<jfacturas.length();i++){
                        jfacturas.getString("k_Folio");

                    }

                    Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
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
}

}