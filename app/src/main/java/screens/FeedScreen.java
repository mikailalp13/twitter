package screens;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.twitter.R;
import Fragments.HomeFragment;
import Fragments.NotificationsFragment;
import Fragments.ProfileFragment;
import Fragments.SearchFragment;

public class FeedScreen extends AppCompatActivity {
/*
    private String currentUserId;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_screen);

        currentUserId = getIntent().getStringExtra("currentUserId");

        fragmentManager = getSupportFragmentManager();
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance(currentUserId)).commit();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    if (item.getItemId() == R.id.nav_home) {
                        selectedFragment = HomeFragment.newInstance(currentUserId);
                        Log.d("Fragment Selection", "HomeFragment selected");
                    } else if (item.getItemId() == R.id.nav_search) {
                        selectedFragment = SearchFragment.newInstance(currentUserId);
                        Log.d("Fragment Selection", "SearchFragment selected");
                    } else if (item.getItemId() == R.id.nav_notifications) {
                        selectedFragment = NotificationsFragment.newInstance(currentUserId);
                        Log.d("Fragment Selection", "NotificationsFragment selected");
                    } else if (item.getItemId() == R.id.nav_profile) {
                        selectedFragment = ProfileFragment.newInstance(currentUserId, currentUserId);
                        Log.d("Fragment Selection", "ProfileFragment selected");
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
        Intent intent = new Intent(FeedScreen.this, WelcomeScreen.class);
        startActivity(intent);
        finish();
    }*/
}