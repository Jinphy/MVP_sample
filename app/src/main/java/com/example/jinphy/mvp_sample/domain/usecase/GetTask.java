package com.example.jinphy.mvp_sample.domain.usecase;

import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.data.model.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/2.
 */

public class GetTask extends UseCase<GetTask.RequestValues,GetTask.ResponseValue> {

    private final TasksRepository repository;

    public GetTask(@NonNull TasksRepository repository) {
        this.repository = checkNotNull(repository);
    }


    @Override
    protected void executeUseCase(RequestValues requestValues) {
        String taskId = requestValues.getTaskId();

        repository.getTask(taskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                getUseCaseCallback().onSuccess(new GetTask.ResponseValue(task));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }


    //============================================================\\

    public static final class RequestValues implements UseCase.RequestValues{
        private final String taskId;

        public RequestValues(@NonNull String taskId) {
            this.taskId = checkNotNull(taskId);
        }

        public String getTaskId() {
            return taskId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue{
        private final Task task;

        public ResponseValue(@NonNull Task task) {
            this.task = checkNotNull(task);
        }

        public Task getTask() {
            return task;
        }
    }
}
