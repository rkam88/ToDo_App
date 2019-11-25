package net.rusnet.sb.todoapp.data.repository;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.provider.BaseColumns;

import net.rusnet.sb.todoapp.data.model.Task;

import java.util.ArrayList;
import java.util.List;

public class TasksRepository implements IDataSource {

    private Context mContext;

    public TasksRepository(Application application) {
        mContext = application.getApplicationContext();
    }

    @Override
    public void getAllTasks(final LoadTasksCallback callback) {
        new GetAllTasksAsyncTasks(mContext, new LoadTasksCallback() {
            @Override
            public void onTasksLoaded(List<Task> tasks) {
                callback.onTasksLoaded(tasks);
            }
        }).execute();
    }

    @Override
    public void addTask(Task task, final AddTaskCallback callback) {
        new AddTaskAsyncTask(task, mContext, new AddTaskCallback() {
            @Override
            public void onTaskAdded(Long newRowId) {
                callback.onTaskAdded(newRowId);
            }
        }).execute();
    }

    @Override
    public void updateTaskStatus(int taskId, boolean newStatus, final UptadeTaskCallback callback) {
        new UpdateTaskStatusAsyncTask(taskId, newStatus, mContext, new UptadeTaskCallback() {
            @Override
            public void onTaskUpdated(Long updatedRowsNumber) {
                callback.onTaskUpdated(updatedRowsNumber);
            }
        }).execute();
    }

    @Override
    public void deleteTask(int taskId, final UptadeTaskCallback callback) {
        new DeleteTaskAsyncTask(taskId, mContext, new UptadeTaskCallback() {
            @Override
            public void onTaskUpdated(Long updatedRowsNumber) {
                callback.onTaskUpdated(updatedRowsNumber);
            }
        }).execute();
    }

    private static class GetAllTasksAsyncTasks extends AsyncTask<Void, Void, List<Task>> {

        private Context mContext;
        private LoadTasksCallback mCallback;

        public GetAllTasksAsyncTasks(Context context, LoadTasksCallback callback) {
            this.mContext = context.getApplicationContext();
            mCallback = callback;
        }

        @Override
        protected void onPostExecute(List<Task> tasks) {
            mCallback.onTasksLoaded(tasks);
        }

        @Override
        protected List<Task> doInBackground(Void... voids) {
            SQLiteDatabase db = new TasksDBHelper(mContext).getReadableDatabase();

            // 2. Определяем данные по колонкам, которые необходимо запросить
            String[] projection = {
                    BaseColumns._ID,
                    TasksDBSchema.TasksTable.Cols.NAME,
                    TasksDBSchema.TasksTable.Cols.TASK_STATUS
            };
            // 3. Условие запроса
            String selection = "";
            String[] selectionArgs = {};
            // 4. Сортировка
            String sortOrder = BaseColumns._ID + " ASC";
            // 5. Получаем объект типа Курсор
            Cursor cursor = db.query(
                    TasksDBSchema.TasksTable.TABLE_NAME, // Название таблиы
                    projection, // Какие колонки нужны - columns
                    selection, // Условие
                    selectionArgs, // Значения условия
                    null, // группировка – groupBy
                    null, // фильтр (после группировки) - having
                    sortOrder // сортировка
            );

            List<Task> tasks = new ArrayList<>();
            try {
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                    String name = cursor.getString(cursor.getColumnIndex(TasksDBSchema.TasksTable.Cols.NAME));
                    int isCompletedInt = cursor.getInt(cursor.getColumnIndex(TasksDBSchema.TasksTable.Cols.TASK_STATUS));
                    boolean isCompleted = (isCompletedInt == 0) ? false : true;
                    tasks.add(new Task(id, name, isCompleted));
                }
            } finally {
                cursor.close();
            }
            return tasks;
        }
    }

    private static class AddTaskAsyncTask extends AsyncTask<Void, Void, Long> {

        private Context mContext;
        private AddTaskCallback mCallback;
        private Task mTaskToAdd;

        public AddTaskAsyncTask(Task task, Context context, AddTaskCallback callback) {
            this.mContext = context.getApplicationContext();
            mCallback = callback;
            mTaskToAdd = task;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            mCallback.onTaskAdded(aLong);
        }

        @Override
        protected Long doInBackground(Void... voids) {
            // Получаем базу данных
            SQLiteDatabase db = new TasksDBHelper(mContext).getReadableDatabase();
            // Создаем пары ключ-значения для добавления строки
            ContentValues values = new ContentValues();
            values.put(TasksDBSchema.TasksTable.Cols.NAME, mTaskToAdd.getName());
            values.put(TasksDBSchema.TasksTable.Cols.TASK_STATUS, mTaskToAdd.isCompleted() ? 1 : 0);

            // Вставляем новую строку, метод возвращает primary key добавленной строки или -1,
            // если вставка завершилась с ошибкой
            long newRowId = db.insert(TasksDBSchema.TasksTable.TABLE_NAME, null, values);
            return newRowId;
        }
    }

    private static class UpdateTaskStatusAsyncTask extends AsyncTask<Void, Void, Long> {

        private Context mContext;
        private UptadeTaskCallback mCallback;
        private int mTaskId;
        private boolean mIsCompleted;

        public UpdateTaskStatusAsyncTask(int taskId, boolean newStatus, Context context, UptadeTaskCallback callback) {
            this.mContext = context.getApplicationContext();
            mCallback = callback;
            mTaskId = taskId;
            mIsCompleted = newStatus;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            mCallback.onTaskUpdated(aLong);
        }

        @Override
        protected Long doInBackground(Void... voids) {
            SQLiteDatabase db = new TasksDBHelper(mContext).getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put(TasksDBSchema.TasksTable.Cols.TASK_STATUS, mIsCompleted ? 1 : 0);
            String selection = BaseColumns._ID + " = ?";
            String[] selectionArgs = {String.valueOf(mTaskId)};
            long count = db.update(
                    TasksDBSchema.TasksTable.TABLE_NAME,
                    values,
                    selection,
                    selectionArgs);
            return count;
        }
    }

    private static class DeleteTaskAsyncTask extends AsyncTask<Void, Void, Long> {

        private Context mContext;
        private UptadeTaskCallback mCallback;
        private int mTaskId;

        public DeleteTaskAsyncTask(int taskId, Context context, UptadeTaskCallback callback) {
            this.mContext = context.getApplicationContext();
            mCallback = callback;
            mTaskId = taskId;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            mCallback.onTaskUpdated(aLong);
        }

        @Override
        protected Long doInBackground(Void... voids) {
            SQLiteDatabase db = new TasksDBHelper(mContext).getReadableDatabase();
            String selection = BaseColumns._ID + " = ?";
            String[] selectionArgs = {String.valueOf(mTaskId)};
            long count = db.delete(
                    TasksDBSchema.TasksTable.TABLE_NAME,
                    selection,
                    selectionArgs);
            return count;
        }
    }
}
