package window.component;

import entity.storage.*;
import manage.SelectManager;
import window.component.item.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * 幻灯片面板
 * 继承自面板类，可用于添加各种形式的组件
 */
public class SlidePanel extends JPanel {
    private List<JComponent> items = new ArrayList<>();

    public SlidePanel() {
        this.setLayout(null);               // 采用绝对布局 (以便自由拖动组件)
        this.setBackground(Color.WHITE);    // 设置为白板 (背景色为白色)

        // 监听器
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 点击背景板时，清除所有组件的选中状态
                SelectManager.getInstance().clearAll();
            }
        });
    }

    public void addContent(AbstractContent content) {
        JComponent item = switch (content.ContentType()) {
            case "text" -> new TextItem((TextContent) content);
            case "line" -> new LineItem((LineContent) content);
            case "rectangle" -> new RectangleItem((RectangleContent) content);
            case "oval" -> new OvalItem((OvalContent) content);
            case "circle" -> new CircleItem((CircleContent) content);
            case "image" -> new ImageItem((ImageContent) content);
            default -> throw new IllegalArgumentException("Unsupported content type: " + content.ContentType());
        };

        // 组件识别成功后，先添加到管理容器，再添加到窗口
        items.add(item);
        this.add(item);
    }

    public void setContents(List<AbstractContent> contents) {
        items.clear();
        for (AbstractContent content : contents) {
            addContent(content);
        }
        repaint();  // 请求重绘
    }
}