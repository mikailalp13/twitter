package Models;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.Timestamp;

public class Activity {
    private String id;
    private String fromUserId;
    private Timestamp timestamp;
    private boolean follow;

    public Activity() {
    }

    public Activity(String id, String fromUserId, Timestamp timestamp, boolean follow) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.timestamp = timestamp;
        this.follow = follow;
    }
    public static Activity fromDoc(DocumentSnapshot doc) {
        return new Activity(
                doc.getId(),
                doc.getString("fromUserId"),
                doc.getTimestamp("timestamp"),
                doc.getBoolean("follow")
        );
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }


    public UserModel getUser() {
        return null;
    }

    public String getText() {
        return null;
    }
}
