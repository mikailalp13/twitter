package screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.twitter.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;

import Fragments.HomeFragment;
import Fragments.NotificationsFragment;
import Fragments.ProfileFragment;
import Fragments.SearchFragment;
import Models.Tweet;
import Models.TweetAdapter;
import Models.UserModel;

public class HomeScreen extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;
    private String currentUserId;
    private List<Tweet> followingTweets = new ArrayList<>();
    private TweetAdapter tweetAdapter;
    private RecyclerView tweetRecyclerView;
    private ProgressBar loadingProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        currentUserId = getIntent().getStringExtra("currentUserId");

        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        tweetRecyclerView = findViewById(R.id.recycler_view_tweets);
        loadingProgressBar = findViewById(R.id.loading_progress_bar);

        setupFollowingTweets();

        FloatingActionButton fab = findViewById(R.id.fab_create_tweet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, CreateTweetScreen.class);
                intent.putExtra("currentUserId", currentUserId);
                startActivity(intent);
            }
        });

        ImageView logoImageView = findViewById(R.id.logo_image_view);
        logoImageView.setImageResource(R.drawable.logo);

        TextView titleTextView = findViewById(R.id.title_text_view);
        titleTextView.setTextColor(getResources().getColor(R.color.black));
        titleTextView.setText("Home Screen");

        tweetRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tweetAdapter = new TweetAdapter(this, followingTweets, currentUserId, new TweetAdapter.UserClickListener() {
            @Override
            public void onClick(UserModel user) {

            }
        }
        );
        tweetRecyclerView.setAdapter(tweetAdapter);
    }

    private void setupFollowingTweets() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("tweets")
                .whereEqualTo("authorId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                                Tweet tweet = document.toObject(Tweet.class);
                                if (tweet != null) {
                                    followingTweets.add(tweet);
                                }
                            }
                            tweetAdapter.notifyDataSetChanged();
                        } else {
                        }
                    } else {
                    }
                    loadingProgressBar.setVisibility(View.GONE);
                });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.nav_home) {
                        selectedFragment = HomeFragment.newInstance(currentUserId);
                    } else if (item.getItemId() == R.id.nav_search) {
                        selectedFragment = SearchFragment.newInstance(currentUserId);
                    } else if (item.getItemId() == R.id.nav_profile) {
                        selectedFragment = ProfileFragment.newInstance(currentUserId, currentUserId);
                    } else if (item.getItemId() == R.id.menu_log_out) {
                        logout();
                    }

                    if (selectedFragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            };
    private void logout() {
        Intent intent = new Intent(HomeScreen.this, WelcomeScreen.class);
        startActivity(intent);
        finish();
    }


}
