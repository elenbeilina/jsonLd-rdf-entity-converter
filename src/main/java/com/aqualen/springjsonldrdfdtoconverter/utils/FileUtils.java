package com.aqualen.springjsonldrdfdtoconverter.utils;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Slf4j
@UtilityClass
public class FileUtils {

    @SneakyThrows
    public static String readFile(String fileName) {
        InputStream inputStream = getFileInputStream(fileName);

        // the stream holding the file content
        if (inputStream == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        }

        return readFromInputStream(inputStream);
    }

    public static InputStream getFileInputStream(String fileName) {
        ClassLoader classLoader = FileUtils.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }

    @SneakyThrows
    private String readFromInputStream(InputStream inputStream) {
        return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
    }
}
