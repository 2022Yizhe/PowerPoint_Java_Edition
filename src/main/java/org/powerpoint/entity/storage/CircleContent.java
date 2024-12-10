package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CircleContent extends AbstractContent{
    private int radius;
    private int x;
    private int y;
    private String color;
    private int thickness;

    /**
     * 初始化默认圆形
     * 坐标参数通过鼠标点击事件传递
     */
    public void initDefault(int x, int y){
        super.setContentType("circle");
        this.radius = 100;
        this.x = x;
        this.y = y;
        this.color = "black";
        this.thickness = 1;
    }
}
