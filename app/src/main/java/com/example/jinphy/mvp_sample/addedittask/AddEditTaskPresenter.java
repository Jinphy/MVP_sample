package com.example.jinphy.mvp_sample.addedittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jinphy.mvp_sample.data.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/1.
 */

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter ,
            TasksDataSource.GetTaskCallback{

    @NonNull
    private final TasksDataSource repository;

    @NonNull private final AddEditTaskContract.View addTaskView;

    @Nullable private final String taskId;

    private boolean isDataMissing;

    public AddEditTaskPresenter(
            @Nullable String taskId,
            @NonNull TasksDataSource repository,
            @NonNull AddEditTaskContract.View addTaskView,
            boolean shouldLoadDataFromRepo) {
        this.taskId = taskId;
        this.repository = checkNotNull(repository);
        this.addTaskView = checkNotNull(addTaskView);
        this.isDataMissing = shouldLoadDataFromRepo;

        addTaskView.setPresenter(this);
    }



    @Override
    public void start() {
        if (!isNewTask() && isDataMissing) {
            populateTask();
        }
    }

    @Override
    public void saveTask(String title, String description) {
        if (isNewTask()) {
            createTask(title, description);
        } else {
            updateTask(title, description);
        }
    }

    @Override
    public void populateTask() {
        if (isNewTask()) {
            throw new RuntimeException("populateTask() was called but task is new.");
        }
        repository.getTask(taskId,this);
    }

    @Override
    public boolean isDataMissing() {
        return isDataMissing;
    }

    private boolean isNewTask() {
        return taskId==null;
    }

    private void createTask(String title,String description) {
        Task task = new Task(title, description);
        if (task.isEmpty()) {
            addTaskView.showEmptyTaskError();
        } else {
            repository.saveTask(task);
            addTaskView.showTaskList();
        }
    }

    @Override
    public void onTaskLoaded(Task task) {
        if (addTaskView.isActive()) {
            addTaskView.setTitle(task.getTitle());
            addTaskView.setDescription(task.getDescription());
        }
        isDataMissing = false;
    }

    private void updateTask(String title, String description) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new");
        } else {
            repository.saveTask(new Task(title,description,taskId));
            addTaskView.showTaskList();
        }
    }

    @Override
    public void onDataNotAvailable() {
        if (addTaskView.isActive()) {
            addTaskView.showEmptyTaskError();
        }
    }
}
