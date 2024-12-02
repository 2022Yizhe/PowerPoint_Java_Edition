package window.component;

import entity.storage.*;
import window.component.item.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 幻灯片面板
 * 继承自面板类，可用于添加各种形式的组件
 */
public class SlidePanel extends JPanel {
    private final List<JComponent> items = new ArrayList<>();

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
        for (AbstractContent content : contents){
            addContent(content);
        }
        repaint();  // 请求重绘
    }
}