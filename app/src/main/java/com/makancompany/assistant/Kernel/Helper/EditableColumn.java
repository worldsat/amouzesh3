package com.makancompany.assistant.Kernel.Helper;

import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditableColumn {

    public Boolean getAllowedEditable(String list, String selected) {


        String replace = list.replace("[", "");
        String replace1 = replace.replace("]", "");
        String replace2 = replace1.replace("{\"ColumnName\":\"", "");
        String replace3 = replace2.replace("\"", "");
        String replace4 = replace3.replace("}", "");

        List<String> myList = new ArrayList<String>(Arrays.asList(replace4.split(",")));

        Log.i("moh3n", "getAllowedEditable: " + myList.toString());
        for (int i = 0; i < myList.size(); i++) {

            if (myList.get(i).equals(selected)) {
                return true;
            }
        }

        return false;
    }
}
