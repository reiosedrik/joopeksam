package datacenter;

import car.Car;
import car.Engine;
import map.Map;

import java.util.ArrayList;
import java.util.List;

public class DataCenter {

    private Map map;
    private int carCount = 0;
    private int internalCombustionEngineCount = 0;
    private double pollution = 0;
    private static final int POLLUTION_AMOUNT_FOR_RESETTING = 400;
    private List<Car> cars = new ArrayList<>();
    private boolean resetting = false;
    private int amountPollutionHasBeenReset = 0;

    public DataCenter(Map map) {
        this.map = map;
    }

    public synchronized void increasePollutionForFiveStreets(Engine engine) {
        switch (engine) {
            case DIESEL:
                pollution += 15;
                break;
            case PETROL:
                pollution += 10;
                break;
            case LEMONADE:
                pollution += 2.5;
                break;
            case ELECTRIC:
                pollution += 0.5;
                break;
        }
//        System.out.println(pollution + " " + engine);
        if (!resetting) {
            if (pollution >= POLLUTION_AMOUNT_FOR_RESETTING) {
                resetPollutionAfter2Seconds();
            }
        }
    }

    private void resetPollutionAfter2Seconds() {
        resetting = true;
        synchronized (this) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            if (internalCombustionEngineCount < 70) {
                                pollution = 0;
                            } else {
                                pollution *= 0.4;
                            }
                            resetting = false;
                            notifyCarsWithPetrolAndDieselEngine();
                        }
                    },
                    2000
            );
        }
        amountPollutionHasBeenReset++;
    }

    private void notifyCarsWithPetrolAndDieselEngine() {
        for (Car car: cars) {
            if (car.getEngine() == Engine.PETROL || car.getEngine() == Engine.DIESEL) {
                synchronized (car) {
                    car.notify();
                }
            }
        }
    }

    public boolean isDrivingAllowedWith(Engine engine) {
        return pollution < 400
                || (!(pollution > 400)
                || engine != Engine.DIESEL) && (!(pollution > 500)
                || engine != Engine.PETROL);
    }

    public void addCar(Car car) {
        synchronized (this) {
            carCount++;
            if (car.getEngine() == Engine.DIESEL || car.getEngine() == Engine.PETROL) {
                internalCombustionEngineCount++;
            }
            cars.add(car);
        }
    }

    public Map getMap() {
        return map;
    }

    public int getAmountPollutionHasBeenReset() {
        return amountPollutionHasBeenReset;
    }
}
