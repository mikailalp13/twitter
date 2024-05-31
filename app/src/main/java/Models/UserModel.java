package Models;

import com.google.firebase.firestore.DocumentSnapshot;

public class UserModel {
    private String id;
    private String name;
    private String profilePicture;
    private String email;

    public UserModel() {
        // Default constructor required for calls to DataSnapshot.getValue(UserModel.class)
    }

    public UserModel(String id, String name, String profilePicture, String email) {
        this.id = id;
        this.name = name;
        this.profilePicture = profilePicture;
        this.email = email;
    }

    public static UserModel fromDoc(DocumentSnapshot doc) {
        return new UserModel(
                doc.getId(),
                doc.getString("name"),
                doc.getString("profilePicture"),
                doc.getString("email")
        );
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
