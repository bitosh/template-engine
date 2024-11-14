package suhyeon.exception;

public enum TemplateEngineExceptionType {
    TEMPLATE_FILE_READ_ERROR("템플릿 파일 읽기 오류"),
    DATA_FILE_READ_ERROR("데이터 파일 읽기 오류"),
    RESOURCE_NOT_FOUND("리소스를 찾을 수 없음 {0}"),
    FILE_WRITE_ERROR("파일 쓰기 오류"),
    FILE_NOT_FOUND("파일을 찾을 수 없음 {0}"),
    ;

    private final String message;

    TemplateEngineExceptionType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
