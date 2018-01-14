package car;

import datacenter.DataCenter;

import java.util.ArrayList;
import java.util.List;

public class HelpCar extends Car {

    private boolean isWorking;
    private List<Car> carsToHelp = new ArrayList<>();


    public HelpCar(DataCenter dataCenter, Engine engine, int n) {
        super(dataCenter, engine, n);
        isWorking = false;
    }

    @Override
    public void drive() throws InterruptedException {
        super.drive();
    }

    @Override
    public void turnToNextStreet() throws InterruptedException {
        getTheStreetBeforeTurning();
        currentCrossRoad = map.getNextCrossRoad(currentCrossRoad);
        checkCrossRoadForCarsNeedingHelp();
        getTheStreetAfterTurning();
    }

    private void checkCrossRoadForCarsNeedingHelp() throws InterruptedException {
        getCarsNeedingHelpAtCurrentCrossroad();
        helpCarsAtCurrentCrossroad();
    }

    private void helpCarsAtCurrentCrossroad() throws InterruptedException {
        for (Car car: carsToHelp) {
            synchronized (this) {
                wait(1000);
            }
            car.setTimesDrivenThroughStreetWithBadConditions(0);
            if (car.getEngine() == Engine.LEMONADE || car.getEngine() == Engine.ELECTRIC) {
                car.setTires(Tires.MARMALADE);
            }
            synchronized (car) {
                car.notify();
            }
            System.out.println("Car " + car.getN() + " changed tires");
            dataCenter.getCarsNeedingHelp().remove(car);

        }
        carsToHelp.clear();
    }

    private void getCarsNeedingHelpAtCurrentCrossroad() {
        List<Car> carsNeedingHelp = dataCenter.getCarsNeedingHelp();
        for (Car car : carsNeedingHelp) {
            if (car.getCurrentCrossRoad() == this.currentCrossRoad) {
                carsToHelp.add(car);
            }
        }
        dataCenter.getCarsNeedingHelp().removeAll(carsToHelp);
    }

    @Override
    public void driveThroughStreet() throws InterruptedException {
        Thread.sleep(getRandomTimeForDrivingThroughStreet());
        amountOfStreetsPassed++;
        dataCenter.increasePollutionForFiveStreets(engine);
    }

    @Override
    public void run() {
        while (dataCenter.getCarsNeedingHelp().size() > 0) {
            try {
                drive();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        isWorking = false;
        synchronized (dataCenter.getCars()) {
            dataCenter.getCars().remove(this);
        }
    }

    public boolean isWorking() {
        return isWorking;
    }

    public void setWorking(boolean working) {
        isWorking = working;
    }
}
