package suhyeon.token;

import com.fasterxml.jackson.databind.JsonNode;
import suhyeon.DataContext;

import java.util.ArrayList;
import java.util.List;

import static suhyeon.token.Tokenizer.END_DIRECTIVE;
import static suhyeon.token.Tokenizer.LOOP_DIRECTIVE;

public final class ForToken extends Token {
    private final List<Token> child;

    public ForToken(final String directive, final List<Token> child) {
        super(directive);
        this.child = child;
    }

    @Override
    public String interpret(final DataContext context) {
        StringBuilder result = new StringBuilder();
        final String[] forDirective = this.directive.substring(this.directive.indexOf(LOOP_DIRECTIVE), this.directive.indexOf(END_DIRECTIVE))
                .split(" ");
        final String variable = forDirective[1];
        final List<JsonNode> loopData = findDataForLoop(forDirective[3], context);
        for (JsonNode jsonNode : loopData) {
            context.add(variable, jsonNode);
            for (Token token : this.child) {
                result.append(token.interpret(context));
            }
            context.remove(variable);
        }
        return result.toString();
    }

    private List<JsonNode> findDataForLoop(final String dataName, final DataContext context) {
        final String[] names = dataName.split("\\.");
        final JsonNode root = context.get(names[0]);
        final List<JsonNode> result = new ArrayList<>();

        if (root.isArray()) {
            root.iterator().forEachRemaining(jsonNode -> result.add(findByPathWithWildcard(jsonNode, names)));
        } else {
            result.add(findByPathWithWildcard(root, names));
        }

        return result;
    }
}
