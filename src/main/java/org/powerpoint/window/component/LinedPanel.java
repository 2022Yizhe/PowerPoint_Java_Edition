package org.powerpoint.window.component;

import javax.swing.*;
import java.awt.*;

/**
 * 边线面板
 * 继承自面板类，为面板画一条分界线
 */
public class LinedPanel extends JPanel {
    private final boolean top;
    private final boolean bottom;
    private final boolean left;
    private final boolean right;

    /**
     * 这里因为只需要底部添加分界线，所以说就不写其他的构造方法了
     * @param bottom 是否添加底部分界线
     */
    public LinedPanel(boolean bottom) {
        this.bottom = bottom;
        this.top = this.left = this.right = false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);   // 原有的绘制不变，以下是额外的边界线绘制操作
        g.setColor(Color.GRAY);
        if (top) g.drawLine(0, 0, this.getWidth(), 0);
        if (bottom) g.drawLine(0, this.getHeight(), this.getWidth(), this.getHeight());
        if (left) g.drawLine(0, 0, 0, this.getHeight());
        if (right) g.drawLine(this.getWidth(), 0, this.getWidth(), this.getHeight());
    }
}
