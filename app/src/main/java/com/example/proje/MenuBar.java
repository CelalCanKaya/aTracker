
package com.example.proje;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.Chart;
import com.github.mzule.fantasyslide.SideBar;
import com.github.mzule.fantasyslide.SimpleFantasyListener;

public abstract class MenuBar extends AppCompatActivity {
    protected DrawerLayout mdrawerLayout;
    private StackTraceElement[] stackTraceElements;
    public TextView text0, text1, text2, text6;


    protected void menuBar() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final DrawerArrowDrawable indicator = new DrawerArrowDrawable(this);
        indicator.setColor(Color.WHITE);
        getSupportActionBar().setHomeAsUpIndicator(indicator);
        stackTraceElements = Thread.currentThread().getStackTrace();
        text0 = findViewById(R.id.but0);
        text1 = findViewById(R.id.but1);
        text2 = findViewById(R.id.but2);
        text6 = findViewById(R.id.but6);
        setListener();
        mdrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mdrawerLayout.setScrimColor(Color.TRANSPARENT);
        mdrawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (((ViewGroup) drawerView).getChildAt(1).getId() == R.id.leftSideBar) {
                    indicator.setProgress(slideOffset);
                }
            }
        });
    }


    private void setListener() {
        SideBar leftSideBar = (SideBar) findViewById(R.id.leftSideBar);
        leftSideBar.setFantasyListener(new SimpleFantasyListener() {
            @Override
            public boolean onHover(@Nullable View view, int index) {
                if(index==0){
                    text0.setTextColor(Color.RED);
                    text1.setTextColor(Color.WHITE);
                    text2.setTextColor(Color.WHITE);
                    text6.setTextColor(Color.WHITE);
                }
                else if(index==1){
                    text0.setTextColor(Color.WHITE);
                    text1.setTextColor(Color.RED);
                    text2.setTextColor(Color.WHITE);
                    text6.setTextColor(Color.WHITE);
                }
                else if(index==2){
                    text0.setTextColor(Color.WHITE);
                    text1.setTextColor(Color.WHITE);
                    text2.setTextColor(Color.RED);
                    text6.setTextColor(Color.WHITE);
                }
                else if(index==6){
                    text0.setTextColor(Color.WHITE);
                    text1.setTextColor(Color.WHITE);
                    text2.setTextColor(Color.WHITE);
                    text6.setTextColor(Color.RED);
                }
                else{
                    text0.setTextColor(Color.WHITE);
                    text1.setTextColor(Color.WHITE);
                    text2.setTextColor(Color.WHITE);
                    text6.setTextColor(Color.WHITE);
                }
                return false;

            }

            @Override
            public boolean onSelect(View view, int index) {
                if (index == 0) {

                } else if (index == 1) {
                    if(!stackTraceElements[3].getClassName().equals(MainScreen.class.getName())){
                        Intent menu = new Intent(getApplicationContext(), MainScreen.class);
                        startActivity(menu);
                        finish();
                    }
                    else{
                        mdrawerLayout.closeDrawer(GravityCompat.START);
                    }
                } else if (index == 2) {
                    if(!stackTraceElements[3].getClassName().equals(ChartScreen.class.getName())){
                        Intent menu = new Intent(getApplicationContext(), ChartScreen.class);
                        startActivity(menu);
                        finish();
                    }
                    else{
                        mdrawerLayout.closeDrawer(GravityCompat.START);
                    }
                } else if (index == 6){
                    finishAndRemoveTask();
                    System.exit(0);
                }
                return false;
            }

            @Override
            public void onCancel() {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if (mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mdrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                mdrawerLayout.openDrawer(GravityCompat.START);
            }
        }
        return true;
    }

    public void onClick(View view) {
    }
}