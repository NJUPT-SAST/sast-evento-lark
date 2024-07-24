package fun.sast.evento.lark.infrastructure.error;

public class GlobalRuntimeException extends RuntimeException {
    public GlobalRuntimeException(String message) {
        super(message);
    }
}
