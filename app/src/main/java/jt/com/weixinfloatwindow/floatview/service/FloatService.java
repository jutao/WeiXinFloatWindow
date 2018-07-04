package jt.com.weixinfloatwindow.floatview.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;

import jt.com.weixinfloatwindow.floatview.FloatActionController;
import jt.com.weixinfloatwindow.floatview.FloatCallBack;
import jt.com.weixinfloatwindow.floatview.manager.BottomFloatWindowManager;
import jt.com.weixinfloatwindow.floatview.manager.DynamicFloatWindowManager;
import jt.com.weixinfloatwindow.floatview.receiver.HomeActionReceiver;

/**
 * @author：琚涛
 * @date：2018/06/28
 * @description：
 */
public class FloatService extends Service implements FloatCallBack {
    /**
     * home键监听
     */
    private HomeActionReceiver homeActionReceiver;
    /**
     * 悬浮窗管理器
     */
    private DynamicFloatWindowManager floatWindowManager;
    private BottomFloatWindowManager bottomFloatWindowManager;

    @Override
    public void onCreate() {
        super.onCreate();
        FloatActionController.getInstance().registerCallLittleMonk(this);
        //注册广播接收者
        homeActionReceiver = new HomeActionReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(homeActionReceiver, homeFilter);
        //初始化悬浮窗UI
        initWindowData();
    }

    /**
     * 初始化WindowManager
     */
    private void initWindowData() {
        bottomFloatWindowManager = new BottomFloatWindowManager();
        bottomFloatWindowManager.createFloatWindow(this);
        floatWindowManager=new DynamicFloatWindowManager(bottomFloatWindowManager);
        floatWindowManager.createFloatWindow(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //移除悬浮窗
        floatWindowManager.removeFloatWindowManager();
        bottomFloatWindowManager.removeFloatWindowManager();
        //注销广播接收者
        if (null != homeActionReceiver) {
            unregisterReceiver(homeActionReceiver);
        }
    }

    /**------------------------以下内容为{@link FloatCallBack} 接口实现-------------------------------------------------------------------*/
    @Override
    public void show() {
        floatWindowManager.show();
    }

    @Override
    public void hide() {
        floatWindowManager.hide();
    }
}
