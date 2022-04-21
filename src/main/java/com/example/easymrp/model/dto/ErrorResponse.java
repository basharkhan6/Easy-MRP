package com.example.easymrp.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private Date timestamp;
    private Integer status;
    private String error;
    private String message;
    private List<Map> fields;
    private String cause;
    private String path;

    public ErrorResponse(HttpStatus status) {
        this.timestamp = new Date();
        this.status = status.value();
        this.error = status.getReasonPhrase();
    }

    public ErrorResponse(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

    public ErrorResponse(HttpStatus status, String message, String path) {
        this(status, message);
        this.path = path;
    }

    public ErrorResponse(HttpStatus status, String message, List<Map> fields, String path) {
        this(status, message, path);
        this.fields = fields;
    }

    public ErrorResponse(HttpStatus status, String message, String cause, String path) {
        this(status, message, path);
        this.cause = cause;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Map> getFields() {
        return fields;
    }

    public void setFields(List<Map> fields) {
        if (fields.size() > 0)
            this.fields = fields;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }
}

