package suhyeon.token;

import com.fasterxml.jackson.databind.JsonNode;
import suhyeon.DataContext;

import java.util.Optional;

public final class VariableToken extends Token {
    public VariableToken(String value) {
        super(value);
    }

    @Override
    public String interpret(final DataContext context) {
        final String[] names = this.directive.replaceAll("<\\?=|\\?>", "").trim().split("\\.");
        return Optional.ofNullable(context.get(names[0]))
                .map(jsonNode -> super.findByPathWithWildcard(jsonNode, names))
                .map(JsonNode::asText)
                .orElse("");
    }
}
