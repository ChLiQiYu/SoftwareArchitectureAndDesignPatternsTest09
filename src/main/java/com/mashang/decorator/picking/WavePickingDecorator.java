package com.mashang.decorator.picking;

public class WavePickingDecorator extends PickingDecorator {
    public WavePickingDecorator(PickingStrategy wrapped) {
        this(wrapped, 20);
    }

    public WavePickingDecorator(PickingStrategy wrapped, int priority) {
        super(wrapped, priority);
    }

    @Override
    public String execute() {
        return wrapped.execute() + " -> 按波次聚合订单批量拣货";
    }
}
