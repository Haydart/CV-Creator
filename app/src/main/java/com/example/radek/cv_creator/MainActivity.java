package com.example.radek.cv_creator;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.ActionMenuItem;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.radek.cv_creator.fragments.CVCreationFragment;
import com.example.radek.cv_creator.fragments.CVManagementFragment;
import com.example.radek.cv_creator.fragments.HomeFragment;
import com.example.radek.cv_creator.fragments.NoProfilesFragment;
import com.example.radek.cv_creator.fragments.ProfileCreationFragment;
import com.example.radek.cv_creator.fragments.ProfileManagementFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener,
        NoProfilesFragment.OnProfileCreateButtonClickListener,
        CVCreationFragment.OnCVCreationListener,
        HomeFragment.HomeEventListener,
        CVManagementFragment.OnFragmentInteractionListener,
        ProfileManagementFragment.ProfileManagementEventListener,
        ProfileCreationFragment.OnProfileCreateFragmentClickListener{

    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;

    ArrayList<Profile> userProfiles;

    NoProfilesFragment noProfilesFragment;
    CVCreationFragment cvCreationFragment;
    CVManagementFragment cvManagementFragment;
    ProfileCreationFragment profileCreationFragment;
    ProfileManagementFragment profileManagementFragment;
    HomeFragment homeFragment;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findRefs();

        userProfiles = getMockProfilesArrayList();

        setSupportActionBar(toolbar);
        fab.setVisibility(View.GONE);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fragmentTransaction.add(R.id.fragmentsRelativeLayout, noProfilesFragment);
        fragmentTransaction.attach(noProfilesFragment);
        fragmentTransaction.commit();
    }

    public void findRefs(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        userProfiles = new ArrayList<>();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        noProfilesFragment = new NoProfilesFragment();
        cvCreationFragment = new CVCreationFragment();
        cvManagementFragment = new CVManagementFragment();
        profileCreationFragment = new ProfileCreationFragment();
        profileManagementFragment = new ProfileManagementFragment();
        homeFragment = new HomeFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fab.setVisibility(View.INVISIBLE);
            getSupportActionBar().setTitle("CV Creator");
            Bundle bundle = new Bundle();
            Fragment fragment =  new HomeFragment();
            bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);
            homeFragment = (HomeFragment)fragment;
            displayFragment(homeFragment);

        } else if (id == R.id.nav_cv_manage) {

            fab.setVisibility(View.VISIBLE);
            fab.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ic_add_white_24dp));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNavigationItemSelected(new ActionMenuItem(getApplicationContext(),0,R.id.nav_cv_create,0,0,""));
                }
            });

            getSupportActionBar().setTitle("Manage existing CVs");
            Bundle bundle = new Bundle();
            Fragment fragment = new CVManagementFragment();
            //bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);
            cvManagementFragment = (CVManagementFragment)fragment;
            displayFragment(cvManagementFragment);

        } else if (id == R.id.nav_cv_create) {
            getSupportActionBar().setTitle("Create new CV");
            Bundle bundle = new Bundle();
            Fragment fragment = new CVCreationFragment();
            bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);
            cvCreationFragment = (CVCreationFragment)fragment;
            displayFragment(cvCreationFragment);

            fab.setVisibility(View.VISIBLE);
            fab.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ic_check_white_24dp));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO: save data into shared prefs + add name of current profile
                    hideKeyboard();
                    Snackbar successSnackbar = Snackbar.make(getCurrentFocus(),"Successfully created new CV for CURRENT PROFILE",Snackbar.LENGTH_SHORT);
                    successSnackbar.show();
                    onBackPressed();
                }
            });

        } else if (id == R.id.nav_manage_profiles) {
            fab.setVisibility(View.VISIBLE);
            fab.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ic_add_white_24dp));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNavigationItemSelected(new ActionMenuItem(getApplicationContext(),0,R.id.nav_profie_create,0,0,""));
                }
            });

            getSupportActionBar().setTitle("Manage existing profiles");
            Fragment fragment = new ProfileManagementFragment();
            Bundle bundle = new Bundle();
            //bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);
            profileManagementFragment = (ProfileManagementFragment)fragment;
            displayFragment(profileManagementFragment);

        } else if (id == R.id.nav_profie_create) {
            getSupportActionBar().setTitle("Create new profile");
            final Fragment fragment = new ProfileCreationFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);
            profileCreationFragment = (ProfileCreationFragment)fragment;
            displayFragment(profileCreationFragment);

            fab.setVisibility(View.VISIBLE);
            fab.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ic_check_white_24dp));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ProfileCreationFragment)fragment).setErrorsDisabled();
                    hideKeyboard();
                    if(((ProfileCreationFragment)fragment).isProfileDataValid()){
                        Snackbar successSnackbar = Snackbar.make(getCurrentFocus(),"Successfully added new profile",Snackbar.LENGTH_SHORT);
                        successSnackbar.show();
                        //TODO: save data into shared prefs
                        onBackPressed();
                    }else{
                        Snackbar failureSnackbar = Snackbar.make(getCurrentFocus(),"Information is either incomplete or faulty",Snackbar.LENGTH_SHORT);
                        failureSnackbar.show();
                    }
                }
            });
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNoProfileButtonClick() {
        onNavigationItemSelected(new ActionMenuItem(getApplicationContext(),0,R.id.nav_profie_create,0,0,""));
    }

    @Override
    public void onCVCreated() {
        Toast.makeText(getApplicationContext(),"CV created",Toast.LENGTH_SHORT).show();
    }

    private void detachAllFragments(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.detach(noProfilesFragment);
        fragmentTransaction.detach(profileCreationFragment);
        fragmentTransaction.detach(homeFragment);
        fragmentTransaction.detach(cvCreationFragment);
        fragmentTransaction.detach(cvManagementFragment);
        fragmentTransaction.detach(profileManagementFragment);

        fragmentTransaction.commit();
    }

    private void displayFragment(Fragment fragment){
        if(!fragment.isAdded())
        getSupportFragmentManager()
                .beginTransaction()
//                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .add(R.id.fragmentsRelativeLayout, fragment)
                .replace(R.id.fragmentsRelativeLayout,fragment)
                .addToBackStack(null)
                .commit();
    }

    private ArrayList<Profile> getMockProfilesArrayList(){
        ArrayList<Profile> result = new ArrayList<>();
        Profile profile = new Profile();
        Profile profile2 = new Profile();
        profile.setName("Jan Kowalski");
        Bitmap bitmap = Bitmap.createBitmap(256,256, Bitmap.Config.ARGB_8888);
        for(int i=0;i<256;i++){
            for(int j=0;i<256;i++){
                bitmap.setPixel(i, j, Color.CYAN);
            }
        }
        profile.setPhoto(bitmap);
        profile2.setPhoto(bitmap);
        profile2.setName("Andrzej Nowak");
        result.add(profile);
        result.add(profile2);
        return result;
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                    hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onSumfin() { // home fragment

    }

    @Override
    public void onCVManagementFragmentInteraction(Uri uri) {

    }

    @Override
    public void onCreateProfileFragmentInteraction(View view) {

    }
}