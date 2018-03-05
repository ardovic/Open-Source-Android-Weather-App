package com.ardovic.weatherappprototype.di;

import com.ardovic.weatherappprototype.App;
import com.ardovic.weatherappprototype.BaseActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    void inject(App app);
    void inject(BaseActivity activity);

}
