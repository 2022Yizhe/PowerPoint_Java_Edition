package org.powerpoint.window.dialog;

import org.powerpoint.window.AbstractWindow;

import javax.swing.*;
import java.awt.*;

/**
 * 输入对话框
 * 接收用户输入，文本通过 input 传递
 */
public class InputDialog extends AbstractDialog {
    String input;

    public InputDialog(AbstractDialog parent) { super(parent, "输入项目名称", new Dimension(400, 200)); }
    public InputDialog(AbstractWindow parent) { super(parent, "输入项目名称", new Dimension(400, 200)); }

    @Override
    protected void initDialogContent() {
        // 设计布局、组件
        this.setLayout(null);
        JTextField textField = new JTextField();
        textField.setEditable(true);
        textField.setBounds(0, this.getHeight() / 4, this.getWidth(), 30);

        // 添加 ActionListener 监听回车键事件
        textField.addActionListener(e -> {
            input = textField.getText(); InputDialog.this.closeDialog();
        });

        // 添加 JTextField 到面板
        this.add(textField, BorderLayout.CENTER);
    }

    /**
     * 获取对话框的用户输入结果
     */
    public String getInput() {
        return input;
    }
}
