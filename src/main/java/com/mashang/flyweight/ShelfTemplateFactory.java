package com.mashang.flyweight;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ShelfTemplateFactory {
    private final Map<String, ShelfTemplate> pool = new HashMap<>();
    private final Set<String> disabled = new HashSet<>();
    private int creationCount = 0;

    public ShelfTemplateFactory() {
        registerDefaultTemplates();
    }

    private void registerDefaultTemplates() {
        registerTemplate("横梁式", 270, 100, 180, 500.0, 4);
        registerTemplate("驶入式", 270, 120, 200, 1000.0, 3);
        registerTemplate("穿梭式", 270, 150, 200, 1500.0, 5);
        registerTemplate("轻型置物架", 120, 60, 150, 100.0, 5);
    }

    private void registerTemplate(String type, int length, int width, int height, double maxLoad, int layers) {
        ShelfTemplate template = new ConcreteShelfTemplate(type, length, width, height, maxLoad, layers);
        pool.put(type, template);
        creationCount++;
    }

    public ShelfTemplate getTemplate(String type) {
        if (disabled.contains(type)) {
            throw new IllegalStateException("货架模板 [" + type + "] 已被禁用，无法获取");
        }
        ShelfTemplate template = pool.get(type);
        if (template == null) {
            template = new ConcreteShelfTemplate(type, 270, 100, 180, 500.0, 4);
            pool.put(type, template);
            creationCount++;
        }
        return template;
    }

    public void disableTemplate(String type) {
        disabled.add(type);
    }

    public void enableTemplate(String type) {
        disabled.remove(type);
    }

    public int getCreationCount() {
        return creationCount;
    }

    public int getPoolSize() {
        return pool.size();
    }
}
