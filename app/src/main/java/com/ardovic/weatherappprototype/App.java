package com.ardovic.weatherappprototype;

import android.app.Application;
import com.ardovic.weatherappprototype.di.AppComponent;
import com.ardovic.weatherappprototype.di.DaggerAppComponent;

public class App extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        //pass the App instance inside @Component.Builder method so that Dagger can bind it to App type
        appComponent = DaggerAppComponent.builder().providesApp(this).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}
