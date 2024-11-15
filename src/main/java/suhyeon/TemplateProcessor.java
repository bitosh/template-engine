package suhyeon;

import com.fasterxml.jackson.databind.JsonNode;
import suhyeon.exception.TemplateEngineException;
import suhyeon.token.Token;
import suhyeon.token.Tokenizer;
import suhyeon.utils.FileUtils;

import java.util.Arrays;
import java.util.List;

import static suhyeon.exception.TemplateEngineExceptionType.FILE_NOT_FOUND;

public class TemplateProcessor {
    private static final String TEMPLATE_EXTENSION = ".txt";
    private static final String DATA_EXTENSION = ".json";
    private static final String DEFAULT_TEMPLATE_FILE_NAME = "template.txt";
    private static final String DEFAULT_DATA_FILE_NAME = "data.json";
    private static final String OUTPUT_FILE_NAME = "output.txt";

    private static final String ROOT_DATA_KEY = "USERS";

    public static void process(final String[] args) {
        final List<Token> tokens = Tokenizer.tokenize(readTemplate(args));
        final DataContext context = new DataContext(ROOT_DATA_KEY, readData(args));
        final StringBuilder outputBuilder = new StringBuilder();

        tokens.forEach(token -> outputBuilder.append(token.interpret(context)));

        FileUtils.write(OUTPUT_FILE_NAME, outputBuilder.toString());
    }

    private static String readTemplate(final String[] args) {
        final String path = getPath(args, TEMPLATE_EXTENSION, DEFAULT_TEMPLATE_FILE_NAME);

        if (FileUtils.exists(path)) {
            return FileUtils.read(path);
        } else {
            return FileUtils.readFromResources(DEFAULT_TEMPLATE_FILE_NAME);
        }
    }

    public static JsonNode readData(final String[] args) {
        final String path = getPath(args, DATA_EXTENSION, DEFAULT_DATA_FILE_NAME);

        if (FileUtils.exists(path)) {
            return FileUtils.readJson(path);
        } else {
            return FileUtils.readJsonFromResources(DEFAULT_DATA_FILE_NAME);
        }
    }

    private static String getPath(final String[] args, final String extension, final String defaultPath) {
        return Arrays.stream(args)
                .filter(argument -> argument.endsWith(extension))
                .findAny()
                .map(TemplateProcessor::getValidFilePath)
                .orElse(defaultPath);
    }

    // 파일 경로를 지정하여 실행하였을 경우에 해당 파일이 존재하지 않다면 Default 파일 사용하지 않고 예외 처리(착오를 방지하기 위함)
    private static String getValidFilePath(final String filePath) {
        if (!FileUtils.exists(filePath)) {
            throw new TemplateEngineException(FILE_NOT_FOUND, filePath);
        }

        return filePath;
    }
}
