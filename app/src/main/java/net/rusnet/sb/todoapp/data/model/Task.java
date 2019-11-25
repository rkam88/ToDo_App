package net.rusnet.sb.todoapp.data.model;

public class Task {

    private int mId;
    private String mName;
    private boolean mIsCompleted;

    public Task(String name) {
        this(-1, name, false);
    }

    public Task(int id, String name) {
        this(id, name, false);
    }

    public Task(int id, String name, boolean isCompleted) {
        mId = id;
        mName = name;
        mIsCompleted = isCompleted;
    }

    public int getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public boolean isCompleted() {
        return mIsCompleted;
    }

    public void setCompleted(boolean completed) {
        mIsCompleted = completed;
    }
}
