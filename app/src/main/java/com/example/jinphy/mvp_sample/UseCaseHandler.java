package com.example.jinphy.mvp_sample;

/**
 * Created by jinphy on 2017/8/2.
 */
/**
 *
 * 该类的作用是用一个{@link UseCaseScheduler} 来执行{@link UseCase}s
 * 使用个任务控制器，控制着每个用例的执行，但真正执行用例的是用例调度器{@link UseCaseScheduler}
 *
 * */
public class UseCaseHandler {

    // 该对象的一个实例，单例模式
    private static UseCaseHandler instance = new UseCaseHandler(new UseCaseThreadPoolScheduler() );

    // 用例调度器，用来调度用例任务，即用例执行者
    private final UseCaseScheduler scheduler;

    private UseCaseHandler(UseCaseScheduler useCaseScheduler) {
        this.scheduler = useCaseScheduler;
    }

    public static UseCaseHandler getInstance(){
        return instance;
    }

    public <Q extends UseCase.RequestValues,R extends UseCase.ResponseValue> void execute(
            final UseCase<Q,R> useCase,
            Q requestValues,
            UseCase.UseCaseCallback<R> callback){

        // 为useCase 设置requestValues 和 callback
        useCase.setRequestValues(requestValues);
        useCase.setCallback(new WrappedCallback(callback,this));

        // 在调度器中调度执行useCase中的任务
        scheduler.execute(useCase::run);

    }

    public <R extends UseCase.ResponseValue> void notifyResponse(
            final R response,
            final UseCase.UseCaseCallback<R> callback) {
        scheduler.notifyResponse(response, callback);
    }

    public <R extends UseCase.ResponseValue >void notifyError(
            final UseCase.UseCaseCallback<R> callback){
        scheduler.notifyError(callback);
    }



    /*
    *
    * 包装该回调的目的是让callback在回调是不直接回调，而是通过包装后，
    * 由UseCaseHandler对象调用notifyXXX()来通知调度器UseCaseScheduler
    * 来执行，调度器执行时回调放在Handler消息队列中执行，所以无论是UseCase
    * 任务还是回调都是有UseCaseHandler处理，再由UseCaseScheduler调度器调
    * 度执行
    *
    *
    * */
    private static final class WrappedCallback<R extends UseCase.ResponseValue> implements UseCase.UseCaseCallback<R>{

        private final UseCase.UseCaseCallback callback;

        private final UseCaseHandler handler;

        public WrappedCallback(UseCase.UseCaseCallback callback, UseCaseHandler handler) {
            this.callback = callback;
            this.handler = handler;
        }
        @Override
        public void onSuccess(R response) {
            handler.notifyResponse(response, callback);
        }

        @Override
        public void onError() {
            handler.notifyError(callback);
        }
    }










}
