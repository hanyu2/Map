package com.example.lenovo.map;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MessageListActivity extends AppCompatActivity {

    Bundle bundle;
    Toolbar toolbar;

    ArrayList<MessageData> dataList;

    TabLayout tabLayout;
    ViewPager viewPager;
    FragmentAdapter mFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_list);

        bundle = this.getIntent().getExtras();
        dataList = bundle.getParcelableArrayList("MessageList");


        toolbar = (Toolbar) this.findViewById(R.id.toolbar_list_message);
        final View.OnClickListener toolbar_listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        };
        toolbar.setNavigationOnClickListener(toolbar_listener);

        tabLayout = (TabLayout) this.findViewById(R.id.tab_messageList);
        viewPager = (ViewPager) this.findViewById(R.id.viewpager_messageList);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        List<String> titles = new ArrayList<>();
        titles.add("Commercial");
        titles.add("Personal");
        titles.add("Social");
        titles.add("All");

        for(int i=0;i<titles.size();i++){
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)));
        }
        List<Fragment> fragments = new ArrayList<>();
        for(int i=0;i<titles.size();i++){
            fragments.add(new ListFragment());
        }
        mFragmentAdapter =
                new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
   
        viewPager.setAdapter(mFragmentAdapter);
      
        tabLayout.setupWithViewPager(viewPager);
        
        tabLayout.setTabsFromPagerAdapter(mFragmentAdapter);
    }

    public ArrayList<MessageData> getDataList() {return dataList;}

}