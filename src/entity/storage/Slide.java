package entity.storage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Slide {
    private String title;
    private List<AbstractContent> content;

    public String Title() { return title; }
    public List<AbstractContent> Contents() { return content; }
}