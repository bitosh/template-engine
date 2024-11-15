package suhyeon.token;

import com.fasterxml.jackson.databind.JsonNode;
import suhyeon.DataContext;
import suhyeon.exception.TemplateEngineException;

import static suhyeon.exception.TemplateEngineExceptionType.TOKEN_DIRECTIVE_NOT_NULL;

public sealed abstract class Token permits ForToken, VariableToken, TextToken {
    protected final String directive;

    protected Token(final String directive) {
        if (directive == null) {
            throw new TemplateEngineException(TOKEN_DIRECTIVE_NOT_NULL);
        }
        this.directive = directive;
    }

    abstract public String interpret(final DataContext context);

    protected JsonNode findByPathWithWildcard(final JsonNode jsonNode, final String[] names) {
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
}
