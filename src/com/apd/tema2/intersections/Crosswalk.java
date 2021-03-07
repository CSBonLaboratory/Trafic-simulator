package com.apd.tema2.intersections;

import com.apd.tema2.Main;
import com.apd.tema2.entities.Intersection;
import com.apd.tema2.entities.Pedestrians;

import java.util.ArrayList;

public class Crosswalk implements Intersection {

    private int executeTime;
    private int maxNumberOfPed;
    private static Crosswalk instance;

    public enum MSG
    {
        VOID,
        GREEN,
        RED
    }

    private ArrayList<MSG> previousMsg;

    @Override
    public void setParams(String[] line) {
        executeTime=Integer.parseInt(line[0]);
        maxNumberOfPed=Integer.parseInt(line[1]);

        Main.pedestrians = new Pedestrians(executeTime,maxNumberOfPed);

        previousMsg = new ArrayList<>();

        for(int i=0;i<Main.carsNo;i++)
        {
            previousMsg.add(MSG.VOID);
        }


    }

    private Crosswalk()
    {

    }

    public static Crosswalk getInstance()
    {
        if(instance==null)
            instance=new Crosswalk();

        return instance;
    }

    public int getExecuteTime() {
        return executeTime;
    }

    public int getMaxNumberOfPed() {
        return maxNumberOfPed;
    }

    public ArrayList<MSG> getPreviousMsg() {
        return previousMsg;
    }
}
