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

    @Override
    public CircleContent clone() {
        CircleContent cloned = new CircleContent();
        cloned.setContentType(this.getContentType());
        cloned.setX(this.getX());
        cloned.setY(this.getY());
        cloned.setRadius(this.getRadius());
        cloned.setColor(this.getColor());
        cloned.setThickness(this.getThickness());
        // 复制其他属性
        return cloned;
    }
}
