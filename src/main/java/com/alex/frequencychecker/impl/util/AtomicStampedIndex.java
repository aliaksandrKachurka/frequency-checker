package com.alex.frequencychecker.impl.util;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Maintains an index along with an integer "stamp", that can be updated atomically.
 * Garbage-free alternative to {@link java.util.concurrent.atomic.AtomicStampedReference} when dealing
 * with integers.
 */
public class AtomicStampedIndex {
    private static final int STAMP_OFFSET = 32;
    private final AtomicLong indexWithStamp;

    public AtomicStampedIndex(int initialIndex, int initialStamp) {
        long initialIndexWithStamp = glueIndexWithStamp(initialIndex, initialStamp);
        indexWithStamp = new AtomicLong(initialIndexWithStamp);
    }

    public int getIndex() {
        return indexWithStamp.intValue();
    }

    public int getStamp() {
        return (int) (indexWithStamp.longValue() >>> STAMP_OFFSET);
    }

    /**
     * Atomically sets the value of both the index and stamp to the given new values
     * if the current index equals expected index and the current stamp is equal to the expected one.
     */
    public boolean compareAndSet(int expectedIndex, int newIndex, int expectedStamp, int newStamp) {
        long expectedIndexWithStamp = glueIndexWithStamp(expectedIndex, expectedStamp);
        long newIndexWithStamp = glueIndexWithStamp(newIndex, newStamp);
        return indexWithStamp.compareAndSet(expectedIndexWithStamp, newIndexWithStamp);
    }

    private long glueIndexWithStamp(int index, int stamp) {
        return (long) stamp << STAMP_OFFSET | (long) index;
    }
}
