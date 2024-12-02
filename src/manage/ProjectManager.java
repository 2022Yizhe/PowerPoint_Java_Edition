package manage;

import com.formdev.flatlaf.json.Json;
import entity.ProcessEntity;
import entity.ProjectEntity;
import entity.storage.Presentation;
import window.enums.ReturnCode;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static manage.DisplayEngine.*;

/**
 * 项目管理器
 * 用于加载项目
 */
public class ProjectManager {
    private static ProjectManager INSTANCE;         // 静态变量用于管理
    private static ProjectEntity PROJECT;
    private static ProcessEntity PROCESS;

    private ProjectManager(){
        PROCESS = new ProcessEntity();
    }

    public static ProjectManager getManager() { return INSTANCE; }
    public static ProjectEntity getProject() { return PROJECT; }
    public static ProcessEntity getProcess() { return PROCESS; }

    /**
     * 创建默认项目，在工作目录下生成默认项目文件: $workspace$/project/default.json
     * @throws IOException 调用这个方法的代码要处理可能发生的异常
     */
    public static void loadDefaultProject() throws IOException {
        // 定位当前工作目录
        String currentDirectory = System.getProperty("user.dir");
        String projectDirectory = "project";    // 定义项目文件夹
        String fileName = "default.json";       // 定义默认存储文件名

        // 生成默认项目文件
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
            PROJECT = new ProjectEntity(fileName, dir.getAbsolutePath());
        }
        INSTANCE = new ProjectManager();

        // 解析默认项目 ~ parse 'default.json'
        parseProject();
    }

    /**
     * 打开一个存在的项目，需指定项目文件名称和存储目录
     * @param name 项目文件名
     * @param filepath 项目文件目录
     * @throws FileNotFoundException 调用这个方法的代码要处理可能发生的异常
     */
    public static void loadProject(String name, String filepath) throws FileNotFoundException {
        // 加载指定项目
        File file = new File(filepath + "/" + name);
        if(!file.exists()) {
            throw new FileNotFoundException("Failed to load project!");
        } else {
            PROJECT = new ProjectEntity(name, filepath);
            INSTANCE = new ProjectManager();
        }

        // 解析指定项目 ~ parse target file
        parseProject();
    }

    /**
     * 新建项目，需指定项目文件名称和存储目录
     * @param name 项目文件名
     * @param filepath 项目文件目录
     */
    public static void createProject(String name, String filepath) {
        PROJECT = new ProjectEntity(name, filepath);
        INSTANCE = new ProjectManager();
        // TODO
    }

    /**
     * 保存当前项目，需指定项目文件名称和存储目录
     * @param name 项目文件名
     * @param filepath 项目文件目录
     */
    public static void saveProject(String name, String filepath) {}

    private static void parseProject() {
        Presentation presentation = JsonParser(PROJECT.Filepath() + '/' + PROJECT.Name());
        if (presentation == null) {
            PROCESS.setPresentation(null);
            PROCESS.setExitcode(ReturnCode.FAIL_TO_PARSE);
        } else {
            PROCESS.setPresentation(presentation);
            PROCESS.setExitcode(ReturnCode.SUCCESS);
        }
    }
}
