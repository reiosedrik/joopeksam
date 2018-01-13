package car;

import carservice.CarService;
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

    public Car(DataCenter dataCenter, Engine engine, int n) {
        this.n = n;
        this.map = dataCenter.getMap();
        currentCrossRoad = map.getRandomEntryCrossroad();
        random = new Random();
        this.dataCenter = dataCenter;
        this.engine = engine;
        dataCenter.addCar(this);
    }


    private void drive() throws InterruptedException {
        driveThroughStreet();
        turnToNextStreet();
    }

    private void turnToNextStreet() throws InterruptedException {
        getTheStreetBeforeTurning();
//        if (currentStreet!= null )System.out.println(n + " drove through " + currentStreet.getName());
        currentCrossRoad = map.getNextCrossRoad(currentCrossRoad);
        useCarServiceIfExists();
        getTheStreetAfterTurning();
    }

    private void useCarServiceIfExists() throws InterruptedException {
        if (currentCrossRoad.hasCarService()) {
            CarService service = currentCrossRoad.getCarService();
            service.waitInLine(this);
            while (!service.isFirstInLine(this)) {
                synchronized (this) {
                    wait();
                }
            }
            service.use(this);
        }
    }

    private void getTheStreetAfterTurning() {
        currentStreet = currentCrossRoad.getNextStreet(currentStreet);
    }

    private void getTheStreetBeforeTurning() {
        currentStreet = currentCrossRoad.getCurrentStreet();
//        if (currentStreet != null )System.out.println(currentStreet.getName());

    }

    private void driveThroughStreet() throws InterruptedException {
        boolean hasDrivenThroughFiveStreetsInARow = amountOfStreetsPassed % 5 == 0;
        boolean hasDrivenThroughSevenStreetsInARow = amountOfStreetsPassed % 7 == 0;
        Thread.sleep(getRandomTimeForDrivingThroughStreet());
        amountOfStreetsPassed++;
        dataCenter.increasePollutionForFiveStreets(engine);
        if (hasDrivenThroughFiveStreetsInARow) {
            sendInfoToDataCenter();
        }
        if (hasDrivenThroughSevenStreetsInARow) {
            if (!dataCenter.isDrivingAllowedWith(engine)) {
//                System.out.println("not allowed " + engine);
                synchronized (this) {
                    wait();
                }
            }
        }
    }


    private void sendInfoToDataCenter() {
        dataCenter.increasePollutionForFiveStreets(engine);
    }

    private int getRandomTimeForDrivingThroughStreet() {
        final int MIN_TIME = 3;
        final int MAX_TIME = 20;
        return random.nextInt((MAX_TIME - MIN_TIME) + 1) + MIN_TIME;
    }

    @Override
    public void run() {
        while (dataCenter.getAmountPollutionHasBeenReset() < 5) {
            try {
                drive();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public Engine getEngine() {
        return engine;
    }

    public int getN() {
        return n;
    }
}
