package com.kepler.respartidores01;

import static androidx.test.InstrumentationRegistry.getContext;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MiAdaptador extends BaseAdapter {
    private Context c;
    private int diseno;
    private ArrayList<Pedidos> ap;
    private String Configuracion,strpass,StrServer;

    public MiAdaptador(Context c, int diseno, ArrayList<Pedidos> ap,String Configuracion) {
        this.c = c;
        this.diseno = diseno;
        this.ap = ap;
        this.Configuracion=Configuracion;


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
        TextView tvtfolio=vistaDiseno.findViewById(R.id.txtfol);
        ImageButton irmap=vistaDiseno.findViewById(R.id.btn_iramap_lis);
        LinearLayout detafac= vistaDiseno.findViewById(R.id.btn_detallefac);
        Button entreado=vistaDiseno.findViewById(R.id.btnntregar);
        Button pendiente=vistaDiseno.findViewById(R.id.btnpendiente);
        LinearLayout viewcomentario = vistaDiseno.findViewById(R.id.viewcomentario);
        ImageView img = vistaDiseno.findViewById(R.id.imgpendiente);
        TextView id_comentario = vistaDiseno.findViewById(R.id.id_comentario);
        TextView id_timer = vistaDiseno.findViewById(R.id.txtTiempo);

            tvNombres.setText(auxi.Nombre);
            tvtelefonouno.setText(auxi.Telefonouno);
            tvtelefodos.setText(auxi.Telefonodos);
            tvtfolio.setText(auxi.Folio);
            id_comentario.setText(auxi.comentario);
            int minutos=Integer.parseInt(auxi.Minutos);
            int horas=Integer.parseInt(auxi.Horas);

            minutos =120-minutos;
        String Horas=formatearMinutosAHoraMinuto(minutos);


if (minutos<=0){
    id_timer.setText(Horas);
    id_timer.setTextColor(Color.RED);
    id_timer.setBackgroundColor(Color.BLACK);
}
if(minutos<60 && minutos>0) {
    id_timer.setText(Horas);
    id_timer.setTextColor(Color.YELLOW);
    id_timer.setBackgroundColor(Color.BLACK);

}
if (minutos>60 && minutos<120){
    id_timer.setText(Horas);
    id_timer.setTextColor(Color.GREEN);
    id_timer.setBackgroundColor(Color.BLACK);
}




            if (auxi.status.equals("P")){
                viewcomentario.setVisibility(View.VISIBLE);
                img.setVisibility(View.VISIBLE);
            }else{
                viewcomentario.setVisibility(View.GONE);
                img.setVisibility(View.GONE);
            }






            irmap.setTag(position);
            detafac.setTag(position);
            entreado.setTag(position);
            pendiente.setTag(position);

        if(Configuracion.equals("1")){
            entreado.setVisibility(View.GONE);
            pendiente.setVisibility(View.GONE);
        }else {
            entreado.setVisibility(View.VISIBLE);
            pendiente.setVisibility(View.VISIBLE);
        }

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

    public String formatearMinutosAHoraMinuto(int minutos) {

        String formato="";
        if(minutos<=0){
            formato="El tiempo expiro";
        }
        if(minutos<60 && minutos>0) {
            formato="Faltan %02d:%02d Minutos para entregar el pedido";
        }
        if (minutos>60 && minutos<120){
            formato="Una hora %02d con %02d minutos";
        }
        long horasReales = TimeUnit.MINUTES.toHours(minutos);
        long minutosReales = TimeUnit.MINUTES.toMinutes(minutos) - TimeUnit.HOURS.toMinutes(TimeUnit.MINUTES.toHours(minutos));
        return String.format(formato, horasReales, minutosReales);
    }
}
