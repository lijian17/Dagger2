package cn.dxs.dagger2.di;

import javax.inject.Singleton;

import cn.dxs.dagger2.App;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

/**
 * @author lijian
 * @date 2019-09-19 9:14
 */
@Singleton
@Component(modules = {AndroidSupportInjectionModule.class, ActivityModules.class, CookModules.class})
public interface CookAppComponent extends AndroidInjector<App> {

    @Component.Builder
    abstract class Builder extends AndroidInjector.Builder<App> {
    }
}
