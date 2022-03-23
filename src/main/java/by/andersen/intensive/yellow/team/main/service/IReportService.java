package by.andersen.intensive.yellow.team.main.service;

import by.andersen.intensive.yellow.team.main.model.dto.ReportDTO;
import by.andersen.intensive.yellow.team.main.model.dto.UserDTO;
import by.andersen.intensive.yellow.team.main.model.entity.Report;
import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IReportService {

    List<Report> getAllReports() throws ServiceException;

    List<Report> getAllReportsForSingleUser(String username) throws ServiceException;

    List<Report> getAllReportsForSingleUserByDate(String username, String date) throws ServiceException, ParseException;

    List<Report> getAllTodayReportsForSingleUser(String username) throws ServiceException;

    Report getReportByTitle(String reportTitle) throws ServiceException;

    Report getUserReportById(String username, long reportId) throws ServiceException;

    Report addUserReport(String username, String reportTitle, String reportBody, double laborCost) throws ServiceException;

    Report updateUserReport(long reportId, String newReportTitle, String newReportBody, double newLaborCost) throws ServiceException;

    Report deleteUserReport(long reportId) throws ServiceException;

    Map<UserDTO, List<ReportDTO>> getAllUsersReportsDtoMap() throws ServiceException, ParseException;

}
