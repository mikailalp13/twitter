package screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.twitter.R;

import Fragments.HomeFragment;
import Services.AuthService;

public class LoginScreen extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        emailEditText = findViewById(R.id.email_edit_text);
        passwordEditText = findViewById(R.id.password_edit_text);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        AuthService.login(email, password, new AuthService.AuthListener() {
            @Override
            public void onSuccess() {
                // Login successful
                startActivity(new Intent(LoginScreen.this, HomeScreen.class));
                finish(); // Close the login activity
            }

            @Override
            public void onFailure(String errorMessage) {
                // Login failed
                // Handle the failure, e.g., show an error message
                System.out.println("Login failed: " + errorMessage);
            }
        });
    }
}
