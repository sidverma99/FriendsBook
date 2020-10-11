package com.example.friendsbook;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AnotherUserProfileActivity extends AppCompatActivity {
    private static final int PICK_IMAGE = 3;
    private ImageView ivUserImage;
    private TextView tvUserName, tvUserEmail;
    private ImageButton ibEditImage;
    private RecyclerView rvUserProfile;

    private DatabaseReference mUserDbRef, mFeedsDbRef;
    private FirebaseAuth mAuth;
    private Uri resultUri;
    private Query userOnlyQuery;
    private String user_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();

        user_id = intent.getStringExtra("feed_uid");

        ivUserImage = (ImageView) findViewById(R.id.xivAnotherUserImage);
        tvUserName = (TextView) findViewById(R.id.xtvAnotherUserName);
        tvUserEmail = (TextView) findViewById(R.id.xtvAnotherUserEmail);
        ibEditImage = (ImageButton) findViewById(R.id.xibEditImage);
        rvUserProfile = (RecyclerView) findViewById(R.id.xrvUserProfile);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        rvUserProfile.setLayoutManager(layoutManager);

        mUserDbRef = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        mFeedsDbRef = FirebaseDatabase.getInstance().getReference().child("feeds");
        mAuth = FirebaseAuth.getInstance();
        String current_uid = mAuth.getCurrentUser().getUid();
        mUserDbRef.keepSynced(true);
        mFeedsDbRef.keepSynced(true);
        if (current_uid.equals(user_id)) {
            ibEditImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent pickImageIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    pickImageIntent.setType("image/*");
                    startActivityForResult(pickImageIntent, PICK_IMAGE);
                }
            });
        } else {
            ibEditImage.setVisibility(View.GONE);
        }
        mUserDbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String image = dataSnapshot.child("profile_image").getValue(String.class);

                getSupportActionBar().setTitle(name);
                tvUserName.setText(name);
                tvUserEmail.setText(email);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter <Feed, MainActivity.FeedsViewHolder> userProfileFirebaseAdapter=new FirebaseRecyclerAdapter<Feed, MainActivity.FeedsViewHolder>(
                Feed.class,
                R.layout.single_row_main,
                MainActivity.FeedsViewHolder.class,
                userOnlyQuery
        ){

        };
    }
}
