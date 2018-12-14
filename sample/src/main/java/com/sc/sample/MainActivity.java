package com.sc.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sc.framework.router.RouteRequest;
import com.sc.framework.router.RouteResponse;
import com.sc.framework.router.Router;
import com.sc.framework.router.MemoryCacheStrategy;
import com.sc.framework.router.ProcessUtils;
import com.sc.sample.bean.TestResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.local_router_request_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteRequest request = new RouteRequest.Builder()
                        .process(ProcessUtils.getMainProcess(v.getContext()))
                        .provider("MainProcessProvider")
                        .action("MainProcessActionOne")
                        .cacheStrategy(MemoryCacheStrategy.FIXED)
                        .build();
                RouteResponse<String> response = Router.route(MainActivity.this, request);
                if (response == null) {
                    return;
                }
                if (response.isSuccess()) {
                    Toast.makeText(MainActivity.this, response.getResult(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.wide_router_without_parameter_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        RouteRequest request = new RouteRequest.Builder()
                                .process(getPackageName() + ":second")
                                .provider("SecondProcessProvider")
                                .action("SecondProcessAction")
                                .cacheStrategy(MemoryCacheStrategy.NONE)
                                .build();
                        final RouteResponse<TestResult> response = Router.route(MainActivity.this, request);
                        if (response == null) {
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccess()) {
                                    Toast.makeText(MainActivity.this, response.getResult() == null ? "" : response.getResult().getRtnMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }.start();
            }
        });

        findViewById(R.id.wide_router_with_parameter_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouteRequest<String> request = new RouteRequest.Builder<String>()
                        .parameter("this is a test parameter")
                        .process(getPackageName() + ":second")
                        .provider("SecondProcessProvider")
                        .action("SecondProcessAction")
                        .cacheStrategy(MemoryCacheStrategy.FIXED)
                        .build();
                RouteResponse<TestResult> response = Router.route(MainActivity.this, request);
                if (response == null) {
                    return;
                }
                if (response.isSuccess()) {
                    Toast.makeText(MainActivity.this, response.getResult().getRtnMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_LONG).show();
                }
            }
        });

        findViewById(R.id.open_second_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SecondActivity.class));
            }
        });

    }
}
