package org.powerpoint.window.dialog;

import org.powerpoint.window.AbstractWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * 图像文件选择对话框
 */
public class ImageChooserDialog extends AbstractDialog {
    private JFileChooser fileChooser;

    public ImageChooserDialog(AbstractDialog parent) {
        super(parent, "请选择一张图片", new Dimension(720, 480));
    }
    public ImageChooserDialog(AbstractWindow parent) { super(parent, "请选择一张图片", new Dimension(720, 480)); }

    @Override
    protected void initDialogContent() {
        // 设定对话框属性
        this.setLayout(new BorderLayout());
        this.setResizable(true);

        // 创建文件过滤器，只允许选择 .jpg/.jpeg/.png 文件
        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files", "jpg", "jpeg", "png");
        fileChooser.setFileFilter(filter);

        this.addComponent(fileChooser, chooser -> {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // 添加监听器，当选择完成后，就关闭当前窗口
            chooser.addActionListener(e -> ImageChooserDialog.this.closeDialog());
        });
    }

    /**
     * 获取对话框的文件选择结果
     */
    public File getSelectedFile(){
        return fileChooser.getSelectedFile();
    }
}
