package com.example.jinphy.mvp_sample.domain.usecase;

import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.data.model.Statistic;
import com.example.jinphy.mvp_sample.data.model.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/2.
 */

public class GetStatistic extends UseCase<GetStatistic.RequestValues,GetStatistic.ResponseValue> {

    private final TasksRepository repository;

    public GetStatistic(@NonNull TasksRepository repository) {
        this.repository = checkNotNull(repository);
    }

    @Override
    protected void executeUseCase(RequestValues requestValues) {
        repository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                int active = getActiveTaskCount(tasks);
                int completed = tasks.size()-active;
                Statistic statistic = new Statistic(completed, active);
                GetStatistic.ResponseValue responseValue = new ResponseValue(statistic);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    private int getActiveTaskCount(List<Task> tasks) {
        int count = 0;
        for (Task task : tasks) {
            if (task.isActive()) {
                count++;
            }
        }
        return count;
    }


    //=========================================================\\

    public static final class RequestValues implements UseCase.RequestValues{

    }

    public static final class ResponseValue implements UseCase.ResponseValue {
        private final Statistic statistic;

        public ResponseValue(@NonNull Statistic statistic) {
            this.statistic = checkNotNull(statistic);
        }

        public Statistic getStatistic() {
            return statistic;
        }
    }

}
