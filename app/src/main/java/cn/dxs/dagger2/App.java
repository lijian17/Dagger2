package cn.dxs.dagger2;

import cn.dxs.dagger2.di.DaggerCookAppComponent;
import dagger.android.AndroidInjector;
import dagger.android.DaggerApplication;

/**
 * @author lijian
 * @date 2019-09-19 9:20
 */
public class App extends DaggerApplication {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerCookAppComponent.builder().create(this);
    }

}
