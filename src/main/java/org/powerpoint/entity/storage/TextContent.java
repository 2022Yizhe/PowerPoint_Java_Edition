package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class TextContent extends AbstractContent {
    private String value;
    private int x;
    private int y;
    private int width;
    private int height;
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
        this.width = 150;
        this.height = 75;
        this.color = "black";
        this.font = "等线";
        this.size = 15;
    }

    @Override
    public TextContent clone() {
        TextContent cloned = new TextContent();
        cloned.setContentType(this.getContentType());
        cloned.setValue(this.value);
        cloned.setX(this.x);
        cloned.setY(this.y);
        cloned.setWidth(this.width);
        cloned.setHeight(this.height);
        cloned.setColor(this.color);
        cloned.setFont(this.font);
        cloned.setSize(this.size);
        return cloned;
    }
}
