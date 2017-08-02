package com.example.jinphy.mvp_sample.addedittask;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.jinphy.mvp_sample.R;
import com.example.jinphy.mvp_sample.UseCaseHandler;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;
import com.example.jinphy.mvp_sample.data.source.local.TasksLocalDataSrouce;
import com.example.jinphy.mvp_sample.data.source.remote.TasksRemoteDataSource;
import com.example.jinphy.mvp_sample.domain.usecase.GetTask;
import com.example.jinphy.mvp_sample.domain.usecase.SaveTask;
import com.example.jinphy.mvp_sample.util.ActivityUtils;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_TASK_ID = "ID";

    public static final String SHOULD_LOAD_DATA_FROM_REPO_KEY = "SHOULD_LOAD_DATA_FROM_REPO_KEY";

    private AddEditTaskPresenter presenter;

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        String taskId = getIntent().getStringExtra(EXTRA_EDIT_TASK_ID);
        setToolbarTitle(taskId);

        // Get the fragment
        AddEditTaskFragment fragment =
                (AddEditTaskFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = AddEditTaskFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.contentFrame
            );
        }

        // Create the TasksRepository
        TasksRepository repository = TasksRepository.getInstance(
                TasksRemoteDataSource.getInstance(),
                TasksLocalDataSrouce.getInstance(this)
        );

        // Create useCase
        GetTask getTask = new GetTask(repository);
        SaveTask saveTask = new SaveTask(repository);
        UseCaseHandler useCaseHandler = UseCaseHandler.getInstance();


        //Create the presenter
        presenter = new AddEditTaskPresenter(
                taskId,
                fragment,
                getTask,
                saveTask,
                useCaseHandler
        );

    }

    private void setToolbarTitle(@Nullable String taskId) {
        if (taskId == null) {
            actionBar.setTitle(R.string.add_task);
        } else {
            actionBar.setTitle(R.string.edit_task);
        }
    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }


}
