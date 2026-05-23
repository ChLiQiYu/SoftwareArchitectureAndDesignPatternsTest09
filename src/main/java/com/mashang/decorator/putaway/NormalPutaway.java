package com.mashang.decorator.putaway;

public class NormalPutaway implements PutawayStrategy {
    @Override
    public String execute() {
        return "空闲库位就近上架";
    }

    @Override
    public int getPriority() {
        return Integer.MAX_VALUE;
    }
}
