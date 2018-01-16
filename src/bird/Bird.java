package bird;

import datacenter.DataCenter;
import format.JSONFormat;

public class Bird implements Runnable{

    private DataCenter dataCenter;
    private int count = 0;
//    private ExecutorService service;

    public Bird(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
        dataCenter.setBird(this);
//        this.service = service;
    }

    @Override
    public void run() {
        while (dataCenter.getAmountPollutionHasBeenReset() < 5) {
            try {
                synchronized (this) {
                    wait(4000);
                    count++;
                }
                if (dataCenter.getPollution() < 400) {
                    System.out.println("Puhas õhk on puhas õhk on rõõmus linnu elu!");
                } else {
                    System.out.println("„Inimene tark, inimene tark – saastet täis on linnapark");
                }
                System.out.println("car left: " + dataCenter.getCars().size());
                dataCenter.getOverViewOfCityTraffic(new JSONFormat());

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("bird finished, waiting pollution: " + dataCenter.getCarsWaitingForPollutionReset().size());
//        dataCenter.getOverViewOfCityTraffic(new JSONFormat());
    }

    public int getCount() {
        return count;
    }
}
