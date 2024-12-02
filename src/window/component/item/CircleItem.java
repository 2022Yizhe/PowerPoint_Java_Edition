package window.component.item;

import entity.storage.CircleContent;
import manage.SelectManager;
import window.enums.ColorName;

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
        this.addMouseListener(new MouseAdapter() {
            /// 注：mouseClicked 包括了 mousePressed 和 mouseReleased 两个监听信号，这里只用到 mousePressed
            @Override
            public void mousePressed(MouseEvent e) {
                // 记录鼠标相对组件的位置
                mouseOffset = e.getPoint();
                SelectManager.getInstance().selectItem(CircleItem.this);  // 通知选择管理
                repaint();  // 鼠标按下时立即请求重绘，以显示边框
            }

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

    /**
     * 组件配置逻辑，设置圆形属性
     */
    private void configure(){
        this.setBounds(circle.X(), circle.Y(), circle.Radius() + 10, circle.Radius() + 10);

        this.setOpaque(false);  // 透明背景
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