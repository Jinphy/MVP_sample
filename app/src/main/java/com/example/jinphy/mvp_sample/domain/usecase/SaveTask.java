package com.example.jinphy.mvp_sample.domain.usecase;

import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.data.model.Task;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/2.
 */

public class SaveTask extends UseCase<SaveTask.RequestValues,SaveTask.ResponseValue>{

    private final TasksRepository repository;

    public SaveTask(TasksRepository repository) {
        this.repository = checkNotNull(repository);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        Task task = requestValues.getTask();
        repository.saveTask(task);
        getUseCaseCallback().onSuccess(new SaveTask.ResponseValue());
    }


    //=================================================\\

    public static final class RequestValues implements UseCase.RequestValues{
        private final Task task;

        public RequestValues(@NonNull Task task) {
            this.task = checkNotNull(task);
        }

        public Task getTask() {
            return task;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue{

    }


}
