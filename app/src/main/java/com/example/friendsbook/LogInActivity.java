package com.example.friendsbook;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LogInActivity extends AppCompatActivity {
    private static final String TAG = "LogInActivity";
    private static int RC_SIGN_IN = 5;
    private TextView bnNewAccount;
    private EditText etEmail,etPassword;
    private SignInButton bnGoogleSignIn;
    private Button bnLogIn;
    private DatabaseReference mReference;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private ProgressDialog mProgressDialog;
    private GoogleSignInClient mSignInClient;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acativity_log_in);
        auth=FirebaseAuth.getInstance();
        mReference= FirebaseDatabase.getInstance().getReference().child("users");
        mReference.keepSynced(true);
        etEmail=(EditText)findViewById(R.id.xetLogInEmail);
        etPassword=(EditText)findViewById(R.id.xetLogInPassword);
        bnLogIn=(Button)findViewById(R.id.xbnLogIn);
        bnNewAccount=(TextView)findViewById(R.id.xbnLogInNewAccount);
        bnGoogleSignIn=(SignInButton)findViewById(R.id.xbnGoogleSignIn);
        mProgressDialog = new ProgressDialog(this);
        bnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=etEmail.getText().toString();
                String password=etPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()){
                    mProgressDialog.setMessage("Logging In...");
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.show();
                    auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                final String userId=auth.getCurrentUser().getUid();
                                mReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.hasChild(userId)){
                                            mProgressDialog.dismiss();
                                            Intent intent=new Intent(LogInActivity.this,MainActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Toast.makeText(getApplicationContext(),"You need to create a new account",Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
        bnNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LogInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken("default id").requestEmail().build();
        mSignInClient= GoogleSignIn.getClient(getApplicationContext(),gso);
    }
}
