package org.powerpoint.entity.storage;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * Slide 类 (单张幻灯片)
 * 存储结构中的中间类，title 这个数据为预览面板中幻灯片的标签名
 */

@Getter @Setter
public class Slide {
    private String title;
    private List<AbstractContent> content;

    /**
     * 初始化 slide 对象
     * title 为默认值 “Slide new”，序列中只有一个 TextContent 组件，其为默认值 “单击此处添加文本”
     */
    public void initDefault(){
        List<AbstractContent> contents = new ArrayList<>();
        TextContent defaultText = new TextContent();
        defaultText.setContentType("text");
        defaultText.setValue("单击此处添加文本");
        defaultText.setX(200);
        defaultText.setY(100);
        defaultText.setColor("BLACK");
        defaultText.setFont("等线");
        defaultText.setSize(12);
        contents.add(defaultText);

        this.setContent(contents);
        this.setTitle("Slide new");
    }

    /**
     * 复制函数
     */
    public void copySlide(Slide slide){
        this.setTitle(slide.getTitle());

        // 创建一个新的内容列表，进行深拷贝
        List<AbstractContent> newContents = new ArrayList<>();
        for (AbstractContent content : slide.getContent()) {
            // 假设 AbstractContent 有一个 clone 方法，进行深拷贝
            newContents.add(content.clone());
        }
        this.setContent(newContents);
    }
}