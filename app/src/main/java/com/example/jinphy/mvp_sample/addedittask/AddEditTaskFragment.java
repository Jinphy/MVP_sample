package com.example.jinphy.mvp_sample.addedittask;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jinphy.mvp_sample.R;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * A simple {@link Fragment} subclass
 * Use the {@link AddEditTaskFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddEditTaskFragment extends Fragment implements AddEditTaskContract.View{

    private AddEditTaskContract.Presenter presenter;

    private TextInputEditText titleText;

    private TextInputEditText descriptionText;




    public AddEditTaskFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddEditTaskFragment.
     */
    public static AddEditTaskFragment newInstance() {
        return new AddEditTaskFragment();
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
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_add_edit_task, container, false);

        titleText = root.findViewById(R.id.add_task_title);
        descriptionText = root.findViewById(R.id.add_task_description);

        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton fab =  getActivity().findViewById(R.id.fab_edit_task_done);
        fab.setImageResource(R.drawable.ic_done);
        fab.setOnClickListener(view -> presenter.saveTask(titleText.getText().toString(), descriptionText.getText().toString()));

    }

    @Override
    public void setPresenter(@NonNull AddEditTaskContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void showEmptyTaskError() {
        Snackbar.make(getView(),getString(R.string.empty_task_message),Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showTaskList() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void setTitle(String title) {
        titleText.setVisibility(View.VISIBLE);
        titleText.setText(title);
    }

    @Override
    public void setDescription(String description) {
        descriptionText.setVisibility(View.VISIBLE);
        descriptionText.setText(description);
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }
}
