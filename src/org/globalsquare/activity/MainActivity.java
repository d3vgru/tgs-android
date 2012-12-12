package org.globalsquare.activity;

import java.util.*;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.*;
import android.support.v4.view.*;
import android.widget.*;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;

import org.globalsquare.*;
import org.globalsquare.framework.*;

public class MainActivity extends SherlockFragmentActivity {
	public final static int TAB_SEARCH = 0;
	public final static int TAB_OVERVIEW = 1;
	
	
	ViewPager mViewPager;
    TabsAdapter mTabsAdapter;
    TextView tabCenter;
    TextView tabText;
    
    /*
    ListFragment mOverviewFragment;
    
    public void setOverviewFragment(ListFragment overviewFragment) {
    	mOverviewFragment = overviewFragment;
    }
    */
    
    
    // adapted from https://bitbucket.org/owentech/abstabsviewpager
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ActionBar bar = getSupportActionBar();
		
        setContentView(R.layout.main_activity);

        setTitle(getString(R.string.short_name));
        
        bar.setDisplayShowHomeEnabled(false);
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		mViewPager = (ViewPager)findViewById(R.id.pager);

		mTabsAdapter = new TabsAdapter(this, mViewPager);

		// TAB_SEARCH
        mTabsAdapter.addTab(
                bar.newTab().setText(getString(R.string.searchBtnLabel)),
                SearchFragment.class, null);
        // TAB_OVERVIEW
        mTabsAdapter.addTab(
                bar.newTab().setText(getString(R.string.overviewLabel)),
                OverviewListFragment.class, null);
        // tabs for each square the user has joined
        mTabsAdapter.addTab(
                bar.newTab().setText("abc"),
                MessageListFragment.class, null);
        mTabsAdapter.addTab(
                bar.newTab().setText("Another Square"),
                MessageListFragment.class, null);
        
        // select Overview tab
        mViewPager.setCurrentItem(TAB_OVERVIEW);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}


	// adapted from https://bitbucket.org/owentech/abstabsviewpager
	public static class TabsAdapter extends FragmentPagerAdapter implements
    ActionBar.TabListener, ViewPager.OnPageChangeListener {
		private final Context mContext;
		private final ActionBar mActionBar;
		private final ViewPager mViewPager;
		private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();

		static final class TabInfo {
			private final Class<?> clss;
			private final Bundle args;

			TabInfo(Class<?> _class, Bundle _args) {
				clss = _class;
				args = _args;
			}
		}

		public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
			super(activity.getSupportFragmentManager());
			mContext = activity;
			mActionBar = activity.getSupportActionBar();
			mViewPager = pager;
			mViewPager.setAdapter(this);
			mViewPager.setOnPageChangeListener(this);
		}

		public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args) {
			TabInfo info = new TabInfo(clss, args);
			tab.setTag(info);
			tab.setTabListener(this);
			mTabs.add(info);
			mActionBar.addTab(tab);
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return mTabs.size();
		}

		@Override
		public Fragment getItem(int position) {
			TabInfo info = mTabs.get(position);
			return Fragment.instantiate(mContext, info.clss.getName(),
                    info.args);
		}

		public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
		}

		public void onPageSelected(int position)
		{
			mActionBar.setSelectedNavigationItem(position);
		}

		public void onPageScrollStateChanged(int state) {
		}

		public void onTabSelected(Tab tab, FragmentTransaction ft) {
			Object tag = tab.getTag();
			for (int i = 0; i < mTabs.size(); i++) {
				if (mTabs.get(i) == tag) {
                    mViewPager.setCurrentItem(i);
                    
                    /*
                    switch(i) {
                		case TAB_SEARCH: // search
                			break;
                		case TAB_OVERVIEW: // "my squares" (combined history)
                			break;
                    	default:
                    		break;
                    }
                    */
                    break;
				}
			}
		}

		public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		}

		public void onTabReselected(Tab tab, FragmentTransaction ft) {
		}
	}
	
}
