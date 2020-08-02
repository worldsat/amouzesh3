package com.makancompany.assistant.Kernel.Helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import com.makancompany.assistant.BuildConfig;

import java.io.File;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class ExceptionHandler implements Thread.UncaughtExceptionHandler {
    private Context c;

    public ExceptionHandler(Context c) {
        this.c = c;

    }

    @Override
    public void uncaughtException(Thread thread, final Throwable throwable) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.US);
            Date today = new Date();
            System.out.println(dateFormat.format(today));

//            saveTodatebase(Log.getStackTraceString(throwable));

            File root = new File(Environment.getExternalStorageDirectory() + "/Makan", "log");
            if (!root.exists()) {
                root.setReadable(true);
                root.setWritable(true);
                root.mkdirs();

            }

            File f = new File(Environment.getExternalStoragePublicDirectory("Makan")
                    , "log/" + dateFormat.format(today) + "_" + UUID.randomUUID().toString()
                    + ".log");

            PrintWriter writer = new PrintWriter(f);
            writer.println("Model: " + Build.MODEL);
            writer.println("Id: " + Build.ID);
            writer.println("Manufacture: " + Build.MANUFACTURER);
            writer.println("brand: " + Build.BRAND);
            writer.println("type: " + Build.TYPE);
            writer.println("User: " + Build.USER);
            writer.println("Base: " + Build.VERSION_CODES.BASE);
            writer.println("Incremental: " + Build.VERSION.INCREMENTAL);
            writer.println("SDK: " + Build.VERSION.SDK_INT);
            writer.println("Board: " + Build.BOARD);
            writer.println("Brand: " + Build.BRAND);
            writer.println("Host: " + Build.HOST);
            writer.println("Fingerprint: " + Build.FINGERPRINT);
            writer.println("Version Code: " + Build.VERSION.RELEASE);
            writer.println("Version app: " + BuildConfig.VERSION_NAME);
            try {

                SharedPreferences sp = c.getSharedPreferences("Token", 0);

                writer.println("----------------");
                writer.println("Login:");

                writer.println("UserName:" + sp.getString("Username", "---"));
                writer.println("Password:" + sp.getString("Password", "---"));
                writer.println("----------------");
            } catch (Exception e) {
                e.printStackTrace();
                writer.println("Error while logging user info");
            }
            writer.println(Log.getStackTraceString(throwable));
            writer.flush();
            writer.close();

//            Intent intent = new Intent(c, MainActivity.class);
//            c.startActivity(intent);
            System.exit(0);
        } catch (Exception e) {
            Log.i("moh3n", "uncaughtException: " + e.toString());
        }

    }

//    public void saveTodatebase(String log) {
//        try {
//            DataBaseHelper dbHelper = new DataBaseHelper(c);
//            dbHelper.crateDatabase();
//            dbHelper.openDataBase();
//            SQLiteDatabase database = dbHelper.getReadableDatabase();
//
//            DateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.US);
//            Date date = new Date();
//            String dt = df.format(date);
//
//            log = log.replace("'", "");
//
//            database.execSQL("CREATE TABLE IF NOT EXISTS  t_exception (time varchar(21),versionApp varchar(30),version varchar(30),brand varchar(250),model varchar(250),exception  text)");
//
//            database.execSQL("INSERT INTO t_exception (time,versionApp,version,brand,model,exception)" +
//                    " VALUES ('" + dt + "','" + BuildConfig.VERSION_NAME + "','" + Build.VERSION.RELEASE + "','" + Build.BRAND + "','" + Build.MODEL + "','" + log + "');");
//
//            database.close();
//        } catch (IOException e) {
//            Log.i("moh3n", "uncaughtException: " + e.toString());
//        }
//
//    }
}




