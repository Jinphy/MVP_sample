package com.example.jinphy.mvp_sample.tasks;

import android.content.Intent;
import android.os.Debug;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ListFragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.jinphy.mvp_sample.R;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;
import com.example.jinphy.mvp_sample.data.source.local.TasksLocalDataSrouce;
import com.example.jinphy.mvp_sample.data.source.remote.TasksRemoteDataSource;
import com.example.jinphy.mvp_sample.statistics.StatisticActivity;
import com.example.jinphy.mvp_sample.util.ActivityUtils;
import com.example.jinphy.mvp_sample.util.inject.BindView;

public class TasksActivity extends AppCompatActivity {

    public static final String CURRENT_FILTERING_KEY = "CURRENT_FILTERING_KEY";

    private DrawerLayout drawerLayout;

    private TasksPresenter presenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);

        //Set up the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        //Set up the navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this::onMenuItemClick);
        }

        // Get the fragment
        TasksFragment tasksFragment = (TasksFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame);
        if (tasksFragment == null) {
            //Create the fragment,if null
            tasksFragment = TasksFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    tasksFragment,
                    R.id.contentFrame
            );
            // Create the repository
            TasksRepository repository = TasksRepository.getInstance(
                    TasksRemoteDataSource.getInstance(),
                    TasksLocalDataSrouce.getInstance(this) );

            // Create the presenter
            presenter = new TasksPresenter(repository, tasksFragment);

            // Load previously saved state, if available
            if (savedInstanceState != null) {
                TasksFilterType currentFilterType = (TasksFilterType) savedInstanceState
                        .getSerializable(CURRENT_FILTERING_KEY);
                presenter.setFiltering(currentFilterType);
            }
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CURRENT_FILTERING_KEY, presenter.getFiltering());
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.list_navigation_menu_item:
                // Do nothing, we're already on that screen
                break;
            case R.id.statistics_navigation_menu_item:
                Intent intent = new Intent(this, StatisticActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
        item.setChecked(true);
        drawerLayout.closeDrawers();
        return true;
    }

}
