package jt.com.weixinfloatwindow;

import android.app.Application;
import android.content.Context;

/**
 * @author：琚涛
 * @date：2018/06/29
 * @description：
 */
public class MyApplication extends Application {
    private static MyApplication app;

    public static Context getInstance() {
        return app;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        app=this;
    }
}
