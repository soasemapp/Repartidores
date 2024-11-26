package com.kepler.respartidores01.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.kepler.respartidores01.R;
import com.kepler.respartidores01.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    TextView nanmereparti, apellido_repartidor, descrepartidor, textemailrepartidor;
    private SharedPreferences preference;
    String strname;
    String strlname;
    String strbran;
    String stremail;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);

        preference= getContext().getSharedPreferences("Login", Context.MODE_PRIVATE);

        strname = preference.getString("name", "null");
        strlname = preference.getString("lname", "null");
        strbran = preference.getString("NameBra", "null");
        stremail=preference.getString("email","null");



        nanmereparti = getView().findViewById(R.id.name_repartidor);
        apellido_repartidor= getView().findViewById(R.id.last_repartidor);
        descrepartidor= getView().findViewById(R.id.descr);
        textemailrepartidor=getView().findViewById(R.id.email);

        nanmereparti.setText(strname);
        apellido_repartidor.setText(strlname);
        descrepartidor.setText(strbran);
        textemailrepartidor.setText(stremail);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}