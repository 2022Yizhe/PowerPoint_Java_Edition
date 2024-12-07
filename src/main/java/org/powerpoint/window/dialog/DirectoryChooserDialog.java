package org.powerpoint.window.dialog;

import org.powerpoint.window.AbstractWindow;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * 目录选择对话框
 */
public class DirectoryChooserDialog extends AbstractDialog {
    private JFileChooser fileChooser;

    public DirectoryChooserDialog(AbstractDialog parent) {
        super(parent, "请选择一个目录", new Dimension(720, 480));
    }
    public DirectoryChooserDialog(AbstractWindow parent) {
        super(parent, "请选择一个目录", new Dimension(720, 480));
    }

    @Override
    protected void initDialogContent() {
        // 设定对话框属性
        this.setLayout(new BorderLayout());
        this.setResizable(true);

        // 添加一个组件: JFileChooser
        fileChooser = new JFileChooser();
        fileChooser.setApproveButtonText("保存");

        this.addComponent(fileChooser, chooser -> {
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);    // 只选择目录
            // 添加监听器，当选择完成后，就关闭当前窗口
            chooser.addActionListener(e -> DirectoryChooserDialog.this.closeDialog());
        });
    }

    /**
     * 获取对话框的文件选择结果
     */
    public File getSelectedFile(){
        return fileChooser.getSelectedFile();
    }
}