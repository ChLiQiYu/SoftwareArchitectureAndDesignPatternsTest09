package com.mashang.decorator.picking;

public abstract class PickingDecorator implements PickingStrategy {
    protected final PickingStrategy wrapped;
    protected final int priority;

    public PickingDecorator(PickingStrategy wrapped, int priority) {
        this.wrapped = wrapped;
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
