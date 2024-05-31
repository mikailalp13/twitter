package Models;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.twitter.R;

import java.util.List;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.ViewHolder> {

    private List<Activity> activities;
    private UserListener userListener;

    public ActivityAdapter(List<Activity> activities, UserListener userListener) {
        this.activities = activities;
        this.userListener = userListener;
    }

    public interface UserListener {
        void onClick(UserModel user);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView activityText;

        ViewHolder(View itemView) {
            super(itemView);
            activityText = itemView.findViewById(R.id.text_activity);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_activity, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Activity activity = activities.get(position);
        holder.activityText.setText(activity.getText());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userListener != null) {
                    userListener.onClick(activity.getUser());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return activities.size();
    }
}
