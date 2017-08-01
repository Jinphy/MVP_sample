package com.example.jinphy.mvp_sample.taskdetail;

import com.example.jinphy.mvp_sample.BasePresenter;
import com.example.jinphy.mvp_sample.BaseView;

/**
 * Created by jinphy on 2017/8/1.
 */

public interface TaskDetailContract {
    interface View extends BaseView<Presenter>{

        void setLoadingIndicator(boolean active);

        void showMissingTask();

        void hideTitle();

        void showTitle(String title);

        void hideDescription();

        void showDescription(String description);

        void showCompletionStatus(boolean complete);

        void showEditTask(String taskId);

        void showTskDeleted();

        void showTaskMarkedComplete();

        void showTaskMarkedActive();

        boolean isActive();

    }




    interface Presenter extends BasePresenter{

        void editTask();

        void deleteTask();

        void completeTask();

        void activateTask();
    }
}
