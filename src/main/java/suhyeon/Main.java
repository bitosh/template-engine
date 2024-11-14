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
    static Map<String, JsonNode> context = new HashMap<>();

    public static void main(String[] args) {
        final JsonNode jsonNode = readJson(args);
        final String template = read(args);
        final List<Token> tokens = Tokenizer.tokenize(template);

        context.put("USERS", jsonNode);

        FileUtils.write("./output.txt", parsing(tokens, 0, tokens.size()));
    }

    private static String parsing(final List<Token> tokens, final Integer start, final Integer end) {
        final StringBuilder outputBuilder = new StringBuilder();
        for (int i = start; i < end; i++) {
            final Token token = tokens.get(i);
            final String tokenValue = token.getValue();

            switch (token.getType()) {
                case TEXT -> outputBuilder.append(token.getValue());
                case FOR -> {
                    final ForToken forToken = (ForToken) token;
                    final String[] forContext = tokenValue.substring(tokenValue.indexOf("for"), tokenValue.indexOf("?>"))
                            .split(" ");
                    final String variable = forContext[1];
                    final List<JsonNode> array = findJsonNodesForLoop(forContext[3]);
                    for (JsonNode jsonNode : array) {
                        context.put(variable, jsonNode);
                        outputBuilder.append(parsing(tokens, i + 1, forToken.getEndForTokenIndex() + 1));
                        context.remove(variable);
                    }
                    i = forToken.getEndForTokenIndex();
                }
                case VARIABLE -> outputBuilder.append(replaceVariableToken(token));
            }
        }
        return outputBuilder.toString();
    }

    private static String replaceVariableToken(final Token token) {
        final String[] names = token.getValue().replace("<?=", "").replace("?>", "").trim().split("\\.");
        return Optional.ofNullable(context.get(names[0]))
                .map(jsonNode -> findByPathWithWildcard(jsonNode, names))
                .map(JsonNode::asText)
                .orElse("");
    }

    private static List<JsonNode> findJsonNodesForLoop(final String arrayName) {
        final String[] names = arrayName.split("\\.");
        final JsonNode root = context.get(names[0]);
        final List<JsonNode> result = new ArrayList<>();

        if (root.isArray()) {
            root.iterator().forEachRemaining(jsonNode -> result.add(findByPathWithWildcard(jsonNode, names)));
        } else {
            result.add(findByPathWithWildcard(root, names));
        }

        return result;
    }

    private static JsonNode findByPathWithWildcard(final JsonNode jsonNode, final String[] names) {
        boolean isBeforeAsterisk = false;
        JsonNode result = jsonNode;

        for (int i = 1; i < names.length; i++) {
            final String name = names[i];

            if (name.equals("*")) {
                isBeforeAsterisk = true;
                continue;
            }

            if (isBeforeAsterisk) {
                result = result.findValue(name);
            } else {
                result = result.get(name);
            }

            isBeforeAsterisk = false;
        }

        return result;
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