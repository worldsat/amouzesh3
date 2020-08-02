package com.makancompany.assistant.Kernel.Controller.Module.SnakBar;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;


import com.google.android.material.snackbar.Snackbar;
import com.makancompany.assistant.R;


import java.io.File;

public class SnakBarDownload {
    public void snakShow(final Context context, String str, final String folderName, final String fileName) {


        final Snackbar snackbar = Snackbar.make(((Activity) context).findViewById(android.R.id.content), str, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("بازکردن فایل", new View.OnClickListener() {
            @Override
            public void onClick(View vv) {

                File root = new File(Environment.getExternalStorageDirectory() + folderName , fileName);

                try {
                    openFile(context, root);
                } catch (ActivityNotFoundException e) {
                    Log.i("moh3n", "onClick: " + e.toString());
                }
            }
        });


        // Changing action button text color
        View sbView = snackbar.getView();
        TextView textView = sbView.findViewById(R.id.snackbar_text);
        TextView action = sbView.findViewById(R.id.snackbar_action);
        textView.setTextColor(Color.WHITE);
        action.setTextColor(Color.RED);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "fonts/iransans_m.ttf");
        textView.setTypeface(font);
        action.setTypeface(font);
        snackbar.show();

    }


    private void openFile(Context context, File url) {

        try {

            Uri uri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".my.package.name.provider", url);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains("..xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.i("moh3n", "openFile: " + e);
            Toast.makeText(context, "برنامه ای برای باز کردن فایل مورد نظر یافت نشد ", Toast.LENGTH_SHORT).show();
        }
    }
}
