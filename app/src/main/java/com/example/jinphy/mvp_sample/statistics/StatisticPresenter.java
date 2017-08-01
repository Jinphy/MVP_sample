package com.example.jinphy.mvp_sample.statistics;

import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.data.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/1.
 */

public class StatisticPresenter implements StatisticContract.Presenter {


    private final TasksRepository repository;

    private final StatisticContract.View statisticView;

    public StatisticPresenter(
            @NonNull TasksRepository repository,
            @NonNull StatisticContract.View statisticView) {
        this.repository = checkNotNull(repository);
        this.statisticView = checkNotNull(statisticView);

        this.statisticView.setPresenter(this);
    }




    @Override
    public void start() {
        loadStatistics();
    }

    private void loadStatistics() {
        statisticView.setProgressIndicator(true);

        repository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                int activeTass = 0;
                int completedTasks =0;

                for (Task task : tasks) {
                    if (task.isCompleted()) {
                        completedTasks++;
                    } else {
                        activeTass++;
                    }
                }
                if (statisticView.isActive()) {
                    statisticView.setProgressIndicator(false);
                    statisticView.showStatistics(activeTass,completedTasks);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (statisticView.isActive()) {
                    statisticView.showLoadingStatisticError();
                }
            }
        });
    }
}
