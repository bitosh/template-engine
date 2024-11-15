package suhyeon.token;

import suhyeon.exception.TemplateEngineException;

import java.util.ArrayList;
import java.util.List;

import static suhyeon.exception.TemplateEngineExceptionType.FOR_LOOP_NOT_CLOSED;
import static suhyeon.exception.TemplateEngineExceptionType.DIRECTIVE_NOT_CLOSED;

public class Tokenizer {
    public static final String START_DIRECTIVE = "<?";
    public static final String END_DIRECTIVE = "?>";
    public static final String LOOP_DIRECTIVE = "for";
    public static final String VARIABLE_START_DIRECTIVE = "<?=";
    public static final String FOR_START_DIRECTIVE = "<? for";
    public static final String END_FOR_DIRECTIVE = "<? endfor ?>";

    public static List<Token> tokenize(final String template) {
        if (template == null) {
            return List.of();
        }

        final List<Token> tokens = new ArrayList<>();
        final int templateLength = template.length();

        int index = 0;

        while (index < templateLength) {
            if (isVariableDirective(template, index)) {
                final int endIndex = findDirectiveEndIndex(template, index);

                tokens.add(new VariableToken(template.substring(index, endIndex)));
                index = endIndex;
            } else if (isForLoopDirective(template, index)) {
                final int end = findDirectiveEndIndex(template, index);
                final int endForTokenIndex = findDirectiveEndForIndex(template, end);
                final String forInTemplate = template.substring(end, endForTokenIndex);
                final List<Token> childTokens = tokenize(forInTemplate);

                tokens.add(new ForToken(template.substring(index, end), childTokens));
                index = endForTokenIndex + END_FOR_DIRECTIVE.length();
            } else {
                int nextTokenIndex = template.indexOf(START_DIRECTIVE, index);

                if (nextTokenIndex == -1) {
                    nextTokenIndex = templateLength;
                }

                tokens.add(new TextToken(template.substring(index, nextTokenIndex)));
                index = nextTokenIndex;
            }
        }

        return tokens;
    }

    private static boolean isVariableDirective(final String template, final int index) {
        return template.startsWith(VARIABLE_START_DIRECTIVE, index);
    }

    private static boolean isForLoopDirective(final String template, final int index) {
        return template.startsWith(FOR_START_DIRECTIVE, index);
    }

    private static boolean isEndForLoopDirective(final String template, final int index) {
        return template.startsWith(END_FOR_DIRECTIVE, index);
    }

    private static int findDirectiveEndForIndex(final String template, final int startIndex) {
        int index = startIndex;
        int nestingLevel = 1;
        final int templateLength = template.length();

        while (index < templateLength) {
            int nextDirectiveIndex = template.indexOf(START_DIRECTIVE, index);
            if (nextDirectiveIndex == -1) {
                throw new TemplateEngineException(FOR_LOOP_NOT_CLOSED, template);
            }

            index = nextDirectiveIndex;

            if (isForLoopDirective(template, index)) {
                index = findDirectiveEndIndex(template, index);
                nestingLevel++;
            } else if (isEndForLoopDirective(template, index)) {
                if (--nestingLevel == 0) {
                    return index;
                } else {
                    index += END_FOR_DIRECTIVE.length();
                }
            } else {
                index++;
            }
        }

        throw new TemplateEngineException(FOR_LOOP_NOT_CLOSED, template);
    }

    private static int findDirectiveEndIndex(final String template, final Integer index) {
        final int endIndex = template.indexOf(END_DIRECTIVE, index);

        if (endIndex == -1) {
            throw new TemplateEngineException(DIRECTIVE_NOT_CLOSED);
        }

        return endIndex + END_DIRECTIVE.length();
    }
}
