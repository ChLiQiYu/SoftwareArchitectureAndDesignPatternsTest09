package com.mashang;

import com.mashang.flyweight.ShelfTemplate;
import com.mashang.flyweight.ShelfTemplateFactory;
import com.mashang.model.Shelf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("享元模式测试")
class FlyweightTest {
    private ShelfTemplateFactory factory;

    @BeforeEach
    void setUp() {
        factory = new ShelfTemplateFactory();
    }

    @Test
    @DisplayName("同一类型模板多次获取应返回同一对象")
    void testTemplateReuse() {
        ShelfTemplate t1 = factory.getTemplate("横梁式");
        ShelfTemplate t2 = factory.getTemplate("横梁式");
        assertSame(t1, t2, "同一类型模板应复用同一实例");
    }

    @Test
    @DisplayName("不同类型模板应返回不同对象")
    void testDifferentTemplates() {
        ShelfTemplate t1 = factory.getTemplate("横梁式");
        ShelfTemplate t2 = factory.getTemplate("驶入式");
        assertNotSame(t1, t2);
        assertEquals("横梁式", t1.getType());
        assertEquals("驶入式", t2.getType());
    }

    @Test
    @DisplayName("创建数量应等于模板种类数")
    void testCreationCount() {
        factory.getTemplate("横梁式");
        factory.getTemplate("横梁式");
        factory.getTemplate("驶入式");
        factory.getTemplate("穿梭式");
        factory.getTemplate("轻型置物架");
        assertEquals(4, factory.getCreationCount(), "预注册4种模板，创建数应为4");
    }

    @Test
    @DisplayName("不同库区货架复用同一模板")
    void testShelfReuse() {
        ShelfTemplate template = factory.getTemplate("横梁式");
        Shelf shelf1 = new Shelf("S-A-001", "A区", template);
        Shelf shelf2 = new Shelf("S-B-002", "B区", template);
        Shelf shelf3 = new Shelf("S-C-003", "C区", template);

        assertSame(shelf1.getTemplate(), shelf2.getTemplate());
        assertSame(shelf2.getTemplate(), shelf3.getTemplate());

        String display1 = shelf1.display();
        assertTrue(display1.contains("S-A-001"));
        assertTrue(display1.contains("A区"));
        assertTrue(display1.contains("横梁式"));

        String display2 = shelf2.display();
        assertTrue(display2.contains("S-B-002"));
        assertTrue(display2.contains("B区"));
    }

    @Test
    @DisplayName("禁用模板后获取应抛出异常")
    void testDisableTemplate() {
        factory.getTemplate("轻型置物架");
        factory.disableTemplate("轻型置物架");
        assertThrows(IllegalStateException.class, () -> factory.getTemplate("轻型置物架"));
    }

    @Test
    @DisplayName("恢复禁用模板后可正常获取")
    void testEnableTemplate() {
        factory.disableTemplate("横梁式");
        assertThrows(IllegalStateException.class, () -> factory.getTemplate("横梁式"));
        factory.enableTemplate("横梁式");
        assertNotNull(factory.getTemplate("横梁式"));
    }

    @Test
    @DisplayName("display输出包含模板规格和外部状态")
    void testDisplayContent() {
        ShelfTemplate template = factory.getTemplate("穿梭式");
        String result = template.display("S-X-001", "X区");
        assertTrue(result.contains("S-X-001"), "应包含货架编号");
        assertTrue(result.contains("X区"), "应包含库区");
        assertTrue(result.contains("穿梭式"), "应包含模板类型");
    }
}
