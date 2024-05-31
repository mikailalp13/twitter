package screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.twitter.R;

import java.util.List;

import Fragments.ProfileFragment;
import Models.Tweet;
import Models.TweetAdapter;
import Models.UserModel;
import Services.AuthService;

public class ProfileScreen extends AppCompatActivity {
/*
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);

        Intent intent = getIntent();
        currentUserId = intent.getStringExtra("currentUserId");
        visitedUserId = intent.getStringExtra("visitedUserId");


        if (findViewById(R.id.fragment_container) != null) {
            if (savedInstanceState != null) {
                return; // If fragment is already added, don't add it again
            }

            ProfileFragment profileFragment = ProfileFragment.newInstance(currentUserId, visitedUserId);

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, profileFragment);
            fragmentTransaction.commit();
        }


        followersTextView = findViewById(R.id.text_followers_count);
        followingTextView = findViewById(R.id.text_following_count);
        recyclerView = findViewById(R.id.recycler_view_tweets);

        loadProfileInfo();
        setupRecyclerView();
        loadTweets();
    }

    private void loadProfileInfo() {
        // Load user profile info and set it to corresponding views
        // Use visitedUserId to fetch user data
        // Update followersCount, followingCount, and isFollowing accordingly
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetAdapter = new TweetAdapter(this, allTweets, currentUserId, new TweetAdapter.UserClickListener() {
            @Override
            public void onClick(UserModel user) {
                // Handle user click (optional)
            }
        });
        recyclerView.setAdapter(tweetAdapter);
    }


    private void loadTweets() {
        // Load tweets of the visited user and update allTweets and mediaTweets lists
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
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.inflate(R.menu.bottom_navigation_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.menu_log_out) {
                    logout();
                    return true;
                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void logout() {
        AuthService.logout();
        startActivity(new Intent(this, WelcomeScreen.class));
        finish();
    } */
}

