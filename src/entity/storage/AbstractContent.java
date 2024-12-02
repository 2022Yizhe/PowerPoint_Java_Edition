package entity.storage;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

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