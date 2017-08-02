package com.example.jinphy.mvp_sample.statistics;

import android.support.annotation.NonNull;
import android.widget.StackView;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.UseCaseHandler;
import com.example.jinphy.mvp_sample.data.model.Statistic;
import com.example.jinphy.mvp_sample.data.model.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;
import com.example.jinphy.mvp_sample.domain.usecase.GetStatistic;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/1.
 */

public class StatisticPresenter implements StatisticContract.Presenter {

    private final StatisticContract.View statisticView;

    private final GetStatistic getStatistic;

    private final UseCaseHandler useCaseHandler;


    public StatisticPresenter(
            @NonNull StatisticContract.View statisticView,
            @NonNull GetStatistic getStatistic,
            @NonNull UseCaseHandler useCaseHandler) {
        this.statisticView = checkNotNull(statisticView);
        this.getStatistic = checkNotNull(getStatistic);
        this.useCaseHandler = checkNotNull(useCaseHandler);

        this.statisticView.setPresenter(this);
    }




    @Override
    public void start() {
        loadStatistics();
    }

    private void loadStatistics() {
        statisticView.setProgressIndicator(true);

        useCaseHandler.execute(
                getStatistic,
                null,
                new UseCase.UseCaseCallback<GetStatistic.ResponseValue>() {
                    @Override
                    public void onSuccess(GetStatistic.ResponseValue response) {
                        Statistic statistic = response.getStatistic();
                        if (statisticView.isActive()) {
                            statisticView.setProgressIndicator(false);
                            statisticView.showStatistics(
                                    statistic.getNumOfActiveTasks(),
                                    statistic.getNumOfCompletedTasks());
                        }
                    }

                    @Override
                    public void onError() {
                        if (statisticView.isActive()) {
                            statisticView.showLoadingStatisticError();
                        }
                    }
                }
        );

    }
}
