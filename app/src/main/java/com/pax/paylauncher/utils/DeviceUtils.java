package com.pax.paylauncher.utils;

import com.pax.dal.ISys;
import com.pax.dal.entity.ENavigationKey;
import com.pax.neptunelite.api.NeptuneLiteUser;
import com.pax.paylauncher.App;

/**
 * @author ligq
 * @date 2018/8/8
 */

@SuppressWarnings("WeakerAccess")
public class DeviceUtils {
    private DeviceUtils() {
        throw new IllegalStateException();
    }

    public static void enableKeys(boolean enable) {
//        enableHomeKey(enable);
//        enableRecentKey(enable);
//        enableStatusBar(enable);
//        enableBackKey(true);
    }

    /**
     * enable/disable status bar
     *
     * @param enable true/false
     */
    public static void enableStatusBar(boolean enable) {
        ISys sys = getSys();
        if (sys != null) {
            sys.enableStatusBar(enable);
        }
    }

    /**
     * enable/disable home key
     *
     * @param enable true/false
     */
    public static void enableHomeKey(boolean enable) {
        ISys sys = getSys();
        if (sys != null) {
            sys.enableNavigationKey(ENavigationKey.HOME, enable);
        }
    }

    /**
     * enable/disable recent key
     *
     * @param enable true/false
     */
    public static void enableRecentKey(boolean enable) {
        ISys sys = getSys();
        if (sys != null) {
            sys.enableNavigationKey(ENavigationKey.RECENT, enable);
        }
    }

    /**
     * enable/disable recent key
     *
     * @param enable true/false
     */
    public static void enableBackKey(boolean enable) {
        ISys sys = getSys();
        if (sys != null) {
            sys.enableNavigationKey(ENavigationKey.BACK, enable);
        }
    }

    public static ISys getSys() {
        try {
            return NeptuneLiteUser.getInstance().getDal(App.getApp()).getSys();
        } catch (Exception e) {
            return null;
        }
    }
}
