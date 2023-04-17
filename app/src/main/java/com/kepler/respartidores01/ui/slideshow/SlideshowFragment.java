package com.kepler.respartidores01.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.kepler.respartidores01.MiAdaptador;
import com.kepler.respartidores01.Pedidos;
import com.kepler.respartidores01.R;
import com.kepler.respartidores01.databinding.FragmentSlideshowBinding;
import com.kepler.respartidores01.ui.gallery.GalleryViewModel;

import java.util.ArrayList;

public class SlideshowFragment extends Fragment {
    ListView listapacentregados;
    ArrayList<Pedidos> lpe=new ArrayList<>();


    private FragmentSlideshowBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    public void onActivityCreated(Bundle state) {
        super.onActivityCreated(state);
      //  generarlista();
        if(lpe!=null){
            listapacentregados = (ListView)getView().findViewById(R.id.listaentregados);
            MiAdaptador miAdaptador = new MiAdaptador(getActivity(), R.layout.diseno_item, lpe);
            listapacentregados.setAdapter(miAdaptador);
        }else
        {
            GalleryViewModel galleryViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
            final TextView textView =  binding.textSlideshow;
            galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}