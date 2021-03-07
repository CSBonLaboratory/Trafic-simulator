package com.apd.tema2.factory;

import com.apd.tema2.Main;
import com.apd.tema2.entities.*;
import com.apd.tema2.intersections.*;
import com.apd.tema2.utils.Constants;

import java.awt.*;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Thread.sleep;

/**
 * Clasa Factory ce returneaza implementari ale InterfaceHandler sub forma unor
 * clase anonime.
 */
public class IntersectionHandlerFactory {

    public static IntersectionHandler getHandler(String handlerType) {
        // simple semaphore intersection
        // max random N cars roundabout (s time to exit each of them)
        // roundabout with exactly one car from each lane simultaneously
        // roundabout with exactly X cars from each lane simultaneously
        // roundabout with at most X cars from each lane simultaneously
        // entering a road without any priority
        // crosswalk activated on at least a number of people (s time to finish all of
        // them)
        // road in maintenance - 2 ways 1 lane each, X cars at a time
        // road in maintenance - 1 way, M out of N lanes are blocked, X cars at a time
        // railroad blockage for s seconds for all the cars
        // unmarked intersection
        // cars racing
        return switch (handlerType) {
            case "simple_semaphore" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    System.out.println("Car " + car.getId() + " has reached the semaphore, now waiting...");
                    try {
                        Thread.sleep(car.getWaitingTime());
                    }catch(Exception e)
                    {
                        //e.printStackTrace();
                    }
                    System.out.println("Car " + car.getId() + " has waited enough, now driving...");
                }
            };
            case "simple_n_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    try{
                        System.out.println("Car "+ car.getId()+ " has reached the roundabout, now waiting...");
                        SimpleNRoundabout.getSemaphore().acquire();

                        System.out.println("Car "+car.getId()+" has entered the roundabout");

                        Thread.sleep(SimpleNRoundabout.getInstance().getParam("roundaboutWaitingTime"));

                        System.out.println(
                         "Car "+car.getId()+" has exited the roundabout after "
                          +SimpleNRoundabout.getInstance().getParam("roundaboutWaitingTime")/1000
                          +" seconds"
                        );

                        SimpleNRoundabout.getSemaphore().release();


                    }catch(Exception e)
                    {
                        //e.printStackTrace();
                    }



                }
            };
            case "simple_strict_1_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    SimpleStrict1CarRoundabout instance = SimpleStrict1CarRoundabout.getInstance();

                    try{
                        System.out.println("Car "+ car.getId()+ " has reached the roundabout");

                        // masina de pe directia i vrea sa treaca de semaforul binar de pe poztita i
                        // din lista de semafoare
                        instance.getSemaphores().get(car.getStartDirection()).acquire();

                        // asteptam sa vina cate o masina din fieacare directie
                        instance.getBarrier().await();

                        System.out.println("Car "+car.getId()+" has entered the roundabout from lane "+car.getStartDirection());

                        Thread.sleep(instance.getRoundaboutWaitingTime());

                        System.out.println("Car "+car.getId()+" has exited the roundabout after "+instance.getRoundaboutWaitingTime()/1000+" seconds");

                        // masina din directia i elibereaza semaforul de pe pozitia i
                        instance.getSemaphores().get(car.getStartDirection()).release();



                    }catch(Exception e)
                    {
                        //e.printStackTrace();
                    }



                }
            };
            case "simple_strict_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    SimpleStrictXCarRoundabout instance = SimpleStrictXCarRoundabout.getInstance();

                    // daca runda a inceput atunci masinile care vin dupa inceperea acesteia
                    // vor astepta pana cand se termina
                    synchronized (this) {
                        while (instance.getRoundStarted().get() == true) {
                            try {
                                car.wait();
                            } catch (Exception e) {
                                //e.printStackTrace();
                            }


                        }
                        if (instance.checkIfLast(car.getStartDirection()))
                            instance.getRoundStarted().getAndSet(true);


                        System.out.println("Car " + car.getId() + " has reached the roundabout, now waiting...");

                        // aduagam masina in coada ce gestioneaza doar masinile de pe o anumita directie
                        // daca deja sunt x masini in coada pentru directia masinii atunci masina va astepta pana cand se elibereaza
                        try {
                            instance.getQueues().get(car.getStartDirection()).put(car);
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                    }

                        // asteptam ca X masini din fiecare directia sa ajunga aici folosind o bariera

                        try {
                            instance.getStartBarrier().await();
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }

                        // setam valoarea booleana ca sa marcam inceputul rundei

                        //instance.getRoundStarted().getAndSet(true);

                        System.out.println("Car " + car.getId() + " was selected to enter the roundabout from lane " + car.getStartDirection());

                        //asteptam ca toate firele sa scrie mesajul

                        try {
                            instance.getMidBarrier().await();
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }

                        System.out.println("Car " + car.getId() + " has entered the roundabout from lane " + car.getStartDirection());

                        // masina asteapta waitingTime secunde
                        try {
                            Thread.sleep(instance.getRoundaboutWaitingTime());
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }

                        System.out.println("Car " + car.getId() + " has exited the roundabout after " + instance.getRoundaboutWaitingTime() / 1000 + " seconds");

                        // scoate masina din coada ce memoreaza masinile de pe directia masinii curente
                        try {
                            instance.getQueues().get(car.getStartDirection()).take();
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }

                        // asteptam ca toate firele sa termine de scos cate o masina din coada aferenta directiei sale

                        try {
                            instance.getEndBarrier().await();
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }

                        // setam variabila booleana ca sa aratam ca runda s-a terminat si sa deschidem lacatul
                        instance.getRoundStarted().getAndSet(false);





                }
            };
            case "simple_max_x_car_roundabout" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance

                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici

                    SimpleMax instance = SimpleMax.getInstance();

                    System.out.println("Car "+car.getId()+" has reached the roundabout from lane "+car.getStartDirection());

                    try
                    {
                        instance.getQueues().get(car.getStartDirection()).put(car);
                    }catch (Exception e)
                    {

                    }

                    System.out.println("Car "+car.getId()+" has entered the roundabout from lane "+car.getStartDirection());

                    try
                    {
                        Thread.sleep(instance.getRoundaboutWaitingTime());
                    }catch (Exception e)
                    {

                    }

                    System.out.println("Car "+car.getId()+" has exited the roundabout after "+instance.getRoundaboutWaitingTime()/1000+" seconds");

                    try{
                        instance.getQueues().get(car.getStartDirection()).take();

                    }catch (Exception e)
                    {

                    }


                }
            };
            case "priority_intersection" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    // Get your Intersection instance





                    try {
                        sleep(car.getWaitingTime());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } // NU MODIFICATI

                    // Continuati de aici

                    PriorityIntersection instance = PriorityIntersection.getInstance();

                    if(car.getPriority()!=instance.HIGH)
                    {
                        instance.getNumberOfHighCars().incrementAndGet();

                        System.out.println("Car "+car.getId()+" with high priority has entered the intersection");

                        try{
                            Thread.sleep(instance.highPrioritySleepTime);
                        }catch (Exception ignored)
                        {

                        }

                        System.out.println("Car "+car.getId()+" with high priority has exited the intersection");

                        try {
                            synchronized (Main.class) {
                                try {
                                    instance.getNumberOfHighCars().decrementAndGet();
                                    car.notify();
                                } catch (Exception ignored) {

                                }
                            }
                        }catch (Exception ignored)
                        {

                        }
                    }
                    else
                    {

                        System.out.println("Car "+car.getId()+" with low priority is trying to enter the intersection...");
                        try {
                            instance.getQueue().put(car);
                        }catch (Exception ignored)
                        {

                        }
                        try {
                            synchronized (Main.class) {
                                while (instance.getNumberOfHighCars().get() != 0) {
                                    try {
                                        car.wait();
                                    } catch (Exception ignored) {

                                    }
                                }

                                try {

                                    Car newCar = instance.getQueue().take();

                                    System.out.println("Car " + newCar.getId() + " with low priority has entered the intersection");
                                    System.out.println("Car " + newCar.getId() + " with low priority has exited the intersection");


                                } catch (Exception ignored) {

                                }
                                synchronized (Main.class) {
                                    car.notify();
                                }

                            }
                        }catch (Exception ignored)
                        {

                        }
                    }


                }
            };
            case "crosswalk" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    Crosswalk instance = Crosswalk.getInstance();

                    //cat timp firul pieton nu s-a terminat continua
                    while (Main.pedestrians.isFinished()==false)
                    {
                        // daca pietonii nu trebuie sa treaca si mesajul precedent nu e green light atunci afiseaza green light
                        if(Main.pedestrians.isPass()==false && instance.getPreviousMsg().get(car.getId())!=Crosswalk.MSG.GREEN)
                        {
                            System.out.println("Car "+car.getId()+" has now green light");
                            instance.getPreviousMsg().set(car.getId(), Crosswalk.MSG.GREEN);
                        }
                        else {
                                synchronized (Main.class) {
                                    if(Main.pedestrians.isPass()==true) {

                                        while (Main.pedestrians.isPass() == true) {
                                            try {
                                                if (instance.getPreviousMsg().get(car.getId()) != Crosswalk.MSG.RED) {
                                                    System.out.println("Car " + car.getId() + " has now red light");
                                                    instance.getPreviousMsg().set(car.getId(), Crosswalk.MSG.RED);
                                                }
                                                car.wait();
                                                } catch (Exception e) {
                                                e.printStackTrace();
                                                }
                                        }
                                        car.notify();
                                    }
                                }

                        }

                    }




                    
                }
            };
            case "simple_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                   SimpleMaintenance instance = SimpleMaintenance.getInstance();
                   System.out.println("Car "+car.getId()+" from side number "+car.getStartDirection()+" has reached the bottleneck");

                    if(car.getStartDirection()==0){

                        try{
                            instance.getDirection0().put(car);
                        }catch (Exception ignored)
                        {

                        }

                    }
                    else
                    {
                        try{
                            instance.getDirection1().put(car);
                        }catch (Exception ignored)
                        {

                        }
                    }

                    // asteptam ca toate masinile sa ajunga la bariera

                    try{
                        instance.getBarr().await();
                    }catch (Exception ignored)
                    {

                    }

                    synchronized (Main.class)
                    {
                        // daca nu au fost deja scoase X masini din coada 0 atunci
                        if(instance.getWritten0().get()<instance.getMaxNumberOfCars())
                        {
                            // incrementam numarul de masini din directia 0 ce au fost scoase
                            instance.getWritten0().incrementAndGet();

                            // daca coada nu e goala si firul trebuie sa scoata masina de pe diretia 0
                            if(instance.getDirection0().size()!=0)
                            {
                                try {
                                    Car newCar = instance.getDirection0().take();
                                    System.out.println("Car "+newCar.getId()+" from side number 0 has passed the bottleneck");
                                }catch (Exception ignored)
                                {

                                }
                            }
                            else {
                                // coada este goala dar firul trebuie sa returneze din masina de pe directia 0
                                // inseamna ca suntem la ultima runda de masini si atunci scoatem de pe coada 1 ce a mai ramas
                                try
                                {
                                    Car newCar = instance.getDirection1().take();
                                    System.out.println("Car "+newCar.getId()+" from side number 1 has passed the bottleneck");
                                }catch (Exception ignored)
                                {

                                }
                            }

                            //daca am atins numarul maxim acum vine randul masinilor de pe directia 1 sa fie scoase
                            if(instance.getWritten0().get()==instance.getMaxNumberOfCars())
                                instance.getWritten1().set(0);
                        }
                        // analog ca prima situatie doar ca acum directiile sunt inversate
                        else
                        {
                            if(instance.getWritten1().get()<instance.getMaxNumberOfCars())
                            {
                                instance.getWritten1().incrementAndGet();

                                if(instance.getDirection1().size()!=0)
                                {
                                    try
                                    {
                                        Car newCar = instance.getDirection1().take();
                                        System.out.println("Car "+newCar.getId()+" from side number 1 has passed the bottleneck");
                                    }catch (Exception ignored)
                                    {

                                    }
                                }
                                else
                                {
                                    try {
                                        Car newCar = instance.getDirection0().take();
                                        System.out.println("Car "+newCar.getId()+" from side number 0 has passed the bottleneck");
                                    }catch (Exception ignored)
                                    {

                                    }
                                }

                                if(instance.getWritten1().get()==instance.getMaxNumberOfCars())
                                    instance.getWritten0().set(0);
                            }
                        }
                    }







                    
                }
            };
            case "complex_maintenance" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {
                    
                }
            };
            case "railroad" -> new IntersectionHandler() {
                @Override
                public void handle(Car car) {

                    Railroad instance = Railroad.getInstance();

                    synchronized (Main.class) {
                        try {
                            //punem masina in coada
                            instance.getQueue().put(car);

                        } catch (Exception ignored) {

                        }
                        System.out.println("Car " + car.getId() + " from side number " + car.getStartDirection() + " has stopped by the railroad");
                    }

                    //asteptam ca toate masinile sa ajunga la bariera
                    try {
                        instance.getBarrier().await();
                    }catch (Exception ignored)
                    {

                    }

                    //doar masina 0 va scrie mesajul
                    if(car.getId()==0)
                        System.out.println("The train has passed, cars can now proceed");

                    // asteptam ca si masina 0 sa termine de scris mesajul ca apoi sa apara numai mesaje de iesire
                    try {
                        instance.getBarrier().await();
                    }catch (Exception ignored)
                    {

                    }




                    synchronized (Main.class) {
                        try {

                            //scoatem din varful cozii o masina
                            Car newCar = instance.getQueue().take();
                            System.out.println("Car " + newCar.getId() + " from side number " + newCar.getStartDirection() + " has started driving");

                        } catch (Exception ignored) {

                        }
                    }





                    
                }
            };
            default -> null;
        };
    }
}
