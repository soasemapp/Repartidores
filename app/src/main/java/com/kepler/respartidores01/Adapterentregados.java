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

public class Adapterentregados extends BaseAdapter {
    private Context c;
    private int diseno;
    private ArrayList<PedidosEntregados> ap;

    public Adapterentregados(Context c, int diseno, ArrayList<PedidosEntregados> ap) {
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

        PedidosEntregados auxi = ap.get(position);

        TextView tvNombres = vistaDiseno.findViewById(R.id.txtNombreentreado);
        TextView tvtsuc =vistaDiseno.findViewById(R.id.id_suc);
        TextView tvtrecibio =vistaDiseno.findViewById(R.id.id_quienrecibio);
        TextView tvfolio=vistaDiseno.findViewById(R.id.id_foli_Entre);
        TextView tvfecha=vistaDiseno.findViewById(R.id.id_fecha);
        Button detafac= vistaDiseno.findViewById(R.id.btn_detallefac);


        tvNombres.setText(auxi.Nombre);
        tvtrecibio.setText(auxi.recibio);
        tvtsuc.setText(auxi.sucursal);
        tvfolio.setText(auxi.Folio);
        detafac.setTag(position);

        detafac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, 0);
            }
        });

        return vistaDiseno;
    }

}
