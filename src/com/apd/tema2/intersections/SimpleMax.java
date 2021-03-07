package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;

public class SimpleMax implements Intersection {

    private int numberOfLanes;
    private int maxNumberOfCars;
    private int roundaboutWaitingTime;
    private static SimpleMax instance;

    private ArrayList<BlockingQueue<Car>> queues;

    private SimpleMax()
    {

    }
    public static SimpleMax getInstance()
    {
        if(instance==null)
            instance=new SimpleMax();

        return instance;
    }
    @Override
    public void setParams(String[] line) {

        numberOfLanes = Integer.parseInt(line[0]);
        roundaboutWaitingTime = Integer.parseInt(line[1]);
        maxNumberOfCars = Integer.parseInt(line[2]);


        queues = new ArrayList<>();

        for(int i=0;i<numberOfLanes;i++)
            queues.add(new ArrayBlockingQueue<Car>(maxNumberOfCars));



    }

    public int getMaxNumberOfCars() {
        return maxNumberOfCars;
    }

    public int getRoundaboutWaitingTime() {
        return roundaboutWaitingTime;
    }

    public int getNumberOfLanes() {
        return numberOfLanes;
    }

    public ArrayList<BlockingQueue<Car>> getQueues() {
        return queues;
    }
}
