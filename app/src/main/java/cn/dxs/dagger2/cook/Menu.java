package cn.dxs.dagger2.cook;

import java.util.Map;

import javax.inject.Inject;

/**
 * @author lijian
 * @date 2019-09-19 9:36
 */
public class Menu {

    public Map<String,Boolean> menus;

    @Inject
    public Menu( Map<String,Boolean> menus){
        this.menus = menus;
    }

    Map<String,Boolean> getMenus(){
        return menus;
    }
}
