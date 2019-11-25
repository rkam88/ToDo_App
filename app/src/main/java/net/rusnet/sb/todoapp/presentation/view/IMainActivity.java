package net.rusnet.sb.todoapp.presentation.view;

import net.rusnet.sb.todoapp.data.model.Task;

import java.util.List;

public interface IMainActivity {

    public void updateRecycler(List<Task> taskList);

    public void showErrorToast();

}
