package com.example.jinphy.mvp_sample.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.jinphy.mvp_sample.data.Task;
import com.example.jinphy.mvp_sample.data.source.TasksDataSource;
import com.example.jinphy.mvp_sample.data.source.local.TasksPersistenceContract.TaskEntry;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by jinphy on 2017/7/30.
 */

public class TasksLocalDataSrouce implements TasksDataSource{

    private static TasksLocalDataSrouce instance ;

    private TasksDBHelper helper;



    private TasksLocalDataSrouce(@NonNull Context context) {
        checkNotNull(context);
        helper = new TasksDBHelper(context);
    }

    public static TasksLocalDataSrouce getInstance(@NonNull Context context) {
        if (instance==null){
            synchronized (TasksLocalDataSrouce.class){
                instance = new TasksLocalDataSrouce(context);
            }
        }
        return instance;
    }

    public String[] getProjection(){
        String[] projection = {
                TaskEntry.COLUMN_NAME_ENTRY_ID,
                TaskEntry.COLUMN_NAME_TITLE,
                TaskEntry.COLUMN_NAME_DESCRIPTION,
                TaskEntry.COLUMN_NAME_COMPLETED
        };
        return projection;
    }


    @Override
    public void getTasks(@NonNull LoadTasksCallback callback) {

        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = getProjection();
        Cursor cursor = db.query(
                TaskEntry.TABLE_NAME, projection, null, null, null, null, null);

        List<Task> tasks = parseTasks(cursor,projection);

        if (cursor != null) {
            cursor.close();
        }
        db.close();
        if (tasks.isEmpty()) {
            callback.onDataNotAvailable();
        }else{
            callback.onTasksLoaded(tasks);
        }
    }

    private List<Task> parseTasks(Cursor cursor,String[] projection) {
        List<Task> tasks = new ArrayList<>();

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                tasks.add(parseTask(cursor,projection));
            }
        }
        return tasks;
    }

    private Task parseTask(Cursor cursor,String[] projection){
        String id = cursor.getString(cursor.getColumnIndexOrThrow(projection[0]));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(projection[1]));
        String descrprion = cursor.getString(cursor.getColumnIndexOrThrow(projection[2]));
        boolean completed = cursor.getInt(cursor.getColumnIndexOrThrow(projection[3]))==1;

        return new Task(title, descrprion, id, completed);
    }

    @Override
    public void getTask(@NonNull String taskId, @NonNull GetTaskCallback callback) {
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = getProjection();

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " like ?";
        String[] selectionArgs = {taskId};

        Cursor cursor =
                db.query(TaskEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

        Task task = null;

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            task = parseTask(cursor,projection);
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        if (task == null) {
            callback.onDataNotAvailable();
        } else {
            callback.onTaskLoaded(task);
        }

    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = getProjection();

        ContentValues values = new ContentValues();
        values.put(projection[0],task.getId());
        values.put(projection[1],task.getTitle());
        values.put(projection[2],task.getDescription());
        values.put(projection[3],task.isCompleted());

        db.insert(TaskEntry.TABLE_NAME, null, values);

        db.close();
    }

    @Override
    public void completeTask(@NonNull Task task) {
        checkNotNull(task);

        changeCompleteState(task.getId(),true);

    }

    @Override
    public void completeTask(@NonNull String taskId) {
        changeCompleteState(taskId,true);
    }

    private void changeCompleteState(String  taskId, boolean completionState){
        SQLiteDatabase db =helper.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(TaskEntry.COLUMN_NAME_COMPLETED,completionState);

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " like ? ";
        String[] selectionArgs = {taskId};

        db.update(TaskEntry.TABLE_NAME, values, selection, selectionArgs);
        db.close();
    }

    @Override
    public void activateTask(@NonNull Task task) {
        checkNotNull(task);
        changeCompleteState(task.getId(),false);
    }

    @Override
    public void activateTask(@NonNull String taskId) {
        changeCompleteState(taskId,false);
    }

    @Override
    public void clearCompletedTasks() {
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = TaskEntry.COLUMN_NAME_COMPLETED+" like? ";
        String[] selectionArgs = {"1"};

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    @Override
    public void refreshTasks() {

    }

    @Override
    public void deleteAllTasks() {
        SQLiteDatabase db = helper.getReadableDatabase();

        db.delete(TaskEntry.TABLE_NAME, null, null);

        db.close();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        SQLiteDatabase db  =helper.getReadableDatabase();

        String selection = TaskEntry.COLUMN_NAME_ENTRY_ID + " like? ";
        String[] selectionArgs = {taskId};

        db.delete(TaskEntry.TABLE_NAME, selection, selectionArgs);

        db.close();
    }
}
