package org.powerpoint;

import com.formdev.flatlaf.*;
import org.powerpoint.window.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * 整个项目的主启动类，项目的运行就从这里开始
 * 注意事项：
 * 1. 如果加载不出图片，请自行更改 default.json 的图像路径。目前不支持图像资源内嵌到 .json 文件
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // 启用 DPI 缩放
        System.setProperty("sun.java2d.uiScale", "1.5");
        // 加载皮肤
        UIManager.setLookAndFeel(new FlatLightLaf());
        UIManager.put("Label.font", new Font("Serif", Font.PLAIN, 15));
        UIManager.put("Button.font", new Font("Serif", Font.PLAIN, 15));
        // 初始化窗口
        MainWindow minipptWindow = new MainWindow("PowerPoint Java Edition");
        minipptWindow.openWindow();
        System.out.println( UIManager.getColor("Panel.background"));    // - UI manager 获取的默认颜色为 swing 类型的 color
    }
}
