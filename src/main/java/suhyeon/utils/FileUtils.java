package suhyeon.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import suhyeon.Main;
import suhyeon.exception.TemplateEngineException;

import java.io.*;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

import static java.nio.charset.StandardCharsets.UTF_8;
import static suhyeon.exception.TemplateEngineExceptionType.*;

public class FileUtils {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static String read(final String path) {
        return read(path, UTF_8);
    }

    public static String read(final String path, final Charset encoding) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(path, encoding))) {
            return bufferedReader.lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new TemplateEngineException(TEMPLATE_FILE_READ_ERROR, e);
        }
    }

    public static JsonNode readJson(final String path) {
        return readJson(path, UTF_8);
    }

    public static JsonNode readJson(final String path, final Charset encoding) {
        try {
            return objectMapper.readTree(new FileReader(path, encoding));
        } catch (IOException e) {
            throw new TemplateEngineException(DATA_FILE_READ_ERROR, e);
        }
    }

    public static String readFromResources(final String fileName) {
        try(InputStream inputStream = readInputStreamFromResources(fileName)) {
            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new TemplateEngineException(TEMPLATE_FILE_READ_ERROR, e);
        }
    }

    public static JsonNode readJsonFromResources(final String fileName) {
        try(InputStream inputStream = readInputStreamFromResources(fileName)) {
            return objectMapper.readTree(inputStream);
        } catch (IOException e) {
            throw new TemplateEngineException(DATA_FILE_READ_ERROR, e);
        }
    }

    private static InputStream readInputStreamFromResources(final String fileName) {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new TemplateEngineException(RESOURCE_NOT_FOUND, fileName);
        }

        return inputStream;
    }

    public static void write(final String path, final String content) {
        write(path, content, UTF_8);
    }

    public static void write(final String path, final String content, final Charset encoding) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(path, encoding))) {
            bufferedWriter.write(content.replace("\\n", System.lineSeparator()));
        } catch (IOException e) {
            throw new TemplateEngineException(FILE_WRITE_ERROR, e);
        }
    }

    public static Boolean exists(final String path) {
        return new File(path).exists();
    }
}
