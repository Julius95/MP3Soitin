package com.example.julius.mp3_soitin;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Created by Julius on 20.2.2018.
 */

public interface FragmentSwitcher {

    interface Screen<E>{
        void setContentByHelperObject(E e);
        void setContentByID(int id);
        void refresh();
        boolean isActive();
    }
    void switchTo(FragmentType type);
    void switchTo(FragmentType type, Consumer<Screen> c);
    void uiUpdate(Runnable run);
}
