package com.example.jinphy.mvp_sample;

/**
 *
 * UseCase 即为用例，是一个任务体，一个用例代表着一个任务的执行
 * Created by jinphy on 2017/8/2.
 */

public abstract class UseCase<Q extends UseCase.RequestValues, R extends UseCase.ResponseValue> {

    private Q requestValues;

    private UseCaseCallback callback;

    public void setRequestValues(Q requestValues) {
        this.requestValues = requestValues;
    }

    public Q getRequestValues() {
        return requestValues;
    }

    public void setCallback(UseCaseCallback<R> useCaseCallback) {
        this.callback = useCaseCallback;
    }

    public UseCaseCallback getUseCaseCallback() {
        return callback;
    }

    void run(){
        executeUseCase(requestValues);
    }
    protected abstract void executeUseCase(Q requestValues);

    //--------------------interface---------------------------------------\\
    public interface RequestValues {
    }

    public interface ResponseValue{

    }
    public interface UseCaseCallback<R>{
        void onSuccess(R response);

        void onError();
    }


}
