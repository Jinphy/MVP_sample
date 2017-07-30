package com.example.jinphy.mvp_sample.tasks;

import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.data.Task;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;


import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/7/30.
 */

public class TasksPresenter implements TasksContract.Presenter {

    private final TasksRepository tasksRepository;

    private final TasksContract.View tasksView;

    private TasksFilterType currentFiltering = TasksFilterType.ALL_TASKS;

    private boolean firstLoad = true;

    public TasksPresenter(
            @NonNull TasksRepository tasksRepository,
            @NonNull TasksContract.View tasksView){
        tasksRepository =checkNotNull(tasksRepository,"tasksRepository cannot be null");
        tasksView = checkNotNull(tasksView, "tasksView cannot be null");

        tasksView.setPresenter(this);
    }





    @Override
    public void start() {
    }

    @Override
    public void result(int requestCode, int resultCode) {

    }

    @Override
    public void loadTasks(boolean forceUpdate) {

    }

    @Override
    public void addNewTask() {

    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {

    }

    @Override
    public void completeTask(@NonNull Task completedTask) {

    }

    @Override
    public void activateTask(@NonNull Task activeTask) {

    }

    @Override
    public void clearCompletedTasks() {

    }

    @Override
    public void setFiltering(TasksFilterType requestType) {

    }

    @Override
    public TasksFilterType getFiltering() {
        return null;
    }
}
