package com.example.jinphy.mvp_sample.tasks;

/**
 * Created by jinphy on 2017/7/30.
 */

public enum TasksFilterType {

    /**
     * Do not filter tasks
     *
     * */
    ALL_TASKS,

    /**
     * Filter only the active tasks(not completed yet).
     * */
    ACTIVE_TASKS,

    /**
     * Filter only the completed tasks.
     * */
    COMPLETED_TASKS
}
