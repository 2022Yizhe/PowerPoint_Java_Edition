package window.component.item;

import entity.storage.LineContent;
import window.enums.ColorName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LineItem extends JComponent {
    private final LineContent line;
    private final JPopupMenu popupMenu;

    public LineItem(LineContent line) {
        super();

        // 配置直线
        this.line = line;
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
    }
}