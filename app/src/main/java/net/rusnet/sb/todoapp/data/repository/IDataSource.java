package net.rusnet.sb.todoapp.data.repository;

import net.rusnet.sb.todoapp.data.model.Task;

import java.util.List;

public interface IDataSource {

    interface LoadTasksCallback {
        void onTasksLoaded(List<Task> tasks);
    }

    interface AddTaskCallback {
        void onTaskAdded(Long newRowId);
    }

    interface UptadeTaskCallback {
        void onTaskUpdated(Long updatedRowsNumber);
    }

    void getAllTasks(final LoadTasksCallback callback);

    void addTask(Task task, final AddTaskCallback callback);

    void updateTaskStatus(int taskId, boolean newStatus, final UptadeTaskCallback callback);

    void deleteTask(int taskId, final UptadeTaskCallback callback);


}
