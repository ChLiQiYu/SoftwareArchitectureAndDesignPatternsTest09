package com.mashang.decorator.picking;

public class NormalPicking implements PickingStrategy {
    @Override
    public String execute() {
        return "按库位顺序拣货";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }
}
