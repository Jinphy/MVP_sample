package com.example.jinphy.mvp_sample.statistics;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jinphy.mvp_sample.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StatisticFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StatisticFragment extends Fragment implements StatisticContract.View {

    private TextView statisticView;

    private StatisticContract.Presenter presenter;



    public StatisticFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StatisticFragment.
     */
    public static StatisticFragment newInstance() {
        return new StatisticFragment();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_statistic, container, false);
        statisticView = root.findViewById(R.id.statistics);

        return root;

    }

    @Override
    public void setPresenter(StatisticContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setProgressIndicator(boolean active) {
        if (active) {
            statisticView.setText(getString(R.string.loading));
        } else {
            statisticView.setText("");
        }
    }

    @Override
    public void showStatistics(int numberOfIncompleteTasks, int numberOfCompleteTasks) {
        if (numberOfCompleteTasks == 0 && numberOfIncompleteTasks == 0) {
            statisticView.setText(getString(R.string.statistics_no_tasks));
        } else {
            String displayString = getString(R.string.statistics_active_tasks);
            displayString+=" "+numberOfIncompleteTasks+"\n";
            displayString += getString(R.string.statistics_completed_tasks);
            displayString += " "+numberOfCompleteTasks;
            statisticView.setText(displayString);
        }
    }

    @Override
    public void showLoadingStatisticError() {
        statisticView.setText(getString(R.string.statistics_error));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
