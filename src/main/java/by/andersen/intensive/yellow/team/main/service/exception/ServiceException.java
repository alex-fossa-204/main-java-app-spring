package by.andersen.intensive.yellow.team.main.service.exception;

public class ServiceException extends Exception {
    public ServiceException() {
        super();
    }

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(Exception exception) {
        super(exception);
    }
}
