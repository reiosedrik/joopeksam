package car;

import carservice.CarService;
import datacenter.DataCenter;
import map.Crossroad;
import map.Map;
import map.Street;

import java.util.Random;

public class Car implements Runnable {

    Engine engine;
    Crossroad currentCrossRoad;
    Map map;
    int amountOfStreetsPassed = 0;
    DataCenter dataCenter;

    private Street currentStreet;
    private Random random;
    private int n;
    private int timesStoppedBecauseOfPollution = 0;
    private int timesDrivenThroughStreetWithBadConditions = 0;
    private boolean needsEconomicEngine = false;
    private Tires tires = Tires.DEFAULT;

    public Car(DataCenter dataCenter, Engine engine, int n) {
        this.n = n;
        this.map = dataCenter.getMap();
        currentCrossRoad = map.getRandomEntryCrossroad();
        random = new Random();
        this.dataCenter = dataCenter;
        this.engine = engine;
        dataCenter.addCar(this);
    }


    public void drive() throws InterruptedException {
        driveThroughStreet();
        turnToNextStreet();
    }

    public void turnToNextStreet() throws InterruptedException {
        getTheStreetBeforeTurning();
        currentCrossRoad = map.getNextCrossRoad(currentCrossRoad);
        if (currentStreet!= null) {
            if (timesDrivenThroughStreetWithBadConditions >= 3 && tires == Tires.DEFAULT) {
                askForHelpAndWait();
            }
        }
        useCarServiceIfExists();
        getTheStreetAfterTurning();
    }

    public void askForHelpAndWait() throws InterruptedException {
        dataCenter.askForHelp(this);
        synchronized (this) {
            wait();
        }
    }

    public void useCarServiceIfExists() throws InterruptedException {
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

    public void getTheStreetAfterTurning() {
        currentStreet = currentCrossRoad.getNextStreet(currentStreet);
    }

    public void getTheStreetBeforeTurning() {
        currentStreet = currentCrossRoad.getCurrentStreet();
//        if (currentStreet != null )System.out.println(currentStreet.getName());

    }

    public void driveThroughStreet() throws InterruptedException {
        if (currentStreet != null && currentStreet.hasBadRoadConditions()) {
            timesDrivenThroughStreetWithBadConditions++;
        }
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
                    synchronized (dataCenter.getCarsWaitingForPollutionReset()) {
                        dataCenter.getCarsWaitingForPollutionReset().add(this);
                    }
                    wait();
                    synchronized (dataCenter.getCarsWaitingForPollutionReset()) {
                        dataCenter.getCarsWaitingForPollutionReset().remove(this);
                    }
                }
                timesStoppedBecauseOfPollution++;
                if (timesStoppedBecauseOfPollution >= 2) {
                    decideIfShouldUseEconomicEngine();
                }
            }
        }
    }

    public void decideIfShouldUseEconomicEngine() {
        if (Math.random() < 1.0 / 6.0) needsEconomicEngine = true;
    }


    public void sendInfoToDataCenter() {
        dataCenter.increasePollutionForFiveStreets(engine);
    }

    public int getRandomTimeForDrivingThroughStreet() {
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
        synchronized (dataCenter.getCars()) {
            dataCenter.getCars().remove(this);
            System.out.println(n + " stopped, left: " + dataCenter.getCars().size());
        }
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public int getN() {
        return n;
    }

    public boolean needsEconomicEngine() {
        return needsEconomicEngine;
    }

    public DataCenter getDataCenter() {
        return dataCenter;
    }

    public Crossroad getCurrentCrossRoad() {
        return currentCrossRoad;
    }

    public void setTimesDrivenThroughStreetWithBadConditions(int timesDrivenThroughStreetWithBadConditions) {
        this.timesDrivenThroughStreetWithBadConditions = timesDrivenThroughStreetWithBadConditions;
    }

    public void setTires(Tires tires) {
        this.tires = tires;
    }
}
