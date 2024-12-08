package org.powerpoint.window;

import org.powerpoint.window.component.NoDotsSplitPane;
import org.powerpoint.window.component.ShortSplitPane;
import org.powerpoint.window.component.SlidePanel;
import org.powerpoint.window.enums.CloseAction;
import org.powerpoint.window.enums.ColorName;
import org.powerpoint.window.layout.ListLayout;
import org.powerpoint.window.service.MainService;

import javax.swing.*;
import java.awt.*;


public class MainWindow extends AbstractWindow <MainService>{

    public MainWindow(String title){
        super(title, new Dimension(1280, 800), true, MainService.class);
        this.setDefaultCloseAction(CloseAction.DISPOSE);    // 窗口关闭不直接退出程序
        this.initWindowContent();                           // 初始化窗口组件

        // 初始化时渲染默认项目
        service.displayDefaultProject();
    }

    /**
     * Frame_Size = 1280 * 800
     * Layout >>
     * ----x1-----
     * ----x2-----
     * -x3--|--x4-
     * ----x5-----
     * <<
     * 主界面上方的面板作为控制区域
     * 主界面下方的面板作为显示区域，分别包含左侧的预览区域，右侧的单页展示和编辑区域，还有最下面的状态栏
     */
    @Override
    protected void initWindowContent() {
        // 控制区域
        this.addComponent("main.panel.control", new ShortSplitPane(JSplitPane.VERTICAL_SPLIT), BorderLayout.NORTH, panel ->{
            panel.setResizeWeight(0.6);     // 此方法指定在分割窗格大小发生变化时如何分配额外空间，下侧面板将保持更大的相对大小
            panel.setEnabled(false);        // 禁用分割条移动

            panel.setTopComponent(this.createControlPanel());       // x1.控制栏
            panel.setBottomComponent(createFileToolsPanel(panel));  // x2.工具栏 (默认显示文件选项的工具栏)
        });

        // 显示区域，包含左侧的预览区域，右侧的单页展示和编辑区域，还有最下面的状态栏区域
        this.addComponent("main.panel.content", new NoDotsSplitPane(JSplitPane.VERTICAL_SPLIT), BorderLayout.CENTER, panel -> {
            // 先纵向分割出最下方状态栏和中心区域两个部分
            panel.setResizeWeight(0.98);    // 上侧面板将保持更大的相对大小
            panel.setEnabled(false);        // 禁用分割条移动

            // 配置最下方的提示行区域
            panel.setBottomComponent(this.createStatusPanel());     // x5.状态栏

            // 配置中心区域，再次分割面板，包含左侧预览区域和右侧单页展示和编辑界面
            NoDotsSplitPane centerPanel = new NoDotsSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            centerPanel.setResizeWeight(0.20);  // 右侧面板将保持更大的相对大小
            centerPanel.setEnabled(false);      // 禁用分割条移动
            centerPanel.setLeftComponent(this.createLeftPanel());   // x3.预览窗口
            centerPanel.setRightComponent(this.createRightPanel()); // x4.展示和编辑窗口

            /// 有时启动程序会发现分割比例并非 2:8 - 采用 3 个解决方法 a/b/c
            // a.明确设置分割条位置
            centerPanel.setDividerLocation(0.2); // 此方法将分隔符位置设置为 JSplitPane 大小的百分比

            // b.重新验证布局
            centerPanel.revalidate();
            centerPanel.repaint();

            panel.setTopComponent(centerPanel);
        });
    }

    /**
     * @ Control
     * 对最上面一排工具栏包括里面的各个按钮进行初始化
     */
    private JPanel createControlPanel(){
        JPanel control_panel = new JPanel();
        control_panel.setPreferredSize(new Dimension(0, 50));   // 宽度为 0 意味着宽度将根据其他组件的布局动态调整

        // 采用流式布局，直接让按钮居右按顺序放置
        FlowLayout tools_layout = new FlowLayout();
        tools_layout.setAlignment(FlowLayout.LEFT);
        control_panel.setLayout(tools_layout);

        // 按钮 - 文件(打开、新建、保存)
        this.addComponent(control_panel, "main.button.file", new JButton("文件"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.addActionListener(e -> service.fileButtonAction());
        });

        // 按钮 - 文本
        this.addComponent(control_panel, "main.button.text", new JButton("文本"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.addActionListener(e -> service.textButtonAction());
        });

        // 按钮 - 绘制
        this.addComponent(control_panel, "main.button.paint", new JButton("绘制"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.addActionListener(e -> service.paintButtonAction());
        });

        // 按钮 - 插入
        this.addComponent(control_panel, "main.button.insert", new JButton("插入"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.setToolTipText("选择一张图片");
            button.addActionListener(e -> service.insertButtonAction());
        });
        return control_panel;
    }

    /**
     * 创建左侧预览面板，用于预览整个项目
     * @return 预览板块
     */
    private JScrollPane createLeftPanel(){
        // 创建一个预览面板
        JPanel previewPanel = new JPanel();
        previewPanel.setLayout(new ListLayout());   // 采用自定义列表布局
        previewPanel.setPreferredSize(new Dimension(0, 400));   // c.设置首选尺寸，避免分割异常的问题
        previewPanel.setBackground(ColorName.DEFAULT.getColor());
        this.mapComponent("main.panel.preview", previewPanel);

        // 配置右键弹出菜单，包括创建新的幻灯片和删除幻灯片 - TODO - 增加一个重命名功能
        JPopupMenu popupMenu = new JPopupMenu();
        this.mapComponent("main.popup.slide", popupMenu);
        this.add(popupMenu);

        JMenuItem createItem = new JMenuItem("新建幻灯片");
        createItem.addActionListener(e -> service.createSlide());
        popupMenu.add(createItem);

        JMenuItem copyItem = new JMenuItem("复制幻灯片");
        copyItem.addActionListener(e -> service.copySlide());
        popupMenu.add(copyItem);

        JMenuItem deleteItem = new JMenuItem("删除幻灯片");
        deleteItem.addActionListener(e -> service.deleteSlide());
        popupMenu.add(deleteItem);

        JScrollPane scrollPane = new JScrollPane(previewPanel);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);    // 禁用横向滚动条，不然会出现显示 bug (minor)
        return scrollPane;
    }

    /**
     * 创建右侧展示和编辑板块，包括对幻灯片进行编辑操作
     * @return 编辑板块
     */
    private JScrollPane createRightPanel(){
        // 创建一个可操作面板
        SlidePanel editPanel = new SlidePanel();
        editPanel.setPreferredSize(new Dimension(0, 400));  // c.设置首选尺寸，避免分割异常的问题
        this.mapComponent("main.panel.edit", editPanel);

        // 配置编辑文本域的撤销和重做功能
        this.service.redoEditArea();

        return new JScrollPane(editPanel);
    }

    /**
     * 创建底部提示行板块，用于展示控制台输出信息
     * @return 底部板块
     */
    private JPanel createStatusPanel(){
        JLabel status_label= new JLabel("  注：未加载任何幻灯片文件");
        this.mapComponent("main.label.status", status_label);
        status_label.setEnabled(false);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.add(status_label, BorderLayout.WEST);
        return bottom;
    }

    /**
     * @ Tools
     * 创建工具栏面板，包括新建、打开、保存文件按钮
     * 由 service 层指定调用
     */
    public JPanel createFileToolsPanel(JSplitPane panel){
        JPanel tools_panel = new JPanel();
        tools_panel.setPreferredSize(new Dimension(0, 50));

        // 采用流式布局，直接让按钮居右按顺序放置
        FlowLayout tools_layout = new FlowLayout();
        tools_layout.setAlignment(FlowLayout.LEFT);
        tools_panel.setLayout(tools_layout);

        // 按钮 - 文件 (打开、新建、保存)
        this.addComponent(tools_panel, "main.button.tools.openFile", new JButton("打开"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.addActionListener(e -> service.openFileButtonAction());
        });
        this.addComponent(tools_panel, "main.button.tools.newFile", new JButton("新建"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.addActionListener(e -> service.newFileButtonAction());
        });
        this.addComponent(tools_panel, "main.button.tools.saveFile", new JButton("保存"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.addActionListener(e -> service.saveFileButtonAction());
        });
        return tools_panel;
    }

    /**
     * @ Tools
     * 创建工具栏面板，包括创建文本按钮
     * 由 service 层指定调用
     */
    public JPanel createTextToolsPanel(JSplitPane panel){
        JPanel tools_panel = new JPanel();
        tools_panel.setPreferredSize(new Dimension(0, 50));

        // 采用流式布局，直接让按钮居右按顺序放置
        FlowLayout tools_layout = new FlowLayout();
        tools_layout.setAlignment(FlowLayout.LEFT);
        tools_panel.setLayout(tools_layout);

        // 按钮 - 文本 (绘制横排文本框)
        this.addComponent(tools_panel, "main.button.tools.textArea", new JButton("绘制横排文本框"), button -> {
            button.setPreferredSize(new Dimension(240, 40));
            button.addActionListener(e -> service.textAreaButtonAction());
        });
        return tools_panel;
    }

    /**
     * @ Tools
     * 创建工具栏面板，包括直线、矩形、椭圆基本图形按钮
     * 由 service 层指定调用
     */
    public JPanel createPaintToolsPanel(JSplitPane panel){
        JPanel tools_panel = new JPanel();
        tools_panel.setPreferredSize(new Dimension(0, 50));

        // 采用流式布局，直接让按钮居右按顺序放置
        FlowLayout tools_layout = new FlowLayout();
        tools_layout.setAlignment(FlowLayout.LEFT);
        tools_panel.setLayout(tools_layout);

        // 按钮 - 绘制 (直线、矩形、椭圆)
        this.addComponent(tools_panel, "main.button.tools.lineShape", new JButton("直线"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.addActionListener(e -> service.lineShapeButtonAction());
        });
        this.addComponent(tools_panel, "main.button.tools.rectangleShape", new JButton("矩形"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.addActionListener(e -> service.rectangleShapeButtonAction());
        });
        this.addComponent(tools_panel, "main.button.tools.ovalShape", new JButton("椭圆"), button -> {
            button.setPreferredSize(new Dimension(80, 40));
            button.addActionListener(e -> service.ovalShapeButtonAction());
        });
        return tools_panel;
    }

    /**
     * @ Tools
     * 创建工具栏面板，包括插入图像按钮
     * 由 service 层指定调用
     */
    public JPanel createInsertToolsPanel(JSplitPane panel){
        JPanel tools_panel = new JPanel();
        tools_panel.setPreferredSize(new Dimension(0, 50));

        // 采用流式布局，直接让按钮居右按顺序放置
        FlowLayout tools_layout = new FlowLayout();
        tools_layout.setAlignment(FlowLayout.LEFT);
        tools_panel.setLayout(tools_layout);

        // 按钮 - 插入 (图像等资源)
        this.addComponent(tools_panel, "main.button.tools.imageInsert", new JButton("此设备"), button -> {
            button.setPreferredSize(new Dimension(120, 40));
            button.addActionListener(e -> service.imageInsertButtonAction());
        });
        return tools_panel;
    }

    /**
     * 关闭窗口之前，应做什么在这里写
     * @return 是否就绪
     */
    @Override
    protected boolean onClose() {
        // 关闭之前的操作
        return service.stopProcess();
    }
}
