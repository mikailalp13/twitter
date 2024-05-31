package Fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.example.twitter.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import Models.UserModel;

public class SearchFragment extends Fragment {

    private static final String ARG_USER_ID = "currentUserId";
    private String currentUserId;
    private EditText editTextSearch;
    private ListView listViewSearchResults;
    private ArrayAdapter<String> adapter;
    private ArrayList<UserModel> users;
    private FirebaseFirestore db;

    public static SearchFragment newInstance(String userId) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentUserId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        db = FirebaseFirestore.getInstance();

        editTextSearch = view.findViewById(R.id.edit_search);
        listViewSearchResults = view.findViewById(R.id.list_search_results);

        users = new ArrayList<>();

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
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
                Toast.makeText(getContext(), "Clicked on user: " + user.getName(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void searchUsers(String query) {
        db.collection("users")
                .whereEqualTo("name", query)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        users.clear();
                        adapter.clear();
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            UserModel user = document.toObject(UserModel.class);
                            users.add(user);
                            adapter.add(user.getName());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Failed to search users", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
