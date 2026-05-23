package com.mashang;

import com.mashang.decorator.picking.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("拣货策略装饰器测试")
class PickingStrategyTest {

    @Test
    @DisplayName("普通拣货：只包含基础逻辑")
    void testNormalPicking() {
        PickingStrategy strategy = new NormalPicking();
        String result = strategy.execute();
        assertEquals("按库位顺序拣货", result);
    }

    @Test
    @DisplayName("普通+波次+冷链：验证三重装饰叠加")
    void testWaveAndColdChainPicking() {
        PickingStrategy strategy = new ColdChainPickingDecorator(
                new WavePickingDecorator(new NormalPicking()));
        String result = strategy.execute();
        assertTrue(result.contains("按库位顺序拣货"));
        assertTrue(result.contains("按波次聚合订单批量拣货"));
        assertTrue(result.contains("低温管控、限定拣货月台"));
    }

    @Test
    @DisplayName("普通+易碎品：验证两重装饰叠加")
    void testFragilePicking() {
        PickingStrategy strategy = new FragilePickingDecorator(new NormalPicking());
        String result = strategy.execute();
        assertTrue(result.contains("按库位顺序拣货"));
        assertTrue(result.contains("禁止抛扔、二次复核"));
    }

    @Test
    @DisplayName("优先级：数值越小优先级越高")
    void testPriority() {
        PickingStrategy coldChain = new ColdChainPickingDecorator(new NormalPicking());
        PickingStrategy fragile = new FragilePickingDecorator(new NormalPicking());
        PickingStrategy wave = new WavePickingDecorator(new NormalPicking());

        assertTrue(fragile.getPriority() < coldChain.getPriority(), "易碎品优先级应高于冷链");
        assertTrue(coldChain.getPriority() < wave.getPriority(), "冷链优先级应高于波次");
    }

    @Test
    @DisplayName("自定义优先级可覆盖默认值")
    void testCustomPriority() {
        PickingStrategy wave = new WavePickingDecorator(new NormalPicking(), 1);
        PickingStrategy fragile = new FragilePickingDecorator(new NormalPicking(), 100);
        assertTrue(wave.getPriority() < fragile.getPriority());
    }

    @Test
    @DisplayName("多重装饰器的执行顺序")
    void testExecutionOrder() {
        PickingStrategy strategy = new ColdChainPickingDecorator(
                new WavePickingDecorator(
                        new FragilePickingDecorator(new NormalPicking())));
        String result = strategy.execute();
        int fragileIdx = result.indexOf("禁止抛扔");
        int waveIdx = result.indexOf("按波次");
        int coldIdx = result.indexOf("低温管控");
        assertTrue(fragileIdx < waveIdx, "易碎品应先于波次执行");
        assertTrue(waveIdx < coldIdx, "波次应先于冷链执行");
    }
}
