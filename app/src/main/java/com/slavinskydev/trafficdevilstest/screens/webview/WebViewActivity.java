package com.slavinskydev.trafficdevilstest.screens.webview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.slavinskydev.trafficdevilstest.screens.game.GameActivity;
import com.slavinskydev.trafficdevilstest.screens.loader.LoaderActivity;
import com.slavinskydev.trafficdevilstest.R;

public class WebViewActivity extends AppCompatActivity {

    private static final String TAG = "";
    private WebView webView;

    private SensorManager sensorManager;
    private float accelerationValue;
    private float accelerationLastValue;
    private float shake;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorManager.registerListener(sensorEventListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);

        accelerationValue = SensorManager.GRAVITY_EARTH;
        accelerationLastValue = SensorManager.GRAVITY_EARTH;
        shake = 0.00f;


        webView = findViewById(R.id.WebView);

        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        //webView.clearCache(true);
        //webView.clearHistory();

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setAllowFileAccess(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCachePath(getApplicationContext().getFilesDir().getPath() + getPackageName() + "/cache/");
        webSettings.setAppCacheEnabled(true);

        CookieManager.getInstance();

        if (savedInstanceState == null) {
            webView.loadUrl("http://html5test.com/");
        }

    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState ){
        super.onSaveInstanceState(outState);
        webView.saveState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        webView.restoreState(savedInstanceState);
    }

    private final SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            accelerationLastValue = accelerationValue;
            accelerationValue = (float) Math.sqrt(x*x + y*y + z*z);
            float delta = accelerationValue - accelerationLastValue;
            shake = shake * 0.9f + delta;

            if (shake > 30) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                LoaderActivity.firstRunPlease();
                                Intent intent = new Intent(WebViewActivity.this, LoaderActivity.class);
                                startActivity(intent);
                                finish();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                break;
                        }
                    }
                };
                AlertDialog.Builder ab = new AlertDialog.Builder(WebViewActivity.this);
                ab.setMessage(getResources().getString(R.string.first_start))
                        .setPositiveButton(getResources().getString(R.string.answer_yes), dialogClickListener)
                        .setNegativeButton(getResources().getString(R.string.answer_no), dialogClickListener)
                        .show();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
}
