package window.dialog;

import window.AbstractWindow;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;

/**
 * 文件目录选择对话框
 */
public class FileChooserDialog extends AbstractDialog {
    private JFileChooser fileChooser;

    public FileChooserDialog(AbstractDialog parent) {
        super(parent, "请选择一个文件", new Dimension(720, 480));
    }
    public FileChooserDialog(AbstractWindow parent) {
        super(parent, "请选择一个文件", new Dimension(720, 480));
    }

    @Override
    protected void initDialogContent() {
        // 设定对话框属性
        this.setLayout(new BorderLayout());
        this.setResizable(true);

        // 创建文件过滤器，只允许选择 .json 文件
        fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("JSON Files", "json");
        fileChooser.setFileFilter(filter);

        this.addComponent(fileChooser, chooser -> {
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            // 添加监听器，当选择完成后，就关闭当前窗口
            chooser.addActionListener(e -> FileChooserDialog.this.closeDialog());
        });
    }

    /**
     * 获取对话框的文件选择结果
     */
    public File getSelectedFile(){
        return fileChooser.getSelectedFile();
    }
}
