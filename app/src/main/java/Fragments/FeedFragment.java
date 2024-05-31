package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.twitter.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import screens.WelcomeScreen;

public class FeedFragment extends Fragment {
    private static final String ARG_UID = "uid";
    private String mUid;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigationView;

    public FeedFragment() {
        // Required empty public constructor
    }

    public static FeedFragment newInstance(String uid) {
        FeedFragment fragment = new FeedFragment();
        Bundle args = new Bundle();
        args.putString(ARG_UID, uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUid = getArguments().getString(ARG_UID);
        }
        fragmentManager = getChildFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        bottomNavigationView = view.findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            fragmentManager.beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance(mUid)).commit();
        }
        return view;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_home) {
                        selectedFragment = HomeFragment.newInstance(mUid);
                        Log.d("Fragment Selection", "HomeFragment selected");
                    } else if (itemId == R.id.nav_search) {
                        selectedFragment = SearchFragment.newInstance(mUid);
                        Log.d("Fragment Selection", "SearchFragment selected");
                    } else if (itemId == R.id.nav_profile) {
                        selectedFragment = ProfileFragment.newInstance(mUid, mUid);
                        Log.d("Fragment Selection", "ProfileFragment selected");
                    } else if (itemId == R.id.menu_log_out) {
                        logout();
                        return true;
                    }

                    if (selectedFragment != null) {
                        fragmentManager.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    }
                    return true;
                }
            };

    private void logout() {
        Intent intent = new Intent(getActivity(), WelcomeScreen.class);
        startActivity(intent);
        getActivity().finish();
    }
}
