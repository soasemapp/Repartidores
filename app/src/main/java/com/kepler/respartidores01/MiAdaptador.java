package com.kepler.respartidores01;

import static androidx.test.InstrumentationRegistry.getContext;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
        TextView tvtelefonouno =vistaDiseno.findViewById(R.id.txttelefono1);
        TextView tvtelefodos =vistaDiseno.findViewById(R.id.txttelefonodos);
        ImageButton irmap=vistaDiseno.findViewById(R.id.btn_iramap_lis);
        Button detafac= vistaDiseno.findViewById(R.id.btn_detallefac);
        Button entreado=vistaDiseno.findViewById(R.id.btnntregar);
        Button pendiente=vistaDiseno.findViewById(R.id.btnpendiente);


            tvNombres.setText(auxi.Nombre);
            tvtelefonouno.setText(auxi.Telefonouno);
            tvtelefodos.setText(auxi.Telefonodos);
            irmap.setTag(position);
            detafac.setTag(position);
            entreado.setTag(position);
            pendiente.setTag(position);

        irmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, 0);
            }
        });

        detafac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, 0);
            }
        });

        entreado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, 0);
            }
        });

        pendiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((ListView) parent).performItemClick(view, position, 0);
            }
        });



        return vistaDiseno;
    }
}
