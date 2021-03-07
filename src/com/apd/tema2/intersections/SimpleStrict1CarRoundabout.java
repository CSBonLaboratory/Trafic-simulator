package com.apd.tema2.intersections;

import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Semaphore;

public class SimpleStrict1CarRoundabout implements Intersection {

    private static SimpleStrict1CarRoundabout instance;

    private ArrayList<Semaphore> semaphores;

    private CyclicBarrier barrier;

    private int numberOfLanes;

    private int roundaboutWaitingTime;


    public static SimpleStrict1CarRoundabout getInstance()
    {
        if(instance==null)
            instance = new SimpleStrict1CarRoundabout();

        return instance;

    }

    private SimpleStrict1CarRoundabout()
    {

    }

    @Override
    public void setParams(String[] line) {

        numberOfLanes = Integer.parseInt(line[0]);
        roundaboutWaitingTime = Integer.parseInt(line[1]);

        semaphores = new ArrayList<>();

        for(int i=0;i<numberOfLanes;i++)
        {
            semaphores.add(new Semaphore(1));
        }

        barrier = new CyclicBarrier(numberOfLanes);


    }

    public ArrayList<Semaphore> getSemaphores() {
        return semaphores;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public int getNumberOfLanes()
    {
        return numberOfLanes;
    }
    public int getRoundaboutWaitingTime()
    {
        return roundaboutWaitingTime;
    }




}
