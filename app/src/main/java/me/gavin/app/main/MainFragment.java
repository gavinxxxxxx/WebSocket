package me.gavin.app.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SearchView;
import android.text.InputType;

import me.gavin.app.contact.QueryContactFragment;
import me.gavin.app.im.IMService;
import me.gavin.base.App;
import me.gavin.base.BindingFragment;
import me.gavin.base.RxBus;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.FragmentMainBinding;

/**
 * 主页
 *
 * @author gavin.xiong 2018/2/3
 */
public class MainFragment extends BindingFragment<FragmentMainBinding> {

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_main;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            App.get().startService(new Intent(App.get(), IMService.class));
//        } else {
//            App.get().startForegroundService(new Intent(App.get(), IMService.class));
//        }

//        ComponentName serviceName = new ComponentName(getContext(), IMJobService.class);
//        JobInfo.Builder builder = new JobInfo.Builder(0x253, serviceName);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            builder.setPeriodic(15 * 60 * 1000);
//        } else {
//            builder.setPeriodic(5 * 60 * 1000);
//        }
//        JobScheduler jobScheduler = (JobScheduler) getContext().getSystemService(Context.JOB_SCHEDULER_SERVICE);
//        jobScheduler.schedule(builder.build());

        mBinding.toolbar.inflateMenu(R.menu.action_search);
        SearchView searchView = (SearchView) mBinding.toolbar.getMenu().findItem(R.id.actionSearch).getActionView();
        searchView.setInputType(InputType.TYPE_CLASS_TEXT);
        searchView.setQueryHint("账号或昵称");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                RxBus.get().post(new StartFragmentEvent(QueryContactFragment.newInstance(query)));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        initViewPager();
    }

    @Override
    public boolean onBackPressedSupport() {
        getActivity().moveTaskToBack(false);
        return true;
    }

    private void initViewPager() {
        MainPagerAdapter pagerAdapter = new MainPagerAdapter(getChildFragmentManager());
        mBinding.viewPager.setAdapter(pagerAdapter);
        mBinding.viewPager.setOffscreenPageLimit(3);
        mBinding.tabLayout.setupWithViewPager(mBinding.viewPager);
    }
}
