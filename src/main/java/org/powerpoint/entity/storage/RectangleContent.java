package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class RectangleContent extends AbstractContent{
    private int width;
    private int height;
    private int x;
    private int y;
    private String color;
    private int thickness;

    /**
     * 初始化默认矩形
     * 坐标参数通过鼠标点击事件传递
     */
    public void initDefault(int x, int y){
        super.setContentType("rectangle");
        this.width = 150;
        this.height = 75;
        this.x = x;
        this.y = y;
        this.color = "black";
        this.thickness = 1;
    }
}
