package com.example.julius.mp3_soitin.views;

/**
 * Created by Julius on 19.2.2018.
 */

public interface BasePresenter<E> {
    void start();
    void stop();
    void setContentByHelperObject(E e);
    void setContentByID(int id);
    void refresh();
    boolean isActive();
    boolean onBackPressed();
}
