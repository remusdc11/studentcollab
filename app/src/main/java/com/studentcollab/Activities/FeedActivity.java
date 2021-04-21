package com.studentcollab.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.studentcollab.Fragments.HomeFragment;
import com.studentcollab.Fragments.SettingsFragment;
import com.studentcollab.Globals.LoadingDialog;
import com.studentcollab.Globals.Methods;
import com.studentcollab.Globals.Variables;
import com.studentcollab.R;

public class FeedActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private LoadingDialog loadingDialog;
    private FragmentManager fragmentManager;
    private BottomNavigationView bottomNavigation;
    private static View container;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //View rootLayout = findViewById(R.id.root_layout);

        container = findViewById(R.id.container);

        Snackbar.make(container, R.string.feed_activity_welcome, Snackbar.LENGTH_LONG).show();

        bottomNavigation = findViewById(R.id.bottom_navigation);

        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Fragment fr = new HomeFragment();
        final int id = fr.getId();
        fragmentTransaction.replace(R.id.container, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(FeedActivity.this);


        bottomNavigation.setSelectedItemId(R.id.navigation_menu_home);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()) {
                    case R.id.navigation_menu_add:
                        Intent addIntent = new Intent(FeedActivity.this, AddActivity.class);
                        startActivity(addIntent);
                        return true;

                    case R.id.navigation_menu_home:
                        selectedFragment = new HomeFragment();

                        break;

                    case R.id.navigation_menu_profile:
                        selectedFragment = new SettingsFragment();
                        break;
                }


                assert selectedFragment != null;
                if(!selectedFragment.isAdded())
                {
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.container, selectedFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

                return true;
            }
        });

        /*View logOut = findViewById(R.id.button_log_out);

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.start();
                mAuth.signOut();
                Variables.user = null;
                Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
                loadingDialog.dismiss();
                startActivity(intent);
                finish();
            }
        });*/

    }

    @Override
    public void onBackPressed() {
        //IMPLEMENT BOTTOM NAVIGATION CHANGE SELECTED ON BACK PRESSED

        int backStack = fragmentManager.getBackStackEntryCount();

        Methods.hideSoftKeyboard(FeedActivity.this);

       //fragmentManager.backsta
        if (backStack > 1) {
            fragmentManager.popBackStack();
        } else {
            //Methods.killProcess(getApplicationContext());
            finish();
        }

        Thread.currentThread().interrupt();
    }

    public static void showSnackBar(int resId) {
        Snackbar.make(container, resId, Snackbar.LENGTH_LONG).show();
    }
}
