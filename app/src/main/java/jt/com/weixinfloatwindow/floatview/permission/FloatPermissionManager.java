package jt.com.weixinfloatwindow.floatview.permission;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import java.io.ObjectStreamException;
import java.lang.reflect.Method;

import jt.com.weixinfloatwindow.floatview.permission.rom.CommonFloatController;
import jt.com.weixinfloatwindow.floatview.permission.rom.HuaWeiFloatController;
import jt.com.weixinfloatwindow.floatview.permission.rom.MIUIFloatController;
import jt.com.weixinfloatwindow.floatview.permission.rom.MeiZuFloatController;
import jt.com.weixinfloatwindow.floatview.permission.rom.QiKuFloatController;
import jt.com.weixinfloatwindow.floatview.permission.rom.RomUtils;

import static android.content.ContentValues.TAG;

/**
 * @author：琚涛
 * @date：2018/06/28
 * @description：
 */
public class FloatPermissionManager {

    private FloatPermissionManager() {
    }

    public static FloatPermissionManager getInstance() {
        return FloatPermissionManagerHolder.sInstance;
    }

    /**
     * 静态内部类
     */
    private static class FloatPermissionManagerHolder {

        private static final FloatPermissionManager sInstance = new FloatPermissionManager();
    }

    /**
     * 为了杜绝对象在反序列化时重新生成对象，则重写Serializable的私有方法
     *
     * @return
     * @throws ObjectStreamException
     */
    private Object readResolve() throws ObjectStreamException {
        return FloatPermissionManagerHolder.sInstance;
    }

    private Dialog dialog;

    /**
     * 申请悬浮窗权限
     *
     * @param context
     * @return
     */
    public boolean applyFloatWindow(Context context) {
        if (checkPermission(context)) {
            return true;
        } else {
            applyPermission(context);
            return false;
        }
    }

    /**
     * 判断悬浮窗权限
     *
     * @param context
     * @return
     */
    private boolean checkPermission(Context context) {
        //6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (RomUtils.checkIsMiuiRom()) {
                return miUiPermissionCheck(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                return meiZuPermissionCheck(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                return huaWeiPermissionCheck(context);
            } else if (RomUtils.checkIs360Rom()) {
                return qiKuPermissionCheck(context);
            }
        }
        return commonROMPermissionCheck(context);
    }

    /**
     * 华为悬浮窗权限校验
     *
     * @param context
     * @return
     */
    private boolean huaWeiPermissionCheck(Context context) {
        return CommonFloatController.checkFloatWindowPermission(context);
    }

    /**
     * 小米悬浮窗权限校验
     *
     * @param context
     * @return
     */
    private boolean miUiPermissionCheck(Context context) {
        return CommonFloatController.checkFloatWindowPermission(context);
    }

    /**
     * 魅族悬浮窗权限校验
     *
     * @param context
     * @return
     */
    private boolean meiZuPermissionCheck(Context context) {
        return CommonFloatController.checkFloatWindowPermission(context);
    }

    /**
     * 360悬浮窗权限校验
     *
     * @param context
     * @return
     */
    private boolean qiKuPermissionCheck(Context context) {
        return CommonFloatController.checkFloatWindowPermission(context);
    }

    /**
     * 其他系统的权限校验
     *
     * @param context
     * @return
     */
    private boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return meiZuPermissionCheck(context);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    Log.e(TAG, Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    /**
     * 申请权限
     *
     * @param context
     */
    private void applyPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (RomUtils.checkIsMiuiRom()) {
                miUiROMPermissionApply(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                meiZuROMPermissionApply(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                huaWeiROMPermissionApply(context);
            } else if (RomUtils.checkIs360Rom()) {
                ROM360PermissionApply(context);
            }
        }
        commonROMPermissionApply(context);
    }

    /**
     * 360权限申请
     * @param context
     */
    private void ROM360PermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    QiKuFloatController.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:360, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 华为权限申请
     * @param context
     */
    private void huaWeiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    HuaWeiFloatController.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:huawei, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 魅族权限申请
     * @param context
     */
    private void meiZuROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MeiZuFloatController.applyPermission(context);
                } else {
                    Log.e(TAG, "ROM:meizu, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }

    /**
     * 小米权限申请
     * @param context
     */
    private void miUiROMPermissionApply(final Context context) {
        showConfirmDialog(context, new OnConfirmResult() {
            @Override
            public void confirmResult(boolean confirm) {
                if (confirm) {
                    MIUIFloatController.applyMiuiPermission(context);
                } else {
                    Log.e(TAG, "ROM:miui, user manually refuse OVERLAY_PERMISSION");
                }
            }
        });
    }
    /**
     * 通用 rom 权限申请
     */
    private void commonROMPermissionApply(final Context context) {
        //这里也一样，魅族系统需要单独适配
        if (RomUtils.checkIsMeizuRom()) {
            meiZuROMPermissionApply(context);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                showConfirmDialog(context, new OnConfirmResult() {
                    @Override
                    public void confirmResult(boolean confirm) {
                        if (confirm) {
                            CommonFloatController.applyPermission(context);
                        } else {
                            Log.d(TAG, "user manually refuse OVERLAY_PERMISSION");
                        }
                    }
                });
            }
        }
    }
    /**
     * Dialog回调
     */
    private interface OnConfirmResult {

        /**
         *  结果回调
         * @param confirm
         */
        void confirmResult(boolean confirm);
    }

    private void showConfirmDialog(Context context, OnConfirmResult result) {
        showConfirmDialog(context, "您的手机没有授予悬浮窗权限，请开启后再试", result);
    }

    private void showConfirmDialog(Context context, String message, final OnConfirmResult result) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        dialog = new AlertDialog.Builder(context).setCancelable(true)
                                                 .setTitle("")
                                                 .setMessage(message)
                                                 .setPositiveButton("现在去开启", new DialogInterface.OnClickListener() {
                                                     @Override
                                                     public void onClick(DialogInterface dialog, int which) {
                                                         result.confirmResult(true);
                                                         dialog.dismiss();
                                                     }
                                                 })
                                                 .setNegativeButton("暂不开启", new DialogInterface.OnClickListener() {

                                                     @Override
                                                     public void onClick(DialogInterface dialog, int which) {
                                                         result.confirmResult(false);
                                                         dialog.dismiss();
                                                     }
                                                 })
                                                 .create();
        dialog.show();
    }
}
