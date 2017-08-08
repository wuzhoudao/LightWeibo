package com.jack.lightweibo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.jack.lightweibo.fragments.HomeFragment;

public class UnloginActivity extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout mHomeTab;
    private RelativeLayout mMessageTab;
    private RelativeLayout mDiscovoryTab;
    private RelativeLayout mProfileTab;
    private RelativeLayout mPostTab;

    private static final int HOME_FRAGMENT = 0X001;
    private static final int MESSAGE_FRAGMENT = 0X002;
    private static final int DISCOVERY_FRAGMENT = 0X004;
    private static final int PROFILE_FRAGMENT = 0X005;

    private int mCurrentIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unlogin);

        mHomeTab = (RelativeLayout) findViewById(R.id.tv_home);
        mMessageTab = (RelativeLayout) findViewById(R.id.tv_message);
        mDiscovoryTab = (RelativeLayout) findViewById(R.id.tv_discovery);
        mProfileTab = (RelativeLayout) findViewById(R.id.tv_profile);
        mPostTab = (RelativeLayout) findViewById(R.id.fl_post);
        mHomeTab.setOnClickListener(this);
        mDiscovoryTab.setOnClickListener(this);
        mMessageTab.setOnClickListener(this);
        mPostTab.setOnClickListener(this);
        mProfileTab.setOnClickListener(this);
        setTabFragment(HOME_FRAGMENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_home:
                setTabFragment(HOME_FRAGMENT);
                break;
            case R.id.tv_message:
                setTabFragment(MESSAGE_FRAGMENT);
                break;
            case R.id.tv_discovery:
                setTabFragment(DISCOVERY_FRAGMENT);
                break;
            case R.id.tv_profile:
                setTabFragment(PROFILE_FRAGMENT);
                break;
            case R.id.fl_post:
                Toast.makeText(UnloginActivity.this, "请先登录！", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void setTabFragment(int index) {
        HomeFragment homeFragment = new HomeFragment();
    }

}
