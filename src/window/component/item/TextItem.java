package window.component.item;

import entity.storage.TextContent;
import manage.SelectManager;
import window.enums.ColorName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 文本组件
 * 继承自文本域组件 - JTextArea
 */
public class TextItem extends JTextArea {
    private final TextContent text;
    private final JPopupMenu popupMenu;

    private boolean isSelected;
    private Point mouseOffset;

    public TextItem(TextContent text) {
        super(text.Value());

        // 配置文本
        this.text = text;
        configure();

        // 配置右键菜单 -- TODO
        this.popupMenu = new JPopupMenu();
        // popupMenu.add(new JMenuItem("Delete"));  // 示例菜单项
        this.setPreferredSize(new Dimension(0, 50));
        this.add(popupMenu);

        // 监听器
        this.addMouseListener(new MouseAdapter() {  // 监听器
            /// 注：mouseClicked 包括了 mousePressed 和 mouseReleased 两个监听信号，这里只用到 mousePressed
            @Override
            public void mousePressed(MouseEvent e) {
                // 记录鼠标相对组件的位置
                mouseOffset = e.getPoint();
                SelectManager.getInstance().selectItem(TextItem.this);  // 通知选择管理
                repaint();  // 鼠标按下时立即请求重绘，以显示边框
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)        // 左键无操作
                    ;   // do nothing
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
     * 组件配置逻辑，设置文本属性
     */
    private void configure(){
        this.setBounds(text.X(), text.Y(), 200, 80);    // 默认外边框
        this.setForeground(ColorName.getColor(text.Color()));

        this.setEditable(true);         // 可编辑
        this.setLineWrap(true);         // 自动换行
        this.setWrapStyleWord(true);    // 按词换行
        this.setOpaque(false);          // 透明背景
    }

    /**
     * 组件绘制逻辑
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 如果选中，绘制边框
        if (isSelected) {
            g.setColor(ColorName.LIGHT_GRAY.getColor());   // 设置边框颜色
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1); // 绘制边框
        }
    }

    /**
     * 组件复选逻辑
     * @param selected 选中使能信号
     */
    public void setSelected(boolean selected) {
        this.isSelected = selected;     // 设置选中状态
        this.setEditable(selected);     // 设置编辑状态 (注: 这里是文本组件和视觉组件不一样的地方)
        this.setFocusable(selected);    // 设置为不可聚焦
        repaint();  // 使能来临时立即请求重绘，以消除边框
    }
}

