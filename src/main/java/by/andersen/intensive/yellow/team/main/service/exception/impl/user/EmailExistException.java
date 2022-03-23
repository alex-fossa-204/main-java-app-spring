package by.andersen.intensive.yellow.team.main.service.exception.impl.user;

import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;

public class EmailExistException extends ServiceException {
    public EmailExistException(String message) {
        super(message);
    }

    public EmailExistException() {
    }
}
