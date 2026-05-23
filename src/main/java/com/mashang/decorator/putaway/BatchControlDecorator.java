package com.mashang.decorator.putaway;

public class BatchControlDecorator extends PutawayDecorator {
    public BatchControlDecorator(PutawayStrategy wrapped) {
        this(wrapped, 10);
    }

    public BatchControlDecorator(PutawayStrategy wrapped, int priority) {
        super(wrapped, priority);
    }

    @Override
    public String execute() {
        return wrapped.execute() + " -> 同批次集中存放";
    }
}
