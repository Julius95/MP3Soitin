package com.example.julius.mp3_soitin;


import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.ListView;

import com.example.julius.mp3_soitin.testApplication.TestApplication;
import com.example.julius.mp3_soitin.views.track.TrackListFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.robolectric.shadows.support.v4.SupportFragmentTestUtil.startFragment;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, application = TestApplication.class)
public class FragmentSwitcherTests {
    MainActivity activity;
    //TrackListFragment fragment;

    @Before
    public void setUp() {
        activity = Robolectric.setupActivity(MainActivity.class);
        //fragment = TrackListFragment.newInstance(null);
    }

    @Test
    public void shouldNotBeNull() throws Exception
    {
        assertNotNull(activity);
        //assertNotNull( fragment );
    }

    @Test
    public void showTracks() throws InterruptedException {
        //ViewPager pager = activity.getWindow().findViewById(R.id.pager);
        //pager.setCurrentItem(0);
        activity.switchTo(FragmentType.Tracks);
        ListView list = activity.findViewById(android.R.id.list);
        System.out.println(list.getCount());
        assertNotNull(list);
        assertEquals(2, list.getCount());
    }

    @Test
    public void showAlbums(){
        //https://github.com/robolectric/robolectric/issues/2871
        ViewPager pager = activity.getWindow().findViewById(R.id.pager);
        pager.setCurrentItem(1);
        //activity.switchTo(FragmentType.Albums);
        ListView list = activity.findViewById(android.R.id.list);
        System.out.println(list.getCount());
        assertNotNull(list);
        assertEquals(0, list.getCount());
    }

}
