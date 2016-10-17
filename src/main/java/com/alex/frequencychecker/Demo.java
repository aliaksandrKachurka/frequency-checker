package com.alex.frequencychecker;

import com.alex.frequencychecker.impl.FrequencyCheckerImpl;

import java.util.stream.IntStream;

/**
 * FrequencyCheckerImpl demo.
 */
public class Demo {
    private static final FrequencyChecker frequencyChecker = new FrequencyCheckerImpl(600, 2);
    private static final int threadsCount = 3;

    public static void main(String[] args) {
        IntStream.range(0, threadsCount).forEach(i -> perform());
    }

    private static void perform() {
        new Thread(() -> {
            while (true) {
                if (frequencyChecker.isAllowed()) {
                    System.out.println(String.format("%s: allowed!", Thread.currentThread().getName()));
                }
            }
        }).start();
    }
}
