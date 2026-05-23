package com.mashang.model;

import com.mashang.flyweight.ShelfTemplate;

public class Shelf {
    private final String shelfCode;
    private final String area;
    private boolean occupied;
    private final ShelfTemplate template;

    public Shelf(String shelfCode, String area, ShelfTemplate template) {
        this.shelfCode = shelfCode;
        this.area = area;
        this.template = template;
        this.occupied = false;
    }

    public String display() {
        return template.display(shelfCode, area);
    }

    public String getShelfCode() { return shelfCode; }
    public String getArea() { return area; }
    public boolean isOccupied() { return occupied; }
    public void setOccupied(boolean occupied) { this.occupied = occupied; }
    public ShelfTemplate getTemplate() { return template; }
}
