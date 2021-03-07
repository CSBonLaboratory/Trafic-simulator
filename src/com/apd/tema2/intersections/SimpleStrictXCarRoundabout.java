package com.apd.tema2.intersections;

import com.apd.tema2.entities.Car;
import com.apd.tema2.entities.Intersection;
import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.atomic.AtomicBoolean;


public class SimpleStrictXCarRoundabout implements Intersection {

    private static SimpleStrictXCarRoundabout instance;

    private ArrayList<BlockingQueue<Car>> queues;

    private int numberOfLanes;

    private int roundaboutWaitingTime;

    private int maxNumberOfCars;

    private CyclicBarrier startBarrier;

    private CyclicBarrier endBarrier;

    private CyclicBarrier midBarrier;

    private AtomicBoolean roundStarted;


    private SimpleStrictXCarRoundabout()
    {



    }

    public static SimpleStrictXCarRoundabout getInstance()
    {
        if(instance==null)
            instance=new SimpleStrictXCarRoundabout();

        return instance;
    }

    public void setParams(String[] line)
    {


        numberOfLanes=Integer.parseInt(line[0]);
        roundaboutWaitingTime=Integer.parseInt(line[1]);
        maxNumberOfCars=Integer.parseInt(line[2]);

        queues = new ArrayList<>();
        for(int i=0;i<numberOfLanes;i++)
            queues.add(new ArrayBlockingQueue<Car>(maxNumberOfCars));

        startBarrier = new CyclicBarrier(numberOfLanes*maxNumberOfCars);

        endBarrier = new CyclicBarrier(numberOfLanes*maxNumberOfCars);

        roundStarted = new AtomicBoolean(false);

        midBarrier = new CyclicBarrier(numberOfLanes*maxNumberOfCars);


        





    }


    public ArrayList<BlockingQueue<Car>> getQueues() {
        return queues;
    }

    public int getNumberOfLanes() {
        return numberOfLanes;
    }

    public int getRoundaboutWaitingTime() {
        return roundaboutWaitingTime;
    }

    public int getMaxNumberOfCars() {
        return maxNumberOfCars;
    }

    public CyclicBarrier getStartBarrier() {
        return startBarrier;
    }

    public CyclicBarrier getEndBarrier() {
        return endBarrier;
    }

    public AtomicBoolean getRoundStarted()
    {
        return roundStarted;
    }

    public CyclicBarrier getMidBarrier() {
        return midBarrier;
    }

    public boolean checkIfLast(int direction)
    {
        if(queues.get(direction).size()!=maxNumberOfCars-1)
            return false;
        for(int i=0;i<queues.size();i++)
            if(i!=direction)
                if(queues.get(i).size()<maxNumberOfCars)
                    return false;
        return true;
    }
}
