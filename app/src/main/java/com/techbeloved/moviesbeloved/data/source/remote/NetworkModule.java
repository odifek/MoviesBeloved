package com.techbeloved.moviesbeloved.data.source.remote;

import com.google.gson.GsonBuilder;
import com.techbeloved.moviesbeloved.BuildConfig;
import com.techbeloved.moviesbeloved.data.source.remote.api.TMDBMovieService;
import com.techbeloved.moviesbeloved.utils.Constants;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Module
public class NetworkModule {
    public static final int CONNECT_TIMEOUT_IN_MS = 30000;

//    @Provides
//    @Singleton
//    static Interceptor providesResponseInterceptor(ResponseInterceptor interceptor) {
//        return interceptor;
//    }

    @Singleton
    @Provides
    static OkHttpClient providesOkhttp() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
                .addInterceptor(chain -> {
                    Request request = chain.request();

                    HttpUrl url = request.url().newBuilder()
                            .addQueryParameter(Constants.API_KEY_QUERY_PARAM, Constants.TMDB_API_KEY)
                            .build();
                    request = request.newBuilder().url(url).build();
                    return chain.proceed(request);
                });
        if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        return builder.build();

    }

    @Singleton
    @Provides
    static Retrofit providesRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(Constants.TMDB_API_BASE_URL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd")
                        .create()))
                .build();
    }

    @Singleton
    @Provides
    static TMDBMovieService providesTmdbMovieService(Retrofit retrofit) {
        return retrofit.create(TMDBMovieService.class);
    }
}
