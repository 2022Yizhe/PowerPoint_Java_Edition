package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class OvalContent extends AbstractContent{
    private int radiusX;
    private int radiusY;
    private int x;
    private int y;
    private String color;
    private int thickness;

    /**
     * 初始化默认椭圆
     * 坐标参数通过鼠标点击事件传递
     */
    public void initDefault(int x, int y){
        super.setContentType("oval");
        this.radiusX = 100;
        this.radiusY = 50;
        this.x = x;
        this.y = y;
        this.color = "black";
        this.thickness = 1;
    }

    @Override
    public OvalContent clone() {
        OvalContent cloned = new OvalContent();
        cloned.setContentType(this.getContentType());
        cloned.setX(this.getX());
        cloned.setY(this.getY());
        cloned.setRadiusX(this.getRadiusX());
        cloned.setRadiusY(this.getRadiusY());
        cloned.setColor(this.getColor());
        cloned.setThickness(this.getThickness());
        // 复制其他属性
        return cloned;
    }
}
