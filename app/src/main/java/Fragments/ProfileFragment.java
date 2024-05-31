package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.twitter.R;

import java.util.List;

import Models.Tweet;
import Models.TweetAdapter;
import Models.UserModel;
import Services.AuthService;
import Services.DatabaseServices;
import screens.WelcomeScreen;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private String currentUserId;
    private String visitedUserId;
    private int followersCount = 0;
    private int followingCount = 0;
    private boolean isFollowing = false;
    private List<Tweet> allTweets;
    private List<Tweet> mediaTweets;
    private TextView followersTextView;
    private TextView followingTextView;
    private RecyclerView recyclerView;
    private TweetAdapter tweetAdapter;
    private Button editButton;

    public static ProfileFragment newInstance(String currentUserId, String visitedUserId) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString("currentUserId", currentUserId);
        args.putString("visitedUserId", visitedUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        currentUserId = getArguments().getString("currentUserId");
        visitedUserId = getArguments().getString("visitedUserId");

        followersTextView = view.findViewById(R.id.text_followers_count);
        followingTextView = view.findViewById(R.id.text_following_count);
        recyclerView = view.findViewById(R.id.recycler_view_tweets);

        if (followersTextView == null || followingTextView == null || recyclerView == null || editButton == null) {
            Log.e(TAG, "One or more views are not initialized properly");
            return;
        }


        loadProfileInfo();
        setupRecyclerView();
        loadTweets();
    }

    private void loadProfileInfo() {
        String userName = "John Doe";
        String bio = "Hi.";
        int followers = 12;
        int following = 13;

        if (followersTextView != null && followingTextView != null) {
            followersTextView.setText("Followers: " + followers);
            followingTextView.setText("Following: " + following);
        } else {
            Log.e(TAG, "TextViews are null in loadProfileInfo");
        }
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        // RecyclerView için adapter'ı burada oluşturuyoruz, ancak veriler henüz yüklenmedi.
        tweetAdapter = new TweetAdapter(getContext(), allTweets, currentUserId, new TweetAdapter.UserClickListener() {
            @Override
            public void onClick(UserModel user) {
                // Kullanıcı tıklamasını işle
            }
        });
        recyclerView.setAdapter(tweetAdapter);
    }

    private void loadTweets() {
        DatabaseServices.getUserTweets(visitedUserId, new DatabaseServices.FirebaseCallback<List<Tweet>>() {
            @Override
            public void onCallback(List<Tweet> data) {
                allTweets = data;
                // Veriler yüklendikten sonra RecyclerView'ı güncelle
                tweetAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e(TAG, "Error loading tweets: " + errorMessage);
            }
        });
    }

    private void followOrUnfollow() {
        if (isFollowing) {
            unFollowUser();
        } else {
            followUser();
        }
    }

    private void unFollowUser() {
        // Unfollow the visited user
        // Update isFollowing and followersCount
    }

    private void followUser() {
        // Follow the visited user
        // Update isFollowing and followersCount
    }

    private void showPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        popupMenu.inflate(R.menu.bottom_navigation_menu);
        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_log_out) {
                logout();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void logout() {
        AuthService.logout();
        startActivity(new Intent(getContext(), WelcomeScreen.class));
        getActivity().finish();
    }
}
