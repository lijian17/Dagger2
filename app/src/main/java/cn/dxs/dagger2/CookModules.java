package cn.dxs.dagger2;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * @author lijian
 * @date 2019-09-19 8:59
 */
@Module
public class CookModules {

    @Singleton
    @Provides
    public Map<String, Boolean> providerMenus(){
        Map<String, Boolean> menus = new LinkedHashMap<>();
        menus.put("酸菜鱼", true);
        menus.put("土豆丝", true);
        menus.put("铁板牛肉", true);
        return menus;
    }
}
