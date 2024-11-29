package window.dialog;

import window.AbstractWindow;

import javax.swing.*;
import java.awt.*;

/**
 * 提示对话框（编译失败、警告等），文本通过 text 参数传递
 */
public class ExampleDialog extends AbstractDialog {
    public ExampleDialog(AbstractWindow parent, String text) {
        super(parent, "窗口名称", new Dimension(600, 300));

        // 设计布局、组件
        this.setLayout(new BorderLayout());
        JTextArea area = new JTextArea(text);
        this.addComponent(new JScrollPane(area), pane -> area.setEditable(false));
    }

    @Override
    protected void initDialogContent() {}
}
