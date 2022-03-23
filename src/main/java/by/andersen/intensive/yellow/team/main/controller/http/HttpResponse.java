package by.andersen.intensive.yellow.team.main.controller.http;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.Objects;
import java.util.StringJoiner;

public class HttpResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy hh:mm:ss", timezone = "Europe/Minsk")
    private Date timeStamp;
    private int httpStatusCode;
    private HttpStatus httpStatus;
    private String reason;
    private String message;

    public HttpResponse(int httpStatusCode, HttpStatus httpStatus, String reason, String message) {
        this.timeStamp = new Date();
        this.httpStatusCode = httpStatusCode;
        this.httpStatus = httpStatus;
        this.reason = reason;
        this.message = message;
    }

    public HttpResponse() {
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpResponse that = (HttpResponse) o;
        return httpStatusCode == that.httpStatusCode && Objects.equals(timeStamp, that.timeStamp) && httpStatus == that.httpStatus && Objects.equals(reason, that.reason) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timeStamp, httpStatusCode, httpStatus, reason, message);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", HttpResponse.class.getSimpleName() + "[", "]")
                .add("timeStamp=" + timeStamp)
                .add("httpStatusCode=" + httpStatusCode)
                .add("httpStatus=" + httpStatus)
                .add("reason='" + reason + "'")
                .add("message='" + message + "'")
                .toString();
    }
}
