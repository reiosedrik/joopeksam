package car;

import map.Crossroad;
import map.Map;
import map.Street;

import java.util.Random;

public class Car implements Runnable {

    private Street currentStreet;
    private Crossroad currentCrossRoad;
    private Random random;
    private Map map;

    public Car(Map map, Crossroad crossroad) {
        currentCrossRoad = crossroad;
        this.map = map;
        random = new Random();
    }

    private void drive() throws InterruptedException {
        driveThroughStreet();
        turnToNextStreet();
    }

    private void turnToNextStreet() {
        currentStreet = currentCrossRoad.getNextStreet(currentStreet);
        currentCrossRoad = map.getNextCrossRoad(currentCrossRoad, currentStreet);
    }

    private void driveThroughStreet() throws InterruptedException {
        Thread.sleep(getRandomTimeForDrivingThroughStreet());
    }

    private int getRandomTimeForDrivingThroughStreet() {
        final int MIN_TIME = 3;
        final int MAX_TIME = 20;
        return random.nextInt((MAX_TIME - MIN_TIME) + 1) + MIN_TIME;
    }

    @Override
    public void run() {
        while (true) {
            try {
                drive();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
