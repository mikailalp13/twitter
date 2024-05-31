package screens;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.twitter.R;

import java.util.List;

import Models.Activity;
import Models.ActivityAdapter;
import Models.UserModel;
import Services.DatabaseServices;

public class NotificationsScreen extends AppCompatActivity {
/*
    private RecyclerView recyclerView;
    private ActivityAdapter activityAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_screen);

        recyclerView = findViewById(R.id.recycler_view_notifications);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        loadActivities();
    }

    private void loadActivities() {
        DatabaseServices.getActivities(new DatabaseServices.ActivityListener() {
            @Override
            public void onSuccess(List<Activity> activities) {
                activityAdapter = new ActivityAdapter(activities, new ActivityAdapter.UserListener() {
                    @Override
                    public void onClick(UserModel user) {
                    }
                });
                recyclerView.setAdapter(activityAdapter);
            }

            @Override
            public void onFailure(String errorMessage) {
            }

            @Override
            public void onCallback(List<Activity> activities) {

            }
        });
    } */
}
