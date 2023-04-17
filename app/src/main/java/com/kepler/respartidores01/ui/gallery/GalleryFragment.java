package com.kepler.respartidores01.ui.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kepler.respartidores01.MainActivity;
import com.kepler.respartidores01.MapsActivity;
import com.kepler.respartidores01.MiAdaptador;
import com.kepler.respartidores01.Pedidos;
import com.kepler.respartidores01.Principal;
import com.kepler.respartidores01.R;
import com.kepler.respartidores01.databinding.FragmentGalleryBinding;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    private FragmentGalleryBinding binding;
    ListView lista;
    ArrayList<Pedidos> lpeA=new ArrayList<>();



   public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }
    @Override
    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
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

    private void generarlista() {
        Pedidos n1 = new Pedidos("Sofia Lizeth Jimenez serna","123445","MOVIL");
        lpeA.add(n1);
        Pedidos n2 = new Pedidos("Sofia Lizeth Jimenez serna","123445","MOVIL");
        lpeA.add(n2);
        Pedidos n3 = new Pedidos("Sofia Lizeth Jimenez serna","123445","MOVIL");
        lpeA.add(n3);
        Pedidos n4= new Pedidos("Sofia Lizeth Jimenez serna","123445","MOVIL");
        lpeA.add(n4);
        Pedidos n5= new Pedidos("Sofia Lizeth Jimenez serna","123445","MOVIL");
        lpeA.add(n5);
        Pedidos n6= new Pedidos("Sofia Lizeth Jimenez serna","123445","MOVIL");
        lpeA.add(n6);
        Pedidos n7= new Pedidos("Sofia Lizeth Jimenez serna","123445","MOVIL");
        lpeA.add(n7);
        Pedidos n8= new Pedidos("Sofia Lizeth Jimenez serna","123445","MOVIL");
        lpeA.add(n8);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}