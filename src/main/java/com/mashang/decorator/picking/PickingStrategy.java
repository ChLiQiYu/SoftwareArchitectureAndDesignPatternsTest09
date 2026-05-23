package com.mashang.decorator.picking;

public interface PickingStrategy {
    String execute();
    int getPriority();
}
