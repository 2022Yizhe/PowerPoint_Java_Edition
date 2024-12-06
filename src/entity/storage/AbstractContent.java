package entity.storage;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

/**
 * 抽象内容类
 * 一张幻灯片中含有各种类型的内容，比如说：Text, Image, Line, Rectangle, Oval, Circle 等
 * 这个类是具体内容类的抽象基类，含有 '内容类型' 一个数据成员，用于标识内容的类型
 * lombok 这个工具用于批量自动生成这些数据类的 set 和 get 方法，而无需手动写即可使用
 */

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"   // 会占用 type 字段，需要新的属性和字段，如：contentType
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = TextContent.class, name = "text"),
        @JsonSubTypes.Type(value = LineContent.class, name = "line"),
        @JsonSubTypes.Type(value = RectangleContent.class, name = "rectangle"),
        @JsonSubTypes.Type(value = OvalContent.class, name = "oval"),
        @JsonSubTypes.Type(value = CircleContent.class, name = "circle"),
        @JsonSubTypes.Type(value = ImageContent.class, name = "image")
})

@Setter @Getter
public abstract class AbstractContent {
    private String contentType;

    public String ContentType(){ return contentType; }
}