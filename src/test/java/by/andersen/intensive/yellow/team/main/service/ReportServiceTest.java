package by.andersen.intensive.yellow.team.main.service;

import by.andersen.intensive.yellow.team.main.model.dto.ReportDTO;
import by.andersen.intensive.yellow.team.main.model.dto.UserDTO;
import by.andersen.intensive.yellow.team.main.model.entity.Report;
import by.andersen.intensive.yellow.team.main.service.exception.ServiceException;
import by.andersen.intensive.yellow.team.main.service.impl.ReportService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@SpringBootTest
public class ReportServiceTest {

    @Autowired
    ReportService reportService;

    @Test
    public void getUserReportsByDate() throws ServiceException, ParseException {
        Calendar calendar = new GregorianCalendar(2022, Calendar.MARCH, 12);
        String currentDate = convertDate(calendar.getTime());

        List<Report> userReports = reportService.getAllReportsForSingleUserByDate("alexey-bakulin500", currentDate);
        System.out.println("Current Date: " + currentDate);
        userReports.forEach(report -> System.out.println(report));
    }

    @Test
    public void getUserReportsToday() throws ServiceException, ParseException {
        List<Report> userReports = reportService.getAllTodayReportsForSingleUser("alexey-bakulin500");
        userReports.forEach(report -> System.out.println(report));
    }

    private String convertDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }

    @Test
    public void getUserReportsMap() throws ServiceException, ParseException {
        Map<UserDTO, List<ReportDTO>> allReports = reportService.getAllUsersReportsDtoMap();
        for (Map.Entry<UserDTO, List<ReportDTO>> entry : allReports.entrySet()) {
            System.out.println(entry.getKey() + " - " + entry.getValue());
        }
    }

    @Test
    public void getAllUserReports() throws ServiceException {
        List<Report> userReports = reportService.getAllReportsForSingleUser("alexey-bakulin404");
        userReports.forEach(report -> System.out.println(report));
    }

}
