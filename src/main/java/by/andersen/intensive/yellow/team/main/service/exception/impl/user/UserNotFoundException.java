package by.andersen.intensive.yellow.team.main.service.exception.impl.user;

import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;

public class UserNotFoundException extends ServiceException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException() {}
}
