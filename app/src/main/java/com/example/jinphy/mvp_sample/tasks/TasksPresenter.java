package com.example.jinphy.mvp_sample.tasks;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.UseCase;
import com.example.jinphy.mvp_sample.UseCaseHandler;
import com.example.jinphy.mvp_sample.data.model.Task;
import com.example.jinphy.mvp_sample.domain.usecase.ActivateTask;
import com.example.jinphy.mvp_sample.domain.usecase.ClearCompletedTasks;
import com.example.jinphy.mvp_sample.domain.usecase.CompleteTask;
import com.example.jinphy.mvp_sample.domain.usecase.GetTasks;

import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/7/30.
 */

public class TasksPresenter implements TasksContract.Presenter {

    public static final int REQUEST_ADD_TASK = 1;

    private final TasksContract.View tasksView;

    private final GetTasks getTasks;

    private final CompleteTask completeTask;

    private final ActivateTask activateTask;

    private final ClearCompletedTasks clearCompletedTasks;


    private final UseCaseHandler useCaseHandler;


    private TasksFilterType currentFiltering = TasksFilterType.ALL_TASKS;

    private boolean firstLoad = true;


    public TasksPresenter(
            @NonNull TasksContract.View tasksView,
            @NonNull GetTasks getTasks,
            @NonNull CompleteTask completeTask,
            @NonNull ActivateTask activateTask,
            @NonNull ClearCompletedTasks clearCompletedTasks,
            @NonNull UseCaseHandler useCaseHandler ) {
        this.tasksView = checkNotNull(tasksView,"tasksView cannot be null!");
        this.getTasks = checkNotNull(getTasks, "getTasks cannot be null!");
        this.completeTask = checkNotNull(completeTask, "completeTask cannot be null!");
        this.activateTask = checkNotNull(activateTask, "activateTask cannot be null!");
        this.clearCompletedTasks = checkNotNull(clearCompletedTasks, "clearCompletedTasks cannot be null");
        this.useCaseHandler = checkNotNull(useCaseHandler, "useCseHandler cannot be null!");

        this.tasksView.setPresenter(this);
    }




    @Override
    public void start() {
        loadTasks(false);
    }

    @Override
    public void result(int requestCode, int resultCode) {
        switch (requestCode) {
            case REQUEST_ADD_TASK:
                if (Activity.RESULT_OK==resultCode) {
                    tasksView.showSuccssfullySavedMessage();
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void loadTasks(boolean forceUpdate) {
        loadTasks(forceUpdate || firstLoad,true);
        firstLoad = false;
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            tasksView.setLoadingIndicator(true);
        }

        GetTasks.RequestValues requestValues = new GetTasks.RequestValues(currentFiltering,forceUpdate);

        useCaseHandler.execute(
                getTasks,
                requestValues,
                new UseCase.UseCaseCallback<GetTasks.ResponseValue>() {
                    @Override
                    public void onSuccess(GetTasks.ResponseValue response) {
                        List<Task>tasks = response.getTasks();
                        if (!tasksView.isActive()) {
                            return;
                        }
                        if (showLoadingUI) {
                            tasksView.setLoadingIndicator(false);
                        }
                        processTasks(tasks);
                    }

                    @Override
                    public void onError() {
                        if (tasksView.isActive()) {
                            tasksView.showLoadingTasksError();
                        }
                    }
                }
        );


    }

    private void processTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            processEmptyTasks();
        }else{
            tasksView.showTasks(tasks);
            showFilterLabel();
        }
    }

    private void showFilterLabel(){
        switch (currentFiltering) {
            case ACTIVE_TASKS:
                tasksView.showActiveFilterLabel();
                break;
            case COMPLETED_TASKS:
                tasksView.showCompletedFilterLabel();
                break;
            default:
                tasksView.showAllFilterLabel();
                break;
        }
    }

    private void processEmptyTasks(){
        switch (currentFiltering) {
            case ACTIVE_TASKS:
                tasksView.showNoActiveTasks();
                break;
            case COMPLETED_TASKS:
                tasksView.showNoCompletedTasks();
                break;
            default:
                tasksView.showNoTasks();
                break;

        }
    }

    @Override
    public void addNewTask() {
        tasksView.showAddTask();
    }

    @Override
    public void openTaskDetails(@NonNull Task requestedTask) {
        checkNotNull(requestedTask);
        tasksView.showTaskDetailsUi(requestedTask.getId());
    }

    @Override
    public void completeTask(@NonNull Task completedTask) {
        checkNotNull(completedTask,"activeTask cannot be null!");

        CompleteTask.RequestValues requestValues =
                new CompleteTask.RequestValues(completedTask.getId());
        useCaseHandler.execute(
                completeTask,
                requestValues,
                new UseCase.UseCaseCallback<CompleteTask.ResponseValue>() {
                    @Override
                    public void onSuccess(CompleteTask.ResponseValue response) {
                        if (tasksView.isActive()) {
                            tasksView.showTaskMarkedComplete();
                            loadTasks(false,false);
                        }
                    }

                    @Override
                    public void onError() {
                        if (tasksView.isActive()) {
                            tasksView.showLoadingTasksError();
                        }
                    }
                }
        );
    }

    @Override
    public void activateTask(@NonNull Task activeTask) {
        checkNotNull(activeTask, "activeTask cannot be null!");

        ActivateTask.RequestValues requestValues =
                new ActivateTask.RequestValues(activeTask.getId());
        useCaseHandler.execute(
                activateTask,
                requestValues,
                new UseCase.UseCaseCallback<ActivateTask.ResponseValue>() {
                    @Override
                    public void onSuccess(ActivateTask.ResponseValue response) {
                        if (tasksView.isActive()) {
                            tasksView.showTaskMarkedActive();
                            loadTasks(false,false);
                        }
                    }

                    @Override
                    public void onError() {
                        if (tasksView.isActive()) {
                            tasksView.showLoadingTasksError();
                        }
                    }
                }
        );


    }

    @Override
    public void clearCompletedTasks() {

        ClearCompletedTasks.RequestValues requestValues =
                new ClearCompletedTasks.RequestValues();
        useCaseHandler.execute(
                clearCompletedTasks,
                requestValues,
                new UseCase.UseCaseCallback<ClearCompletedTasks.ResponseValue>() {
                    @Override
                    public void onSuccess(ClearCompletedTasks.ResponseValue response) {
                        if (tasksView.isActive()) {
                            tasksView.showCompletedTasksCleared();
                            loadTasks(false,false);
                        }
                    }

                    @Override
                    public void onError() {
                        if (tasksView.isActive()) {
                            tasksView.showLoadingTasksError();
                        }
                    }
                }
        );


    }

    @Override
    public void setFiltering(TasksFilterType requestType) {
        currentFiltering = requestType;
    }

    @Override
    public TasksFilterType getFiltering() {
        return currentFiltering;
    }
}
