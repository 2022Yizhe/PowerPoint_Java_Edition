package manage;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.exc.StreamReadException;

import entity.storage.*;
import window.enums.ReturnCode;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.util.List;


/**
 * 这是进程执行引擎，采用标准的 json 语法来存储 ppt 文件，并使用 Jackson 解析器实现对源文件的解析，步骤如下：
 * - 解析源文件 (.json)
 * - 渲染当前页
 * - 执行程序中断操作
 * 所有跟项目相关的操作都使用此执行引擎完成。
 */
public class DisplayEngine {
    public static Presentation JsonParser(String json_path){
        ObjectMapper objectMapper = new ObjectMapper();

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

    public static void stopProcess(){}
}
