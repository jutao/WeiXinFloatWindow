package jt.com.weixinfloatwindow.floatview;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import java.io.ObjectStreamException;

import jt.com.weixinfloatwindow.floatview.permission.FloatPermissionManager;
import jt.com.weixinfloatwindow.floatview.service.FloatService;

/**
 * @author：琚涛
 * @date：2018/06/28
 * @description：
 */
public class FloatActionController {

    private FloatActionController() {
    }

    public static FloatActionController getInstance() {
        return FloatActionController.FloatActionControllerHolder.sInstance;
    }

    /**
     * 静态内部类
     */
    private static class FloatActionControllerHolder {

        private static final FloatActionController sInstance = new FloatActionController();
    }

    /**
     * 为了杜绝对象在反序列化时重新生成对象，则重写Serializable的私有方法
     *
     * @return
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        return FloatActionController.FloatActionControllerHolder.sInstance;
    }

    private FloatCallBack floatCallBack;

    /**
     * 开启服务悬浮窗
     */
    public void startFloatServer(Context context) {
        boolean isPermission = FloatPermissionManager.getInstance().applyFloatWindow(context);
        //有对应权限或者系统版本小于7.0
        if (isPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            //开启悬浮窗
            Intent intent = new Intent(context, FloatService.class);
            context.startService(intent);
        }
    }

    /**
     * 关闭悬浮窗
     */
    public void stopMonkServer(Context context) {
        Intent intent = new Intent(context, FloatService.class);
        context.stopService(intent);
    }

    /**
     * 注册监听
     */
    public void registerCallLittleMonk(FloatCallBack callLittleMonk) {
        floatCallBack = callLittleMonk;
    }

    /**
     * 悬浮窗的显示
     */
    public void show() {
        if (floatCallBack == null) {
            return;
        }
        floatCallBack.show();
    }

    /**
     * 悬浮窗的隐藏
     */
    public void hide() {
        if (floatCallBack == null) {
            return;
        }
        floatCallBack.hide();
    }
}
