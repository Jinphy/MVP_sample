package com.example.jinphy.mvp_sample.taskdetail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.jinphy.mvp_sample.R;
import com.example.jinphy.mvp_sample.addedittask.AddEditTaskActivity;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TaskDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TaskDetailFragment extends Fragment implements TaskDetailContract.View{

    @NonNull
    private static final String ARGUMENT_TASK_ID = "ID";

    @NonNull
    private static final int REQUEST_EDIT_TASK = 1;

    private TaskDetailContract.Presenter presenter;

    private TextView detailTile;

    private TextView detailDescription;

    private CheckBox detailCompleteStatus;










    public TaskDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TaskDetailFragment.
     */
    public static TaskDetailFragment newInstance(@NonNull String taskId) {

        Bundle args = new Bundle();
        args.putString(ARGUMENT_TASK_ID,taskId);

        TaskDetailFragment fragment = new TaskDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume(){
        super.onResume();
        presenter.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_task_detail, container, false);

        setHasOptionsMenu(true);
        // Find view by id
        detailTile = root.findViewById(R.id.task_detail_title);
        detailDescription = root.findViewById(R.id.task_detail_description);
        detailCompleteStatus = root.findViewById(R.id.task_detail_complete);

        // Set up floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_edit_task);
        fab.setOnClickListener(view -> presenter.editTask());


        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_delete:
                presenter.deleteTask();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu,menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_EDIT_TASK:
                if (resultCode == Activity.RESULT_OK) {
                    getActivity().finish();
                }
        }
    }


    @Override
    public void setPresenter(TaskDetailContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(boolean active) {
        if (active) {
            detailTile.setText("");
            detailDescription.setText(getString(R.string.loading));
        }
    }

    @Override
    public void showMissingTask() {
        detailTile.setText("");
        detailDescription.setText(getString(R.string.no_data));

    }

    @Override
    public void hideTitle() {
        detailTile.setVisibility(View.GONE);
    }

    @Override
    public void showTitle(String title) {
        detailTile.setVisibility(View.VISIBLE);
        detailTile.setText(title);

    }

    @Override
    public void hideDescription() {
        detailDescription.setVisibility(View.GONE);
    }

    @Override
    public void showDescription(String description) {
        detailDescription.setVisibility(View.VISIBLE);
        detailDescription.setText(description);
    }

    @Override
    public void showCompletionStatus(final boolean complete) {
        checkNotNull(detailCompleteStatus);

        detailCompleteStatus.setChecked(complete);
        detailCompleteStatus.setOnCheckedChangeListener((button,isChecked)->{
            if (isChecked) {
                presenter.completeTask();
            }else {
                presenter.activateTask();
            }
        });
    }

    @Override
    public void showEditTask(String taskId) {
        Intent intent = new Intent(getContext(), AddEditTaskActivity.class);
        intent.putExtra(AddEditTaskActivity.EXTRA_EDIT_TASK_ID, taskId);
        startActivityForResult(intent,REQUEST_EDIT_TASK);
    }

    @Override
    public void showTaskDeleted() {
        getActivity().finish();
    }

    @Override
    public void showTaskMarkedComplete() {
        Snackbar.make(getView(),getString(R.string.task_marked_complete),Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTaskMarkedActive() {
        Snackbar.make(getView(),getString(R.string.task_marked_active),Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
