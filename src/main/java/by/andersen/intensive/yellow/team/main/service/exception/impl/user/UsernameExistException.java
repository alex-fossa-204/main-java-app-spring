package by.andersen.intensive.yellow.team.main.service.exception.impl.user;

import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;

public class UsernameExistException extends ServiceException {
    public UsernameExistException(String message) {
        super(message);
    }

    public UsernameExistException() {
    }
}
