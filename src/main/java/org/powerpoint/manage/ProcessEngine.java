package org.powerpoint.manage;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.core.exc.StreamReadException;

import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.powerpoint.entity.storage.*;

import java.io.File;
import java.io.IOException;


/**
 * 解析引擎
 * 采用标准的 json 语法来存储 ppt 文件，并使用 Jackson 解析器实现对源文件的解析，步骤如下：
 * - 解析源文件 (.json) 返回一个 presentation 对象
 * Jackson 这个工具是常用的 json 解析工具，我们主要使用它的序列化和反序列化功能
 */
public class ProcessEngine {
    /**
     * 反序列化 - 将 json 字符串转换为 Ppt 对象
     * @param json_path json 文件路径
     * @return 解析结果 presentation，是一个幻灯片序列 (slides)
     */
    public static Presentation JsonParser(String json_path){
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, JsonTypeInfo.As.PROPERTY); // 启用多态性对象检查

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
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 序列化 - 在新建项目时，序列化一段默认 json 文本
     * @return 解析结果 presentation，是一个幻灯片序列 (slides)，但只有一张幻灯片
     */
    public static Presentation JsonParser() {
        String newProject = "{\n" +
                "  \"title\": \"My Presentation\",\n" +
                "  \"author\": \"Author Name\",\n" +
                "  \"slides\": [\n" +
                "    {\n" +
                "      \"title\": \"Slide 1\",\n" +
                "      \"content\": [\n" +
                "        {\n" +
                "          \"type\": \"text\",\n" +
                "          \"contentType\": \"text\",\n" +
                "          \"value\": \"单击此处添加文本\",\n" +
                "          \"x\": 250,\n" +
                "          \"y\": 250,\n" +
                "          \"color\": \"black\"\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance,
                ObjectMapper.DefaultTyping.JAVA_LANG_OBJECT, JsonTypeInfo.As.PROPERTY); // 启用多态性对象检查

        try {
            return objectMapper.readValue(newProject, Presentation.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 序列化 - 将 Ppt 对象转换为 json 字符串，保存到指定路径
     * @param presentation 一个 presentation 实例
     * @param json_path 文件的保存路径
     */
    public static void JsonConverter(Presentation presentation, String json_path){
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(json_path), presentation);
        } catch (StreamWriteException e){
            System.out.println("Error during data binding: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 中断解析过程
     */
    public static void stopProcess(){}
}
