package com.example.jinphy.mvp_sample.data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import java.util.UUID;

/**
 * Created by jinphy on 2017/7/30.
 */

public class Task {

    @NonNull private  final String id;

    @Nullable private final String title;

    @Nullable private final String description;

    private final boolean completed;


    public Task(
            @Nullable String title,
            @Nullable String description,
            @NonNull String id,
            boolean completed){
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
    }

    public Task(
            @Nullable String title,
            @Nullable String description,
            @NonNull String id){
        this(title,description,id,false);
    }

    public Task(@Nullable String title, @Nullable String description) {
        this(title,description, UUID.randomUUID().toString(),false);
    }

    public Task(@Nullable String title, @Nullable String description, boolean completed) {
        this(title, description, UUID.randomUUID().toString(), completed);
    }

    @NonNull public String getId(){
        return id;
    }
    @Nullable String getTitle(){
        return title;
    }
    @Nullable public String getTitleForList(){
        if (!Strings.isNullOrEmpty(title)) {
            return title;
        } else {
            return description;
        }
    }

    @Nullable public String getDescription(){
        return description;
    }

    public boolean isCompleted(){
        return completed;
    }

    public boolean isActive(){
        return !completed;
    }

    public boolean isEmpty(){
        return Strings.isNullOrEmpty(title) &&
                Strings.isNullOrEmpty(description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Task task = (Task) o;
        return Objects.equal(id, task.id) &&
                Objects.equal(title, task.title) &&
                Objects.equal(description, task.description);
    }

    @Override
    public int hashCode() {

        return Objects.hashCode(id, title, description);
    }

    @Override
    public String toString(){
        return "Task with title "+title;
    }

}











