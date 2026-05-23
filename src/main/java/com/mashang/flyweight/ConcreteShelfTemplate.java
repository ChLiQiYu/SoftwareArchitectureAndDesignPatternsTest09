package com.mashang.flyweight;

public class ConcreteShelfTemplate implements ShelfTemplate {
    private final String type;
    private final int length;
    private final int width;
    private final int height;
    private final double maxLoad;
    private final int layers;

    public ConcreteShelfTemplate(String type, int length, int width, int height, double maxLoad, int layers) {
        this.type = type;
        this.length = length;
        this.width = width;
        this.height = height;
        this.maxLoad = maxLoad;
        this.layers = layers;
    }

    @Override
    public String display(String shelfCode, String area) {
        return String.format("货架编号: %s, 库区: %s | 模板类型: %s, 规格: %d×%d×%d, 承重: %.0fkg, 层数: %d",
                shelfCode, area, type, length, width, height, maxLoad, layers);
    }

    @Override
    public String getType() { return type; }
    @Override
    public int getLength() { return length; }
    @Override
    public int getWidth() { return width; }
    @Override
    public int getHeight() { return height; }
    @Override
    public double getMaxLoad() { return maxLoad; }
    @Override
    public int getLayers() { return layers; }
}
