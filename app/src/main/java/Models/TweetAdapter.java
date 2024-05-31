package Models;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.twitter.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.DocumentSnapshot;
import java.util.List;

public class TweetAdapter extends RecyclerView.Adapter<TweetAdapter.ViewHolder> {

    private static final String TAG = "TweetAdapter";
    private Context context;
    private List<Tweet> tweets;
    private String currentUserId;

    public TweetAdapter(Context context, List<Tweet> tweets, String currentUserId, UserClickListener userClickListener) {
        this.context = context;
        this.tweets = tweets;
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tweet, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (tweets != null) {
            Tweet tweet = tweets.get(position);
            if (tweet != null) {
                holder.bind(tweet);
            } else {
                Log.e(TAG, "Tweet at position " + position + " is null");
            }
        } else {
            Log.e(TAG, "tweets list is null");
        }
    }

    @Override
    public int getItemCount() {
        return tweets != null ? tweets.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tweetTextView, username_text_view;
        public ImageView profileImageView, tweetImageView;

        public ViewHolder(View itemView) {
            super(itemView);

            tweetTextView = itemView.findViewById(R.id.tweet_text_view);
            profileImageView = itemView.findViewById(R.id.profile_image_view);
            tweetImageView = itemView.findViewById(R.id.tweet_image_view);
            username_text_view = itemView.findViewById(R.id.username_text_view);
        }

        public void bind(Tweet tweet) {
            tweetTextView.setText(tweet.getText());
            // You can also load the tweet image here if needed
        }
    }

    public interface UserClickListener {
        void onClick(UserModel user);
    }
}