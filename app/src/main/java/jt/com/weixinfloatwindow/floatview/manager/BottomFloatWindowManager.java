package jt.com.weixinfloatwindow.floatview.manager;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

import jt.com.weixinfloatwindow.R;
import jt.com.weixinfloatwindow.floatview.view.BottomFloatView;

/**
 * @author：琚涛
 * @date：2018/06/29
 * @description：
 */
public class BottomFloatWindowManager extends BaseFloatWindowManager{
    /**
     * 底部悬浮窗
     */
    private BottomFloatView bottomView;

    @Override
    public void createFloatWindow(Context context) {
        wmParams = new WindowManager.LayoutParams();
        WindowManager windowManager = getWindowManager(context);
        wmParams.type = getWMType(context);
        //设置图片格式，效果为背景透明
        wmParams.format = PixelFormat.RGBA_8888;
        //不需要触摸的窗口
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        DisplayMetrics dm = new DisplayMetrics();
        //取得窗口属性
        windowManager.getDefaultDisplay().getMetrics(dm);
        //窗口的宽度
        int screenWidth = dm.widthPixels;
        //窗口高度
        int screenHeight = dm.heightPixels;
        //以屏幕左上角为原点，设置x、y初始值，相对于gravity
        wmParams.x = screenWidth;
        wmParams.y = screenHeight;

        //设置悬浮窗口长宽数据
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.windowAnimations= R.style.notice_dialog_anim_bottom2top;
        bottomView= (BottomFloatView) createFloatView(context);
        bottomView.setVisibility(View.GONE);
        windowManager.addView(bottomView,wmParams);
    }

    @Override
    public View createFloatView(Context context) {
        return new BottomFloatView(context);
    }


    @Override
    public View getFloatView() {
        return bottomView;
    }

    @Override
    public void show() {
        if(getFloatView().getVisibility()!= View.VISIBLE){
            getFloatView().setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hide() {
        getFloatView().setVisibility(View.GONE);
    }

    /**
     * 获取底部半圆的半径
     * @return
     */
    public int getBottomRadius() {
        return bottomView.getRadius();
    }
    /**
     * 切换大小
     * @return
     */
    public void changeBottomView() {
         bottomView.changeBottomView();
    }
}
