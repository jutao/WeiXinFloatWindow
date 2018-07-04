package jt.com.weixinfloatwindow.floatview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import jt.com.weixinfloatwindow.R;

/**
 * @author：琚涛
 * @date：2018/06/26
 * @description：
 */
public class BottomFloatView extends FrameLayout {

    private boolean isScale = false;
    private Paint paint;
    /**
     * 普通半径
     */
    private int normalRadius;
    /**
     * 放大后的半径
     */
    private int scaleRadius;

    public BottomFloatView(Context context) {
        super(context);
        init();
    }

    public BottomFloatView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BottomFloatView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#ff5050"));
        paint.setAntiAlias(true);
        normalRadius = 480;
        scaleRadius = 600;
        TextView textView = new TextView(getContext());
        textView.setText("取消悬浮");
        textView.setTextColor(Color.parseColor("#ffffff"));
        int dis144 = 72;
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.mipmap.cancel_suspension_white);
        drawable.setBounds(0, 0, dis144, dis144);
        textView.setCompoundDrawables(null, drawable, null, null);
        textView.setCompoundDrawablePadding(7);
        textView.setTextSize(12);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.BOTTOM | Gravity.RIGHT;
        params.rightMargin = 110;
        params.bottomMargin = 140;
        textView.setLayoutParams(params);
        addView(textView);
        setWillNotDraw(false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(scaleRadius, scaleRadius, isScale ? scaleRadius : normalRadius, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newWidthMeasureSpec = MeasureSpec.makeMeasureSpec(scaleRadius, MeasureSpec.EXACTLY);
        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(scaleRadius, MeasureSpec.EXACTLY);
        super.onMeasure(newWidthMeasureSpec, newHeightMeasureSpec);
    }

    public int getRadius() {
        return normalRadius;
    }

    public void changeBottomView() {
        isScale = !isScale;
        invalidate();
    }
}
