package com.mashang.decorator.putaway;

public abstract class PutawayDecorator implements PutawayStrategy {
    protected final PutawayStrategy wrapped;
    protected final int priority;

    public PutawayDecorator(PutawayStrategy wrapped, int priority) {
        this.wrapped = wrapped;
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
