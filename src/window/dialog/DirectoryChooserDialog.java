package window.dialog;

import window.AbstractWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * 文件目录选择对话框
 */
public class DirectoryChooserDialog extends AbstractDialog {
    private JFileChooser fileChooser;

    public DirectoryChooserDialog(AbstractDialog parent) {
        super(parent, "请选择一个文件", new Dimension(720, 480));
    }
    public DirectoryChooserDialog(AbstractWindow parent) {
        super(parent, "请选择一个文件", new Dimension(720, 480));
    }

    @Override
    protected void initDialogContent() {
        // 设定对话框属性
        this.setLayout(new BorderLayout());
        this.setResizable(true);

        // 添加一个组件: JFileChooser
        fileChooser = new JFileChooser();
        // 创建文件过滤器，只允许选择 .json 文件
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files", "json");
        fileChooser.setFileFilter(filter);

        this.addComponent(fileChooser, chooser -> {
            // 设定选择器只能选择文件夹
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // 监听器，当选择完成后，就关闭当前窗口
            chooser.addActionListener(e -> DirectoryChooserDialog.this.closeDialog());
        });
    }

    public File getSelectedFile(){
        return fileChooser.getSelectedFile();
    }
}
