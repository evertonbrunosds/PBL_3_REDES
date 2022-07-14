package model;

import java.util.Random;

public class Randomize {

    public static int sortIntNumber() {
        final int number = (new Random()).nextInt();
        final boolean isPositive = (new Random()).nextBoolean();
        return isPositive ? number : number * -1;
    }

    public static int sortIntNumber(final int range) {
        final int number = (new Random()).nextInt(range);
        final boolean isPositive = (new Random()).nextBoolean();
        return isPositive ? number : number * -1;
    }

    public static long sortLongNumber() {
        final long number = (new Random()).nextLong();
        final boolean isPositive = (new Random()).nextBoolean();
        return isPositive ? number : number * -1;
    }

    public static int sortIntPositiveNumber(final int range) {
        return (new Random()).nextInt(range);
    }

    public static int sortIntPositiveNumber() {
        return (new Random()).nextInt();
    }
}
