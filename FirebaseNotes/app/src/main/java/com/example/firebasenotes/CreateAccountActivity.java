package com.example.firebasenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class CreateAccountActivity extends AppCompatActivity {

    EditText emailEditText, passwordEditText, confirmPasswordEditText;
    Button createAccountBtn;
    ProgressBar progressBar;
    TextView loginBtnTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        emailEditText = findViewById(R.id.email_et);
        passwordEditText = findViewById(R.id.password_et);
        confirmPasswordEditText = findViewById(R.id.confirm_password_et);
        createAccountBtn = findViewById(R.id.create_account_btn);
        progressBar = findViewById(R.id.progressbar);
        loginBtnTextView = findViewById(R.id.login_tv_btn);

        createAccountBtn.setOnClickListener(v ->createAccount() );
        loginBtnTextView.setOnClickListener(v -> finish());

    }
    void createAccount() {
        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        boolean isValidated = validateData(email, password, confirmPassword);
        if (!isValidated) {
            return;
        }

        createAccountInFirebase(email, password);
    }

    void createAccountInFirebase(String email, String password) {
        changeInProgress(true);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(CreateAccountActivity.this,
                        new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                changeInProgress(false);
                                if(task.isSuccessful()){
                                    Utility.showToast(CreateAccountActivity.this, "Successfully created account, Check email to verify");
                                    firebaseAuth.getCurrentUser().sendEmailVerification();
                                    firebaseAuth.signOut();
                                    finish();
                                } else {
                                    // failure
                                    Utility.showToast(CreateAccountActivity.this, task.getException().getLocalizedMessage());
                                }
                            }
                        });

    }

    void changeInProgress(boolean inProgress) {
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            createAccountBtn.setVisibility(View.GONE);
        }
        else{
            progressBar.setVisibility(View.GONE);
            createAccountBtn.setVisibility(View.VISIBLE);
        }
    }

    boolean validateData(String email, String password, String confirmPassword) {
        // validate the data that are input by user

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailEditText.setError("Email is wrong as fuck bruh");
            return false;
        }
        if (password.length() < 6) {
            passwordEditText.setError("Need more password characters");
            return false;
        }
        if (!password.equals(confirmPassword)) {
            confirmPasswordEditText.setError("Password no matching");
            return false;
        }
        return true;

    }


}