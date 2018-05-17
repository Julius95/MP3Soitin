package com.example.julius.mp3_soitin;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
public class AppDatabaseModule {

    private Context context;

    public AppDatabaseModule(Context context) {
        this.context = context;
    }

    @Provides
    AppDatabase appDatabaseProvide(){
        return AppDatabase.getInstance(context);
    }
}

@Component(modules={AppDatabaseModule.class})
interface MainActivityComponent {

    AppDatabase inject();
}
