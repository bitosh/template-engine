package suhyeon.exception;

public enum TemplateEngineExceptionType {
    TEMPLATE_FILE_READ_ERROR("템플릿 파일 읽기 오류"),
    DATA_FILE_READ_ERROR("데이터 파일 읽기 오류"),
    RESOURCE_NOT_FOUND("리소스를 찾을 수 없음 {0}"),
    FILE_WRITE_ERROR("파일 쓰기 오류"),
    FILE_NOT_FOUND("파일을 찾을 수 없음 {0}"),
    INVALID_FOR_END_TOKEN_INDEX("for end 토큰 인덱스가 유효하지 않음"),
    NULL_TOKEN_TYPE("토큰 타입이 null임"),
    NULL_TOKEN_DIRECTIVE("토큰 지시자가 null임"),
    INVALID_DIRECTIVE("지시자가 닫히지 않음 {0}"),
    FOR_LOOP_NOT_CLOSED("for 루프가 닫히지 않음 {0}"),
    ;

    private final String message;

    TemplateEngineExceptionType(final String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
