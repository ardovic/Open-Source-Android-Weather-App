package com.ardovic.weatherappprototype;

import android.app.Application;

import com.ardovic.weatherappprototype.di.AppComponent;
import com.ardovic.weatherappprototype.di.AppModule;
import com.ardovic.weatherappprototype.di.DaggerAppComponent;

public class App extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);

    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
