package com.makancompany.assistant.Kernel.Controller;


import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.bumptech.glide.Glide;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.makancompany.assistant.Kernel.Controller.Module.SnakBar.SnakBarDownload;
import com.makancompany.assistant.R;

import com.makancompany.assistant.Kernel.Bll.SettingsBll;
import com.makancompany.assistant.Kernel.Controller.Domain.ApiResponse;
import com.makancompany.assistant.Kernel.Controller.Domain.DomainInfo;
import com.makancompany.assistant.Kernel.Controller.Domain.Filter;
import com.makancompany.assistant.Kernel.Controller.Domain.SpinnerDomain;
import com.makancompany.assistant.Kernel.Controller.Interface.CallBackSpinner;
import com.makancompany.assistant.Kernel.Controller.Interface.CallbackGet;
import com.makancompany.assistant.Kernel.Controller.Interface.CallbackOperation;
import com.makancompany.assistant.Kernel.Controller.Interface.IOnResponseListener;
import com.makancompany.assistant.Kernel.Controller.Module.SnakBar.SnakBar;
import com.makancompany.assistant.Kernel.Controller.Module.Volley.VolleyCall;
import com.makancompany.assistant.Kernel.Helper.DataBaseHelper;
import com.makancompany.assistant.Kernel.Helper.DateConverter;
import com.makancompany.assistant.Kernel.Helper.SHA2;
import com.makancompany.assistant.Kernel.Helper.UploadFile;
import com.makancompany.assistant.Kernel.Interface.PercentUploadCallback;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Controller {
    private static String PORT;
    private static String URL;
    private final Context context;
    private final SettingsBll settingsBll;
    private boolean isOnline;
    private Gson gson;
    private ProgressDialog dialog;
    private int count = -1;

    public Controller(Context context) {
        this.context = context;

        //get all settings from SettingsBll which works with sharedPreferences
        settingsBll = new SettingsBll(context);
        URL = settingsBll.getUrlAddress();
        PORT = settingsBll.getPort();
        gson = new Gson();
        isOnline = settingsBll.isOnline();
    }

    public <T> void Get(Class domain, ArrayList<Filter> filter, int take, int skip, boolean allData, CallbackGet callbackGet) {


        try {


            Constructor constructor = domain.getConstructor();
            Object instance = constructor.newInstance();
            Class superClass = domain.getSuperclass();

            Method getTableName = superClass.getDeclaredMethod("getTableName");
            Method getApiAddress = superClass.getDeclaredMethod("getApiAddress");

            String tableName = (String) getTableName.invoke(instance);
            String apiName = (String) getApiAddress.invoke(instance);

            if (isOnline) {
                GetFromApi(domain, apiName, filter, take, skip, allData, callbackGet);
            } else {
                GetFromDatabase(tableName, filter, domain, callbackGet);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // invokes the method at runtime


    }


    private <T> void GetFromDatabase(String tableName, ArrayList<Filter> filters, Class domain, CallbackGet callbackGet) {
        ArrayList<T> result = new ArrayList<>();
        try {

            StringBuilder filterStr = new StringBuilder();
            filterStr.append(" where 1=1 ");
            if (filters != null && filters.size() > 0) {
                for (Filter filter : filters) {
                    if (filter.getField().endsWith("Id")) {
                        filterStr.append(String.format(" and %s like '%s' ", filter.getField(), filter.getValue()));
                    } else if (filter.getField().endsWith("Date") || filter.getField().endsWith("DateTime")) {
                        DateConverter converter = new DateConverter();
                        if (filter.getValue().contains("-")) {
                            String[] dates = filter.getValue().split("-");
                            if (dates.length == 2) {
                                String[] startDate = dates[0].split("/");

                                int year = Integer.parseInt(startDate[0]);
                                int month = Integer.parseInt(startDate[1]);
                                int day = Integer.parseInt(startDate[2]);
                                converter.persianToGregorian(year, month, day);
                                String startDateEn =
                                        String.format("%02d", converter.getYear()) +
                                                "-" + String.format("%02d", converter.getMonth()) +
                                                "-" + String.format("%02d", converter.getDay());


                                String[] endDate = dates[1].split("/");

                                converter.persianToGregorian(
                                        Integer.parseInt(endDate[0]),
                                        Integer.parseInt(endDate[1]),
                                        Integer.parseInt(endDate[2])
                                );
                                String endDateEn =
                                        String.format("%02d", converter.getYear()) +
                                                "-" + String.format("%02d", converter.getMonth()) +
                                                "-" + String.format("%02d", converter.getDay());

                                filterStr.append(
                                        String.format(" and (%s >= '%s' and %s <= '%s')",
                                                filter.getField(),
                                                startDateEn,
                                                filter.getField(),
                                                endDateEn)
                                );
                            }
                        } else {
                            String[] startDate = filter.getValue().split("/");

                            converter.persianToGregorian(
                                    Integer.parseInt(startDate[0]),
                                    Integer.parseInt(startDate[1]),
                                    Integer.parseInt(startDate[2])
                            );


                            String startDateEn =
                                    String.format("%02d", converter.getYear()) +
                                            "-" + String.format("%02d", converter.getMonth()) +
                                            "-" + String.format("%02d", converter.getDay());


                            filterStr.append(
                                    String.format(" and (%s >= '%s' and %s <= '%s')",
                                            filter.getField(),
                                            startDateEn,
                                            filter.getField(),
                                            startDate)
                            );

                        }

                    } else {
                        filterStr.append(String.format(" and %s like '%%%s%%'", filter.getField(), filter.getValue()));
                    }
                }
            }
            String filterString = filterStr.toString();


            DataBaseHelper dbHelper = new DataBaseHelper(context);
            SQLiteDatabase database = dbHelper.openDataBase();

            String query = "Select * from " + tableName + filterString;
            Log.i("moh3n", "GetFromDatabase: " + query);
            Cursor cursor = database.rawQuery(query, null);
            int columnCount = cursor.getColumnCount();

            Constructor constructor = domain.getConstructor();

            if (cursor.moveToFirst()) {
                do {
                    Object item = constructor.newInstance();
                    for (int i = 0; i < columnCount; i++) {
                        String columnName = cursor.getColumnName(i);
                        Method setMethod = domain.getDeclaredMethod("set" + columnName, String.class);
                        if (cursor.getString(i) == null) {
                            setMethod.invoke(item, "null");
                        } else {
                            setMethod.invoke(item, cursor.getString(i));
                        }
                    }

                    result.add((T) item);
                } while (cursor.moveToNext());
            }
            cursor.close();
            database.close();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        callbackGet.onSuccess(result, result.size());
    }

    private <T> void GetFromApi(Class domain, String ApiAddress, ArrayList<Filter> filters,
                                int take, int skip, boolean allData, CallbackGet callbackGet) throws JSONException {


//        if (filters != null && filters.size() > 0) {
//            for (Filter filter : filters) {
//
//                if (filter.getField().endsWith("Date") || filter.getField().endsWith("DateTime")) {
//                    DateConverter converter = new DateConverter();
//                    if (filter.getApiAddress().contains("-")) {
//                        String[] dates = filter.getApiAddress().split("-");
//                        if (dates.length == 2) {
//                            String[] startDate = dates[0].split("/");
//
//                            int year = Integer.parseInt(startDate[0]);
//                            int month = Integer.parseInt(startDate[1]);
//                            int day = Integer.parseInt(startDate[2]);
//                            converter.persianToGregorian(year, month, day);
//                            String startDateEn =
//                                    String.format("%02d", converter.getYear()) +
//                                            "/" + String.format("%02d", converter.getMonth()) +
//                                            "/" + String.format("%02d", converter.getDay());
//
//
//                            String[] endDate = dates[1].split("/");
//
//                            converter.persianToGregorian(
//                                    Integer.parseInt(endDate[0]),
//                                    Integer.parseInt(endDate[1]),
//                                    Integer.parseInt(endDate[2])
//                            );
//                            String endDateEn =
//                                    String.format("%02d", converter.getYear()) +
//                                            "/" + String.format("%02d", converter.getMonth()) +
//                                            "/" + String.format("%02d", converter.getDay());
//
//                            filter.setApiAddress(startDateEn + "-" + endDateEn);
//
//                        }
//                    } else {
//                        String[] startDate = filter.getApiAddress().split("/");
//
//                        converter.persianToGregorian(
//                                Integer.parseInt(startDate[0]),
//                                Integer.parseInt(startDate[1]),
//                                Integer.parseInt(startDate[2])
//                        );
//
//
//                        String startDateEn =
//                                String.format("%02d", converter.getYear()) +
//                                        "/" + String.format("%02d", converter.getMonth()) +
//                                        "/" + String.format("%02d", converter.getDay());
//
//
//                        filter.setApiAddress(startDateEn);
//                    }
//
//                }
//            }
//        }

        Gson gson = new Gson();
        String filterStr = "[]";

        filterStr = gson.toJson(filters);

        VolleyCall volleyCall = new VolleyCall(context);
//        String Address = URL + ":" + PORT + "/" + ApiAddress;
        String Address = URL + "/" + ApiAddress;


        //for filters that has Info
        String filterKey = "filter=";
        if (filterStr.contains("{\"field\":\"Info\",\"value\":\"enable\"}")) {
            filterKey = "Info=";
            filterStr = filterStr.replace("{\"field\":\"Info\",\"value\":\"enable\"}", "").replace("[,{", "[{");
        }

        if (allData) {
            if (filters != null) {
                Address = Address + "?" + filterKey + filterStr;
            }
        } else {
            Address = Address + "?skip=" + String.valueOf(skip) + "&take=" + String.valueOf(take) + "&" + filterKey + filterStr;
        }

        Log.i("ApiCall", Address);

        volleyCall.Get(Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                Log.i("response", response);
                ArrayList<T> result = new ArrayList<>();

                JSONObject jsonRootObject = null;
                try {
                    jsonRootObject = new JSONObject(response);
                    count = jsonRootObject.getInt("Count");

                    if (jsonRootObject.getString("IsError").equals("true")) {
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        context.startActivity(intent);
                    }

                    JSONArray array = jsonRootObject.optJSONArray("Data");
                    Method[] declaredMethods = domain.getDeclaredMethods();

                    if (array != null && array.length() > 0) {

                        for (int i = 0; i < array.length(); i++) {

                            JSONObject obj = array.getJSONObject(i);


                            int columnCount = obj.length();
                            Constructor constructor = domain.getConstructor();

                            Iterator<String> keys = obj.keys();
                            ArrayList<String> keysList = Lists.newArrayList(keys);

                            Object item = constructor.newInstance();
                            for (int j = 0; j < columnCount; j++) {
                                String columnName = keysList.get(j);
                                // Log.i("columnName", columnName);
                                String setColumnName = "set" + columnName;
                                for (int m = 0; m < declaredMethods.length; m++) {
                                    if (setColumnName.equals(declaredMethods[m].getName())) {
                                        if (setColumnName.equals("setLat1")) {
                                            Log.i("Log", "l");
                                        }
                                        declaredMethods[m].invoke(item, obj.getString(keysList.get(j)));
                                        //Log.i("added Item", item.toString());
                                        break;
                                    }
                                }
                            }

                            result.add((T) item);
                        }
                    }


                } catch (JSONException e) {

                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

                callbackGet.onSuccess(result, count);
            }

            @Override
            public void onError(String error) {
                callbackGet.onError(error);
            }
        });
    }


    public <T> void GetFromApi2(String ApiAddress, ArrayList<Filter> filters,
                                int take, int skip, boolean allData, CallbackGet callbackGet) throws JSONException {


//        if (filters != null && filters.size() > 0) {
//            for (Filter filter : filters) {
//
//                if (filter.getField().endsWith("Date") || filter.getField().endsWith("DateTime")) {
//                    DateConverter converter = new DateConverter();
//                    if (filter.getApiAddress().contains("-")) {
//                        String[] dates = filter.getApiAddress().split("-");
//                        if (dates.length == 2) {
//                            String[] startDate = dates[0].split("/");
//
//                            int year = Integer.parseInt(startDate[0]);
//                            int month = Integer.parseInt(startDate[1]);
//                            int day = Integer.parseInt(startDate[2]);
//                            converter.persianToGregorian(year, month, day);
//                            String startDateEn =
//                                    String.format("%02d", converter.getYear()) +
//                                            "/" + String.format("%02d", converter.getMonth()) +
//                                            "/" + String.format("%02d", converter.getDay());
//
//
//                            String[] endDate = dates[1].split("/");
//
//                            converter.persianToGregorian(
//                                    Integer.parseInt(endDate[0]),
//                                    Integer.parseInt(endDate[1]),
//                                    Integer.parseInt(endDate[2])
//                            );
//                            String endDateEn =
//                                    String.format("%02d", converter.getYear()) +
//                                            "/" + String.format("%02d", converter.getMonth()) +
//                                            "/" + String.format("%02d", converter.getDay());
//
//                            filter.setApiAddress(startDateEn + "-" + endDateEn);
//
//                        }
//                    } else {
//                        String[] startDate = filter.getApiAddress().split("/");
//
//                        converter.persianToGregorian(
//                                Integer.parseInt(startDate[0]),
//                                Integer.parseInt(startDate[1]),
//                                Integer.parseInt(startDate[2])
//                        );
//
//
//                        String startDateEn =
//                                String.format("%02d", converter.getYear()) +
//                                        "/" + String.format("%02d", converter.getMonth()) +
//                                        "/" + String.format("%02d", converter.getDay());
//
//
//                        filter.setApiAddress(startDateEn);
//                    }
//
//                }
//            }
//        }

        Gson gson = new Gson();
        String filterStr = "[]";

        filterStr = gson.toJson(filters);

        VolleyCall volleyCall = new VolleyCall(context);
//        String Address = URL + ":" + PORT + "/" + ApiAddress;
        String Address = URL + "/" + ApiAddress;

        if (allData) {
            if (filters != null) {
                Address = Address + "?filter=" + filterStr;
            }
        } else {
            Address = Address + "?skip=" + String.valueOf(skip) + "&take=" + String.valueOf(take) + "&filter=" + filterStr;
        }

        Log.i("ApiCall", Address);

        volleyCall.Get(Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                Log.i("response", response);
                ArrayList<T> result = new ArrayList<>();

                JSONObject jsonRootObject = null;
                try {
                    jsonRootObject = new JSONObject(response);
                    count = jsonRootObject.getInt("Count");

                    if (jsonRootObject.getString("IsError").equals("true")) {
//                        Intent intent = new Intent(context, LoginActivity.class);
//                        context.startActivity(intent);
                    }

                    JSONArray array = jsonRootObject.optJSONArray("Data");

                    if (array.length() > 0) {

                        for (int i = 0; i < array.length(); i++) {

                            String str = array.getString(i);
                            result.add((T) str);
                        }
                    }

                } catch (JSONException e) {
                }

                callbackGet.onSuccess(result, count);
            }

            @Override
            public void onError(String error) {
                callbackGet.onError(error);
            }
        });
    }


    public void Operation(String Operation, Class domain, Context context, String params, CallbackOperation callbackOperation) {
        try {
            Constructor constructor = domain.getConstructor();
            Object instance = constructor.newInstance();
            Class superClass = domain.getSuperclass();

//            Method getTableName = superClass.getDeclaredMethod("getTableName");
            Method getApiAddress = superClass.getDeclaredMethod("getApiAddress");

//            String tableName = (String) getTableName.invoke(instance);
            String apiName = (String) getApiAddress.invoke(instance);


            if (isOnline) {
                OperationApi(context, Operation, apiName, params, callbackOperation);
            } else {
//                    OperationDatabase(context, params, callbackOperation);
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }


    public void OperationApi(Context context, String Operation, String ApiAddress, String params, CallbackOperation callback) {

        String Address = "";
        if (Operation.equals("Manage")) {
//            Address = URL + ":" + PORT + "/" + ApiAddress + "/Manage";
            Address = URL + "/" + ApiAddress + "/Manage";
        } else if (Operation.equals("Delete")) {
//            Address = URL + ":" + PORT + "/" + ApiAddress + "/Delete";
            Address = URL + "/" + ApiAddress + "/Delete";
        } else {
//            Address = URL + ":" + PORT + "/" + ApiAddress;
            Address = URL + "/" + ApiAddress;
        }

        VolleyCall volleyCall = new VolleyCall(context);
        volleyCall.Post(params, Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {

                Gson gson = new Gson();
                ApiResponse apiResponse = gson.fromJson(response, ApiResponse.class);

                if (!apiResponse.IsError) {

                    try {

                        callback.onSuccess(apiResponse.Message);
                        Log.i("moh3n", "Message: " + apiResponse.Message);
                    } catch (Exception e) {

                        Log.i("moh3n", e.toString());
                    }

                } else callback.onError(apiResponse.Message);

            }

            @Override
            public void onError(String error) {

            }
        });
    }

    public void OperationPostBodyApi(Context context, String Operation, String ApiAddress, Map<String, String> params, CallbackOperation callback) {

        String Address = "";
        if (Operation.equals("Manage")) {
//            Address = URL + ":" + PORT + "/" + ApiAddress + "/Manage";
            Address = URL + "/" + ApiAddress + "/Manage";
        } else if (Operation.equals("Delete")) {
//            Address = URL + ":" + PORT + "/" + ApiAddress + "/Delete";
            Address = URL + "/" + ApiAddress + "/Delete";
        } else {
//            Address = URL + ":" + PORT + "/" + ApiAddress;
            Address = URL + "/" + ApiAddress + Operation;
        }

        VolleyCall volleyCall = new VolleyCall(context);
        volleyCall.Volley_POSTBody(params, Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String result) {
                Log.i("moh3n", "onSuccessResponse: " + result);
                try {
                    Gson gson = new Gson();
                    ApiResponse apiResponse = gson.fromJson(result, ApiResponse.class);

                    if (!apiResponse.IsError) {

                        try {
                            callback.onSuccess(apiResponse.Message);
                            Log.i("moh3n", "Message: " + apiResponse.Message);
                        } catch (Exception e) {

                            Log.i("moh3n", e.toString());
                        }

                    } else callback.onError(apiResponse.Message);
                } catch (Exception e) {

                    Log.i("moh3n", "errorPostBody: " + e.toString());
                }

            }

            @Override
            public void onError(String error) {

                SnakBar snakBar = new SnakBar();
                snakBar.snakShow(context, "خطا در دریافت اطلاعات");

                Log.i("moh3n", "errorPostBody: " + error);
            }
        });


    }

    public void DownloadFile(Context context, String fileName, String ApiAddress, String params) {
        String absolutePath = Environment.getExternalStorageDirectory() + File.separator + "Makan" + File.separator;
        File imagePath = new File(absolutePath + fileName);
        if (imagePath.exists()) {

            SnakBarDownload snakBar = new SnakBarDownload();
            snakBar.snakShow(context, "فایلی با این نام در حافظه موجود میباشد.آیا مایل به اجرای آن می باشین؟","/makan", fileName);
        } else {
            String Address = URL + "/" + ApiAddress;

            MaterialDialog waiting_dialog = new MaterialDialog.Builder(context)
                    .customView(R.layout.alert_waiting, false)
                    .autoDismiss(false)
                    .backgroundColor(Color.parseColor("#01000000"))
                    .show();

            VolleyCall volleyCall = new VolleyCall(context);
            volleyCall.download(params, Address, new VolleyCall.VolleyCallbackByte() {
                @Override
                public void onSuccessResponse(byte[] result) {
                    if (result != null) {
                        saveFile(context, fileName, result);
                        waiting_dialog.dismiss();
                    }
                }

                @Override
                public void onError(String error) {
                    Log.i("moh3n", "onErrorDownload: " + error);
                    waiting_dialog.dismiss();
                }
            });
        }

    }

    public void PopulateFilterSpinner(String field, String filter, DomainInfo item, String entryName, String apiAddress, CallBackSpinner callback) {
        VolleyCall volleyCall = new VolleyCall(context);

        String Address = URL + "/" + apiAddress;

        if (filter != null)
            Address = Address + "?filter=" + filter;

        volleyCall.Get(Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {
                ArrayList<SpinnerDomain> result = new ArrayList<>();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONArray jsonArray = jsonObject.optJSONArray("Data");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        String id;

                        // Exception for the domains with RegionId and RegionIdParent
                        if (field.equalsIgnoreCase("RegionIdParent")) {
                            id = jsonArray.getJSONObject(i).getString("RegionId");
                        } else {
                            id = jsonArray.getJSONObject(i).getString(field);
                        }


                        String title = jsonArray.getJSONObject(i).getString(entryName);
                        result.add(new SpinnerDomain(field, title, id));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                callback.onSuccess(result);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }


    public void LoginApi(Context context, Class IntentClass, String ApiAddress, String params, CallbackOperation callbackOperation) {
//        String Address = URL + ":" + PORT + "/" + ApiAddress;
        String Address = URL + "/" + ApiAddress;
        VolleyCall volleyCall = new VolleyCall(context);

        volleyCall.Post(params, Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {

                Gson gson = new Gson();
                ApiResponse apiResponse = gson.fromJson(response, ApiResponse.class);

                if (!apiResponse.IsError && apiResponse.getMessage() == null) {


                    try {

                        String token = new JSONObject(response).getJSONObject("Data").getString("Ticket");
                        String UserPostId = new JSONObject(response).getJSONObject("Data").getString("UserPostIds");
                        String UserName = new JSONObject(response).getJSONObject("Data").getString("UserName");
                        String UserId = new JSONObject(response).getJSONObject("Data").getString("UserId");
//                        Log.i("moh3n", "onSuccessResponse: " + token);
                        settingsBll.setTicket(token);
                        settingsBll.setUserPostId(UserPostId);
                        settingsBll.setUserName(UserName);
                        settingsBll.setUserId(UserId);

                        callbackOperation.onSuccess(response);
//                        context.startActivity(new Intent(context, IntentClass));

                    } catch (JSONException e) {

                        Log.i("moh3n", e.toString());
                    }


                } else callbackOperation.onError(apiResponse.Message);


            }

            @Override
            public void onError(String error) {
                callbackOperation.onError(error);
            }
        });
    }

    public void operationProcess(Context context, String ApiAddress, String params, CallbackOperation callbackOperation) {
//        String Address = URL + ":" + PORT + "/" + ApiAddress;
        String Address = URL + "/" + ApiAddress;
        VolleyCall volleyCall = new VolleyCall(context);

        volleyCall.Post(params, Address, new VolleyCall.VolleyCallback() {
            @Override
            public void onSuccessResponse(String response) {

                Gson gson = new Gson();
                ApiResponse apiResponse = gson.fromJson(response, ApiResponse.class);

                if (!apiResponse.IsError && apiResponse.getMessage() == null) {

                    callbackOperation.onSuccess(apiResponse.Message);

                } else callbackOperation.onError(apiResponse.Message);
            }

            @Override
            public void onError(String error) {
                callbackOperation.onError(error);
            }
        });
    }


    //    public void uploadFile(Context context, String ApiAddress, final File selectedFile, Map<String, Object> params, IOnResponseListener resonse) {
    public void uploadFile(Context context, String ApiAddress, final File selectedFile, Map<String, Object> params, IOnResponseListener resonse) {

//        String Address = URL + ":" + PORT + "/" + ApiAddress;
        String Address = URL + "/" + ApiAddress;

//        dialog = new ProgressDialog(context);
//        dialog.setCancelable(false);
//        dialog.setMessage("در حال آپلود...");
//        dialog.setIndeterminate(false);
//        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        //dialog.setMax(imagesSize + 1);
//        dialog.setProgress(0);
//        dialog.show();
        // dialog = ProgressDialog.show(context, "", "در حال بارگذاری فایل...", true);
        Log.i("moh3n", "uploadFile: " + Address);
        //set progress dialog
        MaterialDialog progress_dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.alert_progress_dialog, false)
                .autoDismiss(false)
                .backgroundColor(Color.parseColor("#01000000"))
                .build();
        RoundCornerProgressBar progressBar = (RoundCornerProgressBar) progress_dialog.findViewById(R.id.progressBar);
        TextView percent = (TextView) progress_dialog.findViewById(R.id.percent);
        ImageView iView = (ImageView) progress_dialog.findViewById(R.id.loading);
        Glide.with(context)
                .load(R.mipmap.loading_processmaker)
                .into(iView);
        TextView warning_message = (TextView) progress_dialog.findViewById(R.id.warning_alert);
        warning_message.setText("در حال ارسال...");

        progress_dialog.show();

//        if (!selectedFile.isFile()) {
//            progress_dialog.dismiss();
//
//            Toast.makeText(context, "فایل قابل انتقال نمی باشد", Toast.LENGTH_SHORT).show();
//
//        } else {
        try {


            Log.i("moh3n", "params: " + params.toString());
            UploadFile ws = new UploadFile(context);

            ws.post(params, Address, settingsBll.getTicket(), new IOnResponseListener() {
                        @Override
                        public void onResponse() {
                            Log.i("moh3n", "onResponse: UploadOk");
                            percent.setText(100 + "%");
                            progressBar.setProgress(100);
                            progress_dialog.dismiss();

                            resonse.onResponse();
                        }

                        @Override
                        public void onError() {

                        }
//                        Toast.makeText(context, "اتمام فرایند بارگذاری", Toast.LENGTH_SHORT).show();
                    }
            );

        } catch (Exception e) {

            Log.i("moh3n", "uploadFileCatch: " + e);
        }

//        }

    }

    public void uploadFileNew(Context context, String ApiAddress, final File selectedFile, Map<String, Object> key, IOnResponseListener iOnResponseListener) {

        String Address = URL + "/" + ApiAddress;

        MaterialDialog progress_dialog = new MaterialDialog.Builder(context)
                .customView(R.layout.alert_progress_dialog, false)
                .autoDismiss(false)
                .canceledOnTouchOutside(false)
                .backgroundColor(Color.parseColor("#01000000"))
                .build();
        RoundCornerProgressBar progressBar = (RoundCornerProgressBar) progress_dialog.findViewById(R.id.progressBar);
        TextView percentTxt = (TextView) progress_dialog.findViewById(R.id.percent);
        TextView cancel = (TextView) progress_dialog.findViewById(R.id.cancel);
        TextView sizeTxt = (TextView) progress_dialog.findViewById(R.id.size);
        TextView speedTxt = (TextView) progress_dialog.findViewById(R.id.speed);
        ImageView iView = (ImageView) progress_dialog.findViewById(R.id.loading);

        Glide.with(context)
                .load(R.mipmap.loading_processmaker)
                .into(iView);
        TextView warning_message = (TextView) progress_dialog.findViewById(R.id.warning_alert);
        warning_message.setText("در حال ارسال...");
        cancel.setOnClickListener(v -> progress_dialog.dismiss());

        progress_dialog.show();

        if (selectedFile != null) {
            if (!selectedFile.isFile()) {
                progress_dialog.dismiss();
                Toast.makeText(context, "فایل قابل انتقال نمی باشد", Toast.LENGTH_SHORT).show();
                return;
            } else {
                key.put("uploaded_file", selectedFile);
            }
        }

        try {

            UploadFile ws = new UploadFile(context);

            ws.post2(key, Address, settingsBll.getTicket(), cancel, new IOnResponseListener() {
                @Override
                public void onResponse() {
                    Log.i("moh3n", "onResponse: UploadOk");
                    progress_dialog.dismiss();
                    iOnResponseListener.onResponse();
                }

                @Override
                public void onError() {
                    Log.i("moh3n", "onResponse: Error");
                    progress_dialog.dismiss();
                    iOnResponseListener.onError();
                }

            }, new PercentUploadCallback() {
                @Override
                public void percent(long totalSize, long sendSize, float percent, float speed, boolean canceled) {
                    if (!canceled) {

                        percentTxt.setText(String.format("%.01f", (100 * percent)) + "%");
                        progressBar.setProgress(100 * percent);
                        sizeTxt.setText("حجم فایل: KB " + (sendSize / 1024) + "/" + (totalSize / 1024));
                        speedTxt.setText("سرعت ارسال: KB/S " + String.format("%.00f", speed));
                    } else {
                        progress_dialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            Log.i("moh3n", "uploadFileCatch: " + e);
        }

//        }

    }


    public void loginDatabase(String userName, String password, CallbackOperation callbackOperation) {

        String hashedUserName = "";
        String hashedPassword = "";
        try {
//            hashedUserName = SHA2.GenerateHash(userName, "512");
            hashedPassword = SHA2.GenerateHash(password, "512");
        } catch (Exception e) {
            Log.e(this.getClass().getName(), e.getMessage());
        }

        String query = "SELECT * FROM t_User WHERE UserUserName LIKE ? AND UserPassword LIKE ? COLLATE NOCASE";
        Log.i("moh3n", "loginDatabase: " + query);
        String[] selectionArgs = {userName, hashedPassword};
        try {
            DataBaseHelper dbHelper = new DataBaseHelper(context);

            SQLiteDatabase database = dbHelper.openDataBase();
            Cursor cursor = database.rawQuery(query, selectionArgs);
            int count = 0;

            // if the returned row size is more than 0 publish the size
            // a successful login must return just 1 row otherwise something has had gone wrong
            if (cursor.moveToFirst())
                count = cursor.getCount();

            cursor.close();
            dbHelper.close();

            callbackOperation.onSuccess(String.valueOf(count));
        } catch (Exception e) {
            e.printStackTrace();
            callbackOperation.onError(e.toString());
        }
    }

    public boolean saveFile(Context context, String fileName, byte[] response) {

//        if (!isExternalStorageAvailable() || isExternalStorageReadOnly()) {
//            return false;
//        }

        boolean success = false;


        File root = new File(Environment.getExternalStorageDirectory() + "/makan", "");
        if (!root.exists()) {
            root.setReadable(true);
            root.setWritable(true);
            root.mkdirs();
        }

        File file = new File(root, fileName);
        FileOutputStream os = null;

        try {
            os = new FileOutputStream(file);
            os.write(response);
            success = true;
        } catch (IOException e) {
        } catch (Exception e) {
        } finally {
            try {
                if (null != os)
                    os.close();
            } catch (Exception ex) {
            }
        }

        SnakBarDownload snakBar = new SnakBarDownload();
        snakBar.snakShow(context, "فایل با موفقیت در پوشه Makan بارگذاری شد","/makan", fileName);


        return success;
    }
}
