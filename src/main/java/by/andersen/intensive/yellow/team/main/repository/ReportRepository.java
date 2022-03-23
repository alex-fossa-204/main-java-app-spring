package by.andersen.intensive.yellow.team.main.repository;

import by.andersen.intensive.yellow.team.main.model.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {

    Report findReportByReportTitle(String title);

}
