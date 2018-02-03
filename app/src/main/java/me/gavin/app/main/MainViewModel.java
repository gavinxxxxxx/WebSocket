package me.gavin.app.main;

import android.content.Context;

import me.gavin.base.FragViewModel;
import me.gavin.im.ws.databinding.FragmentMainBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/2/3
 */
public class MainViewModel extends FragViewModel<MainFragment, FragmentMainBinding> {

    public MainViewModel(Context context, MainFragment fragment, FragmentMainBinding binding) {
        super(context, fragment, binding);
    }

    @Override
    public void afterCreate() {

    }
}
