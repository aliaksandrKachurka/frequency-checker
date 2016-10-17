package com.alex.frequencychecker.impl.util;

import java.util.concurrent.atomic.AtomicLongArray;

/**
 * Represents circular data structure for storing long values.
 */
public class LongCircularBuffer {
    private final AtomicLongArray buffer;
    private final AtomicStampedIndex index;

    public LongCircularBuffer(int capacity) {
        index = new AtomicStampedIndex(0, 0);
        buffer = new AtomicLongArray(capacity);
        for (int i = 0; i < capacity; ++i) {
            buffer.set(i, 0);
        }
    }

    /**
     * Updates the value under expected index and shifts the index if current index equals expected one,
     * current index stamp equals expected index stamp and current value under the expected index
     * equals expected value.
     */
    public boolean updateAndShift(int expectedIndex, int expectedIndexStamp,
                                  long expectedValue, long newValue) {
        return compareAndShift(expectedIndex, expectedIndexStamp)
                && buffer.compareAndSet(expectedIndex, expectedValue, newValue);
    }

    private boolean compareAndShift(int expectedIndex, int expectedIndexStamp) {
        int newIndex = (expectedIndex + 1) % buffer.length();
        int newIndexStamp = expectedIndexStamp + 1;
        return index.compareAndSet(expectedIndex, newIndex, expectedIndexStamp, newIndexStamp);
    }

    public long getValue(int index) {
        return buffer.get(index);
    }

    public int getIndex() {
        return index.getIndex();
    }

    public int getIndexStamp() {
        return index.getStamp();
    }
}