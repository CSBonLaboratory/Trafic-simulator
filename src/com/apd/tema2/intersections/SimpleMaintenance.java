package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleMaintenance implements Intersection {

    private static SimpleMaintenance instance;

    private int maxNumberOfCars;

    private BlockingQueue<Car> direction0;

    private BlockingQueue<Car> direction1;

    private boolean show;

    private CyclicBarrier barr;

    private CyclicBarrier finishBarr;

    private AtomicInteger written0;

    private AtomicInteger written1;


    private SimpleMaintenance()
    {

    }

    public static SimpleMaintenance getInstance()
    {
        if(instance==null)
            instance=new SimpleMaintenance();

        return instance;
    }

    @Override
    public void setParams(String[] line) {
        maxNumberOfCars=Integer.parseInt(line[0]);

        direction0 = new ArrayBlockingQueue<Car>(Main.carsNo);

        direction1 = new ArrayBlockingQueue<>(Main.carsNo);

        barr = new CyclicBarrier(Main.carsNo);

        finishBarr = new CyclicBarrier(2*maxNumberOfCars);

        written0 = new AtomicInteger(0);

        written1 = new AtomicInteger(0);





    }

    public int getMaxNumberOfCars() {
        return maxNumberOfCars;
    }

    public BlockingQueue<Car> getDirection0() {
        return direction0;
    }

    public BlockingQueue<Car> getDirection1() {
        return direction1;
    }

    public CyclicBarrier getBarr() {
        return barr;
    }

    public boolean getShow() {
        return show;
    }

    public void setShow(boolean newValue)
    {
        show=newValue;
    }

    public CyclicBarrier getFinishBarr() {
        return finishBarr;
    }

    public AtomicInteger getWritten0() {
        return written0;
    }

    public AtomicInteger getWritten1() {
        return written1;
    }
}
