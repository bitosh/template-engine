package suhyeon;

import com.fasterxml.jackson.databind.JsonNode;
import suhyeon.exception.TemplateEngineException;
import suhyeon.token.ForToken;
import suhyeon.token.Token;
import suhyeon.token.Tokenizer;
import suhyeon.utils.FileUtils;

import java.util.*;

import static suhyeon.exception.TemplateEngineExceptionType.FILE_NOT_FOUND;

public class TemplateProcessor {
    private final Map<String, JsonNode> context;

    public TemplateProcessor(Map<String, JsonNode> context) {
        this.context = context;
    }

    public String process(final List<Token> tokens) {
        return processTokens(tokens, 0, tokens.size());
    }

    private String processTokens(final List<Token> tokens, final Integer start, final Integer end) {
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
                        outputBuilder.append(processTokens(tokens, i + 1, forToken.getEndForTokenIndex() + 1));
                        context.remove(variable);
                    }
                    i = forToken.getEndForTokenIndex();
                }
                case VARIABLE -> outputBuilder.append(replaceVariableToken(token));
            }
        }
        return outputBuilder.toString();
    }

    private String replaceVariableToken(final Token token) {
        final String[] names = token.getValue().replace("<?=", "").replace("?>", "").trim().split("\\.");
        return Optional.ofNullable(context.get(names[0]))
                .map(jsonNode -> findByPathWithWildcard(jsonNode, names))
                .map(JsonNode::asText)
                .orElse("");
    }

    private List<JsonNode> findJsonNodesForLoop(final String arrayName) {
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

    private JsonNode findByPathWithWildcard(final JsonNode jsonNode, final String[] names) {
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

    private String read(String[] args) {
        final String path = Arrays.stream(args)
                .filter(argument -> argument.endsWith(".txt"))
                .findAny()
                .map(this::getValidFilePath)
                .orElse("./template.txt");

        if (FileUtils.exist(path)) {
            return FileUtils.read(path);
        } else {
            return FileUtils.readFromResources("template.txt");
        }
    }

    public JsonNode readJson(String[] args) {
        final String path = Arrays.stream(args)
                .filter(argument -> argument.endsWith(".json"))
                .findAny()
                .map(this::getValidFilePath)
                .orElse("./data.json");

        if (FileUtils.exist(path)) {
            return FileUtils.readJson(path);
        } else {
            return FileUtils.readJsonFromResources("data.json");
        }
    }

    private String getValidFilePath(final String arg) {
        if (!FileUtils.exist(arg)) {
            throw new TemplateEngineException(FILE_NOT_FOUND, arg);
        }

        return arg;
    }
}
