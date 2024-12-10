package org.powerpoint.window.service;

import org.powerpoint.entity.storage.*;
import org.powerpoint.manage.ProjectManager;
import org.powerpoint.manage.ProcessEngine;
import org.powerpoint.window.MainWindow;
import org.powerpoint.window.component.SlidePanel;
import org.powerpoint.window.component.item.ListItem;
import org.powerpoint.window.dialog.*;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 负责配置和管理 MainWindow 的用户交互服务
 */
public class MainService extends AbstractService {
    private String name;                // 当前项目的名称
    private String directory;           // 当前项目的目录
    private String path;                // 当前项目文件的路径 - 暂时不用

    private String imagePath;           // 选择图像的路径

    private int iterator = 0;           // 当前编辑幻灯片的索引，随用户操作更新

    private boolean toolFlag = false;   // 处理多级监听信号的变量，如点击 tools 后再点击编辑面板，执行操作
    private boolean editFlag = false;
    private String tool;

    private boolean saveFlag = true;    // 标志变量，检查内容是否已经保存 -- TODO 配合重做管理实现更细致的保存检查

    private UndoManager undoManager;    // 重做管理器，用于编辑框支持撤销和重做操作

    /**
     * 设定当前项目的名称和路径
     * @param json_path 路径
     */
    public void setPath(String json_path){ this.path = json_path.replace("\\", "/"); }
    public boolean ifSave(){ return saveFlag; }

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
        JsonChooserDialog chooser_dialog = new JsonChooserDialog(this.getWindow());
        chooser_dialog.openDialog();
        File selectedFile = chooser_dialog.getSelectedFile();
        if(selectedFile == null) return;

        // 获取幻灯片文件名和目录，解析指定项目
        try {
            name = selectedFile.getName();
            directory = selectedFile.getParentFile().getAbsolutePath();
            path = selectedFile.getAbsolutePath();
            ProjectManager.getInstance().loadProject(name, directory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 渲染指定项目
        this.iterator = 0;
        this.clear();
        this.displayProject();

        // 更新状态栏
        JLabel status_label = this.getComponent("main.label.status");
        status_label.setText("  Load project: " + directory + File.separator + name);
        saveFlag = true;
    }

    /**
     * @ Tools
     * 新建一个已有的 miniPpt 项目
     */
    public void newFileButtonAction(){
        // 打开对话框，输入项目文件名
        InputDialog inputDialog = new InputDialog(this.getWindow());
        inputDialog.openDialog();
        String inputString = inputDialog.getInput();
        if(inputString == null) return;

        // 输入合法性检查 (简略)
        if (inputString.isEmpty()){
            name = "untitled.json";
        } else {
            if(!(inputString.startsWith(".json", inputString.length() - 6)) || inputString.length() < 6){
                name = inputString + ".json";
            }
        }

        // 解析新建项目
        ProjectManager.getInstance().createProject(name);

        // 渲染新建项目
        this.iterator = 0;
        this.clear();
        this.displayProject();

        // 更新状态栏
        JLabel status_label = this.getComponent("main.label.status");
        status_label.setText("  New project: " + name + " - Unsaved");
        saveFlag = false;
    }

    /**
     * @ Tools
     * 保存一个已有的 miniPpt 项目
     */
    public void saveFileButtonAction() {
        // 打开文件选择器，选择一个保存位置
        DirectoryChooserDialog chooser_dialog = new DirectoryChooserDialog(this.getWindow());
        chooser_dialog.openDialog();
        File selectedFile = chooser_dialog.getSelectedFile();
        if(selectedFile == null) return;

        // 获取新路径
        name = ProjectManager.getInstance().getProject().getName(); // 文件名与原文件一样
        directory = selectedFile.getAbsolutePath();                 // 新路径
        path = selectedFile.getAbsolutePath();                      // 新目录 - 两者一样是因为选择的是文件夹

        // 保存项目到新路径
        try {
            ProjectManager.getInstance().saveProject(name, directory);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 更新状态栏
        JLabel status_label = this.getComponent("main.label.status");
        status_label.setText("  Load project: " + directory + File.separator + name);
        saveFlag = true;
    }

    /**
     * @ Tools
     * 添加横排文本框 (注：此类添加组件的方式并没有立即保存到 slides，只是单独保存到面板组件)
     */
    public void textAreaButtonAction(){
        // 新建文本框，文本框内容为默认文本，文本框位置为鼠标点击位置
        SlidePanel slidePanel = this.getComponent("main.panel.edit");
        TextContent text = new TextContent();
        text.initDefault(slidePanel.getClickX(), slidePanel.getClickY());

        // 添加到面板组件，并单独渲染之
        slidePanel.addContent(text);
        slidePanel.repaint();
    }

    /**
     * @ Tools
     * 绘制直线
     */
    public void lineShapeButtonAction(){
        // 新建直线，直线固定为横线，直线位置为鼠标点击位置
        SlidePanel slidePanel = this.getComponent("main.panel.edit");
        LineContent line = new LineContent();
        line.initDefault(slidePanel.getClickX(), slidePanel.getClickY());

        // 添加到面板组件，并单独渲染之
        slidePanel.addContent(line);
        slidePanel.repaint();
    }

    /**
     * @ Tools
     * 绘制矩形
     */
    public void rectangleShapeButtonAction(){
        // 新建矩形，矩形具有默认大小，矩形位置为鼠标点击位置
        SlidePanel slidePanel = this.getComponent("main.panel.edit");
        RectangleContent rectangle = new RectangleContent();
        rectangle.initDefault(slidePanel.getClickX(), slidePanel.getClickY());

        // 添加到面板组件，并单独渲染之
        slidePanel.addContent(rectangle);
        slidePanel.repaint();
    }

    /**
     * @ Tools
     * 绘制椭圆形
     */
    public void ovalShapeButtonAction(){
        // 新建椭圆形，椭圆形具有默认大小，位置为鼠标点击位置
        SlidePanel slidePanel = this.getComponent("main.panel.edit");
        OvalContent oval = new OvalContent();
        oval.initDefault(slidePanel.getClickX(), slidePanel.getClickY());

        // 添加到面板组件，并单独渲染之
        slidePanel.addContent(oval);
        slidePanel.repaint();
    }

    /**
     * @ Tools
     * 绘制圆形
     */
    public void circleShapeButtonAction(){
        // 新建圆形，圆形具有默认大小，位置为鼠标点击位置
        SlidePanel slidePanel = this.getComponent("main.panel.edit");
        CircleContent circle = new CircleContent();
        circle.initDefault(slidePanel.getClickX(), slidePanel.getClickY());

        // 添加到面板组件，并单独渲染之
        slidePanel.addContent(circle);
        slidePanel.repaint();
    }

    /**
     * @ Tools
     * 选择一张图片
     */
    public void chooseImage(){
        // 打开文件选择器，选择一张图像文件
        ImageChooserDialog chooser_dialog = new ImageChooserDialog(this.getWindow());
        chooser_dialog.openDialog();
        File selectedFile = chooser_dialog.getSelectedFile();
        if(selectedFile == null) return;

        // 获取图像路径
        imagePath = selectedFile.getAbsolutePath();
    }

    /**
     * @ Tools
     * 插入一张图片
     */
    public void imageInsertButtonAction(){
        // 插入一张图片，位置为鼠标点击位置
        SlidePanel slidePanel = this.getComponent("main.panel.edit");
        ImageContent image = new ImageContent();
        image.initDefault(imagePath, slidePanel.getClickX(), slidePanel.getClickY());

        // 添加到面板组件，并单独渲染之
        slidePanel.addContent(image);
        slidePanel.repaint();
    }

    /**
     * 处理多级监听事件
     * @param event 发出监听信号的组件
     */
    public void handleEvent(String event){
        // 简易图灵机 - 当两个事件连续发生时才均为 true
        if (event.startsWith("button")){
            toolFlag = true;
            tool = event;
        } else if (event.startsWith("panel")){
            editFlag = toolFlag;
        }

        // 进行事件识别，处理任务
        if (toolFlag && editFlag){
            switch (tool){
                case "button_textArea":
                    textAreaButtonAction();
                    break;
                case "button_lineShape":
                    lineShapeButtonAction();
                    break;
                case "button_rectangleShape":
                    rectangleShapeButtonAction();
                    break;
                case "button_ovalShape":
                    ovalShapeButtonAction();
                    break;
                case "button_circleShape":
                    circleShapeButtonAction();
                    break;
                case "button_imageInsert":
                    imageInsertButtonAction();
                    break;
                default:
                    System.out.println("Unhandled event: " + event);
                    break;
            }
            // 处理结束，重置状态
            toolFlag = false;
            editFlag = false;
            saveFlag = false;
        }
    }

    /**
     * @ 配置右键菜单服务
     * 为幻灯片重命名，修改的变量为 Slide.title
     */
    public void renameSlide(){
        // 保存最后一次的修改
        saveEdit();

        // 获取幻灯片序列
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.getSlides();

        // 弹出对话框，修改这张幻灯片的 title
        InputDialog inputDialog = new InputDialog(this.getWindow());
        inputDialog.openDialog();
        String inputStr = inputDialog.getInput();
        if(inputStr == null) return;

        Slide slide = slides.get(iterator);
        slide.setTitle(inputStr);

        // 重新渲染
        clear();
        previewSlides();
        displaySlide(iterator);
        saveFlag = false;
    }

    /**
     * @ 配置右键菜单服务
     * 创建一张幻灯片，插入到右键幻灯片的下一张
     */
    public void createSlide(){
        // 保存最后一次的修改
        saveEdit();

        // 获取幻灯片序列
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.getSlides();

        // 添加一张新 slide
        Slide slide = new Slide();
        slide.initDefault();
        slides.add(++iterator, slide);

        // 重新渲染
        clear();
        previewSlides();
        displaySlide(iterator);
        saveFlag = false;
    }

    /**
     * @ 配置右键菜单服务
     * 复制一张幻灯片，插入到指定幻灯片下一张
     */
    public void copySlide(){
        // 保存最后一次的修改
        saveEdit();

        // 获取幻灯片序列
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.getSlides();

        // 复制这张 slide
        Slide slide = new Slide();
        slide.copySlide(slides.get(iterator));
        slides.add(++iterator, slide);

        // 重新渲染
        clear();
        previewSlides();
        displaySlide(iterator);
        saveFlag = false;
    }

    /**
     * @ 配置右键菜单服务
     * 删除一张幻灯片，删除后 iterator 前移一位
     */
    public void deleteSlide(){
        // 获取幻灯片序列
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.getSlides();

        // 删除这张 slide
        slides.remove(iterator);

        // 重新渲染
        clear();
        previewSlides();
        if (iterator != 0)
            iterator = iterator - 1;
        displaySlide(iterator);
        saveFlag = false;
    }

    /**
     * 配置编辑面板的撤销和重做功能
     */
    public void redoEditArea(){
        // TODO - 参考文本组件的重做管理，同理修改为幻灯片编辑的重做管理
    }

    /**
     * 渲染默认项目
     * default.json 是一份已经写好的 Ppt 文件
     */
    public void displayDefaultProject(){
        // 先加载项目
        try {
            ProjectManager.getInstance().loadDefaultProject();
            name = ProjectManager.getInstance().getProject().getName();
            directory = ProjectManager.getInstance().getProject().getFilepath();
            path = directory + File.separator + name;
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 渲染预览面板和编辑面板
        previewSlides();
        displaySlide(iterator);

        // 更新状态栏
        JLabel status_label = this.getComponent("main.label.status");
        status_label.setText("  Load default project: " + ProjectManager.getInstance().getProject().getFilepath());
    }

    /**
     * 渲染项目
     * iterator 指示渲染编辑面板的幻灯片，也指示预览面板的显示窗口 (例如预览面板最多放 5 张幻灯片)
     */
    public void displayProject(){
        // 渲染预览面板和编辑面板
        previewSlides();
        displaySlide(iterator);
    }

    /**
     * 渲染预览面板的幻灯片序列，面板代号 - main.panel.preview
     */
    private void previewSlides() {
        // 获取幻灯片序列
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.getSlides();

        // ☆ preview ☆ -- TODO 如果渲染预览面板，坐标可能需要压缩
        JPanel previewPanel = this.getComponent("main.panel.preview");
        IntStream.range(0, slides.size())       // 使用 IntStream 获取索引，以便构造 clickAction
                .forEach(index -> {
                    // 配置左、右键渲染
                    Slide slide = slides.get(index);
                    ListItem item = new ListItem(slide.getTitle(), () -> {
                        if(iterator == index)   // 如果点击的 item 是原来的 item，则无需切换渲染
                            return;
                        saveEdit();             // 切换 item 时，先保存旧的操作
                        displaySlide(index);    // 渲染 item
                        iterator = index;       // 渲染 item 后，更新 iterator

                        clearCheckStatus();     // 重置选中状态
                    });
                    // 配置右键菜单 - 在 Window 层构造
                    JPopupMenu popupMenu = this.getComponent("main.popup.slide");
                    item.setPopupMenu(popupMenu);
                    // 配置完毕，装载到预览面板
                    previewPanel.add(item);
                });
    }

    /**
     * 渲染显示和编辑面板的幻灯片，面板代号 - main.panel.edit
     * @param index 幻灯片序列中的索引
     */
    private void displaySlide(int index){
        // 获取指定位置的幻灯片
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.getSlides();
        Slide slide = slides.get(index);

        // ☆ edit ☆
        SlidePanel editPanel = this.getComponent("main.panel.edit");
        this.clearEdit();
        editPanel.setContents(slide.getContent());
    }

    /**
     * 清除旧项目的 UI 组件
     */
    private void clear(){
        // 清除编辑面板组件
        this.clearEdit();

        // 清除预览面板组件
        JPanel previewPanel = this.getComponent("main.panel.preview");
        previewPanel.removeAll();
        previewPanel.revalidate();  // 重新验证
        previewPanel.repaint();     // 请求重绘
    }

    /**
     * 清除现项目的编辑面板组件
     */
    private void clearEdit(){
        SlidePanel editPanel = this.getComponent("main.panel.edit");
        editPanel.clear();
    }

    /**
     * 重置预览面板 Items 的选中状态
     */
    private void clearCheckStatus() {
        JPanel previewPanel = this.getComponent("main.panel.preview");
        Component[] components = previewPanel.getComponents();

        for (Component component : components) {
            if (component instanceof ListItem listItem) { // 检查是否为 ListItem 类型
                listItem.setChecked(false);
                listItem.repaint();
            }
        }
    }

    /**
     * 快速保存终端在编辑面板上的操作，面板代号 - main.panel.edit
     * 每次 DisplaySlide 之前调用此方法，保存上一次的所有修改内容
     */
    private void saveEdit(){
        // 获取当前编辑内容
        SlidePanel editPanel = this.getComponent("main.panel.edit");
        List<AbstractContent> contents = editPanel.getContents();

        // 保存到 presentation
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.getSlides();
        Slide slide = slides.get(iterator);
        slide.setContent(contents);
        saveFlag = true;
    }

    /**
     * 主窗口接收到关闭信号，需要终止所有进程
     * @return 程序是否正常退出
     */
    public boolean stopProcess(boolean ifSave){
        // 选择为是，保存项目
        if (ifSave){
            // 保存项目到路径
            try {
                if (ProjectManager.getInstance().getProject().getFilepath() == null) {  // 如果是新建项目，则保存到 project 文件夹
                    // 保存最后一次添加的组件
                    saveEdit();

                    // 获取 project 路径
                    String currentDirectory = System.getProperty("user.dir");
                    String projectDirectory = "project";
                    name = ProjectManager.getInstance().getProject().getName();
                    directory = currentDirectory + File.separator + projectDirectory;
                    path = directory + File.separator + name;
                }
                ProjectManager.getInstance().saveProject(name, directory);
                saveFlag = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ProcessEngine.stopProcess();
        return true;
    }
}
