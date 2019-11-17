package com.techbeloved.moviesbeloved.data.source.remote;

import android.content.Context;

import com.google.gson.GsonBuilder;
import com.techbeloved.moviesbeloved.BuildConfig;
import com.techbeloved.moviesbeloved.data.source.remote.api.TMDBMovieService;
import com.techbeloved.moviesbeloved.utils.Constants;
import com.techbeloved.moviesbeloved.utils.NetworkUtils;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Named;
import javax.inject.Qualifier;
import javax.inject.Singleton;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.concurrent.TimeUnit;

@Module
public class NetworkModule {
    public static final int CONNECT_TIMEOUT_IN_MS = 30000;

//    @Provides
//    @Singleton
//    static Interceptor providesResponseInterceptor(ResponseInterceptor interceptor) {
//        return interceptor;
//    }

    @Qualifier
    @Retention(RetentionPolicy.CLASS)
    private @interface InternalApi {
    }


    @Provides
    @InternalApi
    static OkHttpClient providesOkhttp(@InternalApi Interceptor cacheControlInterceptor, @Named("http_cache") Cache httpCacheFile) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();

                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter(Constants.API_KEY_QUERY_PARAM, Constants.TMDB_API_KEY)
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                })
                .addNetworkInterceptor(cacheControlInterceptor)
                .cache(httpCacheFile);
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        return builder.build();

    }

    @Singleton
    @Provides
    static Retrofit providesRetrofit(@InternalApi Lazy<OkHttpClient> client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.TMDB_API_BASE_URL)
                .callFactory(request -> client.get().newCall(request))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create()))
                .build();
    }

    @Provides
    @InternalApi
    static Interceptor REWRITE_CACHE_CONTROL_INTERCEPTOR() {
        return chain -> {
            Response originalResponse = chain.proceed(chain.request());
            if (NetworkUtils.isInternetAvailable()) {
                int maxAge = 60; // read from cache for 1 minute
                return originalResponse.newBuilder()
                        .header("Cache-control", "public, max-age=" + maxAge)
                        .build();
            } else {
                int maxStale = 60 * 60 * 24 * 28; // tolerate 4-weeks stale
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .build();
            }
        };
    }

    @Singleton
    @Provides
    static TMDBMovieService providesTmdbMovieService(Retrofit retrofit) {
        return retrofit.create(TMDBMovieService.class);
    }

    @Provides
    @Named("http_cache")
    static Cache providesLocalCache(Context context) {
        File httpCacheDirectory = new File(context.getCacheDir(), "responses");
        int cacheSize = 10 * 1024 * 1024; // 10 MB

        return new Cache(httpCacheDirectory, cacheSize);
    }
}
