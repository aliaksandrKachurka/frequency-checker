package com.alex.frequencychecker.impl;

import com.alex.frequencychecker.FrequencyChecker;
import com.alex.frequencychecker.impl.util.LongCircularBuffer;

public class FrequencyCheckerImpl implements FrequencyChecker {
    private final int interval;
    private final LongCircularBuffer attemptTimestamps;

    public FrequencyCheckerImpl(int intervalMillis, int maxAttemptsPerInterval) {
        interval = intervalMillis;
        attemptTimestamps = new LongCircularBuffer(maxAttemptsPerInterval);
    }

    @Override
    public boolean isAllowed() {
        long currentAttemptTimestamp = System.currentTimeMillis();

        int currentIndex;
        int currentIndexStamp;
        long oldestAttemptTimestamp;
        do {
            currentIndex = attemptTimestamps.getIndex();
            currentIndexStamp = attemptTimestamps.getIndexStamp();
            oldestAttemptTimestamp = attemptTimestamps.getValue(currentIndex);

            if (currentAttemptTimestamp - oldestAttemptTimestamp < interval) {
                return false;
            }
        } while (!attemptTimestamps.updateAndShift(currentIndex, currentIndexStamp,
                oldestAttemptTimestamp, currentAttemptTimestamp));
        return true;
    }
}
