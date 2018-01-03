package map;

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

    public Crossroad(Street[] streets) {
        random = new Random();
        adjacentStreets.addAll(Arrays.asList(streets));
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
        return crossroad;
    }

    public Street getNextStreet(Street current) {
        Street next = current;
        while (next == current) {
            next = adjacentStreets.get(random.nextInt(adjacentStreets.size()));
        }
        return next;
    }

    public List<Street> getAdjacentStreets() {
        return adjacentStreets;
    }

    public void setCurrentStreet(Street currentStreet) {
        this.currentStreet = currentStreet;
    }
}
