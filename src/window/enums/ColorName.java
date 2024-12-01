package window.enums;

import javax.swing.*;
import java.awt.*;

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

    // 根据颜色名称获取 Color 对象
    public static Color getColor(String colorName) {
        try {
            // 使用枚举查找对应的颜色
            return ColorName.valueOf(colorName.toUpperCase()).getColor();
        } catch (IllegalArgumentException e) {
            return Color.BLACK; // 默认返回黑色
        }
    }
}