package suhyeon.exception;

import java.text.MessageFormat;

public class TemplateEngineException extends RuntimeException {
    public TemplateEngineException(TemplateEngineExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }

    public TemplateEngineException(TemplateEngineExceptionType exceptionType, Throwable cause) {
        super(exceptionType.getMessage() + " : " + cause.getMessage(), cause);
    }

    public TemplateEngineException(TemplateEngineExceptionType exceptionType, Object... args) {
        super(MessageFormat.format(exceptionType.getMessage(), args));
    }
}
