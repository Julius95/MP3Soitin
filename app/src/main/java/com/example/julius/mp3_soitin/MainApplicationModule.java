package com.example.julius.mp3_soitin;

import dagger.BindsInstance;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;

@Module
public abstract class MainApplicationModule{
    @ContributesAndroidInjector
    abstract MainActivity contributeActivityInjector();
}

@Component(modules = { AndroidInjectionModule.class, MainApplicationModule.class, AppDatabaseModule.class})
interface MainApplicationComponent extends AndroidInjector<MainApplication> {
    @Component.Builder
    interface Builder {

        MainApplicationComponent build();
        @BindsInstance
        MainApplicationComponent.Builder appDatabase(AppDatabase db);
    }
}



