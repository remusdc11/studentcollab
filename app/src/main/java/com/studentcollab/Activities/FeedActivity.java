package com.studentcollab.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.errorprone.annotations.Var;
import com.google.firebase.auth.FirebaseAuth;
import com.studentcollab.Fragments.FeedFragment;
import com.studentcollab.Fragments.ProfileFragment;
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
    private boolean navigate = true;



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
        fragmentTransaction.replace(R.id.container, new FeedFragment());
        fragmentTransaction.addToBackStack(Variables.FRAGMENT_FEED);
        fragmentTransaction.commit();

        mAuth = FirebaseAuth.getInstance();
        loadingDialog = new LoadingDialog(FeedActivity.this);

        bottomNavigation.getMenu().getItem(2).setTitle(Variables.user.getFirstName() + " " + Variables.user.getLastName());

        bottomNavigation.setSelectedItemId(R.id.navigation_menu_home);

        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                /*if (!navigate) {
                    navigate = true;
                    return true;
                }*/

                Fragment selectedFragment = null;
                String selectedFragmentName = null;

                switch (item.getItemId()) {
                    case R.id.navigation_menu_add:
                        Intent addIntent = new Intent(FeedActivity.this, AddActivity.class);
                        startActivity(addIntent);
                        return false;

                    case R.id.navigation_menu_home:
                        selectedFragment = new FeedFragment();
                        selectedFragmentName = Variables.FRAGMENT_FEED;

                        break;

                    case R.id.navigation_menu_profile:
                        selectedFragment = new ProfileFragment();
                        Bundle args = new Bundle();
                        args.putString("projectId", Variables.user.getUserId());
                        selectedFragment.setArguments(args);
                        selectedFragmentName = Variables.FRAGMENT_PROFILE_OWN;
                        break;
                }


                assert selectedFragment != null;
                if(!selectedFragment.isAdded())
                {
                    Methods.addFragment(fragmentManager, selectedFragment, selectedFragmentName);
                    return true;
                }

                return false;
            }
        });

        bottomNavigation.setOnNavigationItemReselectedListener(new BottomNavigationView.OnNavigationItemReselectedListener() {
            @Override
            public void onNavigationItemReselected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_menu_home:
                        fragmentManager.popBackStack(Variables.FRAGMENT_FEED, 0);
                        break;

                    case R.id.navigation_menu_profile:
                        fragmentManager.popBackStack(Variables.FRAGMENT_PROFILE_OWN, 0);
                        break;
                }
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
            String fragmentName = fragmentManager.getBackStackEntryAt(backStack - 2).getName();

            assert fragmentName != null;
            switch (fragmentName) {
                case Variables.FRAGMENT_FEED:
                    navigate = false;
                    bottomNavigation.setSelectedItemId(R.id.navigation_menu_home);
                    break;

                case Variables.FRAGMENT_PROFILE_OWN:
                    navigate = false;
                    bottomNavigation.setSelectedItemId(R.id.navigation_menu_profile);
                    break;
            }

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
