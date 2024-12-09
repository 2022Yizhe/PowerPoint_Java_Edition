package org.powerpoint.window.component.item;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 视觉组件基类
 * 继承自基本组件 - JComponent
 * 分别派生出 Line, Rectangle, Oval, Circle, Image 等组件，负责设计基本图形和图像类
 */
public abstract class VisualItem extends JComponent {
    protected JPopupMenu popupMenu;
    protected boolean isSelected;
    protected Point mouseOffset;

    public boolean marked;
    protected Runnable deleteAction;

    public VisualItem(Runnable deleteAction) {
        popupMenu = new JPopupMenu();
        isSelected = false;
        mouseOffset = new Point(0,0);
        marked = false;
        this.deleteAction = deleteAction;
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
     * 处理左键拖动 + 右键菜单，这是所有派生组件公用的监听器
     */
    protected void addMouseListeners(){
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
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int x = getX() + e.getX() - mouseOffset.x;
                    int y = getY() + e.getY() - mouseOffset.y;
                    setLocation(x, y);  // 更新组件位置 (移动时)
                    saveChanges();
                }
            }
        });
    }

    /**
     * 保存编辑到 content
     * 由子类实现具体保存做法
     */
    abstract protected void saveChanges();
}
