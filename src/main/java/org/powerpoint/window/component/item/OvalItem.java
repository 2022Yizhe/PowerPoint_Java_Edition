package org.powerpoint.window.component.item;

import org.powerpoint.entity.storage.OvalContent;
import org.powerpoint.manage.SelectManager;
import org.powerpoint.window.enums.ColorName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 椭圆形组件
 * 继承自视觉组件 - VisualItem
 */
public class OvalItem extends VisualItem {
    private final OvalContent oval;

    public OvalItem(OvalContent oval, Runnable deleteAction) {
        super(deleteAction);

        // 配置椭圆形
        this.oval = oval;
        configure();

        // 配置右键菜单
        configureMenu();
        this.add(popupMenu);

        // 监听器
        this.addMouseListeners();
        this.addMouseListener(new MouseAdapter() {
            /// 注：mouseClicked 包括了 mousePressed 和 mouseReleased 两个监听信号，这里只用到 mousePressed
            @Override
            public void mousePressed(MouseEvent e) {
                // 点击右下角时，开始缩放组件大小
                if(getWidth() - e.getX() <= 10 && getHeight() - e.getY() <= 10) {
                    System.out.println("[PowerPoint] (OvalItem) start resizing");
                    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    resizing = true;
                }
                // 记录鼠标相对组件的位置
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseOffset = e.getPoint();
                    SelectManager.getInstance().selectItem(OvalItem.this);  // 通知选择管理
                    repaint();  // 鼠标按下时立即请求重绘，以显示边框
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {          // 左键恢复光标
                    if (resizing) {
                        resizing = false;
                        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
                    }
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            // 监听拖动事件
            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing) {     // 在调整大小时更新组件的宽高
                    int newWidth = e.getX();
                    int newHeight = e.getY();
                    if (newWidth != getWidth() || newHeight != getHeight()) {
                        setSize(Math.max(50, newWidth), Math.max(50, newHeight)); // 最小尺寸限制
                        saveChanges();
                    }
                }
            }
        });
    }

    /**
     * 返回当前 content，用于保存编辑
     */
    public OvalContent getOvalContent() {
        return oval;
    }

    /**
     * 组件配置逻辑，设置椭圆形属性
     */
    private void configure(){
        this.setBounds(oval.getX(), oval.getY(), oval.getRadiusX() + 10, oval.getRadiusY() + 10);   // 更宽以添加外边框
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
        oval.setX(this.getX());
        oval.setY(this.getY());
        oval.setRadiusX(this.getWidth() - 10);
        oval.setRadiusY(this.getHeight() - 10);
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
        g2d.setStroke(new BasicStroke(oval.getThickness()));
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 填充椭圆形
        g2d.setColor(ColorName.DEFAULT.getColor());
//        g2d.fillRect(rectangle.X(), rectangle.Y(), rectangle.Width(), rectangle.Height());

        // 绘制椭圆形边框
        g2d.setColor(ColorName.getColor(oval.getColor()));
        g2d.drawOval(5, 5, oval.getRadiusX(), oval.getRadiusY());   // 边框占用额外宽度，置中心

        // 如果选中，绘制边框
        if (isSelected) {
            g2d.setColor(ColorName.LIGHT_GRAY.getColor());    // 设置边框默认颜色
            g2d.setStroke(new BasicStroke(0.8F));       // 设置边框默认宽度
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    /**
     * 配置颜色菜单项
     * 这种方法只能一个一个添加，很麻烦
     * @return JMenu 一个配置完毕的颜色菜单项
     */
    protected JMenu configureColorMenu(){
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
            oval.setColor(ColorName.BLACK.getColor().toString());  // 保存颜色更改
            this.repaint(); // 重绘
        });
        c_white.addActionListener(e -> { oval.setColor(ColorName.WHITE.getColor().toString()); this.repaint(); });
        c_red.addActionListener(e -> { oval.setColor(ColorName.RED.getColor().toString()); this.repaint(); });
        c_blue.addActionListener(e -> { oval.setColor(ColorName.BLUE.getColor().toString()); this.repaint(); });
        c_green.addActionListener(e -> { oval.setColor(ColorName.GREEN.getColor().toString()); this.repaint(); });
        c_yellow.addActionListener(e -> { oval.setColor(ColorName.YELLOW.getColor().toString()); this.repaint(); });
        c_orange.addActionListener(e -> { oval.setColor(ColorName.ORANGE.getColor().toString()); this.repaint(); });
        c_gray.addActionListener(e -> { oval.setColor(ColorName.GRAY.getColor().toString()); this.repaint(); });
        c_sky_blue.addActionListener(e -> { oval.setColor(ColorName.SKY_BLUE.getColor().toString()); this.repaint(); });
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
        s_1.addActionListener(e -> { oval.setThickness(1); this.repaint(); });
        s_2.addActionListener(e -> { oval.setThickness(2); this.repaint(); });
        s_3.addActionListener(e -> { oval.setThickness(3); this.repaint(); });
        s_4.addActionListener(e -> { oval.setThickness(4); this.repaint(); });
        thickness.add(s_1);
        thickness.add(s_2);
        thickness.add(s_3);
        thickness.add(s_4);
        return thickness;
    }
}