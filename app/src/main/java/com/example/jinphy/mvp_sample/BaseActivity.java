package com.example.jinphy.mvp_sample;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinphy on 2017/7/30.
 */

public class BaseActivity extends AppCompatActivity {
    private static List<BaseActivity> activities = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activities.add(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activities.remove(this);
    }

    public void finishAll(){
        activities.forEach(Activity::finish);
    }


}
