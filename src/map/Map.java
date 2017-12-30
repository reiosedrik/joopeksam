package map;

import java.util.ArrayList;
import java.util.List;

public class Map {

    private List<Crossroad> crossroads;

    public Map() {
        crossroads = new ArrayList<>();
    }

    public static Map ofKristiine() {
        Map map = new Map();
        map.add(Crossroad.withEntry(twoWay("Vuti", "Leevikese")));
        map.add(Crossroad.withEntry(threeWay("Leevikese", "Rästa", "Käo")));
        map.add(Crossroad.withEntry(threeWay("Käo", "Räägu", "Tedre")));
        map.add(Crossroad.withEntry(twoWay("Tedre", "Ööbiku")));
        map.add(Crossroad.withCarService(threeWay("Vuti", "Varese", "Hane")));
        map.add(new Crossroad(fourWay("Hane", "Rästa", "Siidisaba", "Kure")));
        map.add(new Crossroad(fourWay("Siidisaba", "Räägu", "Vindi", "Pardi")));
        map.add(new Crossroad(threeWay("Vindi", "Ööbiku", "Lõokese")));
        map.add(Crossroad.withCarService(threeWay("Varese", "Sule", "Luige")));
        map.add(new Crossroad(fourWay("Sule", "Kure", "Rähni", "Liblika")));
        map.add(new Crossroad(fourWay("Rähni", "Pardi", "Tihase", "Västriku")));
        map.add(Crossroad.withCarService(twoWay("Västriku", "Lõokese")));
        map.add(new Crossroad(twoWay("Luige", "Kotka")));
        map.add(new Crossroad(threeWay("Kotka", "Liblika", "Kajaka")));
        map.add(Crossroad.withCarService(twoWay("Kajaka", "Tihase")));

        return map;
    }

    public void add(Crossroad crossroad) {
        crossroads.add(crossroad);
    }


    private static Street[] twoWay(String n1, String n2) {
        return new Street[] {new Street(n1), new Street(n2)};
    }

    private static Street[] threeWay(String n1, String n2, String n3) {
        return new Street[] {new Street(n1), new Street(n2), new Street(n3)};
    }

    private static Street[] fourWay(String n1, String n2, String n3, String n4) {
        return new Street[] {new Street(n1), new Street(n2), new Street(n3), new Street(n4)};
    }
}
