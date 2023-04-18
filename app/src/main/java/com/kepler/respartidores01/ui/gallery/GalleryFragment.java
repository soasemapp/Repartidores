package com.kepler.respartidores01.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
    String strusr, strpass, strname, strlname, strtype, strbran, strma, StrServer, strcodBra, strcode, strdirecccion;


   public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        preference = getContext().getSharedPreferences("consultas", Context.MODE_PRIVATE);
        editor = preference.edit();

        strcodBra = preference.getString("codBra", "null");
        StrServer = preference.getString("Server", "null");

      // strfolio = preference.getString("escfolios", "null");

        generarlista();
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
    private void LeerWs(){
        String url =StrServer+"/";

        StringRequest postRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    jsonObject = jsonObject.getJSONObject("UserInfo");

                    editor.putString("name", jsonObject.getString("k_name"));
                    editor.putString("lname", jsonObject.getString("k_lname"));
                    editor.putString("type", jsonObject.getString("k_type"));
                    editor.putString("branch", jsonObject.getString("k_branch"));
                    editor.putString("email", jsonObject.getString("k_mail1"));
                    editor.putString("codBra", jsonObject.getString("k_kcode"));
                    editor.putString("NameBra", jsonObject.getString("k_dscr"));
                    editor.putString("Server", StrServer);
                    editor.commit();

                    Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap header = new HashMap();

                return header;
            }
        };
        Volley.newRequestQueue(getContext()).add(postRequest);
    }


    private void generarlista() {
        //Pedidos n1 = new Pedidos(strnombre,strfolio,"MOVIL");
       // lpeA.add(n1);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}