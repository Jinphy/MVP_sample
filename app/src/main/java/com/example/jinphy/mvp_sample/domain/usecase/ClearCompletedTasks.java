package com.example.jinphy.mvp_sample.domain.usecase;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;
import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/2.
 */

public class ClearCompletedTasks extends UseCase<ClearCompletedTasks.RequestValues,ClearCompletedTasks.ResponseValue> {

    private final TasksRepository repository;

    public ClearCompletedTasks(TasksRepository repository) {
        this.repository = checkNotNull(repository, "repository cannot be null!");
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        repository.clearCompletedTasks();
        getUseCaseCallback().onSuccess(new ResponseValue());
    }

    //=====================================================\\
    public static final class RequestValues implements UseCase.RequestValues{}

    public static final class ResponseValue implements UseCase.ResponseValue{}
}
