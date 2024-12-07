package org.powerpoint.entity;

import org.powerpoint.entity.storage.Presentation;
import org.powerpoint.window.enums.ReturnCode;

/**
 * 进程运行实体类
 * ppt
 */
public class ProcessEntity {
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
