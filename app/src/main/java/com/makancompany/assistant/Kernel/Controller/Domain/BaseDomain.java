package com.makancompany.assistant.Kernel.Controller.Domain;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;




public class BaseDomain implements Serializable {
    protected String tableName;
    protected String apiAddressGet;
    protected String apiAddress;


    @NonNull
    protected ArrayList<DomainInfo> domainInfo;

    public ArrayList<DomainInfo> getDomainInfo() {
        return domainInfo;
    }

    public void setDomainInfo(ArrayList<DomainInfo> domainInfo) {
        this.domainInfo = domainInfo;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getApiAddress() {
        return apiAddress;
    }

    public void setApiAddress(String apiAddress) {
        this.apiAddress = apiAddress;
    }


}
