package Widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.twitter.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import Models.Tweet;
import Models.UserModel;
import Services.DatabaseServices;

public class TweetContainer extends LinearLayout {
    private Tweet tweet;
    private UserModel name;
    private String currentUserId;
    private static final FirebaseFirestore _fireStore = FirebaseFirestore.getInstance();

    public static final CollectionReference usersRef = _fireStore.collection("users");

    private TextView likesTextView;
    private ImageView likeButton;
    private TextView authorNameTextView;
    private ImageView profilePictureImageView;
    private int likesCount = 0;
    private boolean isLiked = false;

    public TweetContainer(Context context, Tweet tweet, String currentUserId) {  // Kullanıcı modelini constructor'dan kaldırın.
        super(context);
        this.tweet = tweet;
        this.currentUserId = currentUserId;

        initViews(context);
        initTweetLikes();
        fetchAuthorData();
    }

    private void initViews(Context context) {
        setOrientation(VERTICAL);
        setPadding(dpToPx(20), dpToPx(10), dpToPx(20), dpToPx(10));

        LinearLayout authorRow = new LinearLayout(context);
        authorRow.setOrientation(HORIZONTAL);
        authorRow.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        addView(authorRow);

        profilePictureImageView = new ImageView(context);
        profilePictureImageView.setLayoutParams(new LayoutParams(dpToPx(40), dpToPx(40)));
        profilePictureImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        authorRow.addView(profilePictureImageView);

        authorNameTextView = new TextView(context);
        authorNameTextView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        authorNameTextView.setTextSize(17);
        authorNameTextView.setTextColor(Color.BLACK);
        authorRow.addView(authorNameTextView);

        TextView tweetTextView = new TextView(context);
        tweetTextView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        tweetTextView.setText(tweet.getText());
        tweetTextView.setTextSize(15);
        addView(tweetTextView);

        TextView username_text_view = new TextView(context);
        username_text_view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        //username_text_view.setText();
        username_text_view.setTextSize(20);
        addView(username_text_view);


        View divider = new View(context);
        divider.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, dpToPx(1)));
        divider.setBackgroundColor(Color.GRAY);
        addView(divider);
    }

    private void fetchAuthorData() {
        DatabaseServices.getUserById(tweet.getAuthorId(), new DatabaseServices.FirebaseCallback<UserModel>() {
            @Override
            public void onCallback(UserModel user) {
                if (user != null) {
                    name = user;
                    Log.d("FirebaseTest", "User Name: " + user.getName());  // Test için ekleyin
                    updateAuthorUI();
                } else {
                    Log.d("FirebaseTest", "User is null");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("FirebaseTest", "Error: " + errorMessage);  // Test için ekleyin
            }
        });
    }



    private void updateAuthorUI() {
        if (name != null) {
            authorNameTextView.setText(name.getName());
            if (!name.getProfilePicture().isEmpty()) {
                Picasso.get().load(name.getProfilePicture()).into(profilePictureImageView);
            } else {
                profilePictureImageView.setImageResource(R.drawable.placeholder);
            }
        }
    }

    private void initTweetLikes() {
        DatabaseServices.isLikeTweet(currentUserId, tweet, new DatabaseServices.FirebaseCallback<Boolean>() {
            @Override
            public void onCallback(Boolean result) {
                updateLikesUI();
            }

            @Override
            public void onFailure(String errorMessage) {
            }
        });
    }

    private void likeTweet() {
        if (isLiked) {
            DatabaseServices.unlikeTweet(currentUserId, tweet);
            likesCount--;
        } else {
            DatabaseServices.likeTweet(currentUserId, tweet);
            likesCount++;
        }
        isLiked = !isLiked;
        updateLikesUI();
    }

    private void updateLikesUI() {
        likeButton.setImageResource(isLiked ? R.drawable.ic_favorite : R.drawable.ic_favorite_border);
        likesTextView.setText(likesCount + " Likes");
    }

    private int dpToPx(int dp) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }
}
