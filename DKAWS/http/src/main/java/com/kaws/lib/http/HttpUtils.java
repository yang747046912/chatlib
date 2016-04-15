package com.kaws.lib.http;

import android.content.Context;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kaws.lib.common.utils.CommonUtil;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;


/**
 * Created by 杨才 on 2016/1/10.
 */
public class HttpUtils {
    private static HttpUtils instance;
    private Gson gson;
    private Context context;
    private Object https;
    private Object http;
    private RestAdapter.LogLevel logLevel = RestAdapter.LogLevel.FULL;

    public static HttpUtils getInstance() {
        if (instance == null) {
            synchronized (HttpUtils.class) {
                if (instance == null) {
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
        HttpHead.init(context);
    }

    public <T> T getQinniuService(Class<T> a) {
        File cacheFile = new File(context.getApplicationContext().getCacheDir().getAbsolutePath(), "HttpCache");
        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(cacheFile, cacheSize);
        OkHttpClient client = new OkHttpClient();
        client.setCache(cache);
        client.setConnectTimeout(5, TimeUnit.MINUTES);
        client.setReadTimeout(5, TimeUnit.MINUTES);
        client.setWriteTimeout(5, TimeUnit.MINUTES);
        RestAdapter.Builder builder = new RestAdapter.Builder();
        builder.setClient(new OkClient(client));
        builder.setEndpoint(AppConfig.QINNIU);//设置远程地址
        builder.setConverter(new GsonConverter(getGson()));
        builder.setRequestInterceptor(new RequestInterceptor() {
            @Override
            public void intercept(RequestFacade requestFacade) {
                requestFacade.addHeader("Host", "upload.qiniu.com");
            }
        });
        RestAdapter adapter = builder.build();
        adapter.setLogLevel(logLevel);
        return adapter.create(a);
    }

    public <T> T getHttpService(Class<T> a) {
        if (http == null) {
            synchronized (HttpUtils.class) {
                if (http == null) {
                    File cacheFile = new File(context.getApplicationContext().getCacheDir().getAbsolutePath(), "HttpCache");
                    int cacheSize = 10 * 1024 * 1024;
                    Cache cache = new Cache(cacheFile, cacheSize);
                    OkHttpClient client = new OkHttpClient();
                    client.setCache(cache);
                    client.setConnectTimeout(10, TimeUnit.SECONDS);
                    RestAdapter.Builder builder = new RestAdapter.Builder();
                    builder.setClient(new OkClient(client));
                    builder.setEndpoint(AppConfig.BASEURL);//设置远程地址
                    builder.setConverter(new GsonConverter(getGson()));
                    builder.setRequestInterceptor(new RequestInterceptor() {
                        @Override
                        public void intercept(RequestFacade requestFacade) {
                            requestFacade.addHeader("Accept", "application/json;versions=1");
                            if (CommonUtil.isNetWorkConnected(context)) {
                                int maxAge = 60;
                                requestFacade.addHeader("Cache-Control", "public, max-age=" + maxAge);
                            } else {
                                int maxStale = 60 * 60 * 24 * 28;
                                requestFacade.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
                            }
                        }
                    });
                    RestAdapter adapter = builder.build();
                    adapter.setLogLevel(logLevel);
                    http = adapter.create(a);
                }
            }
        }
        return (T) http;
    }


    /**
     * 登录、注册、改密码时使用 Https
     *
     * @return
     */
    public <T> T getHttpsService(Class<T> a) {
        if (https == null) {
            synchronized (HttpUtils.class) {
                if (https == null) {
                    RestAdapter.Builder builder = new RestAdapter.Builder();
                    builder.setClient(getOkClient());
                    builder.setEndpoint(AppConfig.HTTPSURL);//设置远程地址
                    builder.setConverter(new GsonConverter(getGson()));
                    RestAdapter restAdapter_HTTPS = builder.build();
                    restAdapter_HTTPS.setLogLevel(logLevel);
                    https = restAdapter_HTTPS.create(a);
                }
            }
        }
        return (T) https;
    }

    private Gson getGson() {
        if (gson == null) {
            GsonBuilder builder = new GsonBuilder();
            builder.setFieldNamingStrategy(new AnnotateNaming());
            builder.serializeNulls();
            gson = builder.create();
        }
        return gson;
    }


    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }

    public OkHttpClient getUnsafeOkHttpClient() {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                @Override
                public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            }};
            // Install the all-trusting trust manager
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            OkHttpClient okHttpClient = new OkHttpClient();
            okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
            okHttpClient.setSslSocketFactory(sslSocketFactory);
            okHttpClient.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
//                    Log.d("HttpUtils", "==come");
                    return true;
                }
            });

            return okHttpClient;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public OkClient getOkClient() {
        OkHttpClient client1;
        client1 = getUnsafeOkHttpClient();
        OkClient _client = new OkClient(client1);
        return _client;
    }

}
