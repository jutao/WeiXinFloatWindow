package jt.com.weixinfloatwindow.floatview.permission.rom;

import android.content.Context;
import android.content.Intent;

/**
 * @author：琚涛
 * @date：2018/06/28
 * @description：魅族悬浮窗权限控制器
 */
public class MeiZuFloatController{
    /**
     * 去魅族权限申请页面
     */
    public static void applyPermission(Context context){
        Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
        intent.setClassName("com.meizu.safe", "com.meizu.safe.security.AppSecActivity");
        intent.putExtra("packageName", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
