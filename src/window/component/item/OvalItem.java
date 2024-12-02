package window.component.item;

import entity.storage.OvalContent;
import window.enums.ColorName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class OvalItem extends JComponent {
    private final OvalContent oval;
    private final JPopupMenu popupMenu;

    public OvalItem(OvalContent oval) {
        super();

        // 配置椭圆形
        this.oval = oval;
        configure();

        // 配置右键菜单 -- TODO
        this.popupMenu = new JPopupMenu();
        // popupMenu.add(new JMenuItem("Delete"));  // 示例菜单项
        this.setPreferredSize(new Dimension(0, 50));
        this.add(popupMenu);

        // 监听器
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)        // 左键
                    ;// DO NOTHING
                else if (e.getButton() == MouseEvent.BUTTON3)   // 右键显示菜单
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    /**
     * 组件配置逻辑，设置椭圆形属性
     */
    private void configure(){
        this.setBounds(oval.X(), oval.Y(), oval.RadiusX(), oval.RadiusY());

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

        // 填充椭圆形
        g2d.setColor(ColorName.DEFAULT.getColor());
//        g2d.fillRect(rectangle.X(), rectangle.Y(), rectangle.Width(), rectangle.Height());

        // 绘制椭圆形边框
        g2d.setColor(ColorName.getColor(oval.Color()));
        g2d.drawOval(0, 0, oval.RadiusX() - 10, oval.RadiusY() - 10);   // 边框占用额外宽度
    }
}