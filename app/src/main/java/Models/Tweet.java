package Models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Tweet {
    private String id;
    private String authorId;
    private String text;
    private String image;
    private int retweets;
    private int likes;
    private Timestamp timestamp;

    public Tweet() {
        // Default constructor required for calls to DataSnapshot.getValue(Tweet.class)
    }

    public Tweet(String id, String authorId, String text, String image, int retweets, int likes, Timestamp timestamp) {
        this.id = id;
        this.authorId = authorId;
        this.text = text;
        this.image = image;
        this.retweets = retweets;
        this.likes = likes;
        this.timestamp = timestamp;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> tweetMap = new HashMap<>();
        tweetMap.put("id", id);
        tweetMap.put("authorId", authorId);
        tweetMap.put("text", text);
        tweetMap.put("image", image);
        tweetMap.put("retweets", retweets);
        tweetMap.put("likes", likes);
        tweetMap.put("timestamp", timestamp);
        return tweetMap;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthorId() {
        return authorId;
    }

    public void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getRetweets() {
        return retweets;
    }

    public void setRetweets(int retweets) {
        this.retweets = retweets;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
