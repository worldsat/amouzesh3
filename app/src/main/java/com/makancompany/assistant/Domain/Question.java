package com.makancompany.assistant.Domain;

import com.makancompany.assistant.Kernel.Controller.Domain.BaseDomain;

public class Question extends BaseDomain {
    private String Question;
    private String Type;
    private String NameQuestion;

    public Question(String question, String type, String nameQuestion) {
        Question = question;
        Type = type;
        NameQuestion = nameQuestion;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public String getNameQuestion() {
        return NameQuestion;
    }

    public void setNameQuestion(String nameQuestion) {
        NameQuestion = nameQuestion;
    }
}
