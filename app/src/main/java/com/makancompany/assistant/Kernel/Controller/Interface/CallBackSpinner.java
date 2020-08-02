package com.makancompany.assistant.Kernel.Controller.Interface;


import com.makancompany.assistant.Kernel.Controller.Domain.SpinnerDomain;

import java.util.ArrayList;

public interface CallBackSpinner {
    void onSuccess(ArrayList<SpinnerDomain> result);

    void onError(String error);
}
