package com.mashang.decorator.putaway;

import com.mashang.flyweight.ShelfTemplate;

public class WholeSplitDecorator extends PutawayDecorator {
    private final ShelfTemplate template;

    public WholeSplitDecorator(PutawayStrategy wrapped, ShelfTemplate template) {
        super(wrapped, 15);
        this.template = template;
    }

    public WholeSplitDecorator(PutawayStrategy wrapped, ShelfTemplate template, int priority) {
        super(wrapped, priority);
        this.template = template;
    }

    @Override
    public String execute() {
        int layers = template.getLayers();
        int wholeLayerThreshold = layers / 2;
        return wrapped.execute() + " -> 整托放高层(第" + (wholeLayerThreshold + 1) + "-" + layers + "层)、散件放低层(第1-" + wholeLayerThreshold + "层)";
    }
}
