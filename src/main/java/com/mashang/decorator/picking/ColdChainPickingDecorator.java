package com.mashang.decorator.picking;

public class ColdChainPickingDecorator extends PickingDecorator {
    public ColdChainPickingDecorator(PickingStrategy wrapped) {
        this(wrapped, 10);
    }

    public ColdChainPickingDecorator(PickingStrategy wrapped, int priority) {
        super(wrapped, priority);
    }

    @Override
    public String execute() {
        return wrapped.execute() + " -> 低温管控、限定拣货月台";
    }
}
