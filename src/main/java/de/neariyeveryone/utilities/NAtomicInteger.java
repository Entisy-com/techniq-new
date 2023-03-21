package de.neariyeveryone.utilities;

import java.util.concurrent.atomic.AtomicInteger;

public class NAtomicInteger extends AtomicInteger {
    public NAtomicInteger(int initialValue) {
        super(initialValue);
    }

    public NAtomicInteger() {
        super();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public int intValue() {
        return super.intValue();
    }

    @Override
    public long longValue() {
        return super.longValue();
    }

    @Override
    public float floatValue() {
        return super.floatValue();
    }

    @Override
    public double doubleValue() {
        return super.doubleValue();
    }

    public boolean compare(int toCompare) {
        return this.get() == toCompare;
    }

    public boolean compare(AtomicInteger toCompare) {
        return this.get() == toCompare.get();
    }
}
