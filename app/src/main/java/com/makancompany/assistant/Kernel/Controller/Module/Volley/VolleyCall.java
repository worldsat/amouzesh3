package com.makancompany.assistant.Kernel.Controller.Module.Volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.makancompany.assistant.Kernel.Bll.SettingsBll;
import com.makancompany.assistant.Kernel.Helper.HttpsTrustManager;
import com.makancompany.assistant.Kernel.Helper.InputStreamVolleyRequest;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class VolleyCall {
    public static final int INITIAL_TIMEOUT_MS = 300000;
    Context context;

    public VolleyCall(Context context_) {
        context = context_;
    }

    public void Get(String url, final VolleyCallback callback) {
        VolleyLog.DEBUG = true;
        url = url.replace(" ", "");
        HttpsTrustManager.allowAllSSL();
        StringRequest strREQ = new StringRequest(Request.Method.GET, url,
                Response -> {

                    callback.onSuccessResponse(Response);

//                    Log.i("moh3n", "GetReponse: " + Response);

                }, error -> callback.onError(error.toString())) {
            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> params = new HashMap<String, String>();

                SettingsBll settingsBll = new SettingsBll(context);
                String token = settingsBll.getTicket();
                params.put("Cache-Control", "no-cache");
                if (token != null) {
                    params.put("authority", token);
                }
                return params;
            }


        };
        strREQ.setRetryPolicy(new DefaultRetryPolicy(
                INITIAL_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(context).addToRequestQueue(strREQ);
        Log.i("moh3n", "SendRequest: " + strREQ);
    }

    public void Post(final String params, String url, final VolleyCallback callback) {
        try {
            VolleyLog.DEBUG = true;
            url = url.replace(" ", "");
            Log.i("moh3n", "SendRequest: " + url);
            Log.i("moh3n", "params: " + params);
            HttpsTrustManager.allowAllSSL();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> callback.onSuccessResponse(response),
                    error -> callback.onError(error.getMessage())) {
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();

                    SettingsBll settingsBll = new SettingsBll(context);
                    String token = settingsBll.getTicket();

                    if (token != null) {
                        params.put("authority", token);
                    }

                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return params == null ? null : params.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", params, "utf-8");
                        return null;
                    }
                }

//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//
//                    String responseString = "post_error";
//                    if (response != null) {
//                        try {
//                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
            };


            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    INITIAL_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        } catch (NegativeArraySizeException n) {
            n.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void PostStream(final String params, String url, final VolleyCallback callback) {
        try {

            url = url.replace(" ", "");
            Log.i("moh3n", "SendRequest: " + url);
            Log.i("moh3n", "params: " + params);
            HttpsTrustManager.allowAllSSL();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    response -> callback.onSuccessResponse(response),
                    error -> callback.onError(error.getMessage())) {
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();

                    SettingsBll settingsBll = new SettingsBll(context);
                    String token = settingsBll.getTicket();

                    if (token != null) {
                        params.put("authority", token);
                    }

                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded";
                }

                @Override
                public byte[] getBody() {
                    try {
                        return params == null ? null : params.getBytes("UTF-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", params, "utf-8");
                        return null;
                    }
                }

//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//
//                    String responseString = "post_error";
//                    if (response != null) {
//                        try {
//                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
            };


            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    INITIAL_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        } catch (NegativeArraySizeException n) {
            n.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Option(String url, final VolleyCallback callback) {
        try {

            url = url.replace(" ", "");
            //  Log.i("moh3n", "params: " + params);

            StringRequest stringRequest = new StringRequest(Request.Method.OPTIONS, url, callback::onSuccessResponse, error -> callback.onError(error.getMessage())) {
                /**
                 * Passing some request headers
                 * */
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();

                    SettingsBll settingsBll = new SettingsBll(context);
                    String token = settingsBll.getTicket();

                    if (token != null) {
                        params.put("authority", token);
                    }
                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "text/html; charset=iso-8859-1";
                }

//                @Override
//                public byte[] getBody() {
//                    try {
//                        return params == null ? null : params.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", params, "utf-8");
//                        return null;
//                    }
//                }

//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//
//                    String responseString = "post_error";
//                    if (response != null) {
//                        try {
//                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }
            };


            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    INITIAL_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(context).addToRequestQueue(stringRequest);
        } catch (NegativeArraySizeException n) {
            n.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void Volley_POSTBody(Map<String, String> params, String url, final VolleyCallback callback) {
        try {

            Log.i("moh3n", "SendRequestPost: " + url);
            RequestQueue requestQueue = Volley.newRequestQueue(context);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    callback.onSuccessResponse(response);

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError e) {

                    callback.onSuccessResponse(e.toString());

                }
            }) {
                @Override
                public Map<String, String> getHeaders() {
                    Map<String, String> params = new HashMap<String, String>();

                    SettingsBll settingsBll = new SettingsBll(context);
                    String token = settingsBll.getTicket();

                    if (token != null) {
                        params.put("authority", token);
                    }

                    return params;
                }

                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

//                @Override
//                public byte[] getBody() throws AuthFailureError {
//                    try {
//                        return params == null ? null : params.getBytes("utf-8");
//                    } catch (UnsupportedEncodingException uee) {
//                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", params, "utf-8");
//                        return null;
//                    }
//                }

//                @Override
//                protected Response<String> parseNetworkResponse(NetworkResponse response) {
//
//                    String responseString = "post_error";
//                    if (response != null) {
//                        try {
//                            responseString = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
//                }

                @Override
                protected Map<String, String> getParams() {
//                    Map<String, String> MyData = new HashMap<>();
//
//                    MyData.put("DashboardId", params);
//                    if (comment != null) {
//                        MyData.put("DashboaredComment", comment);
//                    }
                    return params;
                }
            };


            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    300000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (NegativeArraySizeException n) {
            n.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void download(final String params, String url, final VolleyCallbackByte callback) {
        try {

            url = url.replace(" ", "");
            Log.i("moh3n", "SendRequest: " + url);
            Log.i("moh3n", "params: " + params);
            HttpsTrustManager.allowAllSSL();

                InputStreamVolleyRequest stringRequest = new InputStreamVolleyRequest(Request.Method.POST, url,
                        response -> callback.onSuccessResponse(response), error -> callback.onError(error.getMessage()), null) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }
                    @Override
                    public byte[] getBody() {
                        try {
                            return params == null ? null : params.getBytes("UTF-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", params, "utf-8");
                            return null;
                        }
                    }
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> MyData = new HashMap<>();

                        MyData.put("base64", "");
                        MyData.put("contentType", "text/plain");
                        MyData.put("fileName", "test.txt");

                        return MyData;
                    }
                };




            RequestQueue mRequestQueue = Volley.newRequestQueue(context, new HurlStack());
            mRequestQueue.add(stringRequest);
        } catch (NegativeArraySizeException n) {
            n.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public interface VolleyCallback {
        void onSuccessResponse(String result);

        void onError(String error);
    }
    public interface VolleyCallbackByte {
        void onSuccessResponse(byte[] result);

        void onError(String error);
    }

}
