package com.apd.tema2.factory;

import com.apd.tema2.entities.Intersection;
import com.apd.tema2.intersections.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Prototype Factory: va puteti crea cate o instanta din fiecare tip de implementare de Intersection.
 */
public class IntersectionFactory {
    private static Map<String, Intersection> cache = new HashMap<>();

    static {
            cache.put("simple_semaphore",SimpleSemaphore.getInstance());
            cache.put("simple_n_roundabout",SimpleNRoundabout.getInstance());
            cache.put("simple_strict_1_car_roundabout",SimpleStrict1CarRoundabout.getInstance());
            cache.put("simple_strict_x_car_roundabout",SimpleStrictXCarRoundabout.getInstance());
            cache.put("simple_max_x_car_roundabout",SimpleMax.getInstance());
            cache.put("priority_intersection",PriorityIntersection.getInstance());
            cache.put("crosswalk",Crosswalk.getInstance());
            cache.put("simple_maintenance",SimpleMaintenance.getInstance());
            cache.put("railroad",Railroad.getInstance());

    }

    public static Intersection getIntersection(String handlerType) {
        return cache.get(handlerType);
    }

}
