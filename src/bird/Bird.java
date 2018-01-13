package bird;

import datacenter.DataCenter;

public class Bird implements Runnable{

    private DataCenter dataCenter;

    public Bird(DataCenter dataCenter) {
        this.dataCenter = dataCenter;
    }

    @Override
    public void run() {
        while (true) {
            try {
                wait(4000);
                if (dataCenter.getPollution() < 400) {
                    System.out.println("Puhas õhk on puhas õhk on rõõmus linnu elu!");
                } else {
                    System.out.println("„Inimene tark, inimene tark – saastet täis on linnapark");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
