package com.mashang;

import com.mashang.decorator.picking.*;
import com.mashang.decorator.putaway.*;
import com.mashang.flyweight.ShelfTemplate;
import com.mashang.flyweight.ShelfTemplateFactory;
import com.mashang.model.Shelf;

public class Main {
    public static void main(String[] args) {
        System.out.println("========== WMS仓储系统 - 享元模式 + 装饰器模式 ==========\n");

        ShelfTemplateFactory factory = new ShelfTemplateFactory();

        // 一、享元模式：货架模板复用
        System.out.println("【一、享元模式：货架模板复用】");
        ShelfTemplate t1 = factory.getTemplate("横梁式");
        ShelfTemplate t2 = factory.getTemplate("横梁式");
        System.out.println("两次获取'横梁式'模板是否为同一对象: " + (t1 == t2));
        System.out.println();

        Shelf shelf1 = new Shelf("S-A-001", "A区", factory.getTemplate("横梁式"));
        Shelf shelf2 = new Shelf("S-B-002", "B区", factory.getTemplate("横梁式"));
        Shelf shelf3 = new Shelf("S-C-003", "C区", factory.getTemplate("横梁式"));
        System.out.println("三个不同库区货架复用同一模板:");
        System.out.println(shelf1.display());
        System.out.println(shelf2.display());
        System.out.println(shelf3.display());
        System.out.println("模板创建数量: " + factory.getCreationCount());
        System.out.println();

        // 二、装饰器模式：拣货策略
        System.out.println("【二、装饰器模式：拣货策略】");
        PickingStrategy normalPick = new NormalPicking();
        System.out.println("场景1 - 普通拣货: " + normalPick.execute());

        PickingStrategy waveAndCold = new ColdChainPickingDecorator(new WavePickingDecorator(new NormalPicking()));
        System.out.println("场景2 - 普通+波次+冷链: " + waveAndCold.execute());

        PickingStrategy fragile = new FragilePickingDecorator(new NormalPicking());
        System.out.println("场景3 - 普通+易碎品: " + fragile.execute());
        System.out.println();

        // 三、装饰器模式：上架策略
        System.out.println("【三、装饰器模式：上架策略】");
        PutawayStrategy normalPut = new NormalPutaway();
        System.out.println("场景1 - 普通上架: " + normalPut.execute());

        ShelfTemplate shuttleTemplate = factory.getTemplate("穿梭式");
        PutawayStrategy batchAndExpiry = new ExpiryPriorityDecorator(new BatchControlDecorator(new NormalPutaway()));
        System.out.println("场景2 - 普通+批次+效期: " + batchAndExpiry.execute());

        PutawayStrategy wholeSplit = new WholeSplitDecorator(new NormalPutaway(), shuttleTemplate);
        System.out.println("场景3 - 普通+整散分离: " + wholeSplit.execute());
        System.out.println();

        // 四、联合应用
        System.out.println("【四、联合应用：完整仓储作业流程】");
        ShelfTemplate template = factory.getTemplate("驶入式");
        Shelf workflowShelf = new Shelf("S-D-001", "D区", template);
        workflowShelf.setOccupied(true);
        System.out.println("1) 获取货架模板: " + workflowShelf.display());

        PickingStrategy picking = new ColdChainPickingDecorator(new WavePickingDecorator(new NormalPicking()));
        System.out.println("2) 执行拣货: " + picking.execute());

        PutawayStrategy putaway = new ExpiryPriorityDecorator(new BatchControlDecorator(new NormalPutaway()));
        System.out.println("3) 执行上架: " + putaway.execute());
        System.out.println("4) 模板创建总数: " + factory.getCreationCount());
        System.out.println();

        // 五、额外功能：模板禁用
        System.out.println("【五、额外功能：模板禁用】");
        factory.disableTemplate("轻型置物架");
        try {
            factory.getTemplate("轻型置物架");
        } catch (IllegalStateException e) {
            System.out.println("禁用验证: " + e.getMessage());
        }
        factory.enableTemplate("轻型置物架");
        System.out.println("恢复后可正常获取: " + factory.getTemplate("轻型置物架").getType());
    }
}
