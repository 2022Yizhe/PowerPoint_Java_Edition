package org.powerpoint.window.component.button;

import org.powerpoint.window.enums.ColorName;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BottomLineButton extends JButton {
    public BottomLineButton(String text) {
        super(text);

        // UI - 无边框样式
        this.setBackground(ColorName.DEFAULT.getColor());
        this.setBorder(BorderFactory.createLineBorder(ColorName.DEFAULT.getColor()));

        // 设置默认边框
        BottomLineBorder defaultBorder = new BottomLineBorder(ColorName.DEFAULT.getColor(), 1);
        this.setBorder(defaultBorder);

        // 设置选中边框
        BottomLineBorder selectedBorder = new BottomLineBorder(ColorName.SKY_BLUE.getColor(), 1);

        // 添加鼠标监听器
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                setBorder(selectedBorder);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!hasFocus()) {
                    setBorder(defaultBorder);
                }
            }
        });
        // 添加焦点监听器
        this.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                setBorder(selectedBorder);
            }
            @Override
            public void focusLost(FocusEvent e) {
                setBorder(defaultBorder);
            }
        });
    }
}
