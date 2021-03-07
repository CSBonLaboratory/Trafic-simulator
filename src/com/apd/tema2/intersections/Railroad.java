package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;

public class Railroad implements Intersection {

    private static Railroad instance;

    private BlockingQueue<Car> queue;

    private CyclicBarrier barrier;
    @Override
    public void setParams(String[] line) {
        queue = new ArrayBlockingQueue<Car>(Main.carsNo);

        barrier = new CyclicBarrier(Main.carsNo);

    }

    public static Railroad getInstance() {
        if(instance==null)
            instance = new Railroad();

        return instance;
    }

    public BlockingQueue<Car> getQueue() {
        return queue;
    }

    public CyclicBarrier getBarrier() {
        return barrier;
    }
}
