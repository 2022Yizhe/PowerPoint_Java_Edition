package org.powerpoint.window.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

class NoDotsSplitPaneDivider extends BasicSplitPaneDivider {
    public NoDotsSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    }
    @Override
    public void paint(Graphics g) {
        // 设置画笔颜色
        super.paint(g);
        // do nothing
    }
}

class NoDotsSplitPaneUI extends BasicSplitPaneUI {
    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new NoDotsSplitPaneDivider(this);
    }
}

/**
 * 不带三个点的分割面板
 * 继承自分割面板类
 */
public class NoDotsSplitPane extends JSplitPane {
    public NoDotsSplitPane(int orientation) {
        super(orientation);
        setUI(new NoDotsSplitPaneUI());
    }
}
