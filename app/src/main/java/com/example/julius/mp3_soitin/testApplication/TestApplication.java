package com.example.julius.mp3_soitin.testApplication;

import android.app.Activity;
import android.app.Application;

import com.example.julius.mp3_soitin.AppDatabase;
import com.example.julius.mp3_soitin.DaggerMainApplicationComponent;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class TestApplication extends Application implements HasActivityInjector {

    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        DaggerTestApplicationComponent.builder()
                .appDatabase(getBaseContext())
                .build()
                .inject(this);// .inject(this);*/
    }

    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
