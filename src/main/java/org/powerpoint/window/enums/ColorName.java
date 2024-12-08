package org.powerpoint.window.enums;

import javax.swing.*;
import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 定义颜色枚举类
public enum ColorName {
    RED(Color.RED),
    GREEN(Color.GREEN),
    BLUE(Color.BLUE),
    BLACK(Color.BLACK),
    WHITE(Color.WHITE),
    YELLOW(Color.YELLOW),
    CYAN(Color.CYAN),
    MAGENTA(Color.MAGENTA),
    GRAY(Color.GRAY),
    DARK_GRAY(Color.DARK_GRAY),
    ORANGE(Color.ORANGE),
    LIGHT_GRAY(Color.LIGHT_GRAY),
    // 自定义颜色
    DEFAULT(UIManager.getColor("Panel.background")),
    SKY_BLUE(new Color(135, 206, 235));


    private final Color color;

    ColorName(Color color) {
        this.color = color;
    }

    // 获取对应的 Color 对象
    public Color getColor() {
        return color;
    }

    // 根据颜色字符串获取 Color 对象
    public static Color getColor(String colorName) {
        try {
            if(!colorName.startsWith("java.awt.Color")) {
                // 根据颜色别名查找枚举值
                return ColorName.valueOf(colorName.toUpperCase()).getColor();
            } else {
                // 使用正则表达式提取 RGB 值
                Pattern pattern = Pattern.compile("r=(\\d+),g=(\\d+),b=(\\d+)");
                Matcher matcher = pattern.matcher(colorName);

                if (matcher.find()) {
                    // 提取 r, g, b 的字符串
                    String rStr = matcher.group(1);
                    String gStr = matcher.group(2);
                    String bStr = matcher.group(3);

                    // 使用 Integer.parseInt() 将提取的字符串转换为整数
                    int r = Integer.parseInt(rStr);
                    int g = Integer.parseInt(gStr);
                    int b = Integer.parseInt(bStr);

                    return new Color(r, g, b);  // 创建 Color 对象
                } else {
                    throw new IllegalArgumentException("无效的颜色字符串");
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[org.powerpoint][Warning] Unsupported color: " + colorName);
            return Color.BLACK;     // 默认返回黑色
        }
    }

    // 根据 Color 对象返回颜色字符串
    public static String getColorName(Color color) {
        return color.toString();
    }
}