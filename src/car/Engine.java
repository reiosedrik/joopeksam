package car;

public enum Engine {
    DIESEL,
    PETROL,
    LEMONADE,
    ELECTRIC;


    public static Engine createRandom(int n) {
        double randomNum = Math.random();
        if (n % 10 == 0) {
            if (randomNum > 0.5) {
                return ELECTRIC;
            } else {
                return LEMONADE;
            }
        }
        if (randomNum > 0.5) {
            return PETROL;
        }
        return DIESEL;
    }
}
