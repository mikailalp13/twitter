package screens;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.twitter.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import Services.DatabaseServices;

public class CreateTweetScreen extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText tweetText;
    private ImageView pickedImage;
    private ProgressBar loading;
    private Uri imageUri;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_tweet_screen);

        tweetText = findViewById(R.id.tweet_text);
        pickedImage = findViewById(R.id.picked_image);
        Button pickImageButton = findViewById(R.id.pick_image_button);
        Button tweetButton = findViewById(R.id.tweet_button);
        loading = findViewById(R.id.loading);

        currentUserId = getIntent().getStringExtra("id");

        pickImageButton.setOnClickListener(v -> openFileChooser());

        tweetButton.setOnClickListener(v -> postTweet());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            pickedImage.setImageURI(imageUri);
        }
    }

    private void postTweet() {
        String text = tweetText.getText().toString().trim();
        if (text.isEmpty()) {
            Toast.makeText(this, "Enter your Tweet", Toast.LENGTH_SHORT).show();
            return;
        }

        loading.setVisibility(View.VISIBLE);

        followCurrentUser(currentUserId);

        if (imageUri != null) {
            uploadImageAndPostTweet(text);
        } else {
            postTweetWithImageUrl(text, "");
        }
    }

    private void followCurrentUser(String currentUserId) {
        DatabaseServices.followUser(currentUserId, currentUserId);
    }

    private void uploadImageAndPostTweet(String text) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("tweet_images");
        StorageReference fileRef = storageRef.child(System.currentTimeMillis() + ".jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    postTweetWithImageUrl(text, uri.toString());
                }))
                .addOnFailureListener(e -> {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(CreateTweetScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void postTweetWithImageUrl(String text, String imageUrl) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> tweet = new HashMap<>();
        tweet.put("text", text);
        tweet.put("image", imageUrl);
        tweet.put("authorId", currentUserId);
        tweet.put("likes", 0);
        tweet.put("retweets", 0);
        tweet.put("timestamp", Timestamp.now());

        db.collection("tweets")
                .add(tweet)
                .addOnSuccessListener(documentReference -> {
                    loading.setVisibility(View.GONE);
                    finish();
                })
                .addOnFailureListener(e -> {
                    loading.setVisibility(View.GONE);
                    Toast.makeText(CreateTweetScreen.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


}
