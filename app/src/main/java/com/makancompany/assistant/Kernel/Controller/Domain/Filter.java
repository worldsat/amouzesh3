package com.makancompany.assistant.Kernel.Controller.Domain;

public class Filter {
    private String field;
    private String value;
    private String Operation;


    public Filter(String field, String value) {
        this.field = field;
        this.value = value;

    }

    public Filter(String field, String value, String operation) {
        this.field = field;
        this.value = value;
        Operation = operation;
    }

    public String getOperation() {
        return Operation;
    }

    public void setOperation(String operation) {
        Operation = operation;
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


}
