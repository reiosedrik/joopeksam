package format;

import datacenter.DataCenter;

public class TextFormat implements CityDataFormat {

    @Override
    public String formatData(DataCenter dataCenter) {
        return "\nCars: " + dataCenter.getCars().size()
                + "\nPetrol engines: " + dataCenter.countCarsWithPetrolEngine()
                + "\nDiesel engines: " + dataCenter.countCarsWithDieselEngine()
                + "\nElectric engines: " + dataCenter.countCarsWithElectricEngine()
                + "\nLemonade engines: " + dataCenter.countCarsWithLemonadeEngine()
                + "\nPollution: " + dataCenter.getPollution();
    }
}
