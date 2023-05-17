package com.kepler.respartidores01;

import android.os.AsyncTask;

public class HackingBackgroundTask extends AsyncTask<Void, Void, Void> {
    static final int DURACION = 20 * 1000; // 3 segundos de carga

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
     // refreshLayout.setRefreshing(false);

//        // Limpiar elementos antiguos
//        adapter.clear();
//
//        // Añadir elementos nuevos
//        adapter.addAll(result);
//
//        // Parar la animación del indicador
//        refreshLayout.setRefreshing(false);
    }

}
