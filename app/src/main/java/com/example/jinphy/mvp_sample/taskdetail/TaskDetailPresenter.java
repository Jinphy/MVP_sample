package com.example.jinphy.mvp_sample.taskdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import  static com.google.common.base.Preconditions.checkNotNull;

import com.example.jinphy.mvp_sample.data.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;
import com.google.common.base.Strings;

/**
 * Created by jinphy on 2017/8/1.
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TasksRepository repository;

    private final TaskDetailContract.View taskDetailView;

    @Nullable private String taskId;


    public TaskDetailPresenter(
            @Nullable String taskId,
            @NonNull TasksRepository repository,
            @NonNull TaskDetailContract.View taskDetailView) {
        this.taskId = taskId;
        this.repository = checkNotNull(repository,"tasksRepository cannot be null");
        this.taskDetailView = checkNotNull(taskDetailView,"taskDetailView cannot be null");

        this.taskDetailView.setPresenter(this);
    }


    private boolean ensureId() {
        if (Strings.isNullOrEmpty(taskId)) {
            taskDetailView.showMissingTask();
            return false;
        }
        return true;
    }


    @Override
    public void start() {
        openTask();
    }

    private void openTask(){
        if (!ensureId()) {
            return;
        }

        taskDetailView.setLoadingIndicator(true);
        repository.getTask(taskId, new TasksDataSource.GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                if (!taskDetailView.isActive()) {
                    return;
                }
                taskDetailView.setLoadingIndicator(false);
                if (null == task) {
                    taskDetailView.showMissingTask();
                } else {
                    showTask(task);
                }
            }

            @Override
            public void onDataNotAvailable() {
                if (!taskDetailView.isActive()) {
                    return;
                }
                taskDetailView.showMissingTask();
                taskDetailView.setLoadingIndicator(false);
            }
        });
    }

    private void showTask(@NonNull Task task) {
        String title = task.getTitle();
        String description = task.getDescription();

        if (Strings.isNullOrEmpty(title)) {
            taskDetailView.hideTitle();
        }else{
            taskDetailView.showTitle(title);
        }
        if (Strings.isNullOrEmpty(description)) {
            taskDetailView.hideDescription();
        }else{
            taskDetailView.showDescription(description);
        }
        taskDetailView.showCompletionStatus(task.isCompleted());
    }



    @Override
    public void editTask() {
        if (!ensureId()) {
            return;
        }
        taskDetailView.showEditTask(taskId);
    }

    @Override
    public void deleteTask() {
        if (!ensureId()) {
            return;
        }
        repository.deleteTask(taskId);
        taskDetailView.showTskDeleted();
    }

    @Override
    public void completeTask() {
        if (!ensureId()) {
            return;
        }
        repository.completeTask(taskId);
        taskDetailView.showTaskMarkedComplete();
    }

    @Override
    public void activateTask() {
        if (!ensureId()) {
            return;
        }
        repository.activateTask(taskId);
        taskDetailView.showTaskMarkedActive();
    }
}
