package datacenter;

import map.Map;

public class DataCenter {

    private Map map;
    private int carCount = 0;

    public DataCenter(Map map) {
        this.map = map;
    }

    public void incrementCarCount() {
        carCount++;
    }

    public Map getMap() {
        return map;
    }
}
