package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

import javax.sound.sampled.Line;

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

    @Override
    public LineContent clone() {
        LineContent cloned = new LineContent();
        cloned.setContentType(this.getContentType());
        cloned.setStartX(this.getStartX());
        cloned.setStartY(this.getStartY());
        cloned.setEndX(this.getEndX());
        cloned.setEndY(this.getEndY());
        cloned.setColor(this.getColor());
        cloned.setThickness(this.getThickness());
        // 复制其他属性
        return cloned;
    }
}
