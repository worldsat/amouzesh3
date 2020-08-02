package com.makancompany.assistant.Kernel.Helper;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.makancompany.assistant.Kernel.Controller.Domain.BaseDomain;
import com.makancompany.assistant.Kernel.Controller.Module.SnakBar.SnakBar;
import com.makancompany.assistant.R;

import java.util.ArrayList;

public class ListBuilder extends BaseDomain {

    private Context context;

    public ListBuilder(Context context) {
        this.context = context;
    }

    public <T> void Builder(ArrayList<T> response, RecyclerView recyclerViewlist, ProgressBar progressBar, TextView warningTxt, RecyclerView.Adapter ad) {


        if (response.size() == 0) {
            if (warningTxt != null)
                warningTxt.setVisibility(View.VISIBLE);

        } else {

            try {
                if (warningTxt != null)
                    warningTxt.setVisibility(View.GONE);

                recyclerViewlist.setVisibility(View.VISIBLE);
                recyclerViewlist.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                recyclerViewlist.setAdapter(ad);

            } catch (Exception e) {
                SnakBar snakBar = new SnakBar();
                snakBar.snakShow(context, "خطا در دریافت اطلاعات");
                Log.i("moh3n", "error: " + e.getMessage());
            }
        }

        progressBar.setVisibility(View.GONE);

    }

}
