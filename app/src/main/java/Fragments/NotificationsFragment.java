package Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twitter.R;

import java.util.List;

import Models.Activity;
import Models.ActivityAdapter;
import Models.UserModel;
import Services.DatabaseServices;

public class NotificationsFragment extends Fragment {

    private static final String ARG_USER_ID = "currentUserId";
    private String currentUserId;
    private RecyclerView recyclerView;
    private ActivityAdapter activityAdapter;

    public static NotificationsFragment newInstance(String userId) {
        NotificationsFragment fragment = new NotificationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        recyclerView = view.findViewById(R.id.recycler_view_notifications);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        loadActivities();

        return view;
    }

    private void loadActivities() {
        DatabaseServices.getActivities(new DatabaseServices.ActivityListener() {
            @Override
            public void onSuccess(List<Activity> activities) {
                activityAdapter = new ActivityAdapter(activities, new ActivityAdapter.UserListener() {
                    @Override
                    public void onClick(UserModel user) {
                        // Handle user click
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
    }


}
