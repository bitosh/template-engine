package suhyeon.token;

import static suhyeon.token.TokenType.END_FOR;

public final class EndForToken extends Token {
    public EndForToken(String value) {
        super(END_FOR, value);
    }
}
