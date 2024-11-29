package window.component;

import javax.swing.*;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;

class ShortSplitPaneDivider extends BasicSplitPaneDivider {
    public ShortSplitPaneDivider(BasicSplitPaneUI ui) {
        super(ui);
    }
    @Override
    public void paint(Graphics g) {
        // 设置画笔颜色
        super.paint(g);
        g.setColor(Color.LIGHT_GRAY);
        // 根据分割方向绘制线条
        int spacing = 12;
        if (getParent() instanceof JSplitPane) {
            JSplitPane splitPane = (JSplitPane) getParent();
            if (splitPane.getOrientation() == JSplitPane.HORIZONTAL_SPLIT) {
                // 绘制垂直的线条
                int dividerSize = getSize().width;
                g.drawLine(dividerSize / 2, spacing, dividerSize / 2, getHeight() - spacing);
            } else {
                // 绘制水平的线条
                int dividerSize = getSize().height;
                g.drawLine(spacing, dividerSize / 2, getWidth() - spacing, dividerSize / 2);
            }
        }
    }
}

class ShortSplitPaneUI extends BasicSplitPaneUI {
    @Override
    public BasicSplitPaneDivider createDefaultDivider() {
        return new ShortSplitPaneDivider(this);
    }
}

public class ShortSplitPane extends JSplitPane {
    public ShortSplitPane(int orientation) {
        super(orientation);
        setUI(new ShortSplitPaneUI());
    }
}