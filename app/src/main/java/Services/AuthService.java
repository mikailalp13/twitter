package Services;

import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AuthService {

    private static final FirebaseAuth auth = FirebaseAuth.getInstance();
    private static final FirebaseFirestore firestore = FirebaseFirestore.getInstance();

    public static void signUp(String name, String email, String password, final AuthListener listener) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        if (user != null) {
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);
                            userData.put("profilePicture", "");
                            userData.put("coverImage", "");
                            userData.put("bio", "");

                            firestore.collection("users")
                                    .document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> listener.onSuccess())
                                    .addOnFailureListener(e -> listener.onFailure(e.getMessage()));
                        }
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

    public static void login(String email, String password, final AuthListener listener) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listener.onSuccess();
                    } else {
                        listener.onFailure(task.getException().getMessage());
                    }
                });
    }

    public static void logout() {
        auth.signOut();
    }

    public interface AuthListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }
}
