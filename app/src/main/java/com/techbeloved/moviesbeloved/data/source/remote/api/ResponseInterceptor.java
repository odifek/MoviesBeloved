package com.techbeloved.moviesbeloved.data.source.remote.api;

import com.techbeloved.moviesbeloved.utils.Constants;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class ResponseInterceptor implements Interceptor {
    @Inject
    public ResponseInterceptor() {
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        HttpUrl url = request.url().newBuilder()
                .addQueryParameter(Constants.API_KEY_QUERY_PARAM, Constants.TMDB_API_KEY)
                .build();
        request = request.newBuilder().url(url).build();
        return chain.proceed(request);
    }
}
