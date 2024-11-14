package suhyeon.token;

import static suhyeon.token.TokenType.FOR;

public final class ForToken extends Token {
    private int forEndTokenIndex;

    public ForToken(String value) {
        super(FOR, value);
    }

    public int getEndForTokenIndex() {
        return forEndTokenIndex;
    }

    public void setForEndTokenIndex(int endTokenIndex) {
        this.forEndTokenIndex = endTokenIndex;
    }
}
