package org.powerpoint.window.component.item;

import org.powerpoint.entity.storage.ImageContent;
import org.powerpoint.manage.SelectManager;
import org.powerpoint.window.enums.ColorName;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * 图像组件
 * 继承自视觉组件 - VisualItem
 */
public class ImageItem extends VisualItem {
    private final ImageContent imageContent;

    private BufferedImage image;

    public BufferedImage getImage(){ return image; }

    public ImageItem(ImageContent imageContent, Runnable deleteAction) {
        super(deleteAction);

        this.imageContent = imageContent;
        configure();

        // 配置右键菜单
        configureMenu();
        this.add(popupMenu);

        // 监听器
        super.addMouseListeners();
        this.addMouseListener(new MouseAdapter() {
            /// 注：mouseClicked 包括了 mousePressed 和 mouseReleased 两个监听信号，这里只用到 mousePressed
            @Override
            public void mousePressed(MouseEvent e) {
                // 点击右下角时，开始缩放组件大小
                if(getWidth() - e.getX() <= 10 && getHeight() - e.getY() <= 10) {
                    System.out.println("[PowerPoint] (ImageItem) start resizing");
                    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    resizing = true;
                }
                // 记录鼠标相对组件的位置
                if (e.getButton() == MouseEvent.BUTTON1) {
                    mouseOffset = e.getPoint();
                    SelectManager.getInstance().selectItem(ImageItem.this);  // 通知选择管理
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
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            // 监听拖动事件
            @Override
            public void mouseDragged(MouseEvent e) {
                if (resizing) {     // 在调整大小时更新组件的宽高
                    int newWidth = e.getX();
                    int newHeight = e.getY();
                    if (newWidth != getWidth() || newHeight != getHeight()) {
                        setSize(Math.max(50, newWidth), Math.max(50, newHeight)); // 最小尺寸限制
                        saveChanges();
                    }
                }
            }
        });
    }

    /**
     * 返回当前 content，用于保存编辑
     */
    public ImageContent getImageContent() {
        return imageContent;
    }

    /**
     * 组件配置逻辑，设置图像属性
     */
    private void configure(){
        // 加载图像
        try {
            image = ImageIO.read(new File(imageContent.getSrc()));
            this.setBounds(imageContent.getX(), imageContent.getY(), image.getWidth(), image.getHeight());
        } catch (IOException e) {
            System.out.println("[PowerPoint] 图像加载失败!");
            System.out.println("[PowerPoint] ImgSrc: " + imageContent.getSrc());
//            e.printStackTrace();
            image = null;  // 加载图像失败时，设置为 null
            marked = true; // 并且标记删除
            deleteAction.run();
            this.setVisible(false); // 因为已经绘制的不会自动消失，直接设为不可见，再次加载幻灯片时已删除
        }
        this.setOpaque(false);  // 透明背景
    }

    /**
     * 组件配置逻辑，设置右键弹出菜单
     */
    private void configureMenu(){
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
     */
    protected void saveChanges() {
        imageContent.setX(this.getX());
        imageContent.setY(this.getY());
        imageContent.setWidth(this.getWidth());
        imageContent.setHeight(this.getHeight());
    }

    /**
     * 组件绘制逻辑
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 绘制图像到此组件的 (0, 0) 位置 - Bounds(imageContent.X(), imageContent.Y())
        if (image != null) {
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
        }

        // 如果选中，绘制边框
        if (isSelected) {
            g.setColor(ColorName.LIGHT_GRAY.getColor());
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
        }
    }

    /**
     * 获取图像大小，加载失败时初始化为 Size = 100*100
     * @return Dimension 2D
     */
    @Override
    public Dimension getPreferredSize() {
        return image != null ? new Dimension(image.getWidth(), image.getHeight()) : new Dimension(100, 100);
    }
}