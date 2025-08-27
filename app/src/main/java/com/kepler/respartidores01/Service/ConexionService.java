package com.kepler.respartidores01.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

public class ConexionService extends Service {

    private Handler handler;
    private Runnable checker;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        crearCanalNotificacion();

        Notification notification = new NotificationCompat.Builder(this, "conexion_channel")
                .setContentTitle("Monitoreo de Conexión")
                .setContentText("Vigilando Wi-Fi y Datos móviles")
                .setSmallIcon(android.R.drawable.stat_notify_sync)
                .build();

        startForeground(1, notification);

        handler = new Handler();
        checker = new Runnable() {
            @Override
            public void run() {
                String tipo = obtenerTipoConexion();
                if (tipo != null && !hayInternet()) {
                    mostrarNotificacionAlerta(tipo);
                }
                handler.postDelayed(this, 10000); // Verifica cada 10 segundos
            }
        };
        handler.post(checker);

        return START_STICKY;
    }

    private void crearCanalNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("conexion_channel", "Monitoreo de Conexión", NotificationManager.IMPORTANCE_LOW);
            ((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).createNotificationChannel(channel);
        }
    }

    private String obtenerTipoConexion() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getType() == ConnectivityManager.TYPE_WIFI) {
                    return "Wi-Fi";
                } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
                    return "Datos móviles";
                }
            }
        }
        return null;
    }

    private boolean hayInternet() {
        try {
            Process p = Runtime.getRuntime().exec("/system/bin/ping -c 1 8.8.8.8");
            return (p.waitFor() == 0);
        } catch (Exception e) {
            return false;
        }
    }

    private void mostrarNotificacionAlerta(String tipoConexion) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "conexion_channel")
                .setContentTitle("Sin conexión")
                .setContentText("Estás conectado a " + tipoConexion + ", pero no hay acceso a Internet.")
                .setSmallIcon(android.R.drawable.ic_dialog_alert)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(2, builder.build());
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(checker);
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
