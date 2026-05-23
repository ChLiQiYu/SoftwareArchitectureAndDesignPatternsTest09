package com.mashang.decorator.picking;

public class FragilePickingDecorator extends PickingDecorator {
    public FragilePickingDecorator(PickingStrategy wrapped) {
        this(wrapped, 5);
    }

    public FragilePickingDecorator(PickingStrategy wrapped, int priority) {
        super(wrapped, priority);
    }

    @Override
    public String execute() {
        return wrapped.execute() + " -> 禁止抛扔、二次复核";
    }
}
