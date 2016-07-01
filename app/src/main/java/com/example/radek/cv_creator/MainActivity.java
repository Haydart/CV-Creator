package com.example.radek.cv_creator;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.radek.cv_creator.fragments.CVCreationFragment;
import com.example.radek.cv_creator.fragments.FindPeopleFragment;
import com.example.radek.cv_creator.fragments.NoProfilesFragment;
import com.example.radek.cv_creator.fragments.ProfileCreationFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        NoProfilesFragment.OnProfileCreateButtonClickListener,
        CVCreationFragment.OnCVCreationListener{

    PhotoManager photoManager;
    Toolbar toolbar;
    FloatingActionButton fab;
    DrawerLayout drawer;
    NavigationView navigationView;
    Button photoButton;
    ImageView imageView;

    ArrayList<Profile> userProfiles;

    NoProfilesFragment noProfilesFragment;
    CVCreationFragment cvCreationFragment;
    ProfileCreationFragment profileCreationFragment;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findRefs();

        userProfiles = getMockProfilesArrayList();

        setSupportActionBar(toolbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        fab.setVisibility(View.GONE);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        fragmentTransaction.add(R.id.fragmentsRelativeLayout, noProfilesFragment);
        //fragmentTransaction.add(R.id.fragmentsRelativeLayout, cvCreationFragment);
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
        profileCreationFragment = new ProfileCreationFragment();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profiles) {
            detachAllFragments();
            Fragment fragment = new FindPeopleFragment();

            // Handle the camera action
        } else if (id == R.id.nav_cv_create) {
            detachAllFragments();
            getSupportActionBar().setTitle("Create new CV");
            Bundle bundle = new Bundle();
            Fragment fragment = new CVCreationFragment();
            bundle.putParcelableArrayList("profilesResource",userProfiles);
            fragment.setArguments(bundle);

            getSupportFragmentManager()
                    .beginTransaction()
                    //.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                    .replace(R.id.fragmentsRelativeLayout, fragment)
                    .addToBackStack(null)
                    .commit();

        } else if (id == R.id.nav_cv_storage) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_manage_profiles) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onNoProfileButtonClick() {
        Toast.makeText(getApplicationContext(),"Button clicked", Toast.LENGTH_SHORT).show();
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragmentsRelativeLayout, profileCreationFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onCVCreated() {
        Toast.makeText(getApplicationContext(),"CV created",Toast.LENGTH_SHORT).show();
    }

    private void detachAllFragments(){
        fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.detach(noProfilesFragment);
        fragmentTransaction.detach(profileCreationFragment);

        fragmentTransaction.commit();
    }

    private ArrayList<Profile> getMockProfilesArrayList(){
        ArrayList<Profile> result = new ArrayList<>();
        Profile profile = new Profile();
        Profile profile2 = new Profile();
        profile.setName("Jan Kowalski");
        profile2.setName("Andrzej Nowak");
        result.add(profile);
        result.add(profile2);
        return result;
    }
}