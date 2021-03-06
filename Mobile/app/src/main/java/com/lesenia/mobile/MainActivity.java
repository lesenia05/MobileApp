package com.lesenia.mobile;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.firebase.messaging.FirebaseMessaging;

import android.app.Notification;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPager = findViewById(R.id.main_activity_view_pager);
        TabsAdapter tabsAdapter = new TabsAdapter(getSupportFragmentManager(), getLifecycle());

        viewPager.setAdapter(tabsAdapter);

        final String[] tabTitles = new String[]{"Medicine", "Tab 2", "Profile"};
        final TabLayout tabLayout = findViewById(R.id.main_activity_tab_layout);
        new TabLayoutMediator(tabLayout, viewPager,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabTitles[position]);
                    }
                }
        ).attach();
        FirebaseMessaging.getInstance().subscribeToTopic("NEWS");
    }
}