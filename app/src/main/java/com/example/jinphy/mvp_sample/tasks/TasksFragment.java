package com.example.jinphy.mvp_sample.tasks;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jinphy.mvp_sample.R;
import com.example.jinphy.mvp_sample.addedittask.AddEditTaskActivity;
import com.example.jinphy.mvp_sample.data.Task;
import com.example.jinphy.mvp_sample.taskdetail.TaskDetailActivity;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;


/**
 * Created by jinphy on 2017/7/31.
 */

public class TasksFragment extends Fragment implements TasksContract.View {

    private TasksContract.Presenter presenter;// The presenter of MVP

    private TasksAdapter listAdapter;// The listView adapter

    private ListView listView; // The listView

    private View noTaskView; // The view showing when there is no any task

    private ImageView noTaskIcon; // the icon showing when there is no any task

    private TextView noTaskMainView; // ......

    private TextView noTaskAddView; // the view used to make an action to add task when no task

    private LinearLayout tasksView; // the task view, which contain the listView

    private TextView filteringLabelView;

    public TasksFragment(){
        // Requires empty public constructor
    }

    public static TasksFragment newInstance(){
        return new TasksFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listAdapter = new TasksAdapter(new ArrayList<>(), itemListener);
    }


    @Override
    public void onResume(){
        super.onResume();
        if (presenter==null){
            Intent intent = new Intent(getActivity(), TasksActivity.class);
            startActivity(intent);
            getActivity().finish();
        }
        presenter.start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        presenter.result(requestCode,resultCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_tasks, container, false);

        // Set up tasks view
        listView = root.findViewById(R.id.tasks_list);
        listView.setAdapter(listAdapter);
        filteringLabelView = root.findViewById(R.id.filteringLabel);
        tasksView = root.findViewById(R.id.tasksLL);

        // Set up no tasks view
        noTaskView = root.findViewById(R.id.noTasks);
        noTaskIcon = root.findViewById(R.id.noTasksIcon);
        noTaskMainView = root.findViewById(R.id.noTasksMain);
        noTaskAddView = root.findViewById(R.id.noTasksAdd);
        noTaskAddView.setOnClickListener(v->showAddTask());

        // Set up floating action button
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_task);
        fab.setImageResource(R.drawable.ic_add);
        fab.setOnClickListener(v->presenter.addNewTask());

        // Set up progress indicator
        final ScrollChildSwipeRefreshLayout swipeRefreshLayout
                = root.findViewById(R.id.refresh_layout);
        swipeRefreshLayout.setColorSchemeColors(
                ContextCompat.getColor(getActivity(),R.color.colorPrimary),
                ContextCompat.getColor(getActivity(),R.color.colorAccent),
                ContextCompat.getColor(getActivity(),R.color.colorPrimaryDark),
                ContextCompat.getColor(getActivity(),R.color.purple),
                ContextCompat.getColor(getActivity(),R.color.green)
        );
        // Set the scrolling view in the custom SwipeRefreshLayout
        swipeRefreshLayout.setScrollUpChild(listView);

        swipeRefreshLayout.setOnRefreshListener(()->presenter.loadTasks(false));

        // Must set to true,if you want to use options menu in the fragment
        setHasOptionsMenu(true);

        return root;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_clear:
                presenter.clearCompletedTasks();
                break;
            case R.id.menu_filter:
                showFilteringPopUpMenu();
                break;
            case R.id.menu_refresh:
                presenter.loadTasks(true);
                break;
            default:
                break;
        }
        return true;
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu,menu);
    }






    @Override
    public void setPresenter(TasksContract.Presenter presenter) {
        this.presenter = checkNotNull(presenter);
    }

    @Override
    public void setLoadingIndicator(final boolean active) {
        if (getView() == null) {
            return;
        }
        final SwipeRefreshLayout refreshLayout = getView().findViewById(R.id.refresh_layout);

        // Make sure setRefreshing() is called after the layout is done
        // with everything else.
        refreshLayout.post(() -> refreshLayout.setRefreshing(active));
    }

    @Override
    public void showTasks(List<Task> tasks) {
        listAdapter.replaceData(tasks);

        tasksView.setVisibility(View.VISIBLE);
        noTaskView.setVisibility(View.GONE);
    }

    @Override
    public void showAddTask() {
        Intent intent = new Intent(getContext(),AddEditTaskActivity.class);
        startActivityForResult(intent,TasksPresenter.REQUEST_ADD_TASK);

    }

    @Override
    public void showTaskDetailsUi(String taskId) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskId);
        startActivity(intent);
    }

    @Override
    public void showTaskMarkedComplete() {
        showMessage(getString(R.string.task_marked_complete));
    }

    @Override
    public void showTaskMarkedActive() {
        showMessage(getString(R.string.task_marked_active));
    }

    @Override
    public void showCompletedTasksCleared() {
        showMessage(getString(R.string.completed_tasks_cleared));
    }

    @Override
    public void showLoadingTasksError() {
        showMessage(getString(R.string.loading_tasks_error));
        setLoadingIndicator(false);
    }

    private void showMessage(String message) {
        Snackbar.make(getView(),message,Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showNoTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_all),
                R.drawable.ic_assignment_turned_in_24dp,
                false
        );
    }

    @Override
    public void showActiveFilterLabel() {
        filteringLabelView.setText(getResources().getString(R.string.label_active));
    }

    @Override
    public void showCompletedFilterLabel() {
        filteringLabelView.setText(getResources().getString(R.string.label_completed));
    }

    @Override
    public void showAllFilterLabel() {
        filteringLabelView.setText(getResources().getString(R.string.label_all));
    }

    @Override
    public void showNoActiveTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_active),
                R.drawable.ic_check_circle_24dp,
                false
        );

    }

    @Override
    public void showNoCompletedTasks() {
        showNoTasksViews(
                getResources().getString(R.string.no_tasks_completed),
                R.drawable.ic_verified_user_24dp,
                false
        );

    }

    private void showNoTasksViews(String mainText, int iconRes, boolean showAddView) {
        tasksView.setVisibility(View.GONE);
        noTaskView.setVisibility(View.VISIBLE);

        noTaskMainView.setText(mainText);
        noTaskIcon.setImageBitmap(BitmapFactory.decodeResource(getResources(),iconRes));
        noTaskAddView.setVisibility(showAddView?View.VISIBLE:View.GONE);

    }

    @Override
    public void showSuccssfullySavedMessage() {
        showMessage(getString(R.string.successfully_saved_task_message));
    }

    @Override
    public boolean isActive() {
        return isAdded();
    }

    @Override
    public void showFilteringPopUpMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(this::onPopupMenuItemClick);

        popup.show();

    }

    public boolean onPopupMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.active:
                presenter.setFiltering(TasksFilterType.ACTIVE_TASKS);
                break;
            case R.id.completed:
                presenter.setFiltering(TasksFilterType.COMPLETED_TASKS);
                break;
            default:
                presenter.setFiltering(TasksFilterType.ALL_TASKS);
                break;
        }
        presenter.loadTasks(false);
        return true;
    }


    TaskItemListener itemListener = new TaskItemListener() {
        @Override
        public void onTaskClick(Task clickedTask) {
            presenter.openTaskDetails(clickedTask);
        }

        @Override
        public void onCompleteTaskClick(Task completedTask) {
            presenter.completeTask(completedTask);
        }

        @Override
        public void onActivateTaskClick(Task activatedTask) {
            presenter.activateTask(activatedTask);
        }
    };



    private static class TasksAdapter extends BaseAdapter{

        private List<Task> tasks;
        private TaskItemListener itemListener;

        public TasksAdapter(List<Task> tasks, TaskItemListener itemListener) {
            this.tasks = checkNotNull(tasks);
            this.itemListener = checkNotNull(itemListener);
        }
        @Override
        public int getCount() {
            return tasks.size();
        }

        @Override
        public Task getItem(int i) {
            return tasks.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup parent) {

            // Get the current item.
            Task task = tasks.get(i);

            // Create or get a view holder.
            ViewHolder holder = createViewHolder(convertView, parent);

            // bind view holder with data and listener
            bindViewHolder(holder,task);

            // return the item view
            return holder.view;
        }

        private ViewHolder createViewHolder(View view,ViewGroup parent){

            ViewHolder holder ;
            if (view == null) {
                view = LayoutInflater
                        .from(parent.getContext())
                        .inflate(R.layout.task_item, parent, false);
                holder = new ViewHolder();
                holder.title = view.findViewById(R.id.title);
                holder.complete = view.findViewById(R.id.complete);
                holder.itemView = view.findViewById(R.id.item);
                holder.view = view;
                view.setTag(holder);
            }else{
                holder = (ViewHolder) view.getTag();
            }
            return holder;
        }

        private void bindViewHolder(ViewHolder holder, Task task) {

            if (task.isCompleted()) {
                holder.itemView.setBackgroundResource(R.drawable.list_completed_touch_feedback);
            }else{
                holder.itemView.setBackgroundResource(R.drawable.touch_feedback);
            }
            holder.itemView.setOnClickListener(v -> itemListener.onTaskClick(task));

            holder.title.setText(task.getTitleForList());
            holder.complete.setChecked(task.isCompleted());
            holder.complete.setOnClickListener(v -> {
                if (!task.isCompleted()) {
                    itemListener.onCompleteTaskClick(task);
                }else{
                    itemListener.onActivateTaskClick(task);
                }
            });

        }


        public void replaceData(List<Task> tasks) {
            this.tasks = checkNotNull(tasks);
            notifyDataSetChanged();
        }
    }

    private static class ViewHolder{
        TextView title;
        CheckBox complete;
        View itemView;
        View view;

    }



    public interface TaskItemListener{
        void onTaskClick(Task clickedTask);

        void onCompleteTaskClick(Task completedTask);

        void onActivateTaskClick(Task activatedTask);

    }
}
