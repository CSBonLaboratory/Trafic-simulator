package com.apd.tema2.entities;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Utilizata pentru a uniformiza tipul de date ce ajuta la definirea unei intersectii / a unui task.
 * Implementarile acesteia vor contine variabile specifice task-ului, respectiv mecanisme de sincronizare.
 */
public interface Intersection {


    ConcurrentHashMap<String,Integer> params = new ConcurrentHashMap<>();

    void setParams(String[] line);

    default Integer getParam(String key)
    {
        return params.get(key);
    }



}
