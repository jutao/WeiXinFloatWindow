package jt.com.weixinfloatwindow;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import jt.com.weixinfloatwindow.floatview.FloatActionController;
import jt.com.weixinfloatwindow.floatview.permission.FloatPermissionManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
        findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isPermission = FloatPermissionManager.getInstance().applyFloatWindow(MainActivity.this);
                //有对应权限或者系统版本小于7.0
                if (isPermission || Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    //开启悬浮窗
                    FloatActionController.getInstance().startFloatServer(MainActivity.this);
                }
            }
        });
    }
}
