package manage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.exc.StreamReadException;

import entity.storage.*;

import java.io.File;


/**
 * 解析引擎
 * 采用标准的 json 语法来存储 ppt 文件，并使用 Jackson 解析器实现对源文件的解析，步骤如下：
 * - 解析源文件 (.json) 返回一个 presentation 对象
 */
public class ParseEngine {
    /**
     * json 解析器
     * @param json_path json 文件路径
     * @return 解析结果 presentation，是一个幻灯片序列 (slides)
     */
    public static Presentation JsonParser(String json_path){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, JsonTypeInfo.As.PROPERTY);

        try {
            // 读取 JSON 文件
            Presentation presentation = objectMapper.readValue(new File(json_path), Presentation.class);

            // 终端打印部分读取到的数据
            System.out.println("Presentation Title: " + presentation.Title());
            for (Slide slide : presentation.Slides()) {
                System.out.println("Slide Title: " + slide.Title());
                for (AbstractContent content : slide.Contents()) {
                    if (content instanceof TextContent) {
                        System.out.println("Text: " + ((TextContent) content).Value());
                    } else if (content instanceof ImageContent) {
                        System.out.println("Image Source: " + ((ImageContent) content).Src());
                    }
                }
            }
            return presentation;
        } catch (StreamReadException e) {
            // 捕获 DatabindException
            System.out.println("Error during data binding: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 中断解析过程
     */
    public static void stopProcess(){}
}
