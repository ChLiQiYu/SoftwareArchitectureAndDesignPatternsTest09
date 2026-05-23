package com.mashang.decorator.putaway;

public class ExpiryPriorityDecorator extends PutawayDecorator {
    public ExpiryPriorityDecorator(PutawayStrategy wrapped) {
        this(wrapped, 5);
    }

    public ExpiryPriorityDecorator(PutawayStrategy wrapped, int priority) {
        super(wrapped, priority);
    }

    @Override
    public String execute() {
        return wrapped.execute() + " -> 近效期放外侧";
    }
}
