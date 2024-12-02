package window.component.item;

import entity.storage.ImageContent;
import manage.SelectManager;
import window.enums.ColorName;

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

    public ImageItem(ImageContent imageContent) {
        super();

        this.imageContent = imageContent;
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
                SelectManager.getInstance().selectItem(ImageItem.this);  // 通知选择管理
                repaint();  // 鼠标按下时立即请求重绘，以显示边框
            }
        });
    }

    /**
     * 组件配置逻辑，设置图像属性
     */
    private void configure(){
        // 加载图像
        try {
            this.image = ImageIO.read(new File(imageContent.Src()));
            this.setBounds(imageContent.X(), imageContent.Y(), image.getWidth(), image.getHeight());
        } catch (IOException e) {
            e.printStackTrace();
            this.image = null;  // 加载失败时设置为 null
        }

        this.setOpaque(false);  // 透明背景
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