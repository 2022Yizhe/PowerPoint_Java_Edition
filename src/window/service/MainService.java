package window.service;

import entity.storage.*;
import manage.ProjectManager;

import window.MainWindow;
import window.component.SlidePanel;
import window.component.item.ListItem;
import window.dialog.DirectoryChooserDialog;
import window.dialog.FileChooserDialog;
import window.dialog.InputDialog;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.io.*;
import java.util.List;
import java.util.stream.IntStream;

/**
 * 负责配置和管理 MainWindow 的用户交互服务
 */
public class MainService extends AbstractService {
    private String name;                // 当前项目的名称
    private String directory;           // 当前项目的目录
    private String path;                // 当前项目文件的路径

    private int iterator;               // 当前编辑幻灯片的索引，随用户操作更新

    private UndoManager undoManager;    // 重做管理器，用于编辑框支持撤销和重做操作

    /**
     * 设定当前项目的名称和路径
     * @param json_path 路径
     */
    public void setPath(String json_path){
        this.path = json_path.replace("\\", "/");
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
        FileChooserDialog chooser_dialog = new FileChooserDialog(this.getWindow());
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
        status_label.setText("  Load project: " + directory + "\\" + name);
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
        name = ProjectManager.getInstance().getProject().Name();    // 文件名与原文件一样
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
        status_label.setText("  Load project: " + directory + "\\" + name);
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
     * 配置编辑面板的撤销和重做功能
     */
    public void redoEditArea(){
        // TODO - 参考文本组件的重做管理，同理修改为幻灯片编辑的重做管理
    }

    /**
     * 渲染项目
     * iterator 指示渲染编辑面板的幻灯片，也指示预览面板的显示窗口 (例如预览面板最多放 5 张幻灯片)
     */
    public void displayProject(){
        // 渲染预览面板
        previewSlides();

        // 渲染编辑面板
        displaySlide(iterator);
    }

    /**
     * 渲染预览面板的幻灯片序列，面板代号 - main.panel.preview
     */
    private void previewSlides() {
        // 获取幻灯片序列
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.Slides();

        // ☆ preview ☆ -- TODO 如果渲染预览面板，坐标可能需要压缩
        JPanel previewPanel = this.getComponent("main.panel.preview");
        IntStream.range(0, slides.size())       // 使用 IntStream 获取索引，以便构造 clickAction
                .forEach(index -> {
                    // 配置左键渲染
                    Slide slide = slides.get(index);
                    ListItem item = new ListItem(slide.Title(), () -> {
                        if(iterator == index)   // 如果点击的 item 是原来的 item，则无需切换渲染
                            return;
                        saveEdit();             // 切换 item 时，先保存旧的操作
                        displaySlide(index);    // 渲染 item
                        iterator = index;       // 渲染 item 后，更新 iterator
                    });
                    // 配置右键菜单
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
        List<Slide> slides = presentation.Slides();
        Slide slide = slides.get(index);

        // ☆ edit ☆
        SlidePanel editPanel = this.getComponent("main.panel.edit");
        this.clearEdit();
        editPanel.setContents(slide.Contents());
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
     * 快速保存终端在编辑面板上的操作，面板代号 - main.panel.edit
     * 每次 DisplaySlide 之前调用此方法，保存上一次的所有修改内容
     */
    private void saveEdit(){
        // 获取当前编辑内容
        SlidePanel editPanel = this.getComponent("main.panel.edit");
        List<AbstractContent> contents = editPanel.getContents();

        // 保存到 presentation
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.Slides();
        Slide slide = slides.get(iterator);
        slide.setContent(contents);
    }
}
