package by.andersen.intensive.yellow.team.main.service.exception.impl.user;

import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;

public class NotAnImageFileException extends ServiceException {
    public NotAnImageFileException(String message) {
        super(message);
    }

    public NotAnImageFileException() {
    }
}
