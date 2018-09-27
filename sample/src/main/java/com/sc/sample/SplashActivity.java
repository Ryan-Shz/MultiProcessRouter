package com.sc.sample;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.sc.framework.router.InitializeCompleteReceiver;
import com.sc.framework.router.Router;

/**
 * @author ShamsChu
 * @Date 17/8/30 下午6:03
 */
public class SplashActivity extends AppCompatActivity {

    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // enter main activity if router service has been initialized
        if (Router.checkRouterServiceInitCompleted(this)) {
            startMainActivity();
            return;
        }
        // register InitializeCompleteReceiver to receive router initialized broadcast.
        mReceiver = InitializeCompleteReceiver.register(this, new InitializeCompleteReceiver.Callback() {
            @Override
            public void onRouterServiceInitCompleted() {
                startMainActivity();
            }
        });
    }

    /**
     * Start your main activity
     */
    private void startMainActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }
}
