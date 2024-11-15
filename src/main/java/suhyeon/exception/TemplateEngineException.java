package suhyeon.exception;

import java.text.MessageFormat;

public class TemplateEngineException extends RuntimeException {
    public TemplateEngineException(final TemplateEngineExceptionType exceptionType) {
        super(exceptionType.getMessage());
    }

    public TemplateEngineException(final TemplateEngineExceptionType exceptionType, final Throwable cause) {
        super(exceptionType.getMessage() + " : " + cause.getMessage(), cause);
    }

    public TemplateEngineException(final TemplateEngineExceptionType exceptionType, final Object... args) {
        super(MessageFormat.format(exceptionType.getMessage(), args));
    }
}
