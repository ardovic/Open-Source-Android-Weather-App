package com.ardovic.weatherappprototype;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;

import android.support.design.widget.NavigationView;

import android.support.v7.app.AppCompatActivity;

import android.widget.Toast;

public class NavigationDrawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Suspect 2
        setContentView(R.layout.activity_main);
        //Toolbar toolbar = findViewById(R.id.drawer_layout);
        //Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
        //ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //NavigationView navigationView = findViewById(R.id.nav_view);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        Log.d("start", ">>> Navigation Bar");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
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

        //Add
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        //String title = null;
        //Add

        if (id == R.id.nav_auth) {
            // Handle the camera action

        } else if (id == R.id.nav_credits) {
            Log.d("test", ">>> credits");
            fragment = new CreditFragment();
            title = "Credits";
        } else if (id == R.id.nav_forecastType) {

        } else if (id == R.id.nav_notificationOptions) {

        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_settings) {

        }

        //Add
        if (fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_fragment_layout, fragment);
            ft.commit();
        }

        //set the toolbar title
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
        //Add

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
