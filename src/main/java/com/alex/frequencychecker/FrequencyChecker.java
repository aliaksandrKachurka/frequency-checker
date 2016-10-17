package com.alex.frequencychecker;

/**
 * The implementations can restrict actions rate by forcing
 * action performers to call the correspondent method.
 */
public interface FrequencyChecker {

    /**
     * Should be called by action performers to find out whether they allowed to perform the action.
     */
    boolean isAllowed();
}
