package entity.storage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Presentation 类 (一个 PPT 包含若干张幻灯片)
 * 存储结构中的顶层类，title 和 author 这个数据暂未使用
 */
@Getter @Setter
public class Presentation {
    private String title;
    private String author;
    private List<Slide> slides;

    // methods
    public String Title() { return title; }
    public List<Slide> Slides() { return slides; }
}