package jt.com.weixinfloatwindow.floatview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import jt.com.weixinfloatwindow.MyApplication;
import jt.com.weixinfloatwindow.R;
import jt.com.weixinfloatwindow.SizeUtils;
import jt.com.weixinfloatwindow.floatview.manager.BottomFloatWindowManager;
import jt.com.weixinfloatwindow.floatview.manager.DynamicFloatWindowManager;

/**
 * @author：琚涛
 * @date：2018/06/28
 * @description：浮动窗口
 */
public class FloatLayout extends FrameLayout {

    private ValueAnimator animator;
    private final WindowManager windowManager;
    private final ImageView floatView;
    private long startTime;
    private float touchStartX;
    private float touchStartY;
    private boolean isClick;
    private WindowManager.LayoutParams wmParams;
    private Context context;
    private long endTime;
    private final int screenWidth;
    private boolean isInCancelScope;
    private Vibrator vibrator;
    /**
     * 右下角删除区域的圆心
     */
    private int x0, y0;
    private BottomFloatWindowManager bottomFloatWindowManager;
    /**
     * 最小滑动距离
     */
    private final int TOUCHSLOP = 3;
    /**
     * 最小点击时间
     */
    private final long MIN_CLICK_INTERVAL = (long) (0.1 * 1000L);
    /**
     * 贴边边距
     */
    private final int MARGIN =60;
    private int bottomRadius;
    private DynamicFloatWindowManager dynamicWindowManager;

    public FloatLayout(Context context) {
        this(context, null);
        this.context = context;
    }

    public FloatLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.layout_float_dynamic, this);
        //浮动窗口按钮
        floatView = findViewById(R.id.float_id);
        screenWidth = SizeUtils.getScreenWidth();
        x0 = screenWidth;
        y0 = SizeUtils.getScreenHeight();
        vibrator = (Vibrator) MyApplication.getInstance().getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
        isClick = true;
        //下面的这些事件，跟图标的移动无关，为了区分开拖动和点击事件
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startTime = System.currentTimeMillis();
                touchStartX = event.getX();
                touchStartY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                //图标移动的逻辑在这里
                float moveStartX = event.getX();
                float moveStartY = event.getY();
                // 如果移动量大于3才移动
                if (Math.abs(touchStartX - moveStartX) > TOUCHSLOP || Math.abs(touchStartY - moveStartY) > TOUCHSLOP) {
                    isClick = false;
                    // 更新浮动窗口位置参数
                    wmParams.x = (int) (x - touchStartX);
                    wmParams.y = (int) (y - touchStartY);
                    windowManager.updateViewLayout(this, wmParams);
                    bottomFloatWindowManager.show();
                    boolean isInCircle = isInCircle();
                    //不在取消范围内需要捕捉进入动作，在取消范围内需要捕捉出去动作
                    if (!isInCancelScope&&isInCircle) {
                        isInCancelScope=true;
                        bottomFloatWindowManager.changeBottomView();
                        //震动50ms
                        vibrator.vibrate(50);
                    }else if(isInCancelScope&&!isInCircle){
                        isInCancelScope=false;
                        bottomFloatWindowManager.changeBottomView();
                    }
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(isInCancelScope){
                    //在圆圈范围内直接消失
                    dynamicWindowManager.hide();
                }else {
                    //执行贴边，点击事件等事件
                    endTime = System.currentTimeMillis();
                    startAnimator();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) < MIN_CLICK_INTERVAL && isClick) {
                        Toast.makeText(context, "呵呵呵", Toast.LENGTH_SHORT).show();
                    }
                }
                bottomFloatWindowManager.hide();
                break;
            default:
                break;
        }

        return true;
    }

    /**
     * 将小悬浮窗的参数传入，用于更新小悬浮窗的位置。
     *
     * @param params 小悬浮窗的参数
     */
    public void setParams(WindowManager.LayoutParams params) {
        wmParams = params;
    }

    TimeInterpolator interpolator;

    /**
     * 贴边动画
     */
    private void startAnimator() {
        if (wmParams.x >= (screenWidth / 2)) {
            animator = ObjectAnimator.ofInt(wmParams.x, screenWidth - MARGIN - floatView.getWidth());
        } else {
            animator = ObjectAnimator.ofInt(wmParams.x, MARGIN);
        }
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                wmParams.x = (int) animation.getAnimatedValue();
                windowManager.updateViewLayout(FloatLayout.this, wmParams);
            }
        });
        if (interpolator == null) {
            interpolator = new DecelerateInterpolator();
        }
        animator.setInterpolator(interpolator);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (animator == null) {
                    return;
                }
                animator.removeAllUpdateListeners();
                animator.removeAllListeners();
                animator = null;

            }
        });
        animator.setDuration(500).start();
    }

    /**
     * 是否滑动到了右下角的圆内
     *
     * @return 在圆内返回true
     */
    private boolean isInCircle() {
        int x=wmParams.x+floatView.getWidth()/2;
        int y=wmParams.y+floatView.getHeight()/2;
        return (x - x0) * (x - x0) + (y - y0) * (y - y0) < bottomRadius *
                bottomRadius;
    }

    public void setBottomWindowManager(BottomFloatWindowManager bottomWindowManager) {
        this.bottomFloatWindowManager = bottomWindowManager;
        bottomRadius = bottomFloatWindowManager.getBottomRadius();
    }

    public void setDynamicWindowManager(DynamicFloatWindowManager dynamicWindowManager) {
        this.dynamicWindowManager = dynamicWindowManager;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        vibrator.cancel();
    }
}
