package net.rusnet.sb.todoapp.presentation.presentation;

import net.rusnet.sb.todoapp.data.model.Task;
import net.rusnet.sb.todoapp.data.repository.IDataSource;
import net.rusnet.sb.todoapp.data.repository.TasksRepository;
import net.rusnet.sb.todoapp.presentation.view.IMainActivity;

import java.lang.ref.WeakReference;
import java.util.List;

public class MainPresenter {

    private WeakReference<IMainActivity> mMainActivityWeakReference;
    private IDataSource mTasksRepository;

    public MainPresenter(IMainActivity mainActivity, TasksRepository tasksRepository) {
        mMainActivityWeakReference = new WeakReference<IMainActivity>(mainActivity);
        mTasksRepository = tasksRepository;

        getTasks();
    }

    public void getTasks() {
        mTasksRepository.getAllTasks(new IDataSource.LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                IMainActivity iMainActivity = mMainActivityWeakReference.get();
                if (iMainActivity != null) {
                    iMainActivity.updateRecycler(tasks);
                }
            }
        });
    }

    public void addTask(Task task) {
        mTasksRepository.addTask(task, new IDataSource.AddTaskCallback() {
            @Override
            public void onTaskAdded(Long newRowId) {
                if (newRowId != -1) {
                    getTasks();
                } else {
                    showError();
                }
            }
        });
    }

    public void changeStatus(int taskId, boolean isCompleted) {
        mTasksRepository.updateTaskStatus(taskId, isCompleted, new IDataSource.UptadeTaskCallback() {
            @Override
            public void onTaskUpdated(Long updatedRowsNumber) {
                if (updatedRowsNumber != 1) {
                    showError();
                } else {
                    getTasks();
                }
            }
        });
    }

    public void deleteTask(int taskId) {
        mTasksRepository.deleteTask(taskId, new IDataSource.UptadeTaskCallback() {
            @Override
            public void onTaskUpdated(Long updatedRowsNumber) {
                if (updatedRowsNumber != 1) {
                    showError();
                } else {
                    getTasks();
                }
            }
        });
    }

    private void showError() {
        IMainActivity iMainActivity = mMainActivityWeakReference.get();
        if (iMainActivity != null) {
            iMainActivity.showErrorToast();
        }
    }


}
