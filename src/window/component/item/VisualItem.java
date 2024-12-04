package window.component.item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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

    /**
     * 添加鼠标监听器
     * 处理左键拖动 + 右键菜单
     */
    public void addMouseListeners(){
        // 监听
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)        // 左键
                    ;// DO NOTHING
                else if (e.getButton() == MouseEvent.BUTTON3)   // 右键显示菜单
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // 获取当前组件的位置
                int x = getX() + e.getX() - mouseOffset.x;
                int y = getY() + e.getY() - mouseOffset.y;
                setLocation(x, y);  // 更新组件位置 (移动时)
            }
        });
    }
}
