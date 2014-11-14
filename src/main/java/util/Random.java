package util;

public class Random {
    private static java.util.Random rand = new java.util.Random();

    private static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
}
