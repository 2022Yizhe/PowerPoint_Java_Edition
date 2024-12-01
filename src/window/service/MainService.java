package window.service;

import entity.storage.*;
import manage.DisplayEngine;
import manage.ProjectManager;

import window.MainWindow;
import window.component.NoDotsSplitPane;
import window.dialog.DirectoryChooserDialog;
import window.enums.ColorName;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.List;

public class MainService extends AbstractService {
    private int index;                  // 当前编辑幻灯片的索引，随用户操作更新
    private String path;                // 当前项目的路径和项目名称

    private UndoManager undoManager;    // 重做管理器，用于编辑框支持撤销和重做操作

    /**
     * 设定当前项目的名称和路径
     * @param json_path 路径
     */
    public void setPath(String json_path){
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
     * @ 配置右键菜单服务
     * 创建一个幻灯片
     */
    public void createSlide(){
        // TODO
    }

    /**
     * @ 配置右键菜单服务
     * 复制一个幻灯片，直接粘贴在指定幻灯片下一张
     */
    public void copySlide(){
        // TODO
    }

    /**
     * @ 配置右键菜单服务
     * 删除一个幻灯片
     */
    public void deleteSlide(){
        // TODO
    }

    /**
     * @ 配置右键菜单服务
     * @return MouseAdapter
     */
    public MouseAdapter rightClick(){
        JPanel previewPanel = this.getComponent("main.panel.preview");
        JPopupMenu popupMenu = this.getComponent("main.popup.slide");
        return new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3)
                    popupMenu.show(previewPanel, e.getX(), e.getY());
            }
        };
    }

    /**
     * 配置编辑框的各项功能
     */
    public void setupEditArea(){
        // TODO - 这只是文本组件的重做管理，可以同理修改为幻灯片编辑的重做管理
//        JTextArea editArea = this.getComponent("main.textarea.edit");
//        // 当文本内容发生变化时，自动写入到文件中
//        editArea.getDocument().addDocumentListener(new DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                MainService.this.saveFile();
//            }
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                MainService.this.saveFile();
//            }
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                MainService.this.saveFile();
//            }
//        });
//        // 按下 Tab 键时，应该输入四个空格，而不是一个 Tab 缩进（不然太丑）
//        editArea.addKeyListener(new KeyAdapter() {
//            @Override
//            public void keyPressed(KeyEvent e) {
//                if(e.getKeyCode() == 9) {
//                    e.consume();
//                    editArea.insert("    ", editArea.getCaretPosition());
//                }
//            }
//        });
//        // 由于默认的文本区域不支持重做和撤销操作，需要使用 UndoManager 进行配置，这里添加快捷键
//        editArea.getInputMap().put(KeyStroke.getKeyStroke("control Y"), "Redo");
//        editArea.getActionMap().put("Redo", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(undoManager.canRedo()) undoManager.redo();
//            }
//        });
//        editArea.getInputMap().put(KeyStroke.getKeyStroke("control Z"), "Undo");
//        editArea.getActionMap().put("Undo", new AbstractAction() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if(undoManager.canUndo()) undoManager.undo();
//            }
//        });
    }

    public void displayProject(){
        // 渲染预览面板
        previewSlides();

        // 渲染编辑面板
        displaySlide(index);
    }

    /**
     * 渲染预览面板的幻灯片序列，代号 - main.panel.preview
     */
    private void previewSlides() {
        // 获取幻灯片序列
        Presentation presentation = ProjectManager.getProcess().getPresentation();
        List<Slide> slides = presentation.Slides();

        // 渲染 Slide
        // TODO 预览面板的渲染位置可能需要压缩
        JPanel previewPanel = this.getComponent("main.panel.preview");
//        for (Slide slide : slides) {
//            // 渲染 Content
//            for (AbstractContent ele : slide.Contents()) {
//                switch (ele.getType()) {
//                    case "text":
//                        drawTextContent((TextContent) ele, previewPanel);
//                        break;
//                    case "line":
//                        drawLineContent((LineContent) ele, previewPanel);
//                        break;
//                    case "rectangle":
//                        drawRectangleContent((RectangleContent) ele, previewPanel);
//                        break;
//                    case "oval":
//                        drawOvalContent((OvalContent) ele, previewPanel);
//                        break;
//                    case "circle":
//                        drawCircleContent((CircleContent) ele, previewPanel);
//                        break;
//                    case "image":
//                        drawImageContent((ImageContent) ele, previewPanel);
//                        break;
//                    default:
//                        // 未识别的 content
//                        break;
//                }
//            }
//        }
    }

    /**
     * 渲染显示和编辑面板的幻灯片，代号 - main.panel.edit
     * @param index 幻灯片序列中的索引
     */
    private void displaySlide(int index){
        // 获取指定位置的幻灯片
        Presentation presentation = ProjectManager.getProcess().getPresentation();
        List<Slide> slides = presentation.Slides();
        Slide slide = slides.get(index);

        // 渲染 Content
        JPanel editPanel = this.getComponent("main.panel.edit");
        for (AbstractContent ele : slide.Contents()) {
            switch (ele.ContentType()) {
                case "text":
                    drawTextContent((TextContent) ele, editPanel);
                    break;
                case "line":
                    drawLineContent((LineContent) ele, editPanel);
                    break;
                case "rectangle":
                    drawRectangleContent((RectangleContent) ele, editPanel);
                    break;
                case "oval":
                    drawOvalContent((OvalContent) ele, editPanel);
                    break;
                case "circle":
                    drawCircleContent((CircleContent) ele, editPanel);
                    break;
                case "image":
                    drawImageContent((ImageContent) ele, editPanel);
                    break;
                default:
                    // 未识别的 content
                    break;
            }
        }
    }

    /**
     * 渲染文本
     * @param textContent Content 的具体子类
     * @param targetPanel 指定渲染的面板
     * TextContent: String value, int x, int y, String color
     */
    private void drawTextContent(TextContent textContent, JPanel targetPanel) {
        JTextArea value = new JTextArea(textContent.getValue());            // 设置文本
//        value.setLocation(textContent.X(), textContent.Y());              // 设置位置
        value.setBounds(textContent.X(), textContent.Y(), 200, 40);
        value.setForeground(ColorName.getColor(textContent.getColor()));    // 设置颜色

        value.setEditable(true);
        value.setLineWrap(true);        // 启用行换行
        value.setWrapStyleWord(true);   // 只在单词边界换行

        targetPanel.add(value);
    }

    /**
     * 渲染直线
     * @param lineContent Content 的具体子类
     * @param targetPanel 指定渲染的面板
     * LineContent: int startX, int startY, int endX, int endY, String color
     */
    private void drawLineContent(LineContent lineContent, JPanel targetPanel) {}

    /**
     * 渲染矩形
     * @param rectangleContent Content 的具体子类
     * @param targetPanel 指定渲染的面板
     */
    private void drawRectangleContent(RectangleContent rectangleContent, JPanel targetPanel) {}

    /**
     * 渲染椭圆
     * @param ovalContent Content 的具体子类
     * @param targetPanel 指定渲染的面板
     */
    private void drawOvalContent(OvalContent ovalContent, JPanel targetPanel) {}

    /**
     * 渲染圆形
     * @param circleContent Content 的具体子类
     * @param targetPanel 指定渲染的面板
     */
    private void drawCircleContent(CircleContent circleContent, JPanel targetPanel) {}

    /**
     * 渲染图像
     * @param imageContent Content 的具体子类
     * @param targetPanel 指定渲染的面板
     */
    private void drawImageContent(ImageContent imageContent, JPanel targetPanel) {}






    /**
     * 切换展示和编辑的幻灯片，同时保存编辑的内容
     * @param path 文件路径
     */
    public void switchEditSlide(String path) {
        // TODO
    }

    /**
     * 右键菜单 - 删除幻灯片
     * @param index 右键的幻灯片索引
     */
    private void deleteSlide(int index){
        // TODO
    }

    /**
     * 重做管理
     * 保存编辑到文件
     */
    private void saveFile(){
        // TODO
    }
}
