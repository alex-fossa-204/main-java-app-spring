package by.andersen.intensive.yellow.team.main.service.impl;

import by.andersen.intensive.yellow.team.main.model.dto.ReportDTO;
import by.andersen.intensive.yellow.team.main.model.dto.UserDTO;
import by.andersen.intensive.yellow.team.main.model.entity.Report;
import by.andersen.intensive.yellow.team.main.model.entity.User;
import by.andersen.intensive.yellow.team.main.repository.ReportRepository;
import by.andersen.intensive.yellow.team.main.repository.UserRepository;
import by.andersen.intensive.yellow.team.main.service.IReportService;
import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.UserNotFoundException;
import by.andersen.intensive.yellow.team.main.service.exception.impl.user.UsernameExistException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static by.andersen.intensive.yellow.team.main.service.exception.message.MessageConstant.USERNAME_ALREADY_EXISTS;
import static by.andersen.intensive.yellow.team.main.service.exception.message.MessageConstant.USER_NOT_FOUND;

@Service
@Transactional
public class ReportService implements IReportService {

    private static final String EMPTY_REPORT_TITLE = "Empty Report Title";

    private ReportRepository reportRepository;

    private UserRepository userRepository;

    public ReportService(ReportRepository reportRepository, UserRepository userRepository) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Report> getAllReports() {
        return reportRepository.findAll();
    }

    @Override
    public List<Report> getAllReportsForSingleUser(String username) {
        User user = userRepository.findUserByUsername(username);
        return new ArrayList<>(user.getReports());
    }

    @Override
    public List<Report> getAllReportsForSingleUserByDate(String username, String date) {
        User user = userRepository.findUserByUsername(username);
        return user.getReports().stream()
                .filter(report -> {
                    String reportDateFormatted = convertDate(report.getReportDate());
                    return reportDateFormatted.equalsIgnoreCase(date);
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Report> getAllTodayReportsForSingleUser(String username) {
        return getAllReportsForSingleUserByDate(username, convertDate(new Date()));
    }

    @Override
    public Report getReportByTitle(String reportTitle) throws ServiceException {
        if (reportTitle == null) {
            throw new ServiceException(EMPTY_REPORT_TITLE);
        }
        return reportRepository.findReportByReportTitle(reportTitle);
    }

    @Override
    public Report getUserReportById(String username, long reportId) throws UserNotFoundException, UsernameExistException {
        validateUsername(username, null);
        User user = userRepository.findUserByUsername(username);
        Set<Report> reports = user.getReports();
        Optional<Report> result = reports.stream().filter(report -> report.getId() == reportId).findFirst();
        return result.get();
    }

    @Override
    public Report addUserReport(String username, String reportTitle, String reportBody, double laborCost) {
        User user = userRepository.findUserByUsername(username);
        Report newReport = new Report(reportTitle, reportBody, user, laborCost);
        newReport.setReportDate(new Date());
        Report reportSaved = reportRepository.save(newReport);
        return reportSaved;
    }

    @Override
    public Report updateUserReport(long reportId, String newReportTitle, String newReportBody, double newLaborCost) {
        Report report = reportRepository.findById(reportId).get();
        report.setReportTitle(newReportTitle);
        report.setReportBody(newReportBody);
        report.setLaborCost(newLaborCost);
        return reportRepository.save(report);
    }

    @Override
    public Report deleteUserReport(long reportId) {
        Optional<Report> reportOptional = reportRepository.findById(reportId);
        Report report = reportOptional.get();
        reportRepository.deleteById(reportId);
        return report;
    }

    @Override
    public Map<UserDTO, List<ReportDTO>> getAllUsersReportsDtoMap() {
        Map<UserDTO, List<ReportDTO>> reportsMap = new HashMap<>();
        String date = convertDate(new Date());

        userRepository.findAll().forEach(user -> {

            List<Report> reportList = user.getReports().stream()
                    .filter(report -> {
                        String reportDateFormatted = convertDate(report.getReportDate());
                        return reportDateFormatted.equalsIgnoreCase(date);
                    })
                    .collect(Collectors.toList());

            List<ReportDTO> reportListDTO = new ArrayList<>();
            reportList.forEach(report -> {
                reportListDTO.add(new ReportDTO(report.getReportTitle(), report.getReportBody(), report.getLaborCost()));
            });

            reportsMap.put(new UserDTO(user.getFirstName(), user.getLastName()), reportListDTO);
        });
        return reportsMap;
    }

    private String convertDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    private User validateUsername(String currentUsername, String newUsername) throws UserNotFoundException, UsernameExistException {
        User currentUser = userRepository.findUserByUsername(currentUsername);
        User newUser = userRepository.findUserByUsername(newUsername);
        User result = null;
        boolean isNotBlank = StringUtils.isNotBlank(currentUsername);
        if (isNotBlank) {
            if (currentUser == null) {
                throw new UserNotFoundException(String.format("%s. username = %s", USER_NOT_FOUND, currentUsername));
            }
            if (newUser != null && currentUser.getUsername().equals(newUser.getUsername())) {
                throw new UsernameExistException(String.format("%s. username = %s", USERNAME_ALREADY_EXISTS, currentUsername));
            }
            result = currentUser;
        }
        if (!isNotBlank) {
            if (newUser != null) {
                throw new UsernameExistException(String.format("%s. username = %s", USERNAME_ALREADY_EXISTS, currentUsername));
            }
        }
        return result;
    }
}
