package net.rusnet.sb.todoapp.presentation.view;

public interface IClickListener {

    void onCheckBoxClick(int position, boolean isCompleted);

    void onLongPress(int position);

}
