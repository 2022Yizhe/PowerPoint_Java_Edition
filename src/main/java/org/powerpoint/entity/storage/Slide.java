package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Slide 类 (单张幻灯片)
 * 存储结构中的中间类，title 这个数据暂未使用
 */

@Getter @Setter
public class Slide {
    private String title;
    private List<AbstractContent> content;

    public String Title() { return title; }
    public List<AbstractContent> Contents() { return content; }
}