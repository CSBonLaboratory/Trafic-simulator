package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class PriorityIntersection implements Intersection {

    private int numberHigh;

    private int numberLow;

    private BlockingQueue<Car> queue;

    public final int HIGH=1;

    public final int highPrioritySleepTime = 2000;

    private AtomicInteger numberOfHighCars;

    private static PriorityIntersection instance;

    public static PriorityIntersection getInstance()
    {
        if(instance==null)
            instance=new PriorityIntersection();
        return instance;
    }
    private PriorityIntersection()
    {
        numberOfHighCars=new AtomicInteger(0);



    }

    @Override
    public void setParams(String[] line) {
        numberHigh=Integer.parseInt(line[0]);
        numberLow=Integer.parseInt(line[1]);

        queue = new ArrayBlockingQueue<Car>(numberLow);
    }

    public int getNumberHigh() {
        return numberHigh;
    }

    public int getNumberLow() {
        return numberLow;
    }

    public AtomicInteger getNumberOfHighCars() {
        return numberOfHighCars;
    }

    public BlockingQueue<Car> getQueue() {
        return queue;
    }
}
