package dojo.exception;

public class GameAlreadyCompleteException extends RuntimeException {
    public GameAlreadyCompleteException(String message) {
        super(message);
    }
}