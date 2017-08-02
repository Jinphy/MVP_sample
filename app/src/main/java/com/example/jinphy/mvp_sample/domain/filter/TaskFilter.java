package com.example.jinphy.mvp_sample.domain.filter;

import com.example.jinphy.mvp_sample.data.model.Task;

import java.util.List;

/**
 * Created by jinphy on 2017/8/2.
 */

public interface TaskFilter {

    List<Task> filter(List<Task> tasks);

}

