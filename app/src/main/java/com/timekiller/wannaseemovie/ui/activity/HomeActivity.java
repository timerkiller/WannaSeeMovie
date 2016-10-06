package com.timekiller.wannaseemovie.ui.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v7.graphics.Palette;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.timekiller.wannaseemovie.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.timekiller.wannaseemovie.ui.customLayout.CustomViewPager;
import com.timekiller.wannaseemovie.ui.customLayout.PagerSlidingTabStrip;
import com.timekiller.wannaseemovie.ui.fragment.CommunityFragment;
import com.timekiller.wannaseemovie.ui.fragment.MovieFragment;
import com.timekiller.wannaseemovie.ui.fragment.dummy.CommunityDummyContent;
import com.timekiller.wannaseemovie.ui.fragment.dummy.MovieDummyContent;
import com.timekiller.wannaseemovie.util.KeyboardUtils;
import com.timekiller.wannaseemovie.util.StringUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity implements
        View.OnClickListener
        ,MovieFragment.OnListFragmentInteractionListener
        ,CommunityFragment.OnListFragmentInteractionListener{

    private final static String mTag="HomeActivity";
    private static Boolean isExit = false;
    private SearchView mSearchView;
    private EditText mEdit;
    @BindView(R.id.drawer)  DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ShareActionProvider mShareActionProvider;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    @BindView(R.id.view_pager_custom)  CustomViewPager mViewPager;
    @BindView(R.id.toolbar)  Toolbar mToolbar;

    @BindView(R.id.linear_layout_movie) LinearLayout mMovieLinearLayout;
    @BindView(R.id.linear_layout_community) LinearLayout mCommunityLinearLayout;
    @BindView(R.id.button_movie_home) Button mMovieButton;
    @BindView(R.id.button_community) Button mCommunityButton;
    @BindView(R.id.textview_movie_home) TextView mMovieTextView;
    @BindView(R.id.textview_community) TextView mCommunityTextView;
    @BindView(R.id.activity_home_progress_bar) ProgressBar mProgressBar;

    private List<Fragment> mFragmentList;
    private FragmentAdapter mFragmentAdapter;

    final static private int FRAGMENT_ID_MOVIE = 0;
    final static private int FRAGMENT_ID_COMMUNITY = 1;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                /* no-op */
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // hiding the floating action button if text is empty
            if (s.length() == 0) {
                Log.i(mTag,"onTextChanged");
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            // showing the floating action button if avatar is selected and input data is valid
            Log.i(mTag,"afterTextChanged");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        setUpToolbar();
        initContentView();
    }

    private void initContentView(){

        mFragmentList = new ArrayList<>();
        MovieFragment movieFragment = MovieFragment.newInstance(3);
        CommunityFragment communityFragment = CommunityFragment.newInstance(1);
        movieFragment.onAttach(this.getApplicationContext());
        communityFragment.onAttach(this.getApplicationContext());
        mFragmentList.add(movieFragment);
        mFragmentList.add(communityFragment);

        mViewPager = (CustomViewPager)this.findViewById(R.id.view_pager_custom);
        mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),mFragmentList);
        mViewPager.setAdapter(mFragmentAdapter);
        mViewPager.addOnPageChangeListener(new TabOnPageChangeListener());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menu
                .findItem(R.id.action_share));
        final MenuItem item = menu.findItem(R.id.ab_search);
        mSearchView = (SearchView) MenuItemCompat.getActionView(item);
        mSearchView.setIconifiedByDefault(false);
        mEdit = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
//        mEdit.addTextChangedListener(mTextWatcher);

        mSearchView.setQueryHint("输入您感兴趣的...");

        final LinearLayout search_edit_frame= (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        //search_edit_frame.setBackgroundResource(R.drawable.shape_from_edit);
        search_edit_frame.setClickable(true);

        mEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                search_edit_frame.setPressed(hasFocus);
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_edit_frame.setPressed(true);
            }
        });

        mEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
             /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                /*隐藏软键盘*/
                    Log.i(mTag,"go button press");
                    mSearchView.clearFocus();
                    search_edit_frame.setPressed(false);
                    KeyboardUtils.hideSoftInput(HomeActivity.this,mEdit);
                    //清空
                    if (!StringUtils.isEmpty(v.getText().toString())) {
                        String value = v.getText().toString();

                        //TODO:here to request data from server

                    }
                    return true;
                }
                return false;
            }
        });
        return true;
    }

    private void setUpToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("Rocko");// 标题的文字需在setSupportActionBar之前，不然会无效
        setSupportActionBar(mToolbar);
        //noinspection ConstantConditions
        //getSupportActionBar().setDisplayShowTitleEnabled(false);
        ActionBar bar= getSupportActionBar();
        if(bar != null){
            bar.setDisplayHomeAsUpEnabled(true);
        }
        else {
            Log.e(mTag,"Get SupportActionBar error");
        }

        mDrawerLayout=(DrawerLayout)this.findViewById(R.id.drawer);
        /*init drawer */
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open,
                R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_search:
                Toast.makeText(HomeActivity.this,"search",Toast.LENGTH_SHORT).show();
            case R.id.action_settings:
                Toast.makeText(HomeActivity.this, "action_settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_share:
                Toast.makeText(HomeActivity.this, "action_share",  Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
        return true;
    }

    @OnClick({R.id.linear_layout_movie,R.id.linear_layout_community})
    @Override
    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.linear_layout_movie:
//                Toast.makeText(HomeActivity.this,"home activity click",Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.linear_layout_community:
//                Toast.makeText(HomeActivity.this,"community click",Toast.LENGTH_SHORT).show();
//                break;
//            default:
//
//        }

        //启动相应界面的Fragment,
        FragmentManager fragmentManager = getSupportFragmentManager();
        Log.d(mTag,"In Click function");
        resetWidget();
        switch (v.getId()){
            case R.id.linear_layout_movie:
                mMovieButton.setBackgroundResource(R.drawable.icon_bottom_mission_green);
                mMovieTextView.setTextColor(Color.parseColor("#05C0AB"));

                Log.d(mTag,"In task tab click");
                mViewPager.setCurrentItem(FRAGMENT_ID_MOVIE);

                break;
            case R.id.linear_layout_community:
                mCommunityButton.setBackgroundResource(R.drawable.icon_bottom_msg_green);
                mCommunityTextView.setTextColor(Color.parseColor("#05C0AB"));

                Log.d(mTag,"In message tab click");
                mViewPager.setCurrentItem(FRAGMENT_ID_COMMUNITY);
                break;
            default:
                throw new UnsupportedOperationException(
                        "The onClick method has not been implemented for " + getResources()
                                .getResourceEntryName(v.getId()));
        }

    }




    public void resetWidget(){
        mCommunityButton.setBackgroundResource(R.drawable.icon_bottom_mission_gray);
        mMovieButton.setBackgroundResource(R.drawable.icon_bottom_msg_gray);

        mMovieTextView.setTextColor(Color.parseColor("#808080"));
        mCommunityTextView.setTextColor(Color.parseColor("#808080"));

        mCommunityButton.setAlpha(1.0f);
        mMovieButton.setAlpha(1.0f);

        mMovieTextView.setAlpha(1.0f);
        mCommunityTextView.setAlpha(1.0f);
        Log.i(mTag,"reset widget done");
    }

    @Override
    public void onListFragmentInteraction(MovieDummyContent.DummyItem item) {

    }

    @Override
    public void onListFragmentInteraction(CommunityDummyContent.DummyItem item) {

    }

    class TabOnPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Log.i(mTag,"position:" + position+" positionOffset:"+positionOffset + " positionOffsetPixels" + positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            //重置所有TextView和Button的字体颜色
            resetWidget();
            switch (position) {
                case FRAGMENT_ID_MOVIE:
                    mMovieButton.setBackgroundResource(R.drawable.star);
                    mMovieTextView.setTextColor(Color.parseColor("#05C0AB"));
                    break;
                case FRAGMENT_ID_COMMUNITY:
                    mCommunityButton.setBackgroundResource(R.drawable.icon_bottom_msg_green);
                    mCommunityTextView.setTextColor(Color.parseColor("#05C0AB"));
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    public static class FragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragments;
        public FragmentAdapter(FragmentManager fm,List<Fragment> fragments) {
            super(fm);
            this.mFragments=fragments;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click(); //调用双击退出函数
        }
        return false;
    }

    private void exitBy2Click() {
        Timer exitTimer= null;
        if (!isExit) {
            isExit = true; // 准备退出
            Toast.makeText(this,R.string.touch_again_to_exit, Toast.LENGTH_SHORT).show();
            exitTimer = new Timer();
            exitTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ActivityCompat.finishAfterTransition(this);
        }
    }
}
