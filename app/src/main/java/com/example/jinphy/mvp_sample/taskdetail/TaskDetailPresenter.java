package com.example.jinphy.mvp_sample.taskdetail;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import  static com.google.common.base.Preconditions.checkNotNull;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.UseCaseHandler;
import com.example.jinphy.mvp_sample.data.model.Task;
import com.example.jinphy.mvp_sample.domain.usecase.ActivateTask;
import com.example.jinphy.mvp_sample.domain.usecase.CompleteTask;
import com.example.jinphy.mvp_sample.domain.usecase.DeleteTask;
import com.example.jinphy.mvp_sample.domain.usecase.GetTask;
import com.google.common.base.Strings;

/**
 * Created by jinphy on 2017/8/1.
 */

public class TaskDetailPresenter implements TaskDetailContract.Presenter {

    private final TaskDetailContract.View taskDetailView;

    private final GetTask getTask;

    private final CompleteTask completeTask;

    private final ActivateTask activateTask;

    private final DeleteTask deleteTask;

    private final UseCaseHandler useCaseHandler;

    @Nullable private String taskId;


    public TaskDetailPresenter(
            @NonNull String taskId,
            @NonNull TaskDetailContract.View taskDetailView,
            @NonNull GetTask getTask,
            @NonNull CompleteTask completeTask,
            @NonNull ActivateTask activateTask,
            @NonNull DeleteTask deleteTask,
            @NonNull UseCaseHandler useCaseHandler){
        this.taskId = checkNotNull(taskId);
        this.taskDetailView = checkNotNull(taskDetailView);
        this.getTask = checkNotNull(getTask);
        this.completeTask = checkNotNull(completeTask);
        this.activateTask = checkNotNull(activateTask);
        this.deleteTask = checkNotNull(deleteTask);
        this.useCaseHandler = checkNotNull(useCaseHandler);

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

        GetTask.RequestValues requestValues = new GetTask.RequestValues(taskId);
        useCaseHandler.execute(
                getTask,
                requestValues,
                new UseCase.UseCaseCallback<GetTask.ResponseValue>() {
                    @Override
                    public void onSuccess(GetTask.ResponseValue response) {
                        Task task = response.getTask();

                        if (taskDetailView.isActive()) {
                            taskDetailView.setLoadingIndicator(false);
                            showTask(task);
                        }
                    }

                    @Override
                    public void onError() {
                        if (taskDetailView.isActive()) {
                            taskDetailView.showMissingTask();
                            taskDetailView.setLoadingIndicator(false);
                        }
                    }
                }
        );
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
        DeleteTask.RequestValues requestValues = new DeleteTask.RequestValues(taskId);
        useCaseHandler.execute(
                deleteTask,
                requestValues,
                new UseCase.UseCaseCallback<DeleteTask.ResponseValue>() {
                    @Override
                    public void onSuccess(DeleteTask.ResponseValue response) {
                        if (taskDetailView.isActive()) {
                            taskDetailView.showTaskDeleted();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                }
        );
    }

    @Override
    public void completeTask() {
        if (!ensureId()) {
            return;
        }
        CompleteTask.RequestValues requestValues = new CompleteTask.RequestValues(taskId);
        useCaseHandler.execute(
                completeTask,
                requestValues,
                new UseCase.UseCaseCallback<CompleteTask.ResponseValue>() {
                    @Override
                    public void onSuccess(CompleteTask.ResponseValue response) {
                        if (taskDetailView.isActive()) {
                            taskDetailView.showTaskMarkedComplete();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                }
        );
    }

    @Override
    public void activateTask() {
        if (!ensureId()) {
            return;
        }
        ActivateTask.RequestValues requestValues = new ActivateTask.RequestValues(taskId);
        useCaseHandler.execute(
                activateTask,
                requestValues,
                new UseCase.UseCaseCallback<ActivateTask.ResponseValue>() {
                    @Override
                    public void onSuccess(ActivateTask.ResponseValue response) {
                        if (taskDetailView.isActive()) {
                            taskDetailView.showTaskMarkedActive();
                        }
                    }

                    @Override
                    public void onError() {

                    }
                }
        );
    }
}
