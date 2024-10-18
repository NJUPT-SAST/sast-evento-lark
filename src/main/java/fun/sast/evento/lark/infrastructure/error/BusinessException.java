package fun.sast.evento.lark.infrastructure.error;


public class BusinessException extends GlobalRuntimeException {
    private final ErrorEnum error;

    public ErrorEnum error() {
        return error;
    }

    public BusinessException(ErrorEnum error, String message) {
        super(message);
        this.error = error;
    }

    public BusinessException(String message) {
        super(message);
        this.error = ErrorEnum.DEFAULT;
    }
}
