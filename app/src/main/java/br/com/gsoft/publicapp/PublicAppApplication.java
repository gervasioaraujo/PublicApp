package br.com.gsoft.publicapp;

import android.app.Application;
import android.util.Log;

import com.squareup.otto.Bus;

public class PublicAppApplication extends Application {
    private static final String TAG = "PublicAppApplication";
    private static PublicAppApplication instance = null;
    private Bus bus = new Bus();

    public static PublicAppApplication getInstance() {
        return instance; // Singleton
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "PublicAppApplication.onCreate()");
        // Salva a instância para termos acesso como Singleton
        instance = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        Log.d(TAG, "PublicAppApplication.onTerminate()");
    }

    public Bus getBus() {
        return bus;
    }
}
