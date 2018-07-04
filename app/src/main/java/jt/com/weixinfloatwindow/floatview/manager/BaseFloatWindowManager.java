package jt.com.weixinfloatwindow.floatview.manager;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.view.View;
import android.view.WindowManager;

/**
 * @author：琚涛
 * @date：2018/06/29
 * @description：
 */
public abstract class BaseFloatWindowManager {

    WindowManager windowManager;
    WindowManager.LayoutParams wmParams;
    boolean hasShown;

    /**
     * 创建浮动窗口
     * @param context 上下文
     */
    public abstract void createFloatWindow(Context context);

    /**
     * 本管理器托管的界面
     * @param context
     * @return
     */
    public abstract View createFloatView(Context context);
    /**
     * 本管理器托管的界面
     * @return
     */
    public abstract View getFloatView();

    /**
     * 返回当前已创建的WindowManager
     */
    WindowManager getWindowManager(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return windowManager;
    }

    /**
     * 获取合理的 WindowManager Type
     * @return
     */
    protected int getWMType(Context context) {
        //android7.0以后不能用TYPE_TOAST
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            //以下代码块使得android6.0之后的用户不必再去手动开启悬浮窗权限
            String packName = context.getPackageName();
            PackageManager pm = context.getPackageManager();
            boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.SYSTEM_ALERT_WINDOW", packName));
            if (permission) {
                wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            } else {
                wmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        }
        return wmParams.type;
    }

    /**
     * 移除悬浮窗
     */
    public void removeFloatWindowManager() {
        //移除悬浮窗口
        boolean isAttach = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            isAttach = getFloatView().isAttachedToWindow();
        }
        if (hasShown && isAttach && windowManager != null) {
            windowManager.removeView(getFloatView());
        }

    }
    /**
     * 隐藏悬浮窗
     */
    public void hide() {
        if (hasShown) {
            windowManager.removeViewImmediate(getFloatView());
        }
        hasShown = false;
    }
    /**
     * 显示悬浮窗
     */
    public void show() {
        if (!hasShown) {
            windowManager.addView(getFloatView(), wmParams);
        }
        hasShown = true;
    }
}
