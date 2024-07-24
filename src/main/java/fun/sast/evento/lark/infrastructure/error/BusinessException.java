package fun.sast.evento.lark.infrastructure.error;


public class BusinessException extends GlobalRuntimeException {
    private final ErrorEnum error;

    public ErrorEnum error() {
        return error;
    }

    public BusinessException(String message, ErrorEnum error) {
        super(message);
        this.error = error;
    }

    private BusinessException(String message) {
        super(message);
        this.error = ErrorEnum.Default;
    }

}
