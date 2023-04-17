package com.kepler.respartidores01.ui.gallery;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.kepler.respartidores01.MiAdaptador;
import com.kepler.respartidores01.Pedidos;
import com.kepler.respartidores01.Principal;
import com.kepler.respartidores01.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    public GalleryViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("No hay paquetes por entregar");

    }

    public LiveData<String> getText() {

        return mText;
    }
}