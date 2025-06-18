package com.example.tienda_ropa.application;

import android.app.Application;
import android.content.Context;

import androidx.appcompat.app.AppCompatDelegate;
import com.google.firebase.FirebaseApp;

public class TiendaRopaApplication extends Application {
    private static TiendaRopaApplication instance;
    private static Context appContext;

    public static TiendaRopaApplication getInstance() {
        return instance;
    }

    public static Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context mAppContext) {
        this.appContext = mAppContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        FirebaseApp.initializeApp(this);
        this.setAppContext(getApplicationContext());
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }
}
