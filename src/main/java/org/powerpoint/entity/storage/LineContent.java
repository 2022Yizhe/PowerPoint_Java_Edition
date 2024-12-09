package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class LineContent extends AbstractContent{
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private String color;
    private int thickness;

    /**
     * 初始化默认直线
     * 直线长度为固定值 100，坐标参数通过鼠标点击事件传递
     */
    public void initDefault(int x, int y){
        super.setContentType("line");
        this.startX = x;
        this.startY = y;
        this.endX = x + 100;
        this.endY = y;
        this.color = "black";
        this.thickness = 1;
    }
}
