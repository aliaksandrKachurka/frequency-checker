# Frequency Checker
Lock-free low garbage filter for actions rate restriction. 

## Use case
Consider a case when application threads perform repeatable actions concurrently. Actions execution frequency should be bounded somehow. 
For instance a system generates messages at unknown random intervals and *at most 5000 messages per second* should be accepted.

The problem could be solved with `FrequencyCheckerImpl` in the following way:
```java
FrequencyChecker frequencyChecker = new FrequencyCheckerImpl(1000, 5000);
...
if (frequencyChecker.isAllowed()) {
    processMessage();
}
...
```

## Implementation note
`FrequencyCheckerImpl` holds circular data structure internally to store timestamps of the last *N* `isAllowed()` calls. 
Custom `AtomicStampedIndex` is used by the data structure instead of `AtomicStampedReference` to reference current element 
because the latter creates pretty much garbage in form of `AtomicStampedReference$Pair` instances:

![Profiler output on 2000msg/300ms, 6 threads](/pics/atomic_stamped_reference_profiler_output.png?raw=true "Profiler output on 2000msg/300ms, 6 threads")