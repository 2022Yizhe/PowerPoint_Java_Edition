package org.powerpoint.window.component.item;

import org.powerpoint.entity.storage.CircleContent;
import org.powerpoint.manage.SelectManager;
import org.powerpoint.window.enums.ColorName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 圆形组件
 * 继承自视觉组件 - VisualItem
 */
public class CircleItem extends VisualItem {
    private final CircleContent circle;

    public CircleItem(CircleContent circle, Runnable deleteAction) {
        super(deleteAction);

        // 配置圆形
        this.circle = circle;
        configure();

        // 配置右键菜单
        configureMenu();
        this.add(popupMenu);

        // 监听器
        super.addMouseListeners();
        this.addMouseListener(new MouseAdapter() {
            /// 注：mouseClicked 包括了 mousePressed 和 mouseReleased 两个监听信号，这里只用到 mousePressed
            @Override
            public void mousePressed(MouseEvent e) {
                // 记录鼠标相对组件的位置
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseOffset = e.getPoint();
                    SelectManager.getInstance().selectItem(CircleItem.this);  // 通知选择管理
                    repaint();  // 鼠标按下时立即请求重绘，以显示边框
                }
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
        this.setBounds(circle.getX(), circle.getY(), circle.getRadius() + 10, circle.getRadius() + 10);
        this.setOpaque(false);  // 透明背景
    }

    /**
     * 组件配置逻辑，设置右键弹出菜单
     */
    private void configureMenu(){
        // 粗度菜单项
        JMenu thickness = configureThicknessMenu();
        popupMenu.add(thickness);

        // 颜色菜单项
        JMenu color = configureColorMenu();
        popupMenu.add(color);

        // 删除菜单项
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(e -> {
            marked = true;
            deleteAction.run();
            this.setVisible(false); // 因为已经绘制的不会自动消失，直接设为不可见，再次加载幻灯片时已删除
        });
        popupMenu.add(delete);
    }

    /**
     * 保存编辑到 content
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
        g2d.setStroke(new BasicStroke(circle.getThickness()));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 填充圆形
        g2d.setColor(ColorName.DEFAULT.getColor());
//        g2d.fillRect(rectangle.X(), rectangle.Y(), rectangle.Width(), rectangle.Height());

        // 绘制圆形边框
        g2d.setColor(ColorName.getColor(circle.getColor()));
        g2d.drawOval(5, 5, circle.getRadius(), circle.getRadius());   // 边框占用额外宽度，置中心

        // 如果选中，绘制边框
        if (isSelected) {
            g2d.setColor(ColorName.LIGHT_GRAY.getColor());   // 设置边框颜色
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // 绘制边框
        }
    }

    /**
     * 配置颜色菜单项
     * 这种方法只能一个一个添加，很麻烦
     * @return JMenu 一个配置完毕的颜色菜单项
     */
    private JMenu configureColorMenu(){
        JMenu color = new JMenu("颜色");
        JMenuItem c_black = new JMenuItem("Black");
        JMenuItem c_white = new JMenuItem("White");
        JMenuItem c_red = new JMenuItem("Red");
        JMenuItem c_blue = new JMenuItem("Blue");
        JMenuItem c_green = new JMenuItem("Green");
        JMenuItem c_yellow = new JMenuItem("Yellow");
        JMenuItem c_orange = new JMenuItem("Orange");
        JMenuItem c_gray = new JMenuItem("Gray");
        JMenuItem c_sky_blue = new JMenuItem("Sky_Blue");
        c_black.addActionListener(e -> {
            circle.setColor(ColorName.BLACK.getColor().toString());  // 保存颜色更改
            this.repaint(); // 重绘
        });
        c_white.addActionListener(e -> { circle.setColor(ColorName.BLACK.getColor().toString()); this.repaint(); });
        c_red.addActionListener(e -> { circle.setColor(ColorName.RED.getColor().toString()); this.repaint(); });
        c_blue.addActionListener(e -> { circle.setColor(ColorName.BLUE.getColor().toString()); this.repaint(); });
        c_green.addActionListener(e -> { circle.setColor(ColorName.GREEN.getColor().toString()); this.repaint(); });
        c_yellow.addActionListener(e -> { circle.setColor(ColorName.YELLOW.getColor().toString()); this.repaint(); });
        c_orange.addActionListener(e -> { circle.setColor(ColorName.ORANGE.getColor().toString()); this.repaint(); });
        c_gray.addActionListener(e -> { circle.setColor(ColorName.GRAY.getColor().toString()); this.repaint(); });
        c_sky_blue.addActionListener(e -> { circle.setColor(ColorName.SKY_BLUE.getColor().toString()); this.repaint(); });
        color.add(c_black);
        color.add(c_white);
        color.add(c_red);
        color.add(c_blue);
        color.add(c_green);
        color.add(c_yellow);
        color.add(c_orange);
        color.add(c_gray);
        color.add(c_sky_blue);
        return color;
    }

    /**
     * 配置粗度菜单项
     * 这种方法只能一个一个添加，很麻烦
     * @return JMenu 一个配置完毕的菜单项
     */
    private JMenu configureThicknessMenu(){
        JMenu thickness = new JMenu("粗度");
        JMenuItem s_1 = new JMenuItem("1");
        JMenuItem s_2 = new JMenuItem("2");
        JMenuItem s_3 = new JMenuItem("3");
        JMenuItem s_4 = new JMenuItem("4");
        s_1.addActionListener(e -> { circle.setThickness(1); this.repaint(); });
        s_2.addActionListener(e -> { circle.setThickness(2); this.repaint(); });
        s_3.addActionListener(e -> { circle.setThickness(3); this.repaint(); });
        s_4.addActionListener(e -> { circle.setThickness(4); this.repaint(); });
        thickness.add(s_1);
        thickness.add(s_2);
        thickness.add(s_3);
        thickness.add(s_4);
        return thickness;
    }
}