package com.example.jinphy.mvp_sample.data.source.remote;

import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jinphy.mvp_sample.data.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by jinphy on 2017/7/31.
 */

public class TasksRemoteDataSource implements TasksDataSource {

    private static TasksRemoteDataSource instance = new TasksRemoteDataSource();

    // 服务器延时时间
    private static final int SERVICE_LATENCY_IN_MILLIS = 2000;

    // 模拟服务器中的数据
    private static final Map<String, Task> TASKS_SERVICE_DATA;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
        addTask("Build tower in Pisa", "Ground looks good, no foundation work required.");
        addTask("Finish bridge in Tacoma", "Found awesome girders at half the cost!");
    }

    private static void addTask(String title, String description) {
        Task task = new Task(title, description);
        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    private TasksRemoteDataSource(){}

    public static TasksRemoteDataSource getInstance(){
        return instance;
    }


    @Override
    public void getTasks(final @NonNull LoadTasksCallback callback) {
        // Simulate network by delaying the execution
        Handler handler = new Handler();
        handler.postDelayed(
                ()->callback.onTasksLoaded(Lists.newArrayList(TASKS_SERVICE_DATA.values())),
                SERVICE_LATENCY_IN_MILLIS
        );

    }

    @Override
    public void getTask(@NonNull String taskId,final @NonNull GetTaskCallback callback) {
        final Task task = TASKS_SERVICE_DATA.get(taskId);

        Handler handler = new Handler();
        handler.postDelayed(
                () -> callback.onTaskLoaded(task),
                SERVICE_LATENCY_IN_MILLIS
        );
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        TASKS_SERVICE_DATA.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);
        TASKS_SERVICE_DATA.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {

    }

    @Override
    public void activateTask(@NonNull Task task) {
        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId());
        TASKS_SERVICE_DATA.put(task.getId(), activeTask);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
    }

    @Override
    public void clearCompletedTasks() {
        Iterator<Map.Entry<String, Task>> it = TASKS_SERVICE_DATA.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> next = it.next();
            if (next.getValue().isCompleted()) {
                it.remove();
            }
        }
    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        TASKS_SERVICE_DATA.remove(taskId);
    }
}
