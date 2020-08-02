package com.makancompany.assistant.Kernel.Controller.Domain;


import androidx.annotation.NonNull;

public class SpinnerDomain extends BaseDomain {
    private String parentId;
    private String field;
    private String value;

    public SpinnerDomain(String parentId, String field, String value) {
        this.parentId = parentId;
        this.field = field;
        this.value = value;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @NonNull
    @Override
    public String toString() {
        return getField();
    }
}
