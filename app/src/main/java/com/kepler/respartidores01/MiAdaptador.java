package com.kepler.respartidores01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class MiAdaptador extends BaseAdapter {
    private Context c;
    private int diseno;
    private ArrayList<Pedidos> ap;

    public MiAdaptador(Context c, int diseno, ArrayList<Pedidos> ap) {
        this.c = c;
        this.diseno = diseno;
        this.ap = ap;
    }

    @Override
    public int getCount() {
        return ap.size();
    }

    @Override
    public Object getItem(int position) {
        return ap.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vistaDiseno = convertView;
        if (vistaDiseno == null) {
            //hay crear la vista =diseño
            LayoutInflater layoutInflater = android.view.LayoutInflater.from(c);
            vistaDiseno = layoutInflater.inflate(diseno, null);

        }

        Pedidos auxi = ap.get(position);

        TextView tvNombres = vistaDiseno.findViewById(R.id.txtNombre);
        TextView tvtelefono =vistaDiseno.findViewById(R.id.txttelefono);

        //TextView tvfolio = vistaDiseno.findViewById(R.id.movil);

        tvNombres.setText(auxi.Nombre);
        tvtelefono.setText(auxi.Telefono);


        return vistaDiseno;
    }
}
