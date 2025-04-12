package com.test.rewardpoint.mock;

import java.util.Random;
import java.util.UUID;

public class RandomMock {

    private static final Random random = new Random();

    public static int createRandomInteger() {
        return createRandomInteger(Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static int createRandomInteger(int min, int max) {
        return random.nextInt(min, max);
    }

    public static String createRandomString() {
        return UUID.randomUUID().toString();
    }
}
