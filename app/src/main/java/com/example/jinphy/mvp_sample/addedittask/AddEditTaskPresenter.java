package com.example.jinphy.mvp_sample.addedittask;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.UseCaseHandler;
import com.example.jinphy.mvp_sample.data.model.Task;
import com.example.jinphy.mvp_sample.domain.usecase.GetTask;
import com.example.jinphy.mvp_sample.domain.usecase.SaveTask;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/8/1.
 */

public class AddEditTaskPresenter implements AddEditTaskContract.Presenter {

    @NonNull private final AddEditTaskContract.View addTaskView;

    private final GetTask getTask;

    private final SaveTask saveTask;

    private final UseCaseHandler useCaseHandler;


    @Nullable private final String taskId;



    public AddEditTaskPresenter(
            @NonNull String taskId,
            @NonNull AddEditTaskContract.View addTaskView,
            @NonNull GetTask getTask,
            @NonNull SaveTask saveTask,
            @NonNull UseCaseHandler useCaseHandler) {
        this.taskId = taskId;
        this.addTaskView = checkNotNull(addTaskView);
        this.getTask = checkNotNull(getTask);
        this.saveTask = checkNotNull(saveTask);
        this.useCaseHandler = checkNotNull(useCaseHandler);

        this.addTaskView.setPresenter(this);
    }


    @Override
    public void start() {
        if (!isNewTask()) {
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
        GetTask.RequestValues requestValues = new GetTask.RequestValues(taskId);
        useCaseHandler.execute(
                getTask,
                requestValues,
                new UseCase.UseCaseCallback<GetTask.ResponseValue>() {
                    @Override
                    public void onSuccess(GetTask.ResponseValue response) {
                        Task task = response.getTask();
                        showTask(task);
                    }

                    @Override
                    public void onError() {
                        showEmptyTaskError();
                    }
                }
        );
    }


    private boolean isNewTask() {
        return taskId==null;
    }

    private void createTask(String title,String description) {
        Task task = new Task(title, description);
        if (task.isEmpty()) {
            addTaskView.showEmptyTaskError();
        } else {
            SaveTask.RequestValues requestValues = new SaveTask.RequestValues(task);
            useCaseHandler.execute(
                    saveTask,
                    requestValues,
                    new UseCase.UseCaseCallback<SaveTask.ResponseValue>() {
                        @Override
                        public void onSuccess(SaveTask.ResponseValue response) {
                            if (addTaskView.isActive()) {
                                addTaskView.showTaskList();
                            }
                        }

                        @Override
                        public void onError() {
                            showSaveError();
                        }
                    }
            );


        }
    }

    private void updateTask(String title, String description) {
        if (isNewTask()) {
            throw new RuntimeException("updateTask() was called but task is new");
        } else {

            Task task = new Task(title, description, taskId);
            SaveTask.RequestValues requestValues = new SaveTask.RequestValues(task);
            useCaseHandler.execute(
                    saveTask,
                    requestValues,
                    new UseCase.UseCaseCallback<SaveTask.ResponseValue>() {
                        @Override
                        public void onSuccess(SaveTask.ResponseValue response) {
                            if (addTaskView.isActive()) {
                                addTaskView.showTaskList();
                            }
                        }

                        @Override
                        public void onError() {
                            showSaveError();
                        }
                    }
            );
        }
    }

    public void showTask(Task task) {
        if (addTaskView.isActive()) {
            addTaskView.setTitle(task.getTitle());
            addTaskView.setDescription(task.getDescription());
        }
    }

    public void showEmptyTaskError(){
        if (addTaskView.isActive()) {
            addTaskView.showEmptyTaskError();
        }
    }

    private void  showSaveError() {

    }








}
