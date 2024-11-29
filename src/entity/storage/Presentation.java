package entity.storage;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class Presentation {
    private String title;
    private String author;
    private List<Slide> slides;

    // methods
    public String Title() { return title; }
    public List<Slide> Slides() { return slides; }
}