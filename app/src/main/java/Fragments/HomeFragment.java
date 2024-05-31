package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.twitter.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.List;
import Models.Tweet;
import Models.TweetAdapter;
import Models.UserModel;
import screens.CreateTweetScreen;

public class HomeFragment extends Fragment {

    private static final String ARG_USER_ID = "currentUserId";
    private String currentUserId;
    private List<Tweet> followingTweets = new ArrayList<>();
    private TweetAdapter tweetAdapter;
    private RecyclerView tweetRecyclerView;
    private ProgressBar loadingProgressBar;

    public static HomeFragment newInstance(String userId) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tweetRecyclerView = view.findViewById(R.id.recycler_view_tweets);
        loadingProgressBar = view.findViewById(R.id.loading_progress_bar);

        setupFollowingTweets();

        FloatingActionButton fab = view.findViewById(R.id.fab_create_tweet);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CreateTweetScreen.class);
                intent.putExtra("currentUserId", currentUserId);
                startActivity(intent);
            }
        });


        tweetRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        tweetAdapter = new TweetAdapter(getContext(), followingTweets, currentUserId, new TweetAdapter.UserClickListener() {
            @Override
            public void onClick(UserModel user) {
            }
        });
        tweetRecyclerView.setAdapter(tweetAdapter);

        return view;
    }

    private void setupFollowingTweets() {
        loadingProgressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore.getInstance().collection("tweets")
                .whereEqualTo("authorId", currentUserId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
                            Tweet tweet = document.toObject(Tweet.class);
                            followingTweets.add(tweet);
                        }
                        tweetAdapter.notifyDataSetChanged();
                    }
                    loadingProgressBar.setVisibility(View.GONE);
                });
    }
}
