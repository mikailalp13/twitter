package Constants;

import android.graphics.Color;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Constants {

    public static final Color KTweeterColor = Color.valueOf(Color.parseColor("#00acee"));

    private static final FirebaseFirestore _fireStore = FirebaseFirestore.getInstance();

    public static final CollectionReference usersRef = _fireStore.collection("users");
    public static final CollectionReference followersRef = _fireStore.collection("followers");
    public static final CollectionReference followingRef = _fireStore.collection("following");
    public static final CollectionReference tweetsRef = _fireStore.collection("tweets");
    public static final CollectionReference feedRefs = _fireStore.collection("feeds");
    public static final CollectionReference likesRef = _fireStore.collection("likes");
    public static final CollectionReference activitiesRef = _fireStore.collection("activities");

    private static final FirebaseStorage storage = FirebaseStorage.getInstance();
    public static final StorageReference storageRef = storage.getReference();
}
