package org.powerpoint.window.component.item;

import org.powerpoint.entity.storage.LineContent;
import org.powerpoint.manage.SelectManager;
import org.powerpoint.window.enums.ColorName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 直线组件
 * 继承自视觉组件 - VisualItem
 */
public class LineItem extends VisualItem {
    private final LineContent line;

    public LineItem(LineContent line) {
        super();

        // 配置直线
        this.line = line;
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
                    SelectManager.getInstance().selectItem(LineItem.this);  // 通知选择管理
                    repaint();  // 鼠标按下时立即请求重绘，以显示边框
                }
            }
        });
    }

    /**
     * 返回当前 content，用于保存编辑
     */
    public LineContent getLineContent() {
        return line;
    }

    /**
     * 组件配置逻辑，设置直线属性
     */
    private void configure(){
        this.setBounds(
                Math.min(line.StartX(), line.EndX()), Math.min(line.StartY(), line.EndY()),
                Math.abs(line.EndX() - line.StartX()), Math.abs(line.EndY() - line.StartY())
        );
        this.setOpaque(false);  // 透明背景
    }

    /**
     * 组件配置逻辑，设置右键弹出菜单
     */
    private void configureMenu(){
        // 粗度菜单项
        JMenu size = new JMenu("粗度");
        JMenuItem s_1 = new JMenuItem("1");
        JMenuItem s_2 = new JMenuItem("2");
        JMenuItem s_3 = new JMenuItem("3");
        JMenuItem s_4 = new JMenuItem("4");
        s_1.addActionListener(e -> {});
        s_2.addActionListener(e -> {});
        s_3.addActionListener(e -> {});
        s_4.addActionListener(e -> {});
        size.add(s_1);
        size.add(s_2);
        size.add(s_3);
        size.add(s_4);
        popupMenu.add(size);

        // 颜色菜单项
        JMenu color = configureColorMenu();
        popupMenu.add(color);

        // 删除菜单项
        JMenuItem delete = new JMenuItem("Delete");
        delete.addActionListener(e -> {});
        popupMenu.add(delete);
    }

    /**
     * 保存编辑到 content
     */
    protected void saveChanges() {
        line.setStartX(this.getX());
        line.setStartY(this.getY());
        line.setEndX(this.getX() + this.getWidth());
        line.setEndY(this.getY() + this.getHeight());
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

        // 设置线条颜色和宽度
        g2d.setColor(ColorName.getColor(line.Color()));
        g2d.setStroke(new BasicStroke(1.2F)); // 设置线条宽度

        // 绘制直线
        g2d.drawLine(0, 0, line.EndX() - line.StartX(), line.EndY() - line.StartY());

        // 如果选中，绘制边框
        if (isSelected) {
            g.setColor(ColorName.LIGHT_GRAY.getColor());   // 设置边框颜色
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // 绘制边框
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
        JMenuItem c_sky_blue = new JMenuItem("Sky_Blue");
        c_black.addActionListener(e -> {
            line.setColor(ColorName.BLACK.getColor().toString());     // 保存颜色更改
            this.getGraphics().setColor(ColorName.BLACK.getColor());    // 重绘
            this.repaint();
        });
        c_white.addActionListener(e -> {
            line.setColor(ColorName.BLACK.getColor().toString());
            this.getGraphics().setColor(ColorName.WHITE.getColor());
            this.repaint();
        });
        c_red.addActionListener(e -> {
            line.setColor(ColorName.RED.getColor().toString());
            this.getGraphics().setColor(ColorName.RED.getColor());
            this.repaint();
        });
        c_blue.addActionListener(e -> {
            line.setColor(ColorName.BLUE.getColor().toString());
            this.getGraphics().setColor(ColorName.BLUE.getColor());
            this.repaint();
        });
        c_green.addActionListener(e -> {
            line.setColor(ColorName.GREEN.getColor().toString());
            this.getGraphics().setColor(ColorName.GREEN.getColor());
            this.repaint();
        });
        c_sky_blue.addActionListener(e -> {
            line.setColor(ColorName.SKY_BLUE.getColor().toString());
            this.getGraphics().setColor(ColorName.SKY_BLUE.getColor());
            this.repaint();
        });
        color.add(c_black);
        color.add(c_white);
        color.add(c_red);
        color.add(c_blue);
        color.add(c_green);
        color.add(c_sky_blue);
        return color;
    }
}