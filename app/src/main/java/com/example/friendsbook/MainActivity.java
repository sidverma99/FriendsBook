package com.example.friendsbook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private final String LIST_STATE_KEY = "recycler_state";
    FloatingActionButton fab;
    Parcelable mListState;
    private RecyclerView rvMainRecyclerView;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private TextView navigationUserName, navigationUserEmail;
    private CircleImageView navigationUserImage;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference mDatabaseUsersRef;
    private DatabaseReference mDatabaseLikesRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private LinearLayoutManager layoutManager;
    private boolean mProcessLike = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab=(FloatingActionButton)findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PostActivity.class));
            }
        });
        mDrawerLayout=(DrawerLayout)findViewById(R.id.mainDrawerLayout);
        navigationView=(NavigationView)findViewById(R.id.mainNavigationView);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close){
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                fab.setVisibility(View.GONE);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                fab.setAlpha(1-slideOffset);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                fab.setVisibility(View.VISIBLE);
            }
        };
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View mView=navigationView.getHeaderView(0);
        navigationUserImage=(CircleImageView)mView.findViewById(R.id.xcivUserProfileImage);
        navigationUserName=(TextView) mView.findViewById(R.id.xtvUserNameInNavigation);
        navigationUserEmail=(TextView) mView.findViewById(R.id.xtvUserEmailInNavigation);
        mDatabaseRef=FirebaseDatabase.getInstance().getReference().child("feeds");
        mDatabaseUsersRef=FirebaseDatabase.getInstance().getReference().child("users");
        mDatabaseLikesRef=FirebaseDatabase.getInstance().getReference().child("likes");
        mDatabaseRef.keepSynced(true);
        mDatabaseUsersRef.keepSynced(true);
        mDatabaseLikesRef.keepSynced(true);
        rvMainRecyclerView=(RecyclerView)findViewById(R.id.xrvMainRecyclerView);

    }
}