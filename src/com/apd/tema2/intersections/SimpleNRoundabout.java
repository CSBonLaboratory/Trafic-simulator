package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.concurrent.Semaphore;

public class SimpleNRoundabout implements Intersection {

    private static SimpleNRoundabout instance;


    private static Semaphore sem;

    @Override
    public void setParams(String[] line) {
        params.put("maxNumberOfCars",Integer.parseInt(line[0]));
        params.put("roundaboutWaitingTime",Integer.parseInt(line[1]));



        sem = new Semaphore(params.get("maxNumberOfCars"));

    }

    private SimpleNRoundabout()
    {

    }

    public static SimpleNRoundabout getInstance()
    {
        if(instance==null)
            instance=new SimpleNRoundabout();

        return instance;
    }

    public static Semaphore getSemaphore() {
        return sem;
    }

}
