package Services;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.Callable;

public class StorageService {

    private static final StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    public static Task<Uri> uploadProfilePicture(final String url, final File imageFile) {
        final String uniquePhotoId = UUID.randomUUID().toString();

        return compressImage(uniquePhotoId, imageFile).continueWithTask(new Continuation<File, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<File> task) throws Exception {
                File compressedImage = task.getResult();
                return storageRef.child("images/users/userProfile_" + uniquePhotoId + ".jpg").putFile(Uri.fromFile(compressedImage)).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return storageRef.child("images/users/userProfile_" + uniquePhotoId + ".jpg").getDownloadUrl();
                    }
                });
            }
        });
    }

    public static Task<Uri> uploadCoverPicture(final String url, final File imageFile) {
        final String uniquePhotoId = UUID.randomUUID().toString();

        return compressImage(uniquePhotoId, imageFile).continueWithTask(new Continuation<File, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<File> task) throws Exception {
                File compressedImage = task.getResult();
                return storageRef.child("images/users/userCover_" + uniquePhotoId + ".jpg").putFile(Uri.fromFile(compressedImage)).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return storageRef.child("images/users/userCover_" + uniquePhotoId + ".jpg").getDownloadUrl();
                    }
                });
            }
        });
    }

    public static Task<Uri> uploadTweetPicture(final File imageFile) {
        final String uniquePhotoId = UUID.randomUUID().toString();

        return compressImage(uniquePhotoId, imageFile).continueWithTask(new Continuation<File, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<File> task) throws Exception {
                File compressedImage = task.getResult();
                return storageRef.child("images/tweets/tweet_" + uniquePhotoId + ".jpg").putFile(Uri.fromFile(compressedImage)).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        return storageRef.child("images/tweets/tweet_" + uniquePhotoId + ".jpg").getDownloadUrl();
                    }
                });
            }
        });
    }

    private static Task<File> compressImage(final String photoId, final File imageFile) {
        return Tasks.call(new Callable<File>() {
            @Override
            public File call() throws Exception {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream);
                    byte[] byteArray = outputStream.toByteArray();
                    File compressedFile = new File(Environment.getExternalStorageDirectory() + "/compressed_" + photoId + ".jpg");
                    FileOutputStream fos = new FileOutputStream(compressedFile);
                    fos.write(byteArray);
                    fos.flush();
                    fos.close();
                    return compressedFile;
                } catch (IOException e) {
                    throw e; // Re-throw IOException to handle it later
                }
            }
        });
    }

}
