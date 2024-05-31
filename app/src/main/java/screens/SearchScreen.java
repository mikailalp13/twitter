package screens;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.twitter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

import Models.UserModel;

public class SearchScreen extends AppCompatActivity {

    private EditText editTextSearch;
    private ListView listViewSearchResults;
    private ArrayAdapter<String> adapter;
    private ArrayList<UserModel> users;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_screen);

        db = FirebaseFirestore.getInstance();

        editTextSearch = findViewById(R.id.edit_search);
        listViewSearchResults = findViewById(R.id.list_search_results);

        users = new ArrayList<>();

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listViewSearchResults.setAdapter(adapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                searchUsers(s.toString());
            }
        });

        listViewSearchResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserModel user = users.get(position);
                Toast.makeText(SearchScreen.this, "Clicked on user: " + user.getName(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchUsers(String query) {
        db.collection("users")
                .whereEqualTo("name", query)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        users.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            UserModel user = document.toObject(UserModel.class);
                            users.add(user);
                            adapter.add(user.getName());
                        }
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SearchScreen.this, "Error searching users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
