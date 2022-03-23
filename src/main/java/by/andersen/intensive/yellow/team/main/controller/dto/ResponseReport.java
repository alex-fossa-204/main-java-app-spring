package by.andersen.intensive.yellow.team.main.controller.dto;

import by.andersen.intensive.yellow.team.main.model.entity.Report;

import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

public class ResponseReport {

    private long id;

    private String reportTitle;

    private String reportBody;

    private Date reportDate;

    private String reportedBy;

    private double laborCost;

    public ResponseReport(Report report) {
        this.id = report.getId();
        this.reportTitle = report.getReportTitle();
        this.reportBody = report.getReportBody();
        this.reportDate = report.getReportDate();
        this.reportedBy = report.getReportedBy().getUsername();
        this.laborCost = report.getLaborCost();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReportTitle() {
        return reportTitle;
    }

    public void setReportTitle(String reportTitle) {
        this.reportTitle = reportTitle;
    }

    public String getReportBody() {
        return reportBody;
    }

    public void setReportBody(String reportBody) {
        this.reportBody = reportBody;
    }

    public Date getReportDate() {
        return reportDate;
    }

    public void setReportDate(Date reportDate) {
        this.reportDate = reportDate;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(String reportedBy) {
        this.reportedBy = reportedBy;
    }

    public double getLaborCost() {
        return laborCost;
    }

    public void setLaborCost(double laborCost) {
        this.laborCost = laborCost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseReport that = (ResponseReport) o;
        return id == that.id && Double.compare(that.laborCost, laborCost) == 0 && Objects.equals(reportTitle, that.reportTitle) && Objects.equals(reportBody, that.reportBody) && Objects.equals(reportDate, that.reportDate) && Objects.equals(reportedBy, that.reportedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reportTitle, reportBody, reportDate, reportedBy, laborCost);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", ResponseReport.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .add("reportTitle='" + reportTitle + "'")
                .add("reportBody='" + reportBody + "'")
                .add("reportDate='" + reportDate + "'")
                .add("reportedBy='" + reportedBy + "'")
                .add("laborCost=" + laborCost)
                .toString();
    }
}
