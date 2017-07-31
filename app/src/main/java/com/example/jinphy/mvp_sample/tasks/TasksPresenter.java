package com.example.jinphy.mvp_sample.tasks;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.example.jinphy.mvp_sample.data.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;


import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

import static com.google.common.base.Preconditions.checkNotNull;
/**
 * Created by jinphy on 2017/7/30.
 */

public class TasksPresenter implements TasksContract.Presenter {

    public static final int REQUEST_ADD_TASK = 1;

    private final TasksRepository tasksRepository;

    private final TasksContract.View tasksView;

    private TasksFilterType currentFiltering = TasksFilterType.ALL_TASKS;

    private boolean firstLoad = true;

    public TasksPresenter(
            @NonNull TasksRepository tasksRepository,
            @NonNull TasksContract.View tasksView){
        this.tasksRepository =checkNotNull(tasksRepository,"tasksRepository cannot be null");
        this.tasksView = checkNotNull(tasksView, "tasksView cannot be null");

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
    }

    private void loadTasks(boolean forceUpdate, final boolean showLoadingUI) {
        if (showLoadingUI) {
            tasksView.setLoadingIndicator(true);
        }
        if (forceUpdate) {
            // 将数据仓库中的所有缓存数据设置为过期的
            tasksRepository.refreshTasks();
        }


        tasksRepository.getTasks(new TasksDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                List<Task> taskToShow = new ArrayList<>();

                switch (currentFiltering) {
                    case ALL_TASKS:
                        taskToShow.addAll(tasks);
                        break;
                    case ACTIVE_TASKS:
                        Observable.fromIterable(tasks).filter(it->it.isActive()).forEach(taskToShow::add);
                        break;
                    case COMPLETED_TASKS:
                        Observable.fromIterable(tasks).filter(it -> it.isCompleted()).forEach(taskToShow::add);
                        break;
                    default:
                        break;
                }

                if (!tasksView.isActive()) {
                    return;
                }
                if (showLoadingUI) {
                    tasksView.setLoadingIndicator(false);
                }
                processTasks(taskToShow);

            }

            @Override
            public void onDataNotAvailable() {
                if (!tasksView.isActive()) {
                    return;
                }
                tasksView.showLoadingTasksError();
            }
        });
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
        checkNotNull(completedTask);
        tasksRepository.completeTask(completedTask);
        tasksView.showTaskMarkedComplete();
        loadTasks(false,false);
    }

    @Override
    public void activateTask(@NonNull Task activeTask) {
        checkNotNull(activeTask, "activeTask cannot be null!");
        tasksRepository.activateTask(activeTask);
        tasksView.showTaskMarkedActive();
        loadTasks(false,false);
    }

    @Override
    public void clearCompletedTasks() {
        tasksRepository.clearCompletedTasks();
        tasksView.showCompletedTasksCleared();
        loadTasks(false,false);
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
