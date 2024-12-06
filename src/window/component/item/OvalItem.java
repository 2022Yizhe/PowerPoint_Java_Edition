package window.component.item;

import entity.storage.LineContent;
import entity.storage.OvalContent;
import manage.SelectManager;
import window.enums.ColorName;

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
        this.addMouseListeners();
        this.addMouseListener(new MouseAdapter() {
            /// 注：mouseClicked 包括了 mousePressed 和 mouseReleased 两个监听信号，这里只用到 mousePressed
            @Override
            public void mousePressed(MouseEvent e) {
                // 记录鼠标相对组件的位置
                mouseOffset = e.getPoint();
                SelectManager.getInstance().selectItem(OvalItem.this);  // 通知选择管理
                repaint();  // 鼠标按下时立即请求重绘，以显示边框
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
        this.setBounds(oval.X(), oval.Y(), oval.RadiusX() + 10, oval.RadiusY() + 10);   // 更宽以添加外边框

        this.setOpaque(false);  // 透明背景
    }

    /**
     * 保存编辑到 content -- TODO 保存颜色
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
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 填充椭圆形
        g2d.setColor(ColorName.DEFAULT.getColor());
//        g2d.fillRect(rectangle.X(), rectangle.Y(), rectangle.Width(), rectangle.Height());

        // 绘制椭圆形边框
        g2d.setColor(ColorName.getColor(oval.Color()));
        g2d.drawOval(5, 5, oval.RadiusX(), oval.RadiusY());   // 边框占用额外宽度，置中心

        // 如果选中，绘制边框
        if (isSelected) {
            g2d.setColor(ColorName.LIGHT_GRAY.getColor());   // 设置边框颜色
            g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // 绘制边框
        }
    }
}