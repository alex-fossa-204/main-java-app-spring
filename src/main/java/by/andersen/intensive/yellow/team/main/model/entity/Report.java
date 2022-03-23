package by.andersen.intensive.yellow.team.main.model.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(name = "reports")
public class Report implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private long id;

    @Column(name = "title")
    private String reportTitle;

    @Column(name = "body")
    private String reportBody;

    @Column(name = "date")
    private Date reportDate;

    @ManyToOne
    @JoinColumn(name = "reported_by")
    private User reportedBy;

    @Column(name = "labor_costs")
    private double laborCost;

    public Report(String reportTitle, String reportBody, Date reportDate, double laborCost) {
        super();
        this.reportTitle = reportTitle;
        this.reportBody = reportBody;
        this.reportDate = reportDate;
        this.laborCost = laborCost;
    }

    public Report(String reportTitle, String reportBody, User reportedBy, double laborCost) {
        super();
        this.reportTitle = reportTitle;
        this.reportBody = reportBody;
        this.reportedBy = reportedBy;
        this.laborCost = laborCost;
    }

    public Report() {
        super();
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

    public User getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(User reportedBy) {
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
        Report report = (Report) o;
        return laborCost == report.laborCost && Objects.equals(reportTitle, report.reportTitle) && Objects.equals(reportBody, report.reportBody) && Objects.equals(reportDate, report.reportDate) && Objects.equals(reportedBy, report.reportedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reportTitle, reportBody, reportDate, reportedBy, laborCost);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Report.class.getSimpleName() + "[", "]")
                .add("reportTitle='" + reportTitle + "'")
                .add("reportBody='" + reportBody + "'")
                .add("reportDate=" + reportDate)
                .add("reportedBy=" + reportedBy.getUsername())
                .add("laborCost=" + laborCost)
                .toString();
    }
}
