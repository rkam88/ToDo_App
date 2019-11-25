package net.rusnet.sb.todoapp.presentation.view;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.rusnet.sb.todoapp.R;
import net.rusnet.sb.todoapp.adapters.TasksAdapter;
import net.rusnet.sb.todoapp.data.model.Task;
import net.rusnet.sb.todoapp.data.repository.TasksRepository;
import net.rusnet.sb.todoapp.presentation.presentation.MainPresenter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements IClickListener, IMainActivity {
    private List<Task> mTasks = new ArrayList<>();

    private RecyclerView mRecyclerView;
    private TasksAdapter mAdapter;

    private MainPresenter mMainPresenter;

    @Override
    public void onCheckBoxClick(int position, boolean isCompleted) {
        mMainPresenter.changeStatus(mTasks.get(position).getId(), isCompleted);
    }

    @Override
    public void onLongPress(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.dialog_delete_this);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mMainPresenter.deleteTask(mTasks.get(position).getId());
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void updateRecycler(List<Task> taskList) {
        mTasks = taskList;
        mAdapter.setTasks(mTasks);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorToast() {
        Toast.makeText(this, R.string.toast_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initFAB();

        initRecyclerView();

        initPresenter();

    }

    private void initFAB() {
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddDialog();
            }
        });
    }

    private void openAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        final EditText editText = new EditText(this);
        builder.setView(editText);
        builder.setTitle(R.string.dialog_title);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String taskName = editText.getText().toString();
                if (taskName.isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.toast_empty_name, Toast.LENGTH_SHORT).show();
                } else {
                    mMainPresenter.addTask(new Task(taskName));
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recycler_view_tasks);
        mAdapter = new TasksAdapter(mTasks, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initPresenter() {
        mMainPresenter = new MainPresenter(this, new TasksRepository(this.getApplication()));
    }


}
