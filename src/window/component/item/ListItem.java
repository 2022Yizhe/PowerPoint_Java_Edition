package window.component.item;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * 列表单元组件，继承自基本组件类
 * Rule.以此组件填充一个列表，用于构造序列化的 UI
 */
public class ListItem extends JComponent {
    private final String name;
    private final String filepath;
    private boolean mouseOver = false;      // 鼠标是否悬停在该组件上
    private Runnable clickAction = () -> {};

    private final JPopupMenu popupMenu = new JPopupMenu();  // 右键上下文菜单

    public ListItem(String name, String filepath) {
        this.name = name;
        this.filepath = filepath;
        this.setUI(new ListItemUI());
        this.setPreferredSize(new Dimension(0, 50));
        this.add(popupMenu);

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
                if(e.getButton() == MouseEvent.BUTTON1)         // 左键打开项目
                    clickAction.run();
                else if (e.getButton() == MouseEvent.BUTTON3)   // 右键显示菜单
                    popupMenu.show(ListItem.this, e.getX(), e.getY());
            }
        });
    }

    public void setClickAction(Runnable clickAction){
        this.clickAction = clickAction;
    }

    public void configurePopupMenu(Consumer<JPopupMenu> consumer){
        consumer.accept(this.popupMenu);
    }

    // 内部类, 对外观进行布局
    private class ListItemUI extends ComponentUI {
        @Override
        public void paint(Graphics g, JComponent c) {
            // 写圆角矩形
            if(mouseOver) {
                g.setColor(new Color(255, 255, 255, 128));
                g.fillRoundRect(5, 5, c.getWidth() - 10, c.getHeight() - 5, 10, 10);    // 画悬停时的强调背景
            }
            g.setColor(hashColor(name.hashCode()));
            g.fillRoundRect(10, 10, 35, 35, 10, 10);    // 画一个组件小图标（采用随机颜色）

            // 写文件路径
            g.setColor(Color.WHITE);
            g.drawString(filepath, 50, 42);

            // 写名称（指定样式为'PLAIN'）
            Font font = g.getFont();
            g.setFont(new Font(font.getName(), Font.PLAIN, 15));    // 采用原字体, 样式为普通（非粗体、非斜体等）, 大小为 15px
            g.drawString(name, 50, 22);
            g.setColor(Color.WHITE);
            g.setFont(new Font(font.getName(), Font.PLAIN, 20));
            g.drawString(name.substring(0, 1), 20, 35);
        }
    }

    /**
     * 根据哈希值随机选择一个颜色
     * @return 颜色
     */
    private Color hashColor(int hashCode){
        Color[] colors = {Color.PINK, Color.ORANGE, new Color(0, 201, 87), new Color(160, 102, 211),
                new Color(227, 207, 87), new Color(221, 160, 221), new Color(51, 161, 210),
                new Color(46, 139, 87), new Color(252, 230, 201), new Color(128, 138, 135)};
        int index = Math.abs(hashCode) % colors.length;
        return colors[index];
    }
}
