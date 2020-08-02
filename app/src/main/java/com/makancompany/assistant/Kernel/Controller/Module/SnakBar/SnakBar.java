package com.makancompany.assistant.Kernel.Controller.Module.SnakBar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.makancompany.assistant.R;

public class SnakBar {
    public void snakShow(Context context, String str) {


        final Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), str, Snackbar.LENGTH_LONG);
        snackbar.setDuration(4000);
//        snackbar.setAction("بازکردن پوشه", new View.OnClickListener() {
//            @Override
//            public void onClick(View vv) {
//                snackbar.dismiss();
//            }
//        });


        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.WHITE);
//        textView.setTextSize(11);
//        textView.setMinimumHeight(370);
//        textView.setGravity(Gravity.CENTER);


//        textView.setMinimumHeight(AbsoluteLayout.LayoutParams.MATCH_PARENT);


        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/iransans_m.ttf");
        textView.setTypeface(font);
        snackbar.show();

    }
}
