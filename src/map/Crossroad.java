package map;

import carservice.CarService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Crossroad {

    private List<Street> adjacentStreets = new ArrayList<>();
    private Random random;
    private boolean isEntryCrossroad = false;
    private boolean hasCarService = false;
    private Street currentStreet;
    private String name;
    private boolean isCarServiceAvailable;
    private CarService carService;


    public Crossroad(Street[] streets) {
        random = new Random();
        adjacentStreets.addAll(Arrays.asList(streets));
        name = createName();
    }

    private String createName() {
        String finalName = "";
        for (Street street : adjacentStreets) {
            finalName = finalName.concat(street.getName() + "-");
        }
        return finalName.substring(0, finalName.length() - 1);
    }

    public static Crossroad withEntry(Street[] streets) {
        Crossroad crossroad = new Crossroad(streets);
        crossroad.random = new Random();
        crossroad.adjacentStreets.addAll(Arrays.asList(streets));
        crossroad.isEntryCrossroad = true;
        return crossroad;
    }

    public static Crossroad withCarService(Street[] streets) {
        Crossroad crossroad = new Crossroad(streets);
        crossroad.random = new Random();
        crossroad.adjacentStreets.addAll(Arrays.asList(streets));
        crossroad.hasCarService = true;
        crossroad.isCarServiceAvailable = true;
        crossroad.carService = new CarService(crossroad.getName());
        return crossroad;
    }

    public Street getNextStreet(Street current) {
        if (current == null) current = adjacentStreets.get(random.nextInt(adjacentStreets.size()));
        Street next;
        while (true) {
            next = adjacentStreets.get(random.nextInt(adjacentStreets.size()));
            if (!next.getName().equals(current.getName())) {
                return next;
            }
        }
    }

    public boolean hasStreetWithName(String name) {
        for (Street street: adjacentStreets) {
            if (street.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public void startUsingCarService() {
        synchronized (this) {
            isCarServiceAvailable = false;
        }
    }

    public void finishUsingCarService(int n) {
        synchronized (this) {
            isCarServiceAvailable = true;
        }
        System.out.println(String.format("Car %d finished using car service at %s", n, name));
    }


    public boolean isCarServiceAvailable() {
        return isCarServiceAvailable;
    }

    public boolean isNextTo(Crossroad crossroad, Street street) {
        return crossroad.hasStreetWithName(street.getName()) && crossroad != this && this.hasStreetWithName(street.getName());
    }

    public List<Street> getAdjacentStreets() {
        return adjacentStreets;
    }

    public void setCurrentStreet(Street currentStreet) {
        this.currentStreet = currentStreet;
    }

    public boolean isEntryCrossroad() {
        return isEntryCrossroad;
    }

    public boolean hasCarService() {
        return hasCarService;
    }

    public String getName() {
        return name;
    }

    public Street getCurrentStreet() {
        return currentStreet;
    }

    public CarService getCarService() {
        return carService;
    }
}
