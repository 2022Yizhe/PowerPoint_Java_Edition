package org.powerpoint.window.component;

import lombok.Getter;
import org.powerpoint.entity.storage.*;
import org.powerpoint.manage.SelectManager;
import org.powerpoint.window.component.item.*;

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
    private List<TextItem> text_items = new ArrayList<>();
    private List<LineItem> line_items = new ArrayList<>();
    private List<RectangleItem> rectangle_items = new ArrayList<>();
    private List<OvalItem> oval_items = new ArrayList<>();
    private List<CircleItem> circle_items = new ArrayList<>();
    private List<ImageItem> image_items = new ArrayList<>();

    private Runnable clickAction;           // 外嵌代码
    @Getter
    private int clickX = 0;
    @Getter
    private int clickY = 0;

    private String failedPath = null;

    public String getFailedPath() { return failedPath; }

    public SlidePanel(Runnable clickAction) {
        this.clickAction = clickAction;

        this.setLayout(null);               // 采用绝对布局 (以便自由拖动组件)
        this.setBackground(Color.WHITE);    // 设置为白板 (背景色为白色)

        // 监听器
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                // 点击背景板时，清除所有组件的选中状态
                SelectManager.getInstance().clearAll();
                // 如果有多级监听事件，记录鼠标点击的位置，并处理之
                clickX = e.getX();
                clickY = e.getY();
                clickAction.run();
            }
        });
    }

    /**
     * 将 Content 转换为可布局的 JComponent
     * @param content 解析后的一个 content 对象
     */
    public void addContent(AbstractContent content) {
        JComponent item = null;

        // 组件识别成功后，分别添加到管理容器
        switch (content.getContentType()) {
            case "text":
                item = new TextItem((TextContent) content, this::deleteTextItem);
                text_items.add((TextItem) item);
                break;
            case "line":
                item = new LineItem((LineContent) content, this::deleteLineItem);
                line_items.add((LineItem) item);
                break;
            case "rectangle":
                item = new RectangleItem((RectangleContent) content, this::deleteRectangleItem);
                rectangle_items.add((RectangleItem) item);
                break;
            case "oval":
                item = new OvalItem((OvalContent) content, this::deleteOvalItem);
                oval_items.add((OvalItem) item);
                break;
            case "circle":
                item = new CircleItem((CircleContent) content, this::deleteCircleItem);
                circle_items.add((CircleItem) item);
                break;
            case "image":
                item = new ImageItem((ImageContent) content, this::deleteImageItem);
                if (((ImageItem) item).getImage() == null) {      // 检查失败的外嵌图像，常见于移动 json 文件
                    failedPath = ((ImageContent) content).getSrc();
                } else {
                    image_items.add((ImageItem) item);
                }
                break;
            default:
                throw new IllegalArgumentException("Unsupported content type: " + content.getContentType());
        }
        // 布局到窗口
        this.add(item);
    }

    /**
     * 布局一张幻灯片的所有内容
     * @param contents 解析后的一张幻灯片中的内容列表，包含多个 content 对象
     */
    public void setContents(List<AbstractContent> contents) {
        this.failedPath = null;
        this.clear();
        for (AbstractContent content : contents) {
            addContent(content);
        }
        this.repaint();     // 请求重绘
    }

    /**
     * 清空面板上所有已添加的组件，包括管理容器
     * 仅保留面板的基本设置，如布局、背景颜色等，这些在 MainWindow 中就已设置完毕，无需更改
     */
    public void clear(){
        // 先移除窗口的组件
        this.removeAll();
        this.revalidate();

        // 再清空管理容器
        text_items.clear();
        line_items.clear();
        rectangle_items.clear();
        oval_items.clear();
        circle_items.clear();
        image_items.clear();

        // 重绘
        this.repaint();
    }

    /**
     * 生成新的 Content 集合
     * 用于保存终端作出的更改
     */
    public List<AbstractContent> getContents(){
        List<AbstractContent> contents = new ArrayList<>();
        for (TextItem item : text_items) {
            TextContent content = item.getTextContent();
            contents.add(content);
        }
        for (LineItem item : line_items) {
            LineContent content = item.getLineContent();
            // TODO g2d 绘制的图形如何在 Bounds 内确定位置？
            contents.add(content);
        }
        for (RectangleItem item : rectangle_items) {
            RectangleContent content = item.getRectangleContent();
            contents.add(content);
        }
        for (OvalItem item : oval_items) {
            OvalContent content = item.getOvalContent();
            contents.add(content);
        }
        for (CircleItem item : circle_items) {
            CircleContent content = item.getCircleContent();
            contents.add(content);
        }
        for (ImageItem item : image_items) {
            ImageContent content = item.getImageContent();
            contents.add(content);
        }
        return contents;
    }

    /**
     * 删除一个组件
     * 具体组件的右键菜单删除功能的外嵌代码
     */
    private void deleteTextItem(){ text_items.removeIf(item -> item.marked); }
    private void deleteLineItem(){ line_items.removeIf(item -> item.marked); }
    private void deleteRectangleItem(){ rectangle_items.removeIf(item -> item.marked); }
    private void deleteOvalItem(){ oval_items.removeIf(item -> item.marked); }
    private void deleteCircleItem(){ circle_items.removeIf(item -> item.marked); }
    private void deleteImageItem(){ image_items.removeIf(item -> item.marked); }
}