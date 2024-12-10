package org.powerpoint.window.dialog;

import org.powerpoint.window.AbstractWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * 保存修改对话框
 * 接收用户输入，参数通过两个按钮传递
 */
public class SaveChangeDialog extends AbstractDialog {
    boolean ifCancel = false;
    boolean ifSaveChange = false;

    public SaveChangeDialog(AbstractDialog parent) { super(parent, "未保存的项目", new Dimension(400, 200)); }
    public SaveChangeDialog(AbstractWindow parent) { super(parent, "未保存的项目", new Dimension(400, 200)); }

    @Override
    protected void initDialogContent() {
        // 设计布局、组件
        this.setLayout(null);

        // 提示标签
        JLabel hint = new JLabel("是否保存修改？");
        hint.setBounds(0, this.getHeight() / 5, this.getWidth(), 30);
        hint.setHorizontalAlignment(SwingConstants.CENTER);

        // 创建按钮，设置位置
        JButton saveButton = new JButton("是");
        JButton discardButton = new JButton("否");
        JButton cancelButton = new JButton("取消");
        saveButton.setBounds(this.getWidth() / 4 - 50, this.getHeight() / 2, 75, 30);
        discardButton.setBounds(this.getWidth() * 2 / 4 - 50, this.getHeight() / 2, 75, 30);
        cancelButton.setBounds(this.getWidth() * 3 / 4 - 50, this.getHeight() / 2, 75, 30);

        // 添加按钮的事件监听器
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifSaveChange = true;
                dispose(); // 关闭对话框
            }
        });

        discardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifSaveChange = false;
                dispose(); // 关闭对话框
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ifCancel = true;
                dispose();
            }
        });

        // 添加到面板
        this.add(hint);
        this.add(saveButton);
        this.add(discardButton);
        this.add(cancelButton);
    }

    /** 获取用户选择结果
     * @return 是否保存修改
     */
    public boolean isSaveChange() {
        return ifSaveChange;
    }

    /**
     * 获取用户选择结果
     * @return 是否取消退出
     */
    public boolean isCancel() {
        return ifCancel;
    }
}