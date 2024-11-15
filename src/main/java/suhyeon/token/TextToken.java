package suhyeon.token;

import suhyeon.DataContext;

public final class TextToken extends Token {
    public TextToken(final String directive) {
        super(directive);
    }

    @Override
    public String interpret(final DataContext context) {
        return this.directive;
    }
}
