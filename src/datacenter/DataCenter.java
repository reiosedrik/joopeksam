package datacenter;

import bird.Bird;
import car.Car;
import car.Engine;
import car.HelpCar;
import format.CityDataFormat;
import map.Map;

import java.util.ArrayList;
import java.util.List;

public class DataCenter {

    private Map map;
    private int carCount = 0;
    private int internalCombustionEngineCount = 0;
    private double pollution = 0;
    private double pollutionAtBan;
    private static final int POLLUTION_AMOUNT_FOR_RESETTING = 400;
    private List<Car> cars;
    private boolean resetting = false;
    private int amountPollutionHasBeenReset = 0;
    private List<Car> carsNeedingHelp = new ArrayList<>();
    private HelpCar helpCar;
    private Bird bird;
    private List<Car> carsWaitingForPollutionReset = new ArrayList<>();

    public DataCenter(Map map) {
        this.map = map;
    }

    public void askForHelp(Car car) {
        synchronized (carsNeedingHelp) {
            carsNeedingHelp.add(car);
        }
        if (helpCar == null) {
            helpCar = new HelpCar(this, Engine.ELECTRIC, -1);
        }
        if (!helpCar.isWorking()) {
            helpCar.setWorking(true);
            Thread carHelpThread = new Thread(helpCar);
            carHelpThread.start();
        }
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
        if (!resetting) {
            if (pollution >= POLLUTION_AMOUNT_FOR_RESETTING) {
                resetPollutionAfter2Seconds();
            }
        }
    }

    private void resetPollutionAfter2Seconds() {
        pollutionAtBan = pollution;
        resetting = true;
        synchronized (this) {
            new java.util.Timer().schedule(
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            if (internalCombustionEngineCount < 70) {
                                pollution = 0;
                            } else {
                                pollution = 0.4 * pollutionAtBan;
                            }
                            resetting = false;
                            notifyCarsWaiting();
                        }
                    },
                    2000
            );
            amountPollutionHasBeenReset++;

        }
    }

    private void notifyCarsWaiting() {
        synchronized (carsWaitingForPollutionReset) {
            for (Car car : carsWaitingForPollutionReset) {
                synchronized (car) {
                    car.notify();
                }

            }
        }
//        carsWaitingForPollutionReset.clear();
    }

    public boolean isDrivingAllowedWith(Engine engine) {
        return pollution < 400
                || (!(pollution > 400)
                || engine != Engine.DIESEL) && (!(pollution > 500)
                || engine != Engine.PETROL);
    }

    public void addCar(Car car) {
        synchronized (this) {
            if (cars == null) {
                cars = new ArrayList<>();
            }
            carCount++;
            if (car.getEngine() == Engine.DIESEL || car.getEngine() == Engine.PETROL) {
                internalCombustionEngineCount++;
            }
            cars.add(car);
        }
    }

    public synchronized void addCarThatIsWaitingForPollution(Car car) {
        carsWaitingForPollutionReset.add(car);
    }


    public long countCarsWithPetrolEngine() {
        return cars.stream().filter(c -> c.getEngine() == Engine.PETROL).count();
    }

    public long countCarsWithDieselEngine() {
        return cars.stream().filter(c -> c.getEngine() == Engine.DIESEL).count();
    }

    public long countCarsWithElectricEngine() {
        return cars.stream().filter(c -> c.getEngine() == Engine.ELECTRIC).count();
    }

    public long countCarsWithLemonadeEngine() {
        return cars.stream().filter(c -> c.getEngine() == Engine.LEMONADE).count();
    }

    public void getOverViewOfCityTraffic(CityDataFormat strategy) {
        System.out.println(strategy.formatData(this));
    }

    public Map getMap() {
        return map;
    }

    public int getAmountPollutionHasBeenReset() {
        return amountPollutionHasBeenReset;
    }

    public List<Car> getCars() {
        return cars;
    }

    public List<Car> getCarsNeedingHelp() {
        return carsNeedingHelp;
    }

    public double getPollution() {
        return pollution;
    }

    public Bird getBird() {
        return bird;
    }

    public void setBird(Bird bird) {
        this.bird = bird;
    }

    public List<Car> getCarsWaitingForPollutionReset() {
        return carsWaitingForPollutionReset;
    }
}
