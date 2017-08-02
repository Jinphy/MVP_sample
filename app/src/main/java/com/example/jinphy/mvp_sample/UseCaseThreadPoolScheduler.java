package com.example.jinphy.mvp_sample;

import android.os.Handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by jinphy on 2017/8/2.
 */

public class UseCaseThreadPoolScheduler implements UseCaseScheduler {

    private final Handler handler = new Handler();

    public static final int POOL_SIZE = 2;

    public static final int MAX_POOL_SIZE = 4;

    public static final int TIME_OUT = 30;

    ThreadPoolExecutor executor;

    public UseCaseThreadPoolScheduler(){
        executor = new ThreadPoolExecutor(
                POOL_SIZE, MAX_POOL_SIZE, TIME_OUT, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(POOL_SIZE)
        );
    }





    @Override
    public void execute(Runnable task) {
        executor.execute(task);
    }

    @Override
    public <R extends UseCase.ResponseValue> void notifyError(UseCase.UseCaseCallback callback) {
        if (callback != null) {
            handler.post(callback::onError);
        }
    }

    @Override
    public <R extends UseCase.ResponseValue> void notifyResponse(R response, UseCase.UseCaseCallback callback) {
        if (callback != null) {
            handler.post(() -> callback.onSuccess(response));
        }
    }
}
