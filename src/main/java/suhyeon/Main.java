package suhyeon;

import com.fasterxml.jackson.databind.JsonNode;
import suhyeon.exception.TemplateEngineException;
import suhyeon.token.ForToken;
import suhyeon.token.Token;
import suhyeon.token.Tokenizer;
import suhyeon.utils.FileUtils;

import java.util.*;

import static suhyeon.exception.TemplateEngineExceptionType.FILE_NOT_FOUND;

public class Main {
    public static void main(String[] args) {
        final JsonNode jsonNode = readJson(args);
        final String template = read(args);
        final List<Token> tokens = Tokenizer.tokenize(template);
        final TemplateProcessor templateProcessor = new TemplateProcessor(new HashMap<>(){{ put("USERS", jsonNode); }});

        FileUtils.write("./output.txt", templateProcessor.process(tokens));
    }

    private static String read(String[] args) {
        final String path = Arrays.stream(args)
                .filter(argument -> argument.endsWith(".txt"))
                .findAny()
                .map(Main::getValidFilePath)
                .orElse("./template.txt");

        if (FileUtils.exist(path)) {
            return FileUtils.read(path);
        } else {
            return FileUtils.readFromResources("template.txt");
        }
    }

    public static JsonNode readJson(String[] args) {
        final String path = Arrays.stream(args)
                .filter(argument -> argument.endsWith(".json"))
                .findAny()
                .map(Main::getValidFilePath)
                .orElse("./data.json");

        if (FileUtils.exist(path)) {
            return FileUtils.readJson(path);
        } else {
            return FileUtils.readJsonFromResources("data.json");
        }
    }

    private static String getValidFilePath(final String arg) {
        if (!FileUtils.exist(arg)) {
            throw new TemplateEngineException(FILE_NOT_FOUND, arg);
        }

        return arg;
    }
}