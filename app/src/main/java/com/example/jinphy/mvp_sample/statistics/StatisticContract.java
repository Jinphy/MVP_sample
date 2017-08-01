package com.example.jinphy.mvp_sample.statistics;

import com.example.jinphy.mvp_sample.BasePresenter;
import com.example.jinphy.mvp_sample.BaseView;

/**
 * Created by jinphy on 2017/8/1.
 */

public interface StatisticContract {

    interface View extends BaseView<Presenter>{

        void setProgressIndicator(boolean active);

        void showStatistics(int numberOfIncompleteTasks, int numberOfCompleteTasks);

        void showLoadingStatisticError();

        boolean isActive();
    }


    interface Presenter extends BasePresenter{

    }
}
