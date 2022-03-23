package by.andersen.intensive.yellow.team.main.service.exception.impl.report;

import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;

public class ReportNotFoundException extends ServiceException {
    public ReportNotFoundException(String message) {
        super(message);
    }

    public ReportNotFoundException() {
    }
}
