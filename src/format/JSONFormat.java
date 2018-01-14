package format;

import datacenter.DataCenter;

public class JSONFormat implements CityDataFormat {

    @Override
    public String formatData(DataCenter dataCenter) {
        return "{ \"Cars\": " + dataCenter.getCars().size()
                + ", \"Petrol Engines\": " + dataCenter.countCarsWithPetrolEngine()
                + ", \"Diesel Engines\": " + dataCenter.countCarsWithDieselEngine()
                + ", \"Electric Engines\": " + dataCenter.countCarsWithElectricEngine()
                + ", \"Lemonade Engines\": " + dataCenter.countCarsWithLemonadeEngine()
                + ", \"Pollution\": " + dataCenter.getPollution();

    }
}
