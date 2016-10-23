package com.alex.frequencychecker;

import com.alex.frequencychecker.impl.FrequencyCheckerImpl;

import java.util.stream.IntStream;

/**
 * FrequencyCheckerImpl demo.
 */
public class Demo {
    private static final int intervalMillis = 1000;
    private static final int maxMessagesPerInterval = 5000;

    private static final int threadsCount = 3;

    private static final FrequencyChecker frequencyChecker
            = new FrequencyCheckerImpl(intervalMillis, maxMessagesPerInterval);

    public static void main(String[] args) {
        IntStream.range(0, threadsCount).forEach(i -> performActionRepeatedly());
    }

    private static void performActionRepeatedly() {
        new Thread(() -> {
            while (true) {
                if (frequencyChecker.isAllowed()) {
                    System.out.println(String.format("%s: allowed!", Thread.currentThread().getName()));
                }
            }
        }).start();
    }
}
