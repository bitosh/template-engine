package suhyeon.token;

import static suhyeon.token.TokenType.VARIABLE;

public final class VariableToken extends Token {
    public VariableToken(String value) {
        super(VARIABLE, value);
    }
}
