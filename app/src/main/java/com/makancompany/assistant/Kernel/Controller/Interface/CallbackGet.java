package com.makancompany.assistant.Kernel.Controller.Interface;

import java.util.ArrayList;

public interface CallbackGet {

    <T> void onSuccess(ArrayList<T> result, int count);

    void onError(String error);

}
