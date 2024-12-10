package org.powerpoint.window.component.button;

import javax.swing.border.AbstractBorder;
import java.awt.*;

/**
 * 边框样式 - 只画底部一条线
 */
public class BottomLineBorder extends AbstractBorder {
    private final Color color;
    private final int thickness;

    public BottomLineBorder(Color color, int thickness) {
        this.color = color;
        this.thickness = thickness;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRect(x, y + height - thickness, width, thickness);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(0, 0, thickness, 0);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = 0;
        insets.top = 0;
        insets.right = 0;
        insets.bottom = thickness;
        return insets;
    }
}
