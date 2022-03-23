package by.andersen.intensive.yellow.team.main.repository;

import by.andersen.intensive.yellow.team.main.model.entity.Report;
import by.andersen.intensive.yellow.team.main.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReportRepositoryTest {

    @Autowired
    ReportRepository reportRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    public void findAllReportsTest() {
        List<Report> reports = reportRepository.findAll();
        reports.forEach(report -> System.out.println(report));
    }

    @Test
    public void saveReportTest() {
        User user = userRepository.findUserByUsername("alexey-bakulin404");
        Report report = new Report("test spring boot", "test user reports save", user, 10);
        Report reportSaved = reportRepository.save(report);
        System.out.println(reportSaved);
    }

    @Test
    public void deleteReportTest() {
        String title = "test spring boot";
        Report report = reportRepository.findReportByReportTitle(title);
        System.out.println(report);
        reportRepository.deleteById(report.getId());
    }

}
