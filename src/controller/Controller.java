package controller;

import car.Car;
import car.Engine;
import datacenter.DataCenter;
import map.Crossroad;
import map.Map;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Controller {


    public static void main(String[] args) {
        DataCenter dataCenter = new DataCenter(Map.createNew());
        ExecutorService service = Executors.newFixedThreadPool(1);
//        for (int i = 0; i < 1; i++) {
            service.execute(new Car(dataCenter, 1));
//        }
    }
}
