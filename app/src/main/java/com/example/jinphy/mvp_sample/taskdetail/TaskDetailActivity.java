package com.example.jinphy.mvp_sample.taskdetail;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.jinphy.mvp_sample.R;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;
import com.example.jinphy.mvp_sample.data.source.local.TasksLocalDataSrouce;
import com.example.jinphy.mvp_sample.data.source.remote.TasksRemoteDataSource;
import com.example.jinphy.mvp_sample.util.ActivityUtils;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        // Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        // Get the requested task id
        String taskId = getIntent().getStringExtra(EXTRA_TASK_ID);

        // Get the fragment
        TaskDetailFragment fragment =
                (TaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (fragment == null) {
            fragment = TaskDetailFragment.newInstance(taskId);
            ActivityUtils.addFragmentToActivity(getSupportFragmentManager(),fragment,R.id.contentFrame);
        }

        // Get the repository
        TasksRepository repository = TasksRepository.getInstance(
                TasksRemoteDataSource.getInstance(), TasksLocalDataSrouce.getInstance(this)
        );
        // Create the presenter
        TaskDetailPresenter presenter = new TaskDetailPresenter(
                taskId,
                repository,
                fragment );

    }

    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed();
        return true;
    }
}









