package com.ryan.router.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ryan.router.MemoryCacheStrategy;
import com.ryan.router.ProcessUtils;
import com.ryan.router.RouteRequest;
import com.ryan.router.RouteResponse;
import com.ryan.router.Router;

/**
 * @author Ryan
 * @Date 17/8/28 上午10:24
 */
public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        findViewById(R.id.request_main_process_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        RouteRequest request = new RouteRequest.Builder()
                            .process(ProcessUtils.getMainProcess(SecondActivity.this))
                            .provider("MainProcessProvider")
                            .action("MainProcessActionOne")
                            .cacheStrategy(MemoryCacheStrategy.FIXED)
                            .build();
                        final RouteResponse<String> response = Router.route(SecondActivity.this, request);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccess()) {
                                    Toast.makeText(SecondActivity.this, response.getResult(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(SecondActivity.this, response.getError(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }.start();
            }
        });

        findViewById(R.id.exit_second_process_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Router.killCurrProcess();
            }
        });
    }
}
