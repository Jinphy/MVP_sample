package com.example.jinphy.mvp_sample.domain.usecase;

import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jinphy on 2017/8/2.
 */

public class DeleteTask  extends UseCase<DeleteTask.RequestValues,DeleteTask.ResponseValue>{

    private final TasksRepository repository;


    public DeleteTask(@NonNull TasksRepository repository) {
        this.repository = checkNotNull(repository);
    }


    @Override
    protected void executeUseCase(RequestValues requestValues) {
        String taskId = requestValues.getTaskId();
        repository.deleteTask(taskId);
        getUseCaseCallback().onSuccess(new ResponseValue());
    }


    //==================================================\\

    public static final class RequestValues implements UseCase.RequestValues{
        private String taskId;

        public RequestValues(@NonNull String taskId) {
            this.taskId = checkNotNull(taskId);
        }

        public String getTaskId() {
            return taskId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{

    }
}
