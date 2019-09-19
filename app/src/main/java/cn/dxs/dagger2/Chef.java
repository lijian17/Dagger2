package cn.dxs.dagger2;


import java.util.Map;

import javax.inject.Inject;

/**
 * @author lijian
 * @date 2019-09-19 9:35
 */
public class Chef implements Cooking {

    Menu menu;

    @Inject
    public Chef(Menu menu) {
        this.menu = menu;
    }

    @Override
    public String cook() {
        //key菜名， value是否烹饪
        Map<String, Boolean> menuList = menu.getMenus();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Boolean> entry : menuList.entrySet()) {
            if (entry.getValue()) {
                sb.append(entry.getKey()).append(",");
            }
        }

        return sb.toString();
    }
}
