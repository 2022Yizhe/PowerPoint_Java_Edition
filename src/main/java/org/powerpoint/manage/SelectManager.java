package org.powerpoint.manage;

import org.powerpoint.window.component.item.*;

import javax.swing.*;

/**
 * 编辑面板管理器
 * 用于处理鼠标信号，并发出编辑面板对应组件的使能
 */
public class SelectManager {
    private static SelectManager INSTANCE;

    private VisualItem selectedVItem;
    private TextItem selectedTItem;

    private SelectManager() {
        selectedTItem = null;
        selectedVItem = null;
    }

    public static SelectManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SelectManager();
        }
        return INSTANCE;
    }

    /**
     * 注册表，将组件注册到管理器，暂时无用
     * @param item 幻灯片面板中的组件
     */
    public void registerItem(JComponent item) {}

    /**
     * 管理选择信号，只允许一个 VisualItem 被选中
     * @param item VisualItem
     */
    public void selectItem(VisualItem item) {
        if (selectedVItem != null) {
            selectedVItem.setSelected(false);   // 取消之前选中的组件
        }

        selectedVItem = item;
        selectedVItem.setSelected(true);        // 选中新的组件
    }

    /**
     * 管理选择信号，只允许一个 TextItem 被选中
     * @param item TextItem
     */
    public void selectItem(TextItem item) {
        if (selectedTItem != null) {
            selectedTItem.setSelected(false);   // 取消之前选中的组件
        }

        selectedTItem = item;
        selectedTItem.setSelected(true);        // 选中新的组件
    }

    /**
     * 管理选择信号，清除所有组件的选中状态
     * shut down all selection enables
     */
    public void clearAll() {
        if (selectedTItem != null) {
            selectedTItem.setSelected(false);
            selectedTItem = null;
        }
        if (selectedVItem != null) {
            selectedVItem.setSelected(false);
            selectedVItem = null;
        }
    }
}