package com.example.jinphy.mvp_sample;

/**
 * 用例调度器，用来调度任务的执行，以及调度任务的回调
 * 即，{@link UseCase}s 都是用该调度器来执行，并且处理回调
 * 而{@link UseCaseHandler} 则管理{@link UseCase}用例 和 {@link UseCaseHandler}
 * 是的任务（useCase）和任务的执行（UseCaseHandler）相互解耦
 * Created by jinphy on 2017/8/2.
 */

public interface UseCaseScheduler {

    void execute(Runnable task);

    <R extends UseCase.ResponseValue> void notifyResponse(
            final R response,
            final UseCase.UseCaseCallback callback);

    <R extends UseCase.ResponseValue> void notifyError(
            final UseCase.UseCaseCallback callback);

}
