package com.kepler.respartidores01.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kepler.respartidores01.MiAdaptador;
import com.kepler.respartidores01.Pedidos;
import com.kepler.respartidores01.R;
import com.kepler.respartidores01.databinding.FragmentGalleryBinding;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {

    private FragmentGalleryBinding binding;
    ArrayList<Pedidos> lpeA=new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        //GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
       //final TextView textView = binding.textGallery;
      // final ListView listView= binding.listaporentregar;
       //galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //binding = null;
    }
}