package jt.com.weixinfloatwindow.floatview.permission.rom;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * @author：琚涛
 * @date：2018/06/28
 * @description：小米悬浮窗权限控制器
 */
public class MIUIFloatController{
    private static final int MIUI_5=5;
    private static final int MIUI_6=6;
    private static final int MIUI_7=7;
    private static final int MIUI_8=8;
    /**
     * 获取小米 rom 版本号，获取失败返回 -1
     *
     * @return miui rom version code, if fail , return -1
     */
    public static int getMiUiVersion() {
        String version = RomUtils.getSystemProperty("ro.miui.ui.version.name");
        if (version != null) {
            try {
                return Integer.parseInt(version.substring(1));
            } catch (Exception e) {
            }
        }
        return -1;
    }

    /**
     * 小米 ROM 权限申请
     */
    public static void applyMiuiPermission(Context context) {
        int versionCode = getMiUiVersion();
        if (versionCode == MIUI_5) {
            goToMIUIPermissionActivityV5(context);
        } else if (versionCode == MIUI_6) {
            goToMIUIPermissionActivityV6(context);
        } else if (versionCode == MIUI_7) {
            goToMIUIPermissionActivityV7(context);
        } else if (versionCode == MIUI_8) {
            goToMIUIPermissionActivityV8(context);
        } else {
        }
    }



    /**
     * 小米 V5 版本 ROM权限申请
     */
    public static void goToMIUIPermissionActivityV5(Context context) {
        String packageName = context.getPackageName();
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", packageName, null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (CommonFloatController.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
        }
    }

    /**
     * 小米 V6 版本 ROM权限申请
     */
    public static void goToMIUIPermissionActivityV6(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.AppPermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (CommonFloatController.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
        }
    }

    /**
     * 小米 V7 版本 ROM权限申请
     */
    public static void goToMIUIPermissionActivityV7(Context context) {
        goToMIUIPermissionActivityV6(context);
    }

    /**
     * 小米 V8 版本 ROM权限申请
     */
    public static void goToMIUIPermissionActivityV8(Context context) {
        Intent intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
        intent.setClassName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity");
        intent.putExtra("extra_pkgname", context.getPackageName());
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (CommonFloatController.isIntentAvailable(intent, context)) {
            context.startActivity(intent);
        } else {
            intent = new Intent("miui.intent.action.APP_PERM_EDITOR");
            intent.setPackage("com.miui.securitycenter");
            intent.putExtra("extra_pkgname", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (CommonFloatController.isIntentAvailable(intent, context)) {
                context.startActivity(intent);
            } else {
            }
        }
    }
}
