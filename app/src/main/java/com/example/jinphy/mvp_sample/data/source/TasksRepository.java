package com.example.jinphy.mvp_sample.data.source;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.jinphy.mvp_sample.data.Task;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * 该类是一个仓库类，管理者所有的任务数据，包括缓存中的，本地中的和服务器中的
 * 所有的数据操作都有该仓储来管理分配
 *
 *
 *
 * Created by jinphy on 2017/7/30.
 */

public class TasksRepository implements TasksDataSource{

    private static TasksRepository instance;

    // 远程服务器的数据源
    private final TasksDataSource taskRemoteDataSource;
    // 本地数据库的数据源
    private final TasksDataSource taskLocalDataSource;
    // 缓存中的数据源
    private Map<String, Task> cachedTasks;

    // 标志缓存是否过期
    private boolean cacheIsDirty = false;

    private TasksRepository(
            @NonNull TasksDataSource taskRemoteDataSource,
            @NonNull TasksDataSource taskLocalDataSource){
        this.taskRemoteDataSource = taskRemoteDataSource;
        this.taskLocalDataSource = taskLocalDataSource;
    }

    public static TasksRepository getInstance(
            TasksDataSource taskRemoteDataSource,
            TasksDataSource taskLocalDataSource){
        synchronized (TasksRepository.class) {
            if (instance == null) {
                instance = new TasksRepository(taskRemoteDataSource, taskLocalDataSource);
            }
            return instance;
        }
    }

    public static void destroyInstance(){
        instance = null;
    }

    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {
        checkNotNull(callback);

        if (cachedTasks != null && !cacheIsDirty) {
            callback.onTasksLoaded(new ArrayList<>(cachedTasks.values()));
            return;
        }
        if (cacheIsDirty) {
            getTasksFromRemoteDataSource(callback);
        }else{
            taskLocalDataSource.getTasks(new LoadTasksCallback() {
                @Override
                public void onTasksLoaded(List<Task> tasks) {
                    refreshCache(tasks);
                    callback.onTasksLoaded(new ArrayList<>(cachedTasks.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getTasksFromRemoteDataSource(callback);
                }
            });
        }

    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        checkNotNull(taskId);
        checkNotNull(callback);

        Task cachedTask = getTaskWithId(taskId);

        if (cachedTask != null) {
            callback.onTaskLoaded(cachedTask);
            return;
        }
        taskLocalDataSource.getTask(taskId, new GetTaskCallback() {
            @Override
            public void onTaskLoaded(Task task) {
                ensureCachedTasks();
                cachedTasks.put(task.getId(), task);
                callback.onTaskLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                taskRemoteDataSource.getTask(taskId, new GetTaskCallback() {
                    @Override
                    public void onTaskLoaded(Task task) {
                        if (cachedTasks == null) {
                            cachedTasks = new LinkedHashMap<>();
                        }
                        cachedTasks.put(task.getId(), task);
                        callback.onTaskLoaded(task);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        taskRemoteDataSource.saveTask(task);
        taskLocalDataSource.saveTask(task);

        ensureCachedTasks();
        cachedTasks.put(task.getId(), task);
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);
        taskRemoteDataSource.completeTask(task);
        taskLocalDataSource.completeTask(task);

        Task completedTask = new Task(task.getTitle(), task.getDescription(), task.getId(), true);

        ensureCachedTasks();
        cachedTasks.put(task.getId(), completedTask);
    }

    @Override
    public void completeTask(@NonNull String taskId) {
        checkNotNull(taskId);
        completeTask(getTaskWithId(taskId));
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        taskRemoteDataSource.activateTask(task);
        taskLocalDataSource.activateTask(task);
        Task activeTask = new Task(task.getTitle(), task.getDescription(), task.getId(), false);

        ensureCachedTasks();

        cachedTasks.put(task.getId(), task);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        checkNotNull(taskId);
        activateTask(getTaskWithId(taskId));
    }

    @Override
    public void clearCompletedTasks() {
        taskRemoteDataSource.clearCompletedTasks();
        taskLocalDataSource.clearCompletedTasks();

        ensureCachedTasks();

        Iterator<Map.Entry<String, Task>> it = cachedTasks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Task> next = it.next();
            if (next.getValue().isCompleted()) {
                it.remove();
            }
        }

    }

    @Override
    public void refreshTasks() {
        cacheIsDirty = true;
    }

    @Override
    public void deleteAllTasks() {
        taskRemoteDataSource.deleteAllTasks();
        taskLocalDataSource.deleteAllTasks();

        ensureCachedTasks();
        cachedTasks.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        taskRemoteDataSource.deleteTask(taskId);
        taskLocalDataSource.deleteTask(taskId);

        ensureCachedTasks();
        cachedTasks.remove(taskId);
    }

    private void getTasksFromRemoteDataSource(@NonNull final LoadTasksCallback callback) {
        taskRemoteDataSource.getTasks(new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                refreshCache(tasks);
                refreshLocalDataSource(tasks);
                callback.onTasksLoaded(new ArrayList<>(cachedTasks.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Task> tasks) {
        ensureCachedTasks();
        cachedTasks.clear();
        tasks.forEach(it->cachedTasks.put(it.getId(),it));
        cacheIsDirty = false;
    }

    private void refreshLocalDataSource(List<Task> tasks) {
        taskLocalDataSource.deleteAllTasks();
        tasks.forEach(it-> taskLocalDataSource.saveTask(it));
    }

    @Nullable
    private Task getTaskWithId(@NonNull String id) {
        checkNotNull(id);
        if (cachedTasks == null || cachedTasks.isEmpty()) {
            return null;
        }else {
            return cachedTasks.get(id);
        }
    }

    private void ensureCachedTasks() {
        if (cachedTasks == null) {
            cachedTasks = new LinkedHashMap<>();
        }
    }
}










