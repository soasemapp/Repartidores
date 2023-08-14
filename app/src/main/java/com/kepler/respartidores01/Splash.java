package com.kepler.respartidores01;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import pl.droidsonroids.gif.GifImageView;

public class Splash extends AppCompatActivity {

    GifImageView imagegf;
    ImageView imgVi;
    String StrServer;
    LinearLayout Conten;
    private SharedPreferences preference;
    private SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        preference = getSharedPreferences("Login", Context.MODE_PRIVATE);
        editor = preference.edit();
        Conten = findViewById(R.id.fondoSplash);
        imgVi = findViewById(R.id.imageSplash);
        imagegf = findViewById(R.id.gifImageView);

        StrServer = preference.getString("Server", "null");

//        switch (StrServer) {
//            case "http://jacve.dyndns.org:9085":
//                Picasso.with(getApplicationContext()).
//                        load(R.drawable.jacve)
//                        .error(R.drawable.logo)
//                        .fit()
//                        .centerInside()
//                        .into(imgVi);
//                break;
//            case "http://sprautomotive.servehttp.com:9085":
//                Picasso.with(getApplicationContext()).
//                        load(R.drawable.vipla)
//                        .error(R.drawable.logo)
//                        .fit()
//                        .centerInside()
//                        .into(imgVi);
//                break;
//            case "http://cecra.ath.cx:9085":
//
//                Picasso.with(getApplicationContext()).
//                        load(R.drawable.cecra)
//                        .error(R.drawable.logo)
//                        .fit()
//                        .centerInside()
//                        .into(imgVi);
//
//                break;
//            case "http://guvi.ath.cx:9085":
//
//                Picasso.with(getApplicationContext()).
//                        load(R.drawable.guvi)
//                        .error(R.drawable.logo)
//                        .fit()
//                        .centerInside()
//                        .into(imgVi);
//
//                break;
//            case "http://cedistabasco.ddns.net:9085":
//
//                Picasso.with(getApplicationContext()).
//                        load(R.drawable.pressa)
//                        .error(R.drawable.logo)
//                        .fit()
//                        .centerInside()
//                        .into(imgVi);
//
//                break;
//            case "http://autodis.ath.cx:9085":
//
//                Picasso.with(getApplicationContext()).
//                        load(R.drawable.autodis)
//                        .error(R.drawable.logo)
//                        .fit()
//                        .centerInside()
//                        .into(imgVi);
//
//                break;
//
//
//
//
//            default:
//                Picasso.with(getApplicationContext()).
//                        load(R.drawable.logo)
//                        .error(R.drawable.logo)
//                        .fit()
//                        .centerInside()
//                        .into(imgVi);
//
//                break;
//        }
//

        switch (StrServer) {
            case "http://autotop.ath.cx:9090":
                Picasso.with(getApplicationContext()).
                        load(R.drawable.autotop)
                        .error(R.drawable.logo)
                        .fit()
                        .centerInside()
                        .into(imgVi);
                break;
            case "http://autotop.ath.cx:9085":
                Picasso.with(getApplicationContext()).
                        load(R.drawable.totalcar)
                        .error(R.drawable.logo)
                        .fit()
                        .centerInside()
                        .into(imgVi);
                break;
            case "http://autotop.ath.cx:9080":
                Picasso.with(getApplicationContext()).
                        load(R.drawable.logo)
                        .error(R.drawable.logo)
                        .fit()
                        .centerInside()
                        .into(imgVi);
                break;
            default:
                Picasso.with(getApplicationContext()).
                        load(R.drawable.logo)
                        .error(R.drawable.logo)
                        .fit()
                        .centerInside()
                        .into(imgVi);

                break;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent ScreenFir = new Intent(Splash.this, Principal.class);
                startActivity(ScreenFir);
                finish();

            }
        }, 3000);
    }

}