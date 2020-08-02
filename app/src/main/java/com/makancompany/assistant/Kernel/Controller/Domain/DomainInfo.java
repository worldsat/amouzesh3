package com.makancompany.assistant.Kernel.Controller.Domain;


import androidx.annotation.NonNull;

public class DomainInfo extends BaseDomain {
    private String viewMode;
    private String id;
    private String title;
    private String apiAddress;
    //spinner title or view type
    private String viewType;
    private String viewId;
    private String filter;
    private boolean isRequired = false;

    public DomainInfo(String viewMode, String id, String title, String apiAddress, String viewType, boolean isRequired) {
        this.viewMode = viewMode;
        this.id = id;
        this.title = title;
        this.apiAddress = apiAddress;
        this.viewType = viewType;
        this.isRequired = isRequired;
        this.viewId = null;
    }

    public DomainInfo(String viewMode, String id, String title, String apiAddress, String viewType) {
        this.viewMode = viewMode;
        this.id = id;
        this.title = title;
        this.apiAddress = apiAddress;
        this.viewType = viewType;
        this.viewId = null;
    }

    public DomainInfo(String viewMode, String id, String title, String apiAddress, String viewType, String viewId) {
        this.viewMode = viewMode;
        this.id = id;
        this.title = title;
        this.apiAddress = apiAddress;
        this.viewType = viewType;
        this.viewId = viewId;
    }

    public DomainInfo(String viewMode, String id, String title, String apiAddress, String viewType, boolean isRequired, String viewId) {
        this.viewMode = viewMode;
        this.id = id;
        this.title = title;
        this.apiAddress = apiAddress;
        this.viewType = viewType;
        this.isRequired = isRequired;
        this.viewId = viewId;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public String getViewMode() {
        return viewMode;
    }

    public void setViewMode(String viewMode) {
        this.viewMode = viewMode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getApiAddress() {
        return apiAddress;
    }

    public void setApiAddress(String apiAddress) {
        this.apiAddress = apiAddress;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    @NonNull
    @Override
    public String toString() {
        return getTitle();
    }
}
