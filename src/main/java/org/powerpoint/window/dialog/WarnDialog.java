package org.powerpoint.window.dialog;

import org.powerpoint.window.AbstractWindow;

import javax.swing.*;
import java.awt.*;

/**
 * 图像检查对话框
 */
public class WarnDialog extends AbstractDialog {
    String path;

    public WarnDialog(AbstractDialog parent, String path) {
        super(parent, "警告 - 失败的外嵌图像", new Dimension(400, 200));
        this.path = path;
    }
    public WarnDialog(AbstractWindow parent, String path) {
        super(parent, "警告 - 失败的外嵌图像", new Dimension(400, 200));
        this.path = path;
        showInfo();
    }

    @Override
    protected void initDialogContent() {
        // 设计布局、组件
        this.setLayout(null);

        // 提示文本
        JLabel hint1 = new JLabel("图片已丢失或损坏!");
        hint1.setBounds(50, this.getHeight() / 5, this.getWidth() - 100, 30);
        hint1.setHorizontalAlignment(SwingConstants.CENTER);
        hint1.setEnabled(false);

        // 添加到面板
        this.add(hint1);
    }

    /**
     * 显示具体的错误信息
     */
    private void showInfo() {
        JLabel hint2 = new JLabel(path);
        hint2.setBounds(10, this.getHeight() / 3, this.getWidth() - 20, 30);
        hint2.setHorizontalAlignment(SwingConstants.CENTER);
        hint2.setEnabled(false);
        this.add(hint2);
    }
}