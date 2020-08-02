package com.makancompany.assistant.Kernel.Controller.Domain;

public class ApiResponse {
    public Integer Count;
    public Object Data;
    public Object ExtraResult;
    public String ColumnHeader;
    public String Message;
    public Boolean IsError;
    public String DownloadLink;


    public Integer getCount() {
        return Count;
    }

    public void setCount(Integer count) {
        Count = count;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object data) {
        Data = data;
    }

    public Object getExtraResult() {
        return ExtraResult;
    }

    public void setExtraResult(Object extraResult) {
        ExtraResult = extraResult;
    }

    public String getColumnHeader() {
        return ColumnHeader;
    }

    public void setColumnHeader(String columnHeader) {
        ColumnHeader = columnHeader;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public Boolean getError() {
        return IsError;
    }

    public void setError(Boolean error) {
        IsError = error;
    }

    public String getDownloadLink() {
        return DownloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        DownloadLink = downloadLink;
    }
}
