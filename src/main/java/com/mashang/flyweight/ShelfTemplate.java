package com.mashang.flyweight;

public interface ShelfTemplate {
    String display(String shelfCode, String area);

    String getType();
    int getLength();
    int getWidth();
    int getHeight();
    double getMaxLoad();
    int getLayers();
}
