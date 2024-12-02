package window.component.item;

import javax.swing.*;
import java.awt.*;

/**
 * 视觉组件基类
 * 继承自基本组件 - JComponent
 * 分别派生出 Line, Rectangle, Oval, Circle, Image 等组件，负责设计基本图形和图像类
 */
public class VisualItem extends JComponent {
    protected JPopupMenu popupMenu;
    protected boolean isSelected;
    protected Point mouseOffset;

    public VisualItem() {
        this.popupMenu = new JPopupMenu();
        isSelected = false;
        mouseOffset = new Point(0,0);
    }

    /**
     * 组件复选逻辑
     * @param selected 选中使能信号
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;
        repaint();  // 使能来临时立即请求重绘，以消除边框
    }
}
