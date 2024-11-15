package suhyeon.token;

import com.fasterxml.jackson.databind.JsonNode;
import suhyeon.DataContext;

import java.util.Optional;

public final class VariableToken extends Token {
    private static final String VARIABLE_DIRECTIVE_PATTERN = "<\\?=|\\?>";

    public VariableToken(String value) {
        super(value);
    }

    @Override
    public String interpret(final DataContext context) {
        final String[] names = this.directive.replaceAll(VARIABLE_DIRECTIVE_PATTERN, "").trim()
                .split("\\.");

        if(names.length == 0) {
            return "";
        }

        return Optional.ofNullable(context.get(names[0]))
                .map(jsonNode -> super.findByPathWithWildcard(jsonNode, names))
                .map(JsonNode::asText)
                .orElse("");
    }
}
