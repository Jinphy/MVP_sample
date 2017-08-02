package com.example.jinphy.mvp_sample.domain.usecase;

import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.data.model.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;
import com.example.jinphy.mvp_sample.domain.filter.TaskFilter;
import com.example.jinphy.mvp_sample.domain.filter.TaskFilterFactory;
import com.example.jinphy.mvp_sample.tasks.TasksFilterType;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/2.
 */

public class GetTasks extends UseCase<GetTasks.RequestValues,GetTasks.ResponseValue> {

    private final TasksRepository repository;

    public GetTasks(@NonNull TasksRepository repository) {
        this.repository = checkNotNull(repository, "repository cannot be null!");
    }


    @Override
    protected void executeUseCase(RequestValues requestValues) {
        if (requestValues.isForceUpdate()) {
            repository.refreshTasks();
        }
        repository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                TasksFilterType type = requestValues.getFilteringType();
                TaskFilter filter = TaskFilterFactory.produce(type);
                List<Task> filteredTasks = filter.filter(tasks);
                ResponseValue responseValue = new ResponseValue(filteredTasks);
                getUseCaseCallback().onSuccess(responseValue);

            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }


    //=========================================================\\

    public static final class RequestValues implements UseCase.RequestValues {
        private final TasksFilterType type;

        private final boolean forceUpdate;

        public RequestValues(@NonNull TasksFilterType type, boolean forceUpdate){
            this.type = checkNotNull(type,"filtering type cannot be null!");
            this.forceUpdate = forceUpdate;
        }

        public TasksFilterType getFilteringType() {
            return type;
        }

        public boolean isForceUpdate() {
            return forceUpdate;
        }

    }


    public static final class ResponseValue implements UseCase.ResponseValue{
        private List<Task> tasks;

        public ResponseValue(@NonNull List<Task> tasks) {
            this.tasks = checkNotNull(tasks, "tasks cannot be null!");
        }

        public List<Task> getTasks() {
            return tasks;
        }
    }
}
