package com.example.jinphy.mvp_sample.taskdetail;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jinphy.mvp_sample.R;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "id";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
    }
}
