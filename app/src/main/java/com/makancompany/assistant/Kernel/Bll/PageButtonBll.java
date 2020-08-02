package com.makancompany.assistant.Kernel.Bll;

import android.content.Context;


import com.makancompany.assistant.Kernel.Controller.Controller;
import com.makancompany.assistant.Kernel.Controller.Domain.Filter;
import com.makancompany.assistant.Kernel.Controller.Interface.CallbackGet;
import com.makancompany.assistant.Kernel.Domain.PageButton;

import java.util.ArrayList;

public class PageButtonBll {
    private final Context context;

    public PageButtonBll(Context context) {
        this.context = context;
    }

    public void Get(ArrayList<Filter> filter, int take, int skip, boolean allData, CallbackGet callbackGet) {
        Controller controller = new Controller(context);
        controller.Get(PageButton.class, filter, take, skip, allData, callbackGet);
    }
}