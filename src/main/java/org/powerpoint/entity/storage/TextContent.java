package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class TextContent extends AbstractContent {
    private String value;
    private int x;
    private int y;
    private String color;
    private String font;
    private int size;

    /**
     * 初始化默认文本
     * 坐标参数通过鼠标点击事件传递
     */
    public void initDefault(int x, int y){
        super.setContentType("text");
        this.value = "单击此处添加文本";
        this.x = x;
        this.y = y;
        this.color = "black";
        this.font = "等线";
        this.size = 12;
    }
}
