package jt.com.weixinfloatwindow.floatview.permission.rom;

import android.content.Context;
import android.content.Intent;

/**
 * @author：琚涛
 * @date：2018/06/28
 * @description：360悬浮窗权限管理器
 */
public class QiKuFloatController {

    /**
     * 去360权限申请页面
     */
    public static void applyPermission(Context context) {
        Intent intent = new Intent();
        intent.setClassName("com.android.settings", "com.android.settings.Settings$OverlaySettingsActivity");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (CommonFloatController.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent.setClassName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity");
            if (CommonFloatController.isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
            }
        }
    }
}
