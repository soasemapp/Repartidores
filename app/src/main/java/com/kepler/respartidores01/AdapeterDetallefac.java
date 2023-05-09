package com.kepler.respartidores01;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class AdapeterDetallefac extends BaseAdapter {

    private Context c;
    private int diseno;
    private ArrayList<Mdestallefac> ap;

    public AdapeterDetallefac(Context c, int diseno, ArrayList<Mdestallefac> ap) {
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

        Mdestallefac auxi = ap.get(position);

        TextView tvproducto = vistaDiseno.findViewById(R.id.id_producto_dfvm);
        TextView tvdescripcion =vistaDiseno.findViewById(R.id.id_descripcion_dfvm);
        TextView tvcantidad =vistaDiseno.findViewById(R.id.id_cantidad_dfvm);

        tvproducto.setText(auxi.producto);
        tvdescripcion.setText(auxi.descripcion);
        tvcantidad.setText(auxi.cantidad);

        return vistaDiseno;
    }
}
