package controller;

import bird.Bird;
import car.Car;
import car.Engine;
import datacenter.DataCenter;
import map.Map;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {

    private static int TOTAL_CARS_TO_MAKE = 200;

    public static void main(String[] args) {
        DataCenter dataCenter = new DataCenter(Map.createNew());
        ExecutorService service = Executors.newFixedThreadPool(TOTAL_CARS_TO_MAKE + 1);
        service.execute(new Bird(dataCenter));

        for (int i = 0; i < TOTAL_CARS_TO_MAKE; i++) {
            service.execute(new Car(dataCenter, Engine.createRandom(i), i));
        }
        service.shutdown();
    }
}
