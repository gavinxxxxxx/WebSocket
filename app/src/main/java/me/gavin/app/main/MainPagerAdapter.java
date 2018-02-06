package me.gavin.app.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import me.gavin.app.contact.ContactsFragment;
import me.gavin.app.message.MessageFragment;

/**
 * RxJavaPagerAdapter
 *
 * @author gavin.xiong 2016/12/5
 */
class MainPagerAdapter extends FragmentStatePagerAdapter {

    private String[] tabs = new String[]{
            "消息",
            "通讯录",
            "我的",
    };

    MainPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return MessageFragment.newInstance();
            case 1:
                return ContactsFragment.newInstance();
            default:
                return MessageFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        return tabs.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs[position];
    }
}
