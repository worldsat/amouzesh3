package com.makancompany.assistant.Kernel.Helper;

import android.content.Context;
import android.graphics.Color;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.makancompany.assistant.R;


public class Waiting {
    private Context context;

    public Waiting(Context context) {
        this.context = context;
    }

    public MaterialDialog alertWaiting() {

        MaterialDialog waiting_dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.alert_waiting, false)
                .autoDismiss(false)
                .backgroundColor(Color.parseColor("#01000000"))
                .build();

        ImageView loading_circle = (ImageView) waiting_dialog.findViewById(R.id.loading_circle_alert);

        Glide.with(context)
                .load(R.mipmap.loading)
                .into(loading_circle);
        return waiting_dialog;
    }
}
