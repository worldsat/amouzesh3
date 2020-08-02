package com.makancompany.assistant.Kernel.Controller.Domain;


import androidx.annotation.NonNull;

public class FilteredDomain {
    @NonNull
    private String id;
    private String value;
    private String Operation;
    public FilteredDomain(@NonNull String id, String value) {
        this.id = id;
        this.value = value;
    }
    public FilteredDomain(String field, String value, String operation) {
        this.id = field;
        this.value = value;
        this.Operation = operation;
    }
    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
    }
}
