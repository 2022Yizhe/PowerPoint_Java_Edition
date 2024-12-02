package window.component.item;

import entity.storage.ImageContent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageItem extends JComponent {
    private final ImageContent imageContent;
    private final JPopupMenu popupMenu;

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
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) { // 右键显示菜单
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
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

        if (image != null) {
            // 绘制图像到此组件的 (0, 0) 位置 - Bounds(imageContent.X(), imageContent.Y())
            g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), this);
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