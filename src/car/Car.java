package car;

import datacenter.DataCenter;
import map.Crossroad;
import map.Map;
import map.Street;

import java.util.Random;

public class Car implements Runnable {

    private Engine engine;
    private Street currentStreet;
    private Crossroad currentCrossRoad;
    private Random random;
    private Map map;
    private int amountOfStreetsPassed = 0;
    private DataCenter dataCenter;
    private int n;

    public Car(DataCenter dataCenter, int n) {
        this.n = n;
        this.map = dataCenter.getMap();
        currentCrossRoad = map.getRandomEntryCrossroad();
        random = new Random();
        this.dataCenter = dataCenter;
        createRandomEngine();
        dataCenter.incrementCarCount();
    }

    private void createRandomEngine() {
        engine = Engine.values()[random.nextInt(Engine.values().length)];
    }

    private void drive() throws InterruptedException {
        driveThroughStreet();
        turnToNextStreet();
    }

    private void turnToNextStreet() {
        currentStreet = currentCrossRoad.getCurrentStreet();
        if (currentStreet != null )System.out.println(currentStreet.getName());

        currentCrossRoad = map.getNextCrossRoad(currentCrossRoad);
        currentStreet = currentCrossRoad.getNextStreet(currentStreet);
    }

    private void driveThroughStreet() throws InterruptedException {
        boolean hasDrivenThroughFiveStreetsInARow = amountOfStreetsPassed % 5 == 0;
        boolean hasDrivenThroughSevenStreetsInARow = amountOfStreetsPassed % 7 == 0;
        Thread.sleep(getRandomTimeForDrivingThroughStreet());
        amountOfStreetsPassed++;
        if (hasDrivenThroughFiveStreetsInARow) {
            sendInfoToDataCenter();
        }
//        if (hasDrivenThroughSevenStreetsInARow) {
//            if (!dataCenter.isAllowedToDriveWith(Engine engine)) {
//                waitUntilAllowedToDriveAgain();
//            }
//        }
    }

    private void waitUntilAllowedToDriveAgain() {

    }

    private void sendInfoToDataCenter() {

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
