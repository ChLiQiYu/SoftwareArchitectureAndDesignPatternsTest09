package com.mashang;

import com.mashang.decorator.putaway.*;
import com.mashang.flyweight.ShelfTemplate;
import com.mashang.flyweight.ShelfTemplateFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("上架策略装饰器测试")
class PutawayStrategyTest {
    private ShelfTemplateFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ShelfTemplateFactory();
    }

    @Test
    @DisplayName("普通上架：只包含基础逻辑")
    void testNormalPutaway() {
        PutawayStrategy strategy = new NormalPutaway();
        String result = strategy.execute();
        assertEquals("空闲库位就近上架", result);
    }

    @Test
    @DisplayName("普通+批次+效期：验证三重装饰叠加")
    void testBatchAndExpiryPutaway() {
        PutawayStrategy strategy = new ExpiryPriorityDecorator(
                new BatchControlDecorator(new NormalPutaway()));
        String result = strategy.execute();
        assertTrue(result.contains("空闲库位就近上架"));
        assertTrue(result.contains("同批次集中存放"));
        assertTrue(result.contains("近效期放外侧"));
    }

    @Test
    @DisplayName("整散分离装饰器：根据模板层数分配")
    void testWholeSplitDecorator() {
        ShelfTemplate template = factory.getTemplate("横梁式");
        assertEquals(4, template.getLayers());
        PutawayStrategy strategy = new WholeSplitDecorator(new NormalPutaway(), template);
        String result = strategy.execute();
        assertTrue(result.contains("整托放高层"));
        assertTrue(result.contains("散件放低层"));
        assertTrue(result.contains("第1-2层"), "4层模板的散件应放第1-2层");
        assertTrue(result.contains("第3-4层"), "4层模板的整托应放第3-4层");
    }

    @Test
    @DisplayName("整散分离：5层模板分配正确")
    void testWholeSplitDecoratorFiveLayers() {
        ShelfTemplate template = factory.getTemplate("穿梭式");
        assertEquals(5, template.getLayers());
        PutawayStrategy strategy = new WholeSplitDecorator(new NormalPutaway(), template);
        String result = strategy.execute();
        assertTrue(result.contains("第1-2层"), "5层模板的散件应放第1-2层");
        assertTrue(result.contains("第3-5层"), "5层模板的整托应放第3-5层");
    }

    @Test
    @DisplayName("优先级：数值越小优先级越高")
    void testPriority() {
        PutawayStrategy expiry = new ExpiryPriorityDecorator(new NormalPutaway());
        PutawayStrategy batch = new BatchControlDecorator(new NormalPutaway());
        assertTrue(expiry.getPriority() < batch.getPriority(), "效期优先级应高于批次管控");
    }

    @Test
    @DisplayName("完整装饰组合：批次+效期+整散分离")
    void testFullCombination() {
        ShelfTemplate template = factory.getTemplate("驶入式");
        PutawayStrategy strategy = new WholeSplitDecorator(
                new ExpiryPriorityDecorator(
                        new BatchControlDecorator(new NormalPutaway())), template);
        String result = strategy.execute();
        assertTrue(result.contains("空闲库位就近上架"));
        assertTrue(result.contains("同批次集中存放"));
        assertTrue(result.contains("近效期放外侧"));
        assertTrue(result.contains("整托放高层"));
    }
}
