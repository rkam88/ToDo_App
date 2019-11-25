package net.rusnet.sb.todoapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.sb.todoapp.R;
import net.rusnet.sb.todoapp.data.model.Task;
import net.rusnet.sb.todoapp.presentation.view.IClickListener;

import java.util.ArrayList;
import java.util.List;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder> {

    private List<Task> mTasks;
    private IClickListener mClickListener;

    public TasksAdapter(List<Task> tasks, IClickListener clickListener) {
        mTasks = tasks;
        mClickListener = clickListener;
    }

    public void setTasks(List<Task> tasks) {
        mTasks = new ArrayList<>(tasks);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_task, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        Task task = mTasks.get(position);

        TextView textView = viewHolder.mTextViewName;
        textView.setText(task.getName());
        CheckBox checkBox = viewHolder.mCheckBoxCompleted;
        checkBox.setChecked(task.isCompleted());

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mClickListener.onCheckBoxClick(position, isChecked);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private CheckBox mCheckBoxCompleted;
        private TextView mTextViewName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTextViewName = itemView.findViewById(R.id.text_view_task_name);
            mCheckBoxCompleted = itemView.findViewById(R.id.checkbox_completed);

            itemView.setOnLongClickListener(this);
        }

        @Override
        public boolean onLongClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                mClickListener.onLongPress(position);
                return true;
            }
            return false;
        }
    }


}
