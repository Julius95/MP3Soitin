package com.example.julius.mp3_soitin.data;

import com.example.julius.mp3_soitin.AppDatabase;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by Julius on 19.2.2018.
 */

public interface Repository<E> {

    void getAll(Consumer<List<E>> callback);

    void get(int id, Consumer<E> callback);

    /**
     * Synchronous method.
     * @param e
     */
    void update(E e);
    /**
     * Synchronous method.
     * @param name
     * @return
     */
    E findByName(String name);
    /**
     * Synchronous delete method. This method should not be called from main thread.
     * Deletes element from the database.
     * @param e Element to be deleted
     * @return Number of deleted rows
     */
    int delete(E e);

    void delete(E e, Consumer<Integer> callback);

    /**
     * Asynchronous delete method
     * @param e
     */
    void deleteAsync(E e);

    void add(E e, Consumer<Long> callback);

    /**
     * Synchronous insertion method. This method should not be called from main thread.
     * Inserts the given element to database.
     * @param e Element to be inserted to database
     * @return Id of the inserted element
     */
    long add(E e);


    void conversion(Function<AppDatabase, List<E>> conversionFunction, Consumer<List<E>> callback);

}
