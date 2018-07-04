package jt.com.weixinfloatwindow.floatview.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

import jt.com.weixinfloatwindow.floatview.view.FloatLayout;

/**
 * @author：琚涛
 * @date：2018/06/28
 * @description：动态悬浮窗管理器
 */
public class DynamicFloatWindowManager extends BaseFloatWindowManager {

    /**
     * 悬浮窗
     */
    private FloatLayout floatLayout;
    private BottomFloatWindowManager bottomFloatWindowManager;
    public DynamicFloatWindowManager(BottomFloatWindowManager manager) {
        this.bottomFloatWindowManager=manager;
    }

    /**
     * 创建一个小悬浮窗。初始位置为屏幕的右部中间位置。
     *
     * @param context 必须为应用程序的Context.
     */
    @Override
    public void createFloatWindow(Context context) {
        wmParams = new WindowManager.LayoutParams();
        WindowManager windowManager = getWindowManager(context);

        wmParams.type = getWMType(context);

        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        //调整悬浮窗显示的停靠位置为左侧置顶
        wmParams.gravity = Gravity.START | Gravity.TOP;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        windowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = screenWidth/5*4;
        wmParams.y = screenHeight / 8;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        floatLayout = (FloatLayout) createFloatView(context);
        floatLayout.setParams(wmParams);
        floatLayout.setBottomWindowManager(bottomFloatWindowManager);
        floatLayout.setDynamicWindowManager(this);
        windowManager.addView(floatLayout, wmParams);
        hasShown = true;
    }

     @Override
     public View createFloatView(Context context) {
         return new FloatLayout(context);
    }

    @Override
    public View getFloatView() {
        return floatLayout;
    }
}
