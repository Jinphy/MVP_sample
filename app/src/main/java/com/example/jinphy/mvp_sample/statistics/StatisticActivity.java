package com.example.jinphy.mvp_sample.statistics;

import android.support.design.widget.NavigationView;
import android.support.v4.app.NavUtils;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.jinphy.mvp_sample.R;
import com.example.jinphy.mvp_sample.UseCaseHandler;
import com.example.jinphy.mvp_sample.data.source.TasksRepository;
import com.example.jinphy.mvp_sample.data.source.local.TasksLocalDataSrouce;
import com.example.jinphy.mvp_sample.data.source.remote.TasksRemoteDataSource;
import com.example.jinphy.mvp_sample.domain.usecase.GetStatistic;
import com.example.jinphy.mvp_sample.util.ActivityUtils;

public class StatisticActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);

        // Set up toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(R.string.statistics_title);
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);


        // Set up the navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.setStatusBarBackground(R.color.colorPrimaryDark);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }

        // Get the fragment
        StatisticFragment fragment = ((StatisticFragment)
                getSupportFragmentManager().findFragmentById(R.id.contentFrame));
        if (fragment == null) {
            fragment = StatisticFragment.newInstance();
            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(),
                    fragment,
                    R.id.contentFrame
            );
        }

        // Get the repository
        TasksRepository repository = TasksRepository.getInstance(
                TasksRemoteDataSource.getInstance(),
                TasksLocalDataSrouce.getInstance(this)
        );


        // Create useCase
        GetStatistic getStatistic = new GetStatistic(repository);
        UseCaseHandler useCaseHandler = UseCaseHandler.getInstance();

        // Create the presenter
        new StatisticPresenter(
                fragment,
                getStatistic,
                useCaseHandler
        );


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Log.d("Main", "menu clicked");
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.list_navigation_menu_item:
//                    NavUtils.navigateUpFromSameTask(StatisticActivity.this);
                    onBackPressed();
                    break;
                case R.id.statistics_navigation_menu_item:

                    break;
                default:
                    break;
            }
            item.setChecked(true);
            drawerLayout.closeDrawers();
            return true;
        });
    }
}









