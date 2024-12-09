package org.powerpoint.manage;

import org.powerpoint.entity.ProcessEntity;
import org.powerpoint.entity.ProjectEntity;
import org.powerpoint.entity.storage.Presentation;
import org.powerpoint.window.enums.ReturnCode;

import java.io.*;

import static org.powerpoint.manage.ProcessEngine.*;

/**
 * 项目管理器
 * 用于加载项目
 */
public class ProjectManager {
    private ProjectEntity PROJECT;
    private ProcessEntity PROCESS;

    private static ProjectManager INSTANCE; // 静态变量用于管理

    private ProjectManager(){
        PROCESS = new ProcessEntity();
    }

    public static ProjectManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ProjectManager();
        }
        return INSTANCE;
    }

    public ProjectEntity getProject() { return PROJECT; }
    public ProcessEntity getProcess() { return PROCESS; }

    /**
     * 创建默认项目，在工作目录下生成默认项目文件: $workspace$/project/default.json
     * @throws IOException 调用这个方法的代码要处理可能发生的异常
     */
    public void loadDefaultProject() throws IOException {
        // 定位当前工作目录
        String currentDirectory = System.getProperty("user.dir");
        String projectDirectory = "project";    // 定义项目文件夹
        String fileName = "default.json";       // 定义默认存储文件名

        // 创建默认项目文件目录
        File dir = new File(currentDirectory, projectDirectory);
        if (!dir.exists()) {
            if(!dir.mkdir())
                throw new IOException("Failed to create folder!");
        }
        File file = new File(dir, fileName);    // 创建存储文件对象

        // 加载默认项目
        if(!file.exists()) {
            if (file.createNewFile())
                PROJECT = new ProjectEntity(fileName, dir.getAbsolutePath());
            else
                throw new IOException("Failed to create project!");
        } else {
            PROJECT = new ProjectEntity(fileName, dir.getAbsolutePath());   // 创建默认实例
        }

        // 解析默认项目 ~ parse 'default.json'
        parseProject();
    }

    /**
     * 打开一个存在的项目，需指定项目文件名称和存储目录
     * @param name 项目文件名
     * @param directory 项目文件目录
     * @throws FileNotFoundException 调用这个方法的代码要处理可能发生的异常
     */
    public void loadProject(String name, String directory) throws FileNotFoundException {
        // 加载指定项目 - 删除旧项目，加载新项目
        File file = new File(directory + "/" + name);
        if(!file.exists()) {
            throw new FileNotFoundException("Failed to load project!");
        } else {
            PROJECT = null;                                 // 提示垃圾回收器 (GC) 可以回收
            PROJECT = new ProjectEntity(name, directory);   // 创建新实例
        }

        // 解析指定项目 ~ parse target file
        parseProject();
    }

    /**
     * 新建项目，需指定项目文件名称，文件路径在保存时再确定
     * @param name 项目文件名
     */
    public void createProject(String name) {
        // 加载新项目
        PROJECT = null;                                     // 提示垃圾回收器 (GC) 可以回收
        PROJECT = new ProjectEntity(name, null);    // 创建新实例

        // 解析新项目 ~ parse default string
        parseProject();
    }

    /**
     * 序列化当前项目，保存文件到指定目录
     * @param directory 文件目录
     */
    public void saveProject(String name, String directory) throws IOException {
        // 创建项目文件目录，并检查
        File dir = new File(directory);
        boolean flag = dir.exists() && dir.isDirectory();
        if (!flag) {
            if(!dir.mkdir())
                throw new IOException("Failed to create folder!");
        }

        // '反解析' - 并保存项目为 json 文件
        JsonConverter(PROCESS.getPresentation(), directory + '/' + name);

        // 更新 PROJECT 实例
        PROJECT = null;                                 // 提示垃圾回收器 (GC) 可以回收
        PROJECT = new ProjectEntity(name, directory);   // 创建新实例
    }

    /**
     * 解析幻灯片项目，在加载完毕项目之后进行
     */
    private void parseProject() {
        Presentation presentation;

        // 解析项目
        if (PROJECT.getFilepath() == null)
            presentation = JsonParser();    // 无参方法表示解析新建项目
        else
            presentation = JsonParser(PROJECT.getFilepath() + '/' + PROJECT.getName());

        // 检查解析情况
        if (presentation == null) {
            PROCESS.setPresentation(null);
            PROCESS.setExitcode(ReturnCode.FAIL_TO_PARSE);
        } else {
            PROCESS.setPresentation(presentation);
            PROCESS.setExitcode(ReturnCode.SUCCESS);
        }
    }
}
