package window.service;

import manage.DisplayEngine;
import manage.ProjectManager;

import window.MainWindow;
import window.dialog.DirectoryChooserDialog;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;
import java.awt.event.*;
import java.io.*;

public class MainService extends AbstractService {
    private String path;            // 当前项目的路径和项目名称
    private File currentFile;       // 用于记录当前正在编辑的文件
    private UndoManager undoManager;            // 重做管理器，用于编辑框支持撤销和重做操作的

    /**
     * 设定当前项目的名称和路径
     * @param path 路径
     */
    public void setPath(String path){
        this.path = path.replace("\\", "/");
    }

    /**
     * @ Control
     * 新建、打开、保存项目
     */
    public void fileButtonAction(){
        // 刷新工具栏
        MainWindow window = (MainWindow) this.getWindow();
        JSplitPane panel = getComponent("main.panel.control");
        panel.setBottomComponent(window.createFileToolsPanel(panel));
    }

    /**
     * @ Control
     * 新建文本框
     */
    public void textButtonAction(){
        // 刷新工具栏
        MainWindow window = (MainWindow) this.getWindow();
        JSplitPane panel = getComponent("main.panel.control");
        panel.setBottomComponent(window.createTextToolsPanel(panel));
    }

    /**
     * @ Control
     * 绘制基本图形
     */
    public void paintButtonAction(){
        MainWindow window = (MainWindow) this.getWindow();
        JSplitPane panel = getComponent("main.panel.control");
        panel.setBottomComponent(window.createPaintToolsPanel(panel));
    }

    /**
     * @ Control
     * 插入资源
     */
    public void insertButtonAction(){
        MainWindow window = (MainWindow) this.getWindow();
        JSplitPane panel = getComponent("main.panel.control");
        panel.setBottomComponent(window.createInsertToolsPanel(panel));
    }

    /**
     * @ Tools
     * 打开一个已有的 miniPpt 项目
     */
    public void openFileButtonAction(){
        // 打开文件选择器，选择一个文件 (.json)
        DirectoryChooserDialog chooser_dialog = new DirectoryChooserDialog(this.getWindow());
        chooser_dialog.openDialog();
        File selectedFile = chooser_dialog.getSelectedFile();
        if(selectedFile == null) return;

        // 配置项目，并刷新显示面板
        try {
            ProjectManager.loadProject(selectedFile.getName(), selectedFile.getParentFile().getAbsolutePath()); // 获取幻灯片文件名和目录 (用于配置项目实体)
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.setPath(selectedFile.getAbsolutePath());   // 保存幻灯片文件 (.json) 路径

        MainWindow window = (MainWindow) this.getWindow();
//        window.refreshSlideQueue();
    }

    /**
     * @ Tools
     * 新建一个已有的 miniPpt 项目
     */
    public void newFileButtonAction(){

    }

    /**
     * @ Tools
     * 保存一个已有的 miniPpt 项目
     */
    public void saveFileButtonAction() {

    }

    /**
     * @ Tools
     * 添加横排文本框
     */
    public void textAreaButtonAction(){

    }

    /**
     * @ Tools
     * 绘制直线
     */
    public void lineShapeButtonAction(){

    }

    /**
     * @ Tools
     * 绘制矩形
     */
    public void rectangleShapeButtonAction(){

    }

    /**
     * @ Tools
     * 绘制椭圆形（包含圆形）
     */
    public void ovalShapeButtonAction(){

    }

    /**
     * @ Tools
     * 插入一张图片
     */
    public void imageInsertButtonAction(){

    }


    /**
     * 配置文件树的右键弹出窗口
     * @return MouseAdapter
     */
    public MouseAdapter fileTreeRightClick(){
        JTree fileTree = this.getComponent("main.tree.files");
        JPopupMenu treePopupMenu = this.getComponent("main.popup.tree");
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON3)
                treePopupMenu.show(fileTree, e.getX(), e.getY());
            }
        };
    }

    /**
     * 配置右键菜单服务
     * 创建一个新的文件并生成初始信息
     */
    public void createNewFile(){
        // TODO
    }

    public void deleteProjectFile(){
        // TODO
    }

    /**
     * 配置编辑框的各项功能
     */
    public void setupEditArea(){
        // TODO - 这是文本组件的重做管理，可以类似扩展到幻灯片编辑
        JTextArea editArea = this.getComponent("main.textarea.edit");
        // 当文本内容发生变化时，自动写入到文件中
        editArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                MainService.this.saveFile();
            }
            @Override
            public void removeUpdate(DocumentEvent e) {
                MainService.this.saveFile();
            }
            @Override
            public void changedUpdate(DocumentEvent e) {
                MainService.this.saveFile();
            }
        });
        // 按下 Tab 键时，应该输入四个空格，而不是一个 Tab 缩进（不然太丑）
        editArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == 9) {
                    e.consume();
                    editArea.insert("    ", editArea.getCaretPosition());
                }
            }
        });
        // 由于默认的文本区域不支持重做和撤销操作，需要使用 UndoManager 进行配置，这里添加快捷键
        editArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
        editArea.getActionMap().put("Redo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(undoManager.canRedo()) undoManager.redo();
            }
        });
        editArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
        editArea.getActionMap().put("Undo", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(undoManager.canUndo()) undoManager.undo();
            }
        });
    }

    /**
     * 切换当前展示和编辑的幻灯片，并更新编辑面板中的内容
     * @param path 文件路径
     */
    public void switchEditSlide(String path) {
        // TODO
    }

    private void deleteFile(String name){
        // TODO
    }

    /**
     * 创建源文件，并生成默认代码
     * @param name 名称
     */
    private void createFile(String name){
        MainWindow window = (MainWindow) this.getWindow();
        if(name == null) return;
        String[] split = name.split("\\.");
        String className = split[split.length - 1];
        String packageName = name.substring(0, name.length() - className.length() - 1);

        try {
            File dir = new File(path+"/src/"+packageName.replace(".", "/"));
            if(!dir.exists() && !dir.mkdirs()) {
                JOptionPane.showMessageDialog(window, "无法创建文件夹！");
                return;
            }
            File file = new File(path+"/src/"+packageName.replace(".", "/")+"/"+className+".java");
            if(file.exists() || !file.createNewFile()) {
                JOptionPane.showMessageDialog(window, "无法创建，此文件已存在！");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存当前编辑幻灯片中的内容到当前文件中
     */
    private void saveFile(){
        // TODO
    }
}
