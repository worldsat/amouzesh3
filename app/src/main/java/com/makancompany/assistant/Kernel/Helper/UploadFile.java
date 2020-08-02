package com.makancompany.assistant.Kernel.Helper;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makancompany.assistant.Kernel.Controller.Domain.ApiResponse;
import com.makancompany.assistant.Kernel.Controller.Interface.IOnResponseListener;
import com.makancompany.assistant.Kernel.Interface.PercentUploadCallback;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import io.github.lizhangqu.coreprogress.ProgressHelper;
import io.github.lizhangqu.coreprogress.ProgressUIListener;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.TlsVersion;

public class UploadFile {
    private static long stopTime;
    private OkHttpClient client;
    private Activity mActivity;

    public UploadFile(Context context) {
//        SSLContext sslContext=null;
//        try {
//            sslContext = SSLContext.getInstance("SSL");
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//
        this.mActivity = ((Activity) context);
        ConnectionSpec spec = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                        CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                        CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256)
                .build();
        HttpsTrustManager.allowAllSSL();
//        client = new OkHttpClient.Builder()
//                .writeTimeout(10, TimeUnit.MINUTES)
//                .readTimeout(1, TimeUnit.HOURS)
//                .connectTimeout(1, TimeUnit.HOURS)
////                .addInterceptor(interceptor)
////                .connectionSpecs(Collections.singletonList(spec))
////                .sslSocketFactory(sslContext.getSocketFactory())
////                .connectionSpecs(Arrays.asList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS))
////                .connectionSpecs(Collections.singletonList(spec))
//                .build();

//        OkHttpClient  okHttpClient = new OkHttpClient();
//        try {
//            Security.addProvider(new BouncyCastleProvider());
//            KeyStore ksTrust = KeyStore.getInstance("BKS");
//            InputStream instream = context.getResources().openRawResource(R.raw.mytruststore);
//            ksTrust.load(instream, "secret".toCharArray());
//
//            // TrustManager decides which certificate authorities to use.
//            TrustManagerFactory tmf = TrustManagerFactory
//                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
//            tmf.init(ksTrust);
//            SSLContext sslContext = SSLContext.getInstance("TLS");
//            sslContext.init(null, tmf.getTrustManagers(), null);
//
//            client.newBuilder().sslSocketFactory( sslContext.getSocketFactory()).build();
//
//        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
//            e.printStackTrace();
//        }


//        client = getHttpClient(10000);
        client = getUnsafeOkHttpClient();
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            OkHttpClient okHttpClient = builder
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .build();
            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    //    public void post(Map<String, Object> params, String Link, String ticket, IOnResponseListener onResponseListener) {
    public void post(JSONObject params, String Link, String ticket, IOnResponseListener onResponseListener) {
        try {

            HttpUrl url = HttpUrl.parse(Link);
            MultipartBody.Builder b = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            /** this is for JSONObject Params*/
            Iterator keysToCopyIterator = params.keys();
            List<String> keysList = new ArrayList<String>();
            while (keysToCopyIterator.hasNext()) {
                String key = (String) keysToCopyIterator.next();
                keysList.add(key);
            }


            if (params != null) {
                for (int i = 0; i < keysList.size(); i++) {
                    Object key = keysList.get(i);
                    Object value = params.get(keysList.get(i));
                    if (value instanceof File) {
                        File f = (File) value;
                        b.addFormDataPart(key.toString(), f.getName(), RequestBody.create(null, f));
                    } else
                        b.addFormDataPart(key.toString(), value.toString());
                }
            }

//            if (params != null) {
//                Object[] keys = params.keySet().toArray();
//                Object[] values = params.values().toArray();
//                for (int i = 0; i < params.size(); i++) {
//                    Object key = keys[i];
//                    Object value = values[i];
//                    if (value instanceof File) {
//                        File f = (File) value;
//                        b.addFormDataPart(key.toString(), f.getName(), RequestBody.create(null, f));
//                    } else
//                        b.addFormDataPart(key.toString(), value.toString());
//                }
//            }

//            Log.i("moh3n", "ticket: " + ticket);

            RequestBody body = b.build();
            Request req = new Request.Builder()
                    .tag(System.currentTimeMillis())
                    .url(url)
                    .addHeader("Authority", ticket)
                    .post(body)
                    .build();


            client.newCall(req).enqueue(onResponse(onResponseListener));

        } catch (Exception e) {

            Log.i("moh3n", "post: " + e);
            throw new RuntimeException(e);
        }
    }

    public void post2(Map<String, Object> params, String Link, String ticket, TextView cancel_button, IOnResponseListener onResponseListener, PercentUploadCallback percentUploadCallback) {
        try {

            HttpUrl url = HttpUrl.parse(Link);
            MultipartBody.Builder b = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);

            if (params != null) {
                Object[] keys = params.keySet().toArray();
                Object[] values = params.values().toArray();
                for (int i = 0; i < params.size(); i++) {
                    Object key = keys[i];
                    Object value = values[i];
                    if (value instanceof File) {
                        File f = (File) value;
                        b.addFormDataPart(key.toString(), f.getName(), RequestBody.create(null, f));
                    } else
                        b.addFormDataPart(key.toString(), value.toString());
                }
            }

            MultipartBody body2 = b.build();
//            RequestBody body = b.build();
            RequestBody body = ProgressHelper.withProgress(body2, new ProgressUIListener() {
                @Override
                public void onUIProgressChanged(long numBytes, long totalBytes, float percent, float speed) {
                    Log.e("moh3n", "=============start===============");
                    Log.e("moh3n", "numBytes:" + numBytes);
                    Log.e("moh3n", "totalBytes:" + totalBytes);
                    Log.e("moh3n", "percent:" + percent);
                    Log.e("moh3n", "speed:" + speed);
                    Log.e("moh3n", "============= end ===============");
                    percentUploadCallback.percent(totalBytes, numBytes, percent, speed, false);
                }
            });

            Request req = new Request.Builder()
                    .tag(System.currentTimeMillis())
                    .url(url)
                    .addHeader("Authority", ticket)
                    .post(body)
                    .build();


            client.newCall(req).enqueue(onResponse(onResponseListener));

            if (cancel_button != null) {
                cancel_button.setOnClickListener(v -> {
                    client.dispatcher().cancelAll();
                    percentUploadCallback.percent(0, 0, 0, 0, true);
                });

            }
        } catch (
                Exception e) {

            Log.i("moh3n", "post: " + e);

        }

    }

    public void post(Map<String, Object> params, String Link, String ticket, IOnResponseListener onResponseListener) {
        try {

            HttpUrl url = HttpUrl.parse(Link);
            MultipartBody.Builder b = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM);
            if (params != null) {
                Object[] keys = params.keySet().toArray();
                Object[] values = params.values().toArray();
                for (int i = 0; i < params.size(); i++) {
                    Object key = keys[i];
                    Object value = values[i];
                    if (value instanceof File) {
                        File f = (File) value;
                        b.addFormDataPart(key.toString(), f.getName(), RequestBody.create(null, f));
                    } else
                        b.addFormDataPart(key.toString(), value.toString());
                }
            }


            RequestBody body = b.build();
            Request req = new Request.Builder()
                    .tag(System.currentTimeMillis())
                    .url(url)
                    .addHeader("Authority", ticket)
                    .post(body)
                    .build();


            client.newCall(req).enqueue(onResponse(onResponseListener));

        } catch (Exception e) {

            Log.i("moh3n", "post: " + e);
            throw new RuntimeException(e);
        }
    }


    private Callback onResponse(final IOnResponseListener onResponseListener) {
        return new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                onResponseListener.onError();
                long requestTime = (Long) call.request().tag();
                if (requestTime <= stopTime)
                    return;
                if (mActivity != null && !mActivity.isFinishing())
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, "خطا در انجام فرایند", Toast.LENGTH_SHORT).show();

                        }
                    });

            }

            @Override
            public void onResponse(Call call, final Response response) {
                Log.i("moh3n", "onResponse: " + response);
                long requestTime = (Long) call.request().tag();
//                if (requestTime <= stopTime)
//                    return;
//                try {
//                    final String res = response.body().string();
//                    Log.i("moh3n", "upload:" + res);
//                    onResponseListener.onResponse();
//                }catch (Exception e){
//                    Log.i("moh3n", "errorUpload: "+e.toString());
//                }
                if (mActivity != null && !mActivity.isFinishing()) {
                    try {
                        final String res = response.body().string();
                        mActivity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    response.close();

                                    Log.i("moh3n", "upload:" + res);
                                    Gson gson = new Gson();
                                    ApiResponse apiResponse = gson.fromJson(res, ApiResponse.class);
                                    Toast.makeText(mActivity, apiResponse.Message, Toast.LENGTH_SHORT).show();
                                    if (!apiResponse.IsError) {
                                        onResponseListener.onResponse();
                                    }else{
                                        onResponseListener.onError();
                                    }

                                } catch (Exception e) {
                                    //alertWaiting(mActivity).dismiss();
                                    Log.i("moh3n", "uploadCatch: "+e);
                                    onResponseListener.onError();
                                }
                            }
                        });
                    } catch (IOException e) {
                        onResponseListener.onError();
                        Log.i("moh3n", "uploadCatch2: "+e);
                    }
                }
            }
        };
    }
}