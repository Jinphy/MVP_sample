package com.example.jinphy.mvp_sample.addedittask;

import com.example.jinphy.mvp_sample.BasePresenter;
import com.example.jinphy.mvp_sample.BaseView;

/**
 * Created by jinphy on 2017/8/1.
 */

public interface AddEditTaskContract {

    interface View extends BaseView<Presenter> {
        void showEmptyTaskError();

        void showTaskList();

        void setTitle(String title);

        void setDescription(String description);

        boolean isActive();

    }



    interface Presenter extends BasePresenter{
        void saveTask(String title, String description);

        void populateTask();

        boolean isDataMissing();
    }
}
