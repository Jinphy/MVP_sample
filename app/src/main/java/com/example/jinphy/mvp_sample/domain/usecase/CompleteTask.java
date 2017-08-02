package com.example.jinphy.mvp_sample.domain.usecase;

import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/2.
 */

public class CompleteTask extends UseCase<CompleteTask.RequestValues,CompleteTask.ResponseValue> {


    private final TasksRepository repository;

    public CompleteTask(@NonNull TasksRepository repository) {
        this.repository = checkNotNull(repository, "repository cannot be null!");
    }



    @Override
    protected void executeUseCase(RequestValues requestValues) {
        String taskId = requestValues.getTaskId();
        repository.completeTask(taskId);
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    //=============================================================\\
    public static final class RequestValues implements UseCase.RequestValues{
        private String taskId;

        public RequestValues(@NonNull String taskId) {
            this.taskId = checkNotNull(taskId, "taskId cannot be null!");
        }

        public String getTaskId() {
            return taskId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        // in this case the response value is not required
    }












}
