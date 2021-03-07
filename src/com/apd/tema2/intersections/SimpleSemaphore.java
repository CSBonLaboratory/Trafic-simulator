package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

public class SimpleSemaphore implements Intersection {

    private static SimpleSemaphore instance;

    @Override
    public void setParams(String[] line) {

    }

    private SimpleSemaphore()
    {

    }

    public static SimpleSemaphore getInstance()
    {
        if(instance==null)
            instance = new SimpleSemaphore();

        return instance;
    }
}
