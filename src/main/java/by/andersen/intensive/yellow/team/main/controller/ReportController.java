package by.andersen.intensive.yellow.team.main.controller;

import by.andersen.intensive.yellow.team.main.controller.dto.ResponseReport;
import by.andersen.intensive.yellow.team.main.controller.http.HttpResponse;
import by.andersen.intensive.yellow.team.main.model.dto.ReportDTO;
import by.andersen.intensive.yellow.team.main.model.dto.UserDTO;
import by.andersen.intensive.yellow.team.main.model.entity.Report;
import by.andersen.intensive.yellow.team.main.service.impl.ReportService;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static by.andersen.intensive.yellow.team.main.service.exception.message.MessageConstant.REPORT_DELETED_SUCCESSFULLY;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = {"/report"})
public class ReportController {

    private ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/add")
    public ResponseEntity<ResponseReport> addReport(@RequestParam("username") String username,
                                            @RequestParam("reportTitle") String reportTitle,
                                            @RequestParam("reportBody") String reportBody,
                                            @RequestParam("laborCost") String laborCost) {
        laborCost = laborCost.replaceAll("\"", "");
        Report report = reportService.addUserReport(username, reportTitle, reportBody, Double.valueOf(laborCost));
        return new ResponseEntity<>(new ResponseReport(report), OK);
    }

    @GetMapping("/all/user/{username}")
    public ResponseEntity<List<ResponseReport>> getAllUserReports(@PathVariable("username") String username) {
        List<Report> reports = reportService.getAllReportsForSingleUser(username);
        return new ResponseEntity<>(convertReportsToResponseReportsList(reports), OK);
    }

    @GetMapping("/all/today/user/{username}")
    public ResponseEntity<List<ResponseReport>> getAllTodayUserReports(@PathVariable("username") String username) {
        List<Report> reports = reportService.getAllTodayReportsForSingleUser(username);
        return new ResponseEntity<>(convertReportsToResponseReportsList(reports), OK);
    }

    @GetMapping("/all/user/{username}/date/{date}")
    public ResponseEntity<List<ResponseReport>> getAllUserReportsByDate(@PathVariable("username") String username, @PathVariable("date") String date) {
        List<Report> reports = reportService.getAllReportsForSingleUserByDate(username, date);
        return new ResponseEntity<>(convertReportsToResponseReportsList(reports), OK);
    }

    @GetMapping("/map")
    public ResponseEntity<String> getReportsMap() {
        Map<UserDTO, List<ReportDTO>> reportsMap = reportService.getAllUsersReportsDtoMap();
        Gson gson = getGsonParser();
        String jsonResponse = gson.toJson(reportsMap);
        return new ResponseEntity<>(jsonResponse, OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResponseReport>> getAllReports() {
        List<Report> reports = reportService.getAllReports();
        return new ResponseEntity<>(convertReportsToResponseReportsList(reports), OK);
    }

    @PostMapping("/update")
    public ResponseEntity<ResponseReport> updateUserReport(@RequestParam("id") String id,
                                                   @RequestParam("reportTitle") String newReportTitle,
                                                   @RequestParam("reportBody") String newReportBody,
                                                   @RequestParam("laborCost") double newLaborCost) {
        id = id.replaceAll("\"", "");
        Report report = reportService.updateUserReport(Long.valueOf(id), newReportTitle, newReportBody, newLaborCost);
        return new ResponseEntity<>(new ResponseReport(report), OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpResponse> deleteUserReport(@PathVariable("id") long reportId) {
        Report report = reportService.deleteUserReport(reportId);
        return response(OK, String.format("%s : %s", REPORT_DELETED_SUCCESSFULLY, report.getReportTitle()));
    }

    private Gson getGsonParser() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.disableHtmlEscaping();
        gsonBuilder.enableComplexMapKeySerialization();
        Gson gson = gsonBuilder.create();
        return gson;
    }

    private List<ResponseReport> convertReportsToResponseReportsList(List<Report> reports) {
        List<ResponseReport> responseReports = new ArrayList<>();
        reports.forEach(report -> {
            responseReports.add(new ResponseReport(report));
        });
        return responseReports;
    }

    private ResponseEntity<HttpResponse> response(HttpStatus httpStatus, String message) {
        return new ResponseEntity<>(new HttpResponse(httpStatus.value(), httpStatus, httpStatus.getReasonPhrase().toUpperCase(), message.toUpperCase()), httpStatus);
    }

}
