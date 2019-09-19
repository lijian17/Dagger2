package cn.dxs.dagger2.di;

import cn.dxs.dagger2.MainActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * @author lijian
 * @date 2019-09-19 9:00
 */
@Module
public abstract class ActivityModules {

    @ContributesAndroidInjector
    abstract MainActivity contributeMainActivity();

}
