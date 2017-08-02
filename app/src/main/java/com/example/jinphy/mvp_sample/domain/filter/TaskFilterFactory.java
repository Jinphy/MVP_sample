package com.example.jinphy.mvp_sample.domain.filter;

import com.example.jinphy.mvp_sample.data.model.Task;
import com.example.jinphy.mvp_sample.tasks.TasksFilterType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * 过滤器工厂，生产各种过滤器
 * Created by jinphy on 2017/8/2.
 */

public class TaskFilterFactory {

    private static final Map<TasksFilterType,TaskFilter> FILTER = new HashMap<>(3);

    static {
        FILTER.put(TasksFilterType.ALL_TASKS, new AllTaskFilter());
        FILTER.put(TasksFilterType.ACTIVE_TASKS, new ActiveTaskFilter());
        FILTER.put(TasksFilterType.COMPLETED_TASKS, new CompleteTaskFilter());
    }

    /**
     * 工厂方法，生产各种顾虑器
     *
     * */
    public static TaskFilter produce(TasksFilterType type) {
        return FILTER.get(type);
    }

    //=============================================================\\

    //所有任务过滤器，即不顾虑
    private static class AllTaskFilter implements TaskFilter {
        @Override
        public List<Task> filter(List<Task> tasks) {

            return new ArrayList<>(tasks);
        }
    }

    // 完成的任务过滤器
    private static class CompleteTaskFilter implements TaskFilter {
        @Override
        public List<Task> filter(List<Task> tasks) {
            List<Task> result = new ArrayList<>();
            for (Task task : tasks) {
                if (task.isCompleted()) {
                    result.add(task);
                }
            }

            return result;
        }
    }

    // 活跃任务过滤器
    private static class ActiveTaskFilter implements TaskFilter {
        @Override
        public List<Task> filter(List<Task> tasks) {

            List<Task> result = new ArrayList<>();
            for (Task task : tasks) {
                if (task.isActive()) {
                    result.add(task);
                }
            }

            return result;
        }
    }

}
