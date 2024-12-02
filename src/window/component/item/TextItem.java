package window.component.item;

import entity.storage.TextContent;
import window.enums.ColorName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class TextItem extends JTextArea {
    private final TextContent text;
    private final JPopupMenu popupMenu;

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
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1)        // 左键无操作
                    ;// do nothing
                else if (e.getButton() == MouseEvent.BUTTON3)   // 右键显示菜单
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    /**
     * 组件配置逻辑，设置文本属性
     */
    private void configure(){
        this.setBounds(text.X(), text.Y(), 200, 80);
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
        // 如果有额外的绘制逻辑，可在此处添加
    }
}

