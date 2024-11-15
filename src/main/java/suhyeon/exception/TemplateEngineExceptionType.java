package suhyeon.exception;

public enum TemplateEngineExceptionType {
    TEMPLATE_FILE_READ_ERROR("Error reading template file"),
    DATA_FILE_READ_ERROR("Error reading data file"),
    RESOURCE_NOT_FOUND("Resource not found: {0}"),
    FILE_WRITE_ERROR("Error writing to file"),
    FILE_NOT_FOUND("File not found: {0}"),
    INVALID_FOR_END_TOKEN_INDEX("Invalid for-end token index"),
    TOKEN_DIRECTIVE_NOT_NULL("Token directive is null"),
    DIRECTIVE_NOT_CLOSED("Directive is not closed: {0}"),
    FOR_LOOP_NOT_CLOSED("For loop is not closed: {0}"),
    KEY_NOT_NULL("Key cannot be null"),
    DATA_NOT_NULL("Data cannot be null"),
    FILE_SIZE_100MB_EXCEEDED("File size exceeded 100MB: {0}"),

    ;

    private final String message;

    TemplateEngineExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
