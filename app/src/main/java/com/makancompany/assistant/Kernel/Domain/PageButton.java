package com.makancompany.assistant.Kernel.Domain;


import com.makancompany.assistant.Kernel.Controller.Domain.BaseDomain;

public class PageButton extends BaseDomain {

    private String RowNumber;
    private String PageButtonId;
    private String PageButtonName;
    private String PageButtonTitle;
    private String PageButtonOrder;
    private String PageButtonGridName;
    private String PageButtonOpenWindow;
    private String PageButtonCallFunction;
    private String PageButtonParameter;
    private String DashboardTitleId;
    private String DashboardTitleName;
    private String DashboardTitleTableInfoId;
    private String DashboardTitleTableInfoPageAddress;
    private String DashboardTitleTableInfoName;
    private String PageButtonTypeId;
    private String PageButtonTypeName;
    private String DashboardTitleStartMessage;


    public PageButton() {
        setApiAddress("api/pgPageButton");
    }
    public PageButton(String PageButtonTitle, String PageButtonCallFunction){
        this.PageButtonCallFunction=PageButtonCallFunction;
        this.PageButtonTitle=PageButtonTitle;
    }

    public String getRowNumber() {
        return RowNumber;
    }

    public void setRowNumber(String rowNumber) {
        RowNumber = rowNumber;
    }

    public String getPageButtonId() {
        return PageButtonId;
    }

    public void setPageButtonId(String pageButtonId) {
        PageButtonId = pageButtonId;
    }

    public String getPageButtonName() {
        return PageButtonName;
    }

    public void setPageButtonName(String pageButtonName) {
        PageButtonName = pageButtonName;
    }

    public String getPageButtonTitle() {
        return PageButtonTitle;
    }

    public void setPageButtonTitle(String pageButtonTitle) {
        PageButtonTitle = pageButtonTitle;
    }

    public String getPageButtonOrder() {
        return PageButtonOrder;
    }

    public void setPageButtonOrder(String pageButtonOrder) {
        PageButtonOrder = pageButtonOrder;
    }

    public String getPageButtonGridName() {
        return PageButtonGridName;
    }

    public void setPageButtonGridName(String pageButtonGridName) {
        PageButtonGridName = pageButtonGridName;
    }

    public String getPageButtonOpenWindow() {
        return PageButtonOpenWindow;
    }

    public void setPageButtonOpenWindow(String pageButtonOpenWindow) {
        PageButtonOpenWindow = pageButtonOpenWindow;
    }

    public String getPageButtonCallFunction() {
        return PageButtonCallFunction;
    }

    public void setPageButtonCallFunction(String pageButtonCallFunction) {
        PageButtonCallFunction = pageButtonCallFunction;
    }

    public String getPageButtonParameter() {
        return PageButtonParameter;
    }

    public void setPageButtonParameter(String pageButtonParameter) {
        PageButtonParameter = pageButtonParameter;
    }

    public String getDashboardTitleId() {
        return DashboardTitleId;
    }

    public void setDashboardTitleId(String dashboardTitleId) {
        DashboardTitleId = dashboardTitleId;
    }

    public String getDashboardTitleName() {
        return DashboardTitleName;
    }

    public void setDashboardTitleName(String dashboardTitleName) {
        DashboardTitleName = dashboardTitleName;
    }

    public String getDashboardTitleTableInfoId() {
        return DashboardTitleTableInfoId;
    }

    public void setDashboardTitleTableInfoId(String dashboardTitleTableInfoId) {
        DashboardTitleTableInfoId = dashboardTitleTableInfoId;
    }

    public String getDashboardTitleTableInfoPageAddress() {
        return DashboardTitleTableInfoPageAddress;
    }

    public void setDashboardTitleTableInfoPageAddress(String dashboardTitleTableInfoPageAddress) {
        DashboardTitleTableInfoPageAddress = dashboardTitleTableInfoPageAddress;
    }

    public String getDashboardTitleTableInfoName() {
        return DashboardTitleTableInfoName;
    }

    public void setDashboardTitleTableInfoName(String dashboardTitleTableInfoName) {
        DashboardTitleTableInfoName = dashboardTitleTableInfoName;
    }

    public String getPageButtonTypeId() {
        return PageButtonTypeId;
    }

    public void setPageButtonTypeId(String pageButtonTypeId) {
        PageButtonTypeId = pageButtonTypeId;
    }

    public String getPageButtonTypeName() {
        return PageButtonTypeName;
    }

    public void setPageButtonTypeName(String pageButtonTypeName) {
        PageButtonTypeName = pageButtonTypeName;
    }

    public String getDashboardTitleStartMessage() {
        return DashboardTitleStartMessage;
    }

    public void setDashboardTitleStartMessage(String dashboardTitleStartMessage) {
        DashboardTitleStartMessage = dashboardTitleStartMessage;
    }
}
