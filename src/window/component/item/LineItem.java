package window.component.item;

import entity.storage.LineContent;
import manage.SelectManager;
import window.enums.ColorName;

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

        // 配置右键菜单 -- TODO
        this.popupMenu = new JPopupMenu();
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
                SelectManager.getInstance().selectItem(LineItem.this);  // 通知选择管理
                repaint();  // 鼠标按下时立即请求重绘，以显示边框
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

        // 如果选中，绘制边框
        if (isSelected) {
            g.setColor(ColorName.LIGHT_GRAY.getColor());   // 设置边框颜色
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // 绘制边框
        }
    }
}