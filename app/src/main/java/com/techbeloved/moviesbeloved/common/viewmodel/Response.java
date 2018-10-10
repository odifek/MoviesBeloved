package com.techbeloved.moviesbeloved.common.viewmodel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Use the generic Response class to receive data from API or database
 *
 * @param <T>
 */
public class Response<T> {
    public final Status status;

    @Nullable
    public final T data;

    @Nullable
    public final Throwable error;

    private Response(Status status, @Nullable T data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static <T> Response<T> loading() {
        return new Response<>(Status.LOADING, null, null);
    }

    public static <T> Response<T> success(@NonNull T data) {
        return new Response<>(Status.SUCCESS, data, null);
    }

    public static <T> Response<T> error(@NonNull Throwable error) {
        return new Response<>(Status.ERROR, null, error);
    }
}
