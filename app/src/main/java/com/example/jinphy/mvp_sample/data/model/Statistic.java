package com.example.jinphy.mvp_sample.data.model;

/**
 * Created by jinphy on 2017/8/2.
 */

public class Statistic {

    private final int numOfCompletedTasks;

    private final int numOfActiveTasks;

    public Statistic(int numOfCompletedTasks, int numOfActiveTasks) {
        this.numOfActiveTasks = numOfActiveTasks;
        this.numOfCompletedTasks = numOfCompletedTasks;
    }

    public int getNumOfCompletedTasks() {
        return numOfCompletedTasks;
    }

    public int getNumOfActiveTasks() {
        return numOfActiveTasks;
    }
}
