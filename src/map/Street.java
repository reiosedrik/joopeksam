package map;


public class Street {

    private String name;
    private boolean badRoadConditions = false;

    public Street(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public boolean hasBadRoadConditions() {
        return badRoadConditions;
    }

    public void setBadRoadConditions(boolean badRoadConditions) {
        this.badRoadConditions = badRoadConditions;
    }
}
