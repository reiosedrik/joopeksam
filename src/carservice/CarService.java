package carservice;

import car.Car;
import car.Engine;
import datacenter.DataCenter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class CarService {

    private List<Car> carsWaiting = new ArrayList<>();
    private String name;
    private int timesUsed = 0;
    private List<Car> carsThatHaveUsedThis = new ArrayList<>();
    private DataCenter dataCenter;

    public CarService(String name) {
        this.name = name + " Car Service";
        this.dataCenter = dataCenter;
    }

    public void waitInLine(Car car) {
        synchronized (carsWaiting) {
            carsWaiting.add(car);
        }
    }

    public boolean isFirstInLine(Car car) {
        return carsWaiting.indexOf(car) == 0;
    }

    public synchronized void use(Car car) throws InterruptedException {
        if (car.needsEconomicEngine()) {
            car.setEngine(getNewEngineForCar());
        }
        synchronized (car) {
            car.wait(50);
        }
        carsWaiting.remove(car);
        if (carsWaiting.size() > 0) {
            Car nextInLine = carsWaiting.get(0);
            if (nextInLine != null) {
                synchronized (nextInLine) {
                    nextInLine.notify();
                }
            }
        }
        timesUsed++;
        System.out.println(String.format("Car %d used %s", car.getN(), name));
//        if (timesUsed > 100) {
//            getInfoAboutServedCars(c -> c.getN() > 5);
//        }
    }

    public void getInfoAboutServedCars(Predicate<Car> p) {
        System.out.println("served cars");
    }

    private Engine getNewEngineForCar() {
        return Math.random() < 0.5 ? Engine.ELECTRIC : Engine.LEMONADE;
    }
}
