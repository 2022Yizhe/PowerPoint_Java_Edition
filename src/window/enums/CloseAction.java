package window.enums;

import window.AbstractWindow;

import java.awt.*;
import java.util.function.Consumer;

/**
 * 预定义的窗口关闭行为
 */
public enum CloseAction {
    DISPOSE(Window::dispose),    // 调用窗口的 dispose 方法
    EXIT(window -> System.exit(0));   // 调用窗口的 exit方法

    private final Consumer<AbstractWindow<?>> action;
    CloseAction(Consumer<AbstractWindow<?>> action){
        this.action = action;
    }

    public void doAction(AbstractWindow<?> window){
        action.accept(window);
    }
}
