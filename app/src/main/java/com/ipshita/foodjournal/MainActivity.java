package com.ipshita.foodjournal;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser firebaseUser;

    private Button loginButton;
    private Button createAccountButton;

    private EditText emailEditText;
    private EditText passwordEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                firebaseUser = firebaseAuth.getCurrentUser();

                if (firebaseUser != null){
                    Toast.makeText(MainActivity.this,
                            getString(R.string.signed_in_toast_message), Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(MainActivity.this,
                            getString(R.string.not_signed_in_toast_message), Toast.LENGTH_LONG).show();

                }
            }
        };

        emailEditText = (EditText) findViewById(R.id.editTextEmail);
        passwordEditText = (EditText) findViewById(R.id.editTextPassword);
        loginButton = (Button)findViewById(R.id.buttonLogin);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(emailEditText.getText().toString())
                        && !TextUtils.isEmpty(passwordEditText.getText().toString())){
                    String email = emailEditText.getText().toString();
                    String password = passwordEditText.getText().toString();
                    login(email, password);
                }
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_sign_out){
            firebaseAuth.signOut();
        }
        return super.onOptionsItemSelected(item);
    }

    private void login(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(MainActivity.this,
                            getString(R.string.login_toast_messge),Toast.LENGTH_LONG).show();
                    startActivity(new Intent(MainActivity.this, MealListActivity.class));
                }else{
                    Toast.makeText(MainActivity.this,
                            getString(R.string.unsuccessful_login_toast_messge),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authStateListener != null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }
}
