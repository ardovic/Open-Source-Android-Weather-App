package com.ardovic.weatherappprototype.di;

import com.ardovic.weatherappprototype.App;
import com.ardovic.weatherappprototype.activities.BaseActivity;
import dagger.BindsInstance;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = {AppModule.class, NetworkModule.class})
public interface AppComponent {

    void inject(BaseActivity activity);

    @Component.Builder   //Use @Component.Builder when we have a Dependency which is available while building component
    interface Builder {

        @BindsInstance // Tells Dagger to bind the argument to its Type. Dagger can now provide App if requested
        Builder providesApp(App app);


        AppComponent build(); //this method is must within any @Component.Builder
    }

}
