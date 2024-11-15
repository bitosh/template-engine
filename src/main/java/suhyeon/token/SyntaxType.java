package suhyeon.token;

public enum SyntaxType {
    START_DIRECTIVE("<?"),
    END_DIRECTIVE("?>"),
    LOOP_DIRECTIVE("for"),
    VARIABLE_START_DIRECTIVE("<?="),
    LOOP_START_DIRECTIVE(START_DIRECTIVE.syntax + " " + LOOP_DIRECTIVE.syntax), // <? for
    LOOP_END_DIRECTIVE(START_DIRECTIVE + " endfor " + END_DIRECTIVE), // <? endfor ?>
    ;

    private final String syntax;

    SyntaxType(final String syntax) {
        this.syntax = syntax;
    }

    public String getSyntax() {
        return syntax;
    }
}
