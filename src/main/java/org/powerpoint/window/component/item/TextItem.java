package org.powerpoint.window.component.item;

import org.powerpoint.entity.storage.TextContent;
import org.powerpoint.manage.SelectManager;
import org.powerpoint.window.enums.ColorName;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * 文本组件
 * 继承自文本域组件 - JTextArea
 * 由于与 VisualItem 类不同派生，故本类的方法只能集中实现，代码相比 LineItem 等类较多
 */
public class TextItem extends JTextArea {
    private final TextContent text;
    private final JPopupMenu popupMenu;

    private boolean isSelected = false;
    private Point mouseOffset;

    public boolean marked = false;
    private Runnable deleteAction;

    private boolean resizing = false;

    public TextItem(TextContent text, Runnable deleteAction) {
        super(text.getValue());
        this.setFont(new Font(text.getFont(), Font.PLAIN, text.getSize()));

        // 配置文本
        this.text = text;
        configure();

        // 配置右键菜单
        this.popupMenu = new JPopupMenu();
        configureMenu();
        this.add(popupMenu);

        // 配置 Runnable
        this.deleteAction = deleteAction;

        // 添加 Mouse 监听器，监听鼠标点击事件
        this.addMouseListener(new MouseAdapter() {
            /// 注：mouseClicked 包括了 mousePressed 和 mouseReleased 两个监听信号，这里只用到 mousePressed
            @Override
            public void mousePressed(MouseEvent e) {
                // 点击右下角时，开始缩放组件大小
                if(getWidth() - e.getX() <= 10 && getHeight() - e.getY() <= 10) {
                    System.out.println("start resize");
                    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    resizing = true;
                }
                // 记录鼠标相对组件的位置
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseOffset = e.getPoint();
                    SelectManager.getInstance().selectItem(TextItem.this);  // 通知选择管理
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
                } else if (e.getButton() == MouseEvent.BUTTON3) {   // 右键显示菜单
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // 添加 MouseMotionListener 监听器
        this.addMouseMotionListener(new MouseAdapter() {
            // 监听鼠标拖动事件
            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing) {     // 在调整大小时更新组件的宽高
                    int newWidth = e.getX();
                    int newHeight = e.getY();
                    if (newWidth != getWidth() || newHeight != getHeight()) {
                        setSize(Math.max(25, newWidth), Math.max(25, newHeight)); // 最小尺寸限制
                        saveChanges();
                    }
                } else if (SwingUtilities.isLeftMouseButton(e)) {   // 获取当前组件的位置
                    if (!popupMenu.isVisible()) {
                        int x = getX() + e.getX() - mouseOffset.x;
                        int y = getY() + e.getY() - mouseOffset.y;
                        setLocation(x, y);  // 更新组件位置 (移动时)
                        saveChanges();
                    }
                }
            }
        });

        // 添加 DocumentListener 监听器，监听文本变化
        this.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { saveChanges(); }

            @Override
            public void removeUpdate(DocumentEvent e) { saveChanges(); }

            @Override
            public void changedUpdate(DocumentEvent e) { saveChanges(); }
        });
    }

    /**
     * 返回当前 content，用于保存到 slide.List<content>
     */
    public TextContent getTextContent() {
        return text;
    }

    /**
     * 组件配置逻辑，设置文本属性
     */
    private void configure(){
        this.setBounds(text.getX(), text.getY(), text.getWidth(), text.getHeight());    // TODO 支持鼠标调整大小
        this.setForeground(ColorName.getColor(text.getColor()));

        this.setEditable(true);         // 可编辑
        this.setLineWrap(true);         // 自动换行
        this.setWrapStyleWord(true);    // 按词换行
        this.setOpaque(false);          // 透明背景
    }

    /**
     * 组件配置逻辑，设置右键弹出菜单
     */
    private void configureMenu() {
        // 字体菜单项
        JMenu font = configureFontMenu();
        popupMenu.add(font);

        // 大小菜单项
        JMenu size = configureSizeMenu();
        popupMenu.add(size);

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
     * 可以优化，没必要每次编辑后都更新多个属性，因为一次性只更新一个属性
     */
    private void saveChanges() {
        text.setX(this.getX());
        text.setY(this.getY());
        text.setWidth(this.getWidth());
        text.setHeight(this.getHeight());
        text.setValue(this.getText());
        text.setColor(ColorName.getColorName(this.getForeground()));
        text.setFont(this.getFont().getFamily());
        text.setSize(this.getFont().getSize());
        // 还可以添加其他保存逻辑，比如保存到文件或数据库
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

    /**
     * 配置颜色菜单项
     * 这种方法只能一个一个添加，很麻烦
     * @return JMenu 一个配置完毕的菜单项
     */
    private JMenu configureColorMenu() {
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
        c_black.addActionListener(e -> { this.setForeground(ColorName.BLACK.getColor()); this.saveChanges();});     // 保存颜色更改
        c_white.addActionListener(e -> { this.setForeground(ColorName.WHITE.getColor()); this.saveChanges();});
        c_red.addActionListener(e -> { this.setForeground(ColorName.RED.getColor()); this.saveChanges();});
        c_blue.addActionListener(e -> { this.setForeground(ColorName.BLUE.getColor()); this.saveChanges();});
        c_green.addActionListener(e -> { this.setForeground(ColorName.GREEN.getColor()); this.saveChanges();});
        c_yellow.addActionListener(e -> { this.setForeground(ColorName.YELLOW.getColor()); this.saveChanges();});
        c_orange.addActionListener(e -> { this.setForeground(ColorName.ORANGE.getColor()); this.saveChanges();});
        c_gray.addActionListener(e -> { this.setForeground(ColorName.GRAY.getColor()); this.saveChanges();});
        c_sky_blue.addActionListener(e -> { this.setForeground(ColorName.SKY_BLUE.getColor()); this.saveChanges();});
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
     * 配置字体菜单项
     * 这种方法只能一个一个添加，很麻烦
     * @return JMenu 一个配置完毕的菜单项
     */
    private JMenu configureFontMenu() {
        JMenu font = new JMenu("字体");
        JMenuItem f_songti = new JMenuItem("宋体");
        JMenuItem f_kaiti = new JMenuItem("楷体");
        JMenuItem f_dengxian = new JMenuItem("等线");
        JMenuItem f_Microsoft_YaHei = new JMenuItem("Microsoft YaHei");
        JMenuItem f_Arial_Unicode_MS = new JMenuItem("Arial Unicode MS");
        f_songti.addActionListener(e -> { this.setFont(new Font("宋体", Font.PLAIN, text.getSize())); this.saveChanges(); });
        f_kaiti.addActionListener(e -> { this.setFont(new Font("楷体", Font.PLAIN, text.getSize())); this.saveChanges(); });
        f_dengxian.addActionListener(e -> { this.setFont(new Font("等线", Font.PLAIN, text.getSize())); this.saveChanges(); });
        f_Microsoft_YaHei.addActionListener(e -> { this.setFont(new Font("Microsoft YaHei", Font.PLAIN, text.getSize())); this.saveChanges(); });
        f_Arial_Unicode_MS.addActionListener(e -> { this.setFont(new Font("Arial Unicode MS", Font.PLAIN, text.getSize())); this.saveChanges(); });
        font.add(f_songti);
        font.add(f_kaiti);
        font.add(f_dengxian);
        font.add(f_Microsoft_YaHei);
        font.add(f_Arial_Unicode_MS);
        return font;
    }

    /**
     * 配置大小菜单项 (字体大小)
     * 这种方法只能一个一个添加，很麻烦
     * @return JMenu 一个配置完毕的菜单项
     */
    private JMenu configureSizeMenu() {
        JMenu size = new JMenu("大小");
        JMenuItem s_9 = new JMenuItem("9");
        JMenuItem s_12 = new JMenuItem("12");
        JMenuItem s_15 = new JMenuItem("15");
        JMenuItem s_18 = new JMenuItem("18");
        JMenuItem s_21 = new JMenuItem("21");
        JMenuItem s_24 = new JMenuItem("24");
        JMenuItem s_30 = new JMenuItem("30");
        JMenuItem s_45 = new JMenuItem("45");
        s_9.addActionListener(e -> { this.setFont(new Font(text.getFont(), Font.PLAIN, 9)); this.saveChanges(); });
        s_12.addActionListener(e -> { this.setFont(new Font(text.getFont(), Font.PLAIN, 12)); this.saveChanges(); });
        s_15.addActionListener(e -> { this.setFont(new Font(text.getFont(), Font.PLAIN, 15)); this.saveChanges(); });
        s_18.addActionListener(e -> { this.setFont(new Font(text.getFont(), Font.PLAIN, 18)); this.saveChanges(); });
        s_21.addActionListener(e -> { this.setFont(new Font(text.getFont(), Font.PLAIN, 21)); this.saveChanges(); });
        s_24.addActionListener(e -> { this.setFont(new Font(text.getFont(), Font.PLAIN, 24)); this.saveChanges(); });
        s_30.addActionListener(e -> { this.setFont(new Font(text.getFont(), Font.PLAIN, 30)); this.saveChanges(); });
        s_45.addActionListener(e -> { this.setFont(new Font(text.getFont(), Font.PLAIN, 45)); this.saveChanges(); });
        size.add(s_9);
        size.add(s_12);
        size.add(s_15);
        size.add(s_18);
        size.add(s_21);
        size.add(s_24);
        size.add(s_30);
        size.add(s_45);
        return size;
    }
}

