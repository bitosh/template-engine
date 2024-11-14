package suhyeon.token;

import static suhyeon.token.TokenType.TEXT;

public final class TextToken extends Token {
    public TextToken(String value) {
        super(TEXT, value);
    }
}
