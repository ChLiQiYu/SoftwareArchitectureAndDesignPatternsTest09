package com.mashang;

import com.mashang.decorator.picking.*;
import com.mashang.decorator.putaway.*;
import com.mashang.flyweight.ShelfTemplate;
import com.mashang.flyweight.ShelfTemplateFactory;
import com.mashang.model.Shelf;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("联合应用测试")
class IntegrationTest {

    @Test
    @DisplayName("完整仓储流程：获取模板→拣货→上架")
    void testFullWorkflow() {
        ShelfTemplateFactory factory = new ShelfTemplateFactory();

        ShelfTemplate template = factory.getTemplate("驶入式");
        Shelf shelf = new Shelf("S-W-001", "D区", template);
        shelf.setOccupied(true);

        assertNotNull(shelf.getTemplate());
        assertEquals("驶入式", shelf.getTemplate().getType());
        assertTrue(shelf.isOccupied());
        assertTrue(shelf.display().contains("S-W-001"));

        PickingStrategy picking = new ColdChainPickingDecorator(
                new WavePickingDecorator(new NormalPicking()));
        String pickResult = picking.execute();
        assertTrue(pickResult.contains("按库位顺序拣货"));
        assertTrue(pickResult.contains("按波次聚合订单批量拣货"));
        assertTrue(pickResult.contains("低温管控、限定拣货月台"));

        PutawayStrategy putaway = new ExpiryPriorityDecorator(
                new BatchControlDecorator(new NormalPutaway()));
        String putResult = putaway.execute();
        assertTrue(putResult.contains("空闲库位就近上架"));
        assertTrue(putResult.contains("同批次集中存放"));
        assertTrue(putResult.contains("近效期放外侧"));
    }

    @Test
    @DisplayName("验证享元模式内存优化：4种模板只创建4个实例")
    void testMemoryOptimization() {
        ShelfTemplateFactory factory = new ShelfTemplateFactory();

        for (int i = 0; i < 100; i++) {
            factory.getTemplate("横梁式");
            factory.getTemplate("驶入式");
            factory.getTemplate("穿梭式");
            factory.getTemplate("轻型置物架");
        }

        assertEquals(4, factory.getCreationCount(), "获取400次模板，但只创建4个实例");
    }

    @Test
    @DisplayName("联合应用含整散分离装饰器")
    void testWorkflowWithWholeSplit() {
        ShelfTemplateFactory factory = new ShelfTemplateFactory();
        ShelfTemplate template = factory.getTemplate("轻型置物架");

        PutawayStrategy putaway = new WholeSplitDecorator(
                new ExpiryPriorityDecorator(new NormalPutaway()), template);
        String result = putaway.execute();
        assertTrue(result.contains("空闲库位就近上架"));
        assertTrue(result.contains("近效期放外侧"));
        assertTrue(result.contains("整托放高层"));
    }
}
