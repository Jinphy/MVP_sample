package com.example.jinphy.mvp_sample.addedittask;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.jinphy.mvp_sample.R;

public class AddEditTaskActivity extends AppCompatActivity {

    public static final String EXTRA_EDIT_TASK_ID = "ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_task);
    }
}
