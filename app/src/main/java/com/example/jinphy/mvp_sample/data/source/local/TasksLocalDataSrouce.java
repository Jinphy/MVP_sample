package com.example.jinphy.mvp_sample.data.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.data.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by jinphy on 2017/7/30.
 */

public class TasksLocalDataSrouce implements TasksDataSource{

    private static TasksLocalDataSrouce instance ;

    private TasksDBHelper helper;

    private TasksLocalDataSrouce(@NonNull Context context) {
        checkNotNull(context);
        helper = new TasksDBHelper(context);
    }

    public static TasksLocalDataSrouce getInstance(@NonNull Context context) {
        if (instance==null){
            synchronized (TasksLocalDataSrouce.class){
                instance = new TasksLocalDataSrouce(context);
            }
        }
        return instance;
    }


    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {

    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {

    }

    @Override
    public void saveTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull Task task) {

    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull Task task) {

    }

    @Override
    public void activateTask(@NonNull String taskId) {

    }

    @Override
    public void clearCompletedTasks() {

    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {

    }

    @Override
    public void deleteTask(@NonNull String taskId) {

    }
}
