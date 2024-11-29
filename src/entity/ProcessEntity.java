package entity;

import entity.storage.Presentation;
import window.enums.ReturnCode;

/**
 * 进程运行实体类
 * ppt
 */
public class ProcessEntity {
    // 由 ProjectManager 调用方法，DisplayEngine 负责执行生成对象实体
    private Presentation presentation;
    private ReturnCode exitcode;

    public ProcessEntity() {
        this.presentation = new Presentation();
    }
    public Presentation getPresentation() { return presentation; }
    public void setPresentation(Presentation presentation) { this.presentation = presentation; }
    public ReturnCode getExitcode() { return exitcode; }
    public void setExitcode(ReturnCode exitcode) { this.exitcode = exitcode; }
}
