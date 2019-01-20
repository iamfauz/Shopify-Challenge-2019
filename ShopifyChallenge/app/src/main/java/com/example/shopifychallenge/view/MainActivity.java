package com.example.shopifychallenge.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.shopifychallenge.R;
import com.example.shopifychallenge.utils.FragmentUtils;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentUtils.replaceFragment(this, CollectionListFragment.newInstance(), R.id.fragment_container, false);
    }

    /**
     * Method to change action bar title
     *
     * @param title
     */
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }
}
