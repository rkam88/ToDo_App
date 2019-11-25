package net.rusnet.sb.todoapp.data.repository;

public class TasksDBSchema {
    public static final class TasksTable {
        public static final String TABLE_NAME = "tasks";

        public static final class Cols {
            public static final String NAME = "name";
            public static final String TASK_STATUS = "task_status";
        }
    }

}
