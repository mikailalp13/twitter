package Services;

import static android.content.ContentValues.TAG;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Models.Activity;
import Models.Tweet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import Models.UserModel;

public class DatabaseServices {

    public static final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final CollectionReference followersRef = db.collection("followers");
    private static final CollectionReference followingRef = db.collection("following");
    private static final CollectionReference usersRef = db.collection("users");
    private static final CollectionReference tweetsRef = db.collection("tweets");
    private static final CollectionReference feedRefs = db.collection("feed");
    private static final CollectionReference likesRef = db.collection("likes");
    private static final CollectionReference activitiesRef = db.collection("activities");


    public static void getUserById(String userId, FirebaseCallback<UserModel> callback) {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                if (user != null) {
                    callback.onCallback(user);
                } else {
                    callback.onFailure("User not found");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }


    public interface FirebaseCallback<T> {
        void onCallback(T data);
        void onFailure(String errorMessage);

    }


    public interface ActivityListener {
        void onSuccess(List<Activity> activities);
        void onFailure(String errorMessage);

        void onCallback(List<Activity> activities);
    }

    public static void followersNum(String userId, final FirebaseCallback<Long> callback) {
        followersRef.document(userId).collection("Followers").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            callback.onCallback((long) task.getResult().size());
                        }
                    }
                });
    }
    public static void followingNum(String userId, final FirebaseCallback<Long> callback) {
        followingRef.document(userId).collection("Following").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            callback.onCallback((long) task.getResult().size());
                        }
                    }
                });
    }



    public static void searchUsers(String name, final FirebaseCallback<QuerySnapshot> callback) {
        Query query = usersRef.whereGreaterThanOrEqualTo("name", name).whereLessThan("name", name + "z");
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(task.getResult());
                }
            }
        });
    }

    public static void followUser(String currentUserId, String visitedUserId) {
        // Check if currentUserId or visitedUserId is null or empty
        if (currentUserId == null || currentUserId.isEmpty() || visitedUserId == null || visitedUserId.isEmpty()) {
            Log.e(TAG, "CurrentUserId or VisitedUserId is null or empty");
            return; // or handle the error appropriately
        }

        // Proceed with creating the Firestore documents
        followingRef.document(currentUserId).collection("Following").document(visitedUserId).set(new HashMap<>());
        followersRef.document(visitedUserId).collection("Followers").document(currentUserId).set(new HashMap<>());

        addActivity(currentUserId, null, true, visitedUserId);
    }

    public static void unFollowUser(String currentUserId, String visitedUserId) {
        followingRef.document(currentUserId).collection("Following").document(visitedUserId).delete();
        followersRef.document(visitedUserId).collection("Followers").document(currentUserId).delete();
    }

    public static void isFollowingUser(String currentUserId, String visitedUserId, final FirebaseCallback<Boolean> callback) {
        followersRef.document(visitedUserId).collection("Followers").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(task.getResult().exists());
                }
            }
        });
    }

    public static void createTweet(Tweet tweet) {
        tweetsRef.document(tweet.getId()).set(tweet.toMap())
                .addOnSuccessListener(documentReference -> {
                    tweetsRef.document(tweet.getId()).collection("userTweets").add(tweet.toMap())
                            .addOnSuccessListener(documentReference1 -> {
                                QuerySnapshot followerSnapshot = followersRef.document(tweet.getAuthorId()).collection("Followers").get().getResult();
                                for (DocumentSnapshot docSnapshot : followerSnapshot.getDocuments()) {
                                    feedRefs.document(docSnapshot.getId()).collection("userFeed").add(tweet.toMap());
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("DatabaseServices", "Error adding tweet to user's tweets collection: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("DatabaseServices", "Error adding tweet to 'tweets' collection: " + e.getMessage());
                });
    }


    public static void getUserTweets(String userId, final FirebaseCallback<List<Tweet>> callback) {
        tweetsRef.document(userId).collection("userTweets").orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Tweet> userTweets = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Tweet tweet = document.toObject(Tweet.class);
                            userTweets.add(tweet);
                        }
                        callback.onCallback(userTweets);
                    } else {
                        callback.onFailure("Error fetching user tweets: " + task.getException().getMessage());
                    }
                });
    }

    public static void getHomeTweets(String currentUserId, final FirebaseCallback<List<Tweet>> callback) {
        feedRefs.document(currentUserId).collection("userFeed").orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Tweet> followingTweets = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Tweet tweet = document.toObject(Tweet.class);
                            followingTweets.add(tweet);
                        }
                        callback.onCallback(followingTweets);
                    } else {
                        callback.onFailure("Error fetching home tweets: " + task.getException().getMessage());
                    }
                });
    }


    public static void likeTweet(String currentUserId, final Tweet tweet) {
        final DocumentReference tweetDocProfile = tweetsRef.document(tweet.getAuthorId()).collection("userTweets").document(tweet.getId());
        tweetDocProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    int likes = task.getResult().getLong("likes").intValue();
                    tweetDocProfile.update("likes", likes + 1);
                }
            }
        });

        final DocumentReference tweetDocFeed = feedRefs.document(currentUserId).collection("userFeed").document(tweet.getId());
        tweetDocFeed.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    int likes = task.getResult().getLong("likes").intValue();
                    tweetDocFeed.update("likes", likes + 1);
                }
            }
        });

        likesRef.document(tweet.getId()).collection("tweetLikes").document(currentUserId).set(new HashMap<>());

        addActivity(currentUserId, tweet, false, null);
    }

    public static void unlikeTweet(String currentUserId, final Tweet tweet) {
        final DocumentReference tweetDocProfile = tweetsRef.document(tweet.getAuthorId()).collection("userTweets").document(tweet.getId());
        tweetDocProfile.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    int likes = task.getResult().getLong("likes").intValue();
                    tweetDocProfile.update("likes", likes - 1);
                }
            }
        });

        final DocumentReference tweetDocFeed = feedRefs.document(currentUserId).collection("userFeed").document(tweet.getId());
        tweetDocFeed.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    int likes = task.getResult().getLong("likes").intValue();
                    tweetDocFeed.update("likes", likes - 1);
                }
            }
        });

        likesRef.document(tweet.getId()).collection("tweetLikes").document(currentUserId).delete();
    }

    public static void isLikeTweet(String currentUserId, final Tweet tweet, final FirebaseCallback<Boolean> callback) {
        likesRef.document(tweet.getId()).collection("tweetLikes").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    callback.onCallback(task.getResult().exists());
                }
            }
        });
    }

    public static void getActivities(String userId, final FirebaseCallback<List<Activity>> callback) {
        activitiesRef.document(userId).collection("userActivities").orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Activity> activities = task.getResult().toObjects(Activity.class);
                            callback.onCallback(activities);
                        }
                    }
                });
    }

    public static void addActivity(String currentUserId, Tweet tweet, boolean follow, String followedUserId) {
        if (follow) {
            activitiesRef.document(followedUserId).collection("userActivities").add(new Activity(currentUserId, followedUserId, Timestamp.now(), true));
        } else {
            activitiesRef.document(tweet.getAuthorId()).collection("userActivities").add(new Activity(currentUserId, followedUserId, Timestamp.now(), false));
        }
    }

    public static void getActivities(final ActivityListener callback) {
        activitiesRef.orderBy("timestamp", Query.Direction.DESCENDING).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Activity> activities = new ArrayList<>();
                            for (DocumentSnapshot document : task.getResult()) {
                                Activity activity = document.toObject(Activity.class);
                                activities.add(activity);
                            }
                            callback.onCallback(activities);
                        } else {
                            callback.onFailure(task.getException().getMessage());
                        }
                    }
                });
    }


}

