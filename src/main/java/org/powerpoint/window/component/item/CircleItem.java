package org.powerpoint.window.component.item;

import org.powerpoint.entity.storage.CircleContent;
import org.powerpoint.manage.SelectManager;
import org.powerpoint.window.enums.ColorName;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 圆形组件
 * 继承自视觉组件 - VisualItem
 */
public class CircleItem extends VisualItem {
    private final CircleContent circle;

    public CircleItem(CircleContent circle) {
        super();

        // 配置圆形
        this.circle = circle;
        configure();

        // 配置右键菜单 -- TODO
        // popupMenu.add(new JMenuItem("Delete"));  // 示例菜单项
        this.setPreferredSize(new Dimension(0, 50));
        this.add(popupMenu);

        // 监听器
        this.addMouseListeners();
        this.addMouseListener(new MouseAdapter() {
            /// 注：mouseClicked 包括了 mousePressed 和 mouseReleased 两个监听信号，这里只用到 mousePressed
            @Override
            public void mousePressed(MouseEvent e) {
                // 记录鼠标相对组件的位置
                mouseOffset = e.getPoint();
                SelectManager.getInstance().selectItem(CircleItem.this);  // 通知选择管理
                repaint();  // 鼠标按下时立即请求重绘，以显示边框
            }
        });
    }

    /**
     * 返回当前 content，用于保存编辑
     */
    public CircleContent getCircleContent() {
        return circle;
    }

    /**
     * 组件配置逻辑，设置圆形属性
     */
    private void configure(){
        this.setBounds(circle.X(), circle.Y(), circle.Radius() + 10, circle.Radius() + 10);

        this.setOpaque(false);  // 透明背景
    }

    /**
     * 保存编辑到 content -- TODO 保存颜色
     */
    protected void saveChanges() {
        circle.setX(this.getX());
        circle.setY(this.getY());
        circle.setRadius(this.getWidth() - 10);
    }

    /**
     * 组件绘制逻辑
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 将 Graphics 转换为 Graphics2D, 从而可以启用抗锯齿
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 填充圆形
        g2d.setColor(ColorName.DEFAULT.getColor());
//        g2d.fillRect(rectangle.X(), rectangle.Y(), rectangle.Width(), rectangle.Height());

        // 绘制圆形边框
        g2d.setColor(ColorName.getColor(circle.Color()));
        g2d.drawOval(5, 5, circle.Radius(), circle.Radius());   // 边框占用额外宽度，置中心

        // 如果选中，绘制边框
        if (isSelected) {
            g2d.setColor(ColorName.LIGHT_GRAY.getColor());   // 设置边框颜色
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // 绘制边框
        }
    }
}