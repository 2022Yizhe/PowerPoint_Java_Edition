import com.formdev.flatlaf.*;
import manage.ProjectManager;
import window.MainWindow;

import javax.swing.*;
import java.awt.*;

/**
 * 整个项目的主启动类，项目的运行就从这里开始
 */
public class Main {
    public static void main(String[] args) throws Exception {
        // 启用 DPI 缩放
        System.setProperty("sun.java2d.uiScale", "1.5");
        // 加载项目
        ProjectManager.loadDefaultProject();
        // 加载皮肤
        UIManager.setLookAndFeel(new FlatLightLaf());
        UIManager.put("Label.font", new Font("Serif", Font.PLAIN, 15));
        UIManager.put("Button.font", new Font("Serif", Font.PLAIN, 15));
        // 初始化窗口
        MainWindow minipptWindow = new MainWindow("PowerPoint Java Edition");
        minipptWindow.openWindow();
    }
}
