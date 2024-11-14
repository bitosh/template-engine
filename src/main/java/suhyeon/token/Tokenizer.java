package suhyeon.token;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Tokenizer {
    private static final int DIRECTIVE_END_OFFSET = 2;

    public static List<Token> tokenize(final String template) {
        final List<Token> tokens = new ArrayList<>();
        final int templateLength = template.length();
        final Stack<ForToken> forTokens = new Stack<>();

        int index = 0;

        while (index < templateLength) {
            if (isVariableDirective(template, index)) {
                final int endIndex = findDirectiveEndIndex(template, index);

                tokens.add(new VariableToken(template.substring(index, endIndex)));
                index = endIndex;
            } else if (isForLoopDirective(template, index)) {
                final int end = findDirectiveEndIndex(template, index);
                final ForToken forToken = new ForToken(template.substring(index, end));

                tokens.add(forToken);
                forTokens.add(forToken);
                index = end;
            } else if (isEndForLoopDirective(template, index)) {
                final int endIndex = findDirectiveEndIndex(template, index);
                final ForToken forToken = forTokens.pop();

                forToken.setForEndTokenIndex(tokens.size() - 1);
                tokens.add(new EndForToken(template.substring(index, endIndex)));
                index = endIndex;
            } else {
                int nextToken = template.indexOf("<?", index);

                if (nextToken == -1) {
                    nextToken = templateLength;
                }

                tokens.add(new TextToken(template.substring(index, nextToken)));
                index = nextToken;
            }
        }

        return tokens;
    }

    private static boolean isVariableDirective(final String template, final Integer index) {
        return template.startsWith("<?=", index);
    }

    private static boolean isForLoopDirective(final String template, final Integer index) {
        return template.startsWith("<? for", index) || template.startsWith("<?for", index);
    }

    private static boolean isEndForLoopDirective(final String template, final Integer index) {
        return template.startsWith("<? endfor", index) || template.startsWith("<?endfor", index);
    }

    private static int findDirectiveEndIndex(final String template, final Integer index) {
        return template.indexOf("?>", index) + DIRECTIVE_END_OFFSET;
    }
}
