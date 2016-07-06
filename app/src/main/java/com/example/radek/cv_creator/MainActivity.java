package com.example.radek.cv_creator;

import android.content.Context;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.view.menu.ActionMenuItem;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.radek.cv_creator.fragments.CVCreationFragment;
import com.example.radek.cv_creator.fragments.CVManagementFragment;
import com.example.radek.cv_creator.fragments.HomeFragment;
import com.example.radek.cv_creator.fragments.NoProfilesFragment;
import com.example.radek.cv_creator.fragments.ProfileCreationFragment;
import com.example.radek.cv_creator.fragments.ProfileEditFragment;
import com.example.radek.cv_creator.fragments.ProfileManagementFragment;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;

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
    private static AppCompatActivity instance;
    public static AppCompatActivity getInstance(){
        return instance;
    }

    ArrayList<Profile> userProfiles;
    int activeProfileIndex;
    ImageView navDrawerAddProfileImageView;
    ContentStorageManager contentStorageManager;

    NoProfilesFragment noProfilesFragment;
    CVCreationFragment cvCreationFragment;
    CVManagementFragment cvManagementFragment;
    ProfileCreationFragment profileCreationFragment;
    ProfileEditFragment profileEditFragment;
    ProfileManagementFragment profileManagementFragment;
    HomeFragment homeFragment;
    FragmentTransaction fragmentTransaction;

    public static final int ADD_TO_BACKSTACK = 1;
    public static final int DONT_ADD_TO_BACKSTACK = 0;

    int currentFragmentId;
    boolean hasCurrentProfileChanged = true; // we have to set the initial profile

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        getReferences();

        //contentStorageManager.deleteAllProfileRecords();
        userProfiles = contentStorageManager.getProfilesFromDatabase();
        activeProfileIndex = 0;

        if(userProfiles.size()==0 | userProfiles==null){

            Snackbar noProfilesSnackbar = Snackbar.make(navigationView,"You have no created profiles",Snackbar.LENGTH_LONG);
            noProfilesSnackbar.setAction("CREATE ONE", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    MainActivity.this.onNavigationItemSelected(new ActionMenuItem(getApplicationContext(),0,R.id.nav_profie_create,0,0,""));
                }
            });
            noProfilesSnackbar.show();

        }else{
            Snackbar successfulImportSnackbar = Snackbar.make(navigationView,"Profiles successfully loaded from database",Snackbar.LENGTH_SHORT);
            successfulImportSnackbar.show();
        }

        setSupportActionBar(toolbar);
        fab.setVisibility(View.GONE);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if(userProfiles.size()>0 & userProfiles!=null)
                    setNavViewProfile();
            }
        };
        drawer.setDrawerListener(toggle);

        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navDrawerAddProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onNavigationItemSelected(new ActionMenuItem(getApplicationContext(),0,R.id.nav_profie_create,0,0,""));
            }
        });

        fragmentTransaction.replace(R.id.fragmentsRelativeLayout,homeFragment);
        fragmentTransaction.addToBackStack(String.valueOf(homeFragment.getId()));
        currentFragmentId = homeFragment.getId();
        fragmentTransaction.commit();
    }

    public void getReferences(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        navDrawerAddProfileImageView = (ImageView)header.findViewById(R.id.navDrawerAddNewProfile);

        userProfiles = new ArrayList<>();
        contentStorageManager = ContentStorageManager.getInstance();

        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        noProfilesFragment = new NoProfilesFragment();
        cvCreationFragment = new CVCreationFragment();
        cvManagementFragment = new CVManagementFragment();
        profileCreationFragment = new ProfileCreationFragment();
        profileEditFragment = new ProfileEditFragment();
        profileManagementFragment = new ProfileManagementFragment();
        homeFragment = new HomeFragment();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if(getSupportFragmentManager().getBackStackEntryCount()==1)
                finish();
            else{
                super.onBackPressed();
                handleFabAndActionBarTitle(fragmentManager.findFragmentById(R.id.fragmentsRelativeLayout));
            }
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
            Snackbar.make(getCurrentFocus(),"NIY :>",Snackbar.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Bundle bundle = new Bundle();
            Fragment fragment =  new HomeFragment();
            bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);
            homeFragment = (HomeFragment)fragment;
            displayFragment(homeFragment, ADD_TO_BACKSTACK);

        } else if (id == R.id.nav_cv_manage) {
            Bundle bundle = new Bundle();
            Fragment fragment = new CVManagementFragment();
            //bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);
            cvManagementFragment = (CVManagementFragment)fragment;
            displayFragment(cvManagementFragment, ADD_TO_BACKSTACK);

        } else if (id == R.id.nav_cv_create) {
            Bundle bundle = new Bundle();
            Fragment fragment = new CVCreationFragment();
            bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);
            cvCreationFragment = (CVCreationFragment)fragment;
            displayFragment(cvCreationFragment, ADD_TO_BACKSTACK);

        } else if (id == R.id.nav_manage_profiles) {
            Fragment fragment = new ProfileManagementFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);
            profileManagementFragment = (ProfileManagementFragment)fragment;
            displayFragment(profileManagementFragment, ADD_TO_BACKSTACK);

        } else if (id == R.id.nav_profie_create) {
            final Fragment fragment = new ProfileCreationFragment();
            Bundle bundle = new Bundle();
            //bundle.putParcelableArrayList("profilesResource", userProfiles);
            fragment.setArguments(bundle);
            profileCreationFragment = (ProfileCreationFragment) fragment;
            displayFragment(profileCreationFragment, ADD_TO_BACKSTACK);
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

    private void displayFragment(Fragment fragment, int backStackMode){
        handleFabAndActionBarTitle(fragment);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.fragmentsRelativeLayout, fragment);
        transaction.replace(R.id.fragmentsRelativeLayout,fragment);

        if(backStackMode == ADD_TO_BACKSTACK)
            transaction.addToBackStack(null);

        transaction.commit();
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

    private void setNavViewProfile(){
        if(hasCurrentProfileChanged){
            ((CircularImageView)findViewById(R.id.navDrawerCircularImageView)).setImageBitmap(userProfiles.get(activeProfileIndex).getPhoto());
            ((CircularImageView)findViewById(R.id.navDrawerCircularImageView)).setBorderColor(Color.WHITE);
            ((TextView)findViewById(R.id.navDrawerProfileNameTextView)).setText(userProfiles.get(activeProfileIndex).getName());
            ((TextView)findViewById(R.id.navDrawerProfileMailTextView)).setText(userProfiles.get(activeProfileIndex).getEmail());
            hasCurrentProfileChanged = false;
        }
    }

    private void handleFabAndActionBarTitle(final Fragment currentFragment){
        if(currentFragment instanceof HomeFragment){
            fab.setVisibility(View.GONE);
            getSupportActionBar().setTitle("CV Creator");

        }else if(currentFragment instanceof CVManagementFragment){
            getSupportActionBar().setTitle("Manage CVs");
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNavigationItemSelected(new ActionMenuItem(getApplicationContext(),0,R.id.nav_cv_create,0,0,""));
                }
            });
            fab.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ic_add_white_24dp));

        }else if(currentFragment instanceof CVCreationFragment){
            getSupportActionBar().setTitle("Create new CV");
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

        }else if(currentFragment instanceof ProfileManagementFragment){
            getSupportActionBar().setTitle("Manage profiles");
            fab.setVisibility(View.VISIBLE);
            fab.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ic_add_white_24dp));
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onNavigationItemSelected(new ActionMenuItem(getApplicationContext(),0,R.id.nav_profie_create,0,0,""));
                }
            });

        }else if(currentFragment instanceof ProfileEditFragment){
            getSupportActionBar().setTitle("Edit profile");
            fab.setVisibility(View.VISIBLE);
            fab.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ic_check_white_24dp));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ProfileEditFragment) currentFragment).setErrorsDisabled();
                    hideKeyboard();
                    if (((ProfileEditFragment) currentFragment).isProfileDataValid()) {
                        Snackbar successSnackbar = Snackbar.make(getCurrentFocus(), "Successfully edited the profile", Snackbar.LENGTH_SHORT);
                        successSnackbar.show();

                    } else {
                        Snackbar failureSnackbar = Snackbar.make(getCurrentFocus(), "Information is either incomplete or faulty", Snackbar.LENGTH_SHORT);
                        failureSnackbar.show();
                    }
                }
            });

        }else if(currentFragment instanceof ProfileCreationFragment){
            getSupportActionBar().setTitle("Create new profile");
            fab.setVisibility(View.VISIBLE);
            fab.setImageBitmap(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                    R.drawable.ic_check_white_24dp));

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((ProfileCreationFragment) currentFragment).setErrorsDisabled();
                    hideKeyboard();
                    if (((ProfileCreationFragment) currentFragment).isProfileDataValid()) {
                        Snackbar successSnackbar = Snackbar.make(getCurrentFocus(), "Successfully added new profile", Snackbar.LENGTH_SHORT);
                        successSnackbar.show();
                        Profile newlyCreatedProfile;
                        newlyCreatedProfile = profileCreationFragment.getNewProfile();
                        userProfiles.add(newlyCreatedProfile);
                        try {
                            contentStorageManager.addProfileToDatabase(newlyCreatedProfile);
                        } catch (SQLiteException ex) {
                            Snackbar sqlFailSnackBar = Snackbar.make(getCurrentFocus(), "Failed writing to database, sorry :/", Snackbar.LENGTH_SHORT);
                            sqlFailSnackBar.setAction("REPORT BUG", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                }
                            });
                            sqlFailSnackBar.show();
                        }

                        fab.setVisibility(View.GONE);
                        onBackPressed();
                    } else {
                        Snackbar failureSnackbar = Snackbar.make(getCurrentFocus(), "Information is either incomplete or faulty", Snackbar.LENGTH_SHORT);
                        failureSnackbar.show();
                    }
                }
            });
        }
    }

    private void loadProfiles(){
        userProfiles = contentStorageManager.loadProfilesList();
    }

    private void saveProfiles(ArrayList<Profile> profiles){
        contentStorageManager.saveProfilesList(profiles);
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

    @Override
    public void onEditProfilePressed(int userProfilePosition) { //profile management fragment
        Toast.makeText(getApplicationContext(), "editing profile: " + userProfilePosition, Toast.LENGTH_SHORT).show();

        final Fragment fragment = new ProfileEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("editedProfile", userProfiles.get(userProfilePosition));
        fragment.setArguments(bundle);
        profileEditFragment = (ProfileEditFragment) fragment;
        displayFragment(profileEditFragment, ADD_TO_BACKSTACK);
        handleFabAndActionBarTitle(profileEditFragment);
    }

    @Override
    public void onDeleteProfilePressed(int userProfilePosition) {//profile management fragment
        Toast.makeText(getApplicationContext(), "deleting profile: " + userProfilePosition, Toast.LENGTH_SHORT).show();
    }
}