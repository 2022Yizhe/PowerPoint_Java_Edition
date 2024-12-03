package window.service;

import com.sun.tools.javac.Main;
import entity.storage.*;
import manage.ProjectManager;

import window.MainWindow;
import window.component.SlidePanel;
import window.component.item.ListItem;
import window.dialog.DirectoryChooserDialog;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.event.*;
import java.io.*;
import java.util.List;
import java.util.stream.IntStream;

public class MainService extends AbstractService {
    private String name;                // 当前项目的名称
    private String directory;           // 当前项目的目录
    private String path;                // 当前项目文件的路径

    private int index;                  // 当前编辑幻灯片的索引，随用户操作更新

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
        DirectoryChooserDialog chooser_dialog = new DirectoryChooserDialog(this.getWindow());
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
        this.clear();
        this.displayProject();
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
     * 配置编辑面板的撤销和重做功能
     */
    public void redoEditArea(){
        // TODO - 参考文本组件的重做管理，同理修改为幻灯片编辑的重做管理
    }

    /**
     * 渲染项目
     * index 指示渲染编辑面板的幻灯片，也指示预览面板的显示窗口 (例如预览面板最多放 5 张幻灯片)
     */
    public void displayProject(){
        // 渲染预览面板
        previewSlides();

        // 渲染编辑面板
        displaySlide(index);
    }

    /**
     * 渲染预览面板的幻灯片序列，面板代号 - main.panel.preview
     */
    private void previewSlides() {
        // 获取幻灯片序列
        Presentation presentation = ProjectManager.getInstance().getProcess().getPresentation();
        List<Slide> slides = presentation.Slides();

        // ☆ preview ☆ -- TODO 渲染预览面板时，坐标可能需要压缩
        JPanel previewPanel = this.getComponent("main.panel.preview");
        IntStream.range(0, slides.size())       // 使用 IntStream 获取索引，以便构造 clickAction
                .forEach(index -> {
                    Slide slide = slides.get(index);
                    ListItem item = new ListItem(slide.Title(), () -> {
                        displaySlide(index);    // TODO 存在重复显示的问题
                    });
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
     * 切换展示和编辑的幻灯片，同时保存编辑的内容
     * @param path 文件路径
     */
    private void switchEditSlide(String path) {
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
