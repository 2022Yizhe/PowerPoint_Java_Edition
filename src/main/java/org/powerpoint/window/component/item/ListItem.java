package org.powerpoint.window.component.item;

import lombok.Setter;
import org.powerpoint.window.enums.ColorName;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 列表单元组件
 * 继承自基本组件 - JComponent
 * 以此组件填充一个列表，用于构造预览面板
 */
public class ListItem extends JComponent {
    private final String title;     // 幻灯片的标题 (独立，并不属于 content)

    @Setter
    private boolean isChecked;      // 选中状态

    /**
     * -- SETTER --
     *  配置右键菜单
     *  在 clickAction 中配置
     */
    @Setter
    private JPopupMenu popupMenu;   // 右键上下文菜单

    /**
     * -- SETTER --
     *  允许外部代码设置 clickAction 的具体实现
     */
    @Setter
    private Runnable clickAction;       // 用于存储点击时要执行的操作，即外嵌代码，初始为 null
    private boolean mouseOver = false;  // 鼠标是否悬停在该组件上，初始为 false

    public ListItem(String title, Runnable leftClickAction) {
        this.title = title;
        this.popupMenu = null;
        this.clickAction = leftClickAction; // 左键渲染项目，并配置右键菜单

        // 配置自定义外观
        this.setUI(new ListItemUI());
        this.setPreferredSize(new Dimension(0, 50));

        // 监听器
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                mouseOver = true;
                ListItem.this.repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                mouseOver = false;
                ListItem.this.repaint();
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                // 点击操作
                if(e.getButton() == MouseEvent.BUTTON1)         // 左键渲染项目 - clickAction 在 Service 层构造
                    clickAction.run();
                else if (e.getButton() == MouseEvent.BUTTON3) { // 右键显示菜单，也渲染项目 - clickAction 在 Service 层构造
                    clickAction.run();
                    popupMenu.show(ListItem.this, e.getX(), e.getY());
                }
                // 为 Item 增加强调背景
                isChecked = true;
                ListItem.this.repaint();
            }
        });
    }

    // 内部类, 对外观进行布局
    private class ListItemUI extends ComponentUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            // 将 Graphics 转换为 Graphics2D, 从而可以启用抗锯齿
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 绘制强调背景 (鼠标悬停时)
            if(mouseOver) {
                g.setColor(new Color(255, 255, 255, 192));      // 设置强调颜色
                g.fillRoundRect(5, 5, c.getWidth() - 5, c.getHeight() - 5, 10, 10);
                g.setColor(ColorName.DEFAULT.getColor());                   // 设置边框颜色
                g.drawRect(5, 5, c.getWidth() - 5, c.getHeight() - 5);
            } else {
                // 绘制选中状态背景
                if (isChecked){
                    g.setColor(new Color(224, 224, 224, 192));  // 设置选中颜色
                    g.fillRoundRect(5, 5, c.getWidth() - 5, c.getHeight() - 5, 10, 10);
                } else {
                    g.setColor(ColorName.DEFAULT.getColor());               // 设置未选中颜色
                    g.fillRoundRect(5, 5, c.getWidth() - 5, c.getHeight() - 5, 10, 10);
                }
            }

            // 绘制幻灯片标题 (指定样式为 'PLAIN')
            Font font = g2d.getFont();
            g2d.setColor(ColorName.DARK_GRAY.getColor());
            g2d.setFont(new Font(font.getName(), Font.PLAIN, 15));  // 采用原字体, 样式为普通（非粗体、非斜体等）, 大小为 15px
            g2d.drawString(title, 50, 25);

            // 绘制一个小图标 (随机颜色)
            g2d.setColor(hashColor(title.hashCode()));
            g2d.fillRoundRect(10, 10, 35, 35, 10, 10);

            // 绘制小图标上的字
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font(font.getName(), Font.PLAIN, 20));
            g2d.drawString(title.substring(0, 1), 20, 35);
        }
    }

    /**
     * 根据哈希值随机选择一个颜色
     * @return 颜色
     */
    private Color hashColor(int hashCode){
        Color[] colors = {
                Color.PINK, Color.ORANGE, new Color(0, 201, 87), new Color(160, 102, 211),
                new Color(227, 207, 87), new Color(221, 160, 221), new Color(51, 161, 210),
                new Color(46, 139, 87), new Color(252, 230, 201), new Color(128, 138, 135)
        };
        int index = Math.abs(hashCode) % colors.length;
        return colors[index];
    }
}
