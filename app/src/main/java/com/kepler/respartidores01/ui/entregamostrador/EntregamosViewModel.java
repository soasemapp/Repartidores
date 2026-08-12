package com.kepler.respartidores01.ui.entregamostrador;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EntregamosViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    public EntregamosViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("No hay paquetes por entregar");

    }

    public LiveData<String> getText() {

        return mText;
    }
}