
package com.example.proje;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public abstract class MenuBar extends AppCompatActivity {
    protected DrawerLayout mdrawerLayout;
    protected ActionBarDrawerToggle actionBarDrawerToggle;
    private StackTraceElement[] stackTraceElements;


    protected void menuBar(){
        mdrawerLayout = findViewById(R.id.drawerLayout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, mdrawerLayout, R.string.open, R.string.close);
        mdrawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        stackTraceElements = Thread.currentThread().getStackTrace();


        NavigationView navigation = findViewById(R.id.toolbar);
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int id = item.getItemId();
                switch (id) {
                    case R.id.log:
                        if (!stackTraceElements[3].getClassName().equals(ChartScreen.class.getName())) {
                            Intent menu = new Intent(getApplicationContext(), ChartScreen.class);
                            startActivity(menu);
                            finish();
                        } else
                            mdrawerLayout.closeDrawers();
                        break;
                    case R.id.home:
                        if (!stackTraceElements[3].getClassName().equals(MainScreen.class.getName())) {
                            Intent menu = new Intent(getApplicationContext(), MainScreen.class);
                            startActivity(menu);
                            finish();
                        } else
                            mdrawerLayout.closeDrawers();
                        break;
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(actionBarDrawerToggle.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

}

