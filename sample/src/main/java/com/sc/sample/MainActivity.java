package com.sc.sample;

import android.content.Intent;
import android.os.Bundle;
import android.os.Process;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sc.framework.router.Router;
import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;
import com.sc.framework.router.cache.MemoryCacheStrategy;
import com.sc.framework.router.cache.DefaultRouterCache;
import com.sc.framework.router.utils.ProcessUtils;
import com.sc.sample.bean.TestResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.local_router_request_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterRequest request = new RouterRequest.Builder()
                    .process(ProcessUtils.getMainProcess(v.getContext()))
                    .provider("MainProcessProvider")
                    .action("MainProcessActionOne")
                    .cacheStrategy(MemoryCacheStrategy.FIXED)
                    .build();
                RouterResponse<String> response = Router.route(MainActivity.this, request);
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
                RouterRequest request = new RouterRequest.Builder()
                    .process(getPackageName() + ":second")
                    .provider("SecondProcessProvider")
                    .action("SecondProcessAction")
                    .cacheStrategy(MemoryCacheStrategy.NONE)
                    .build();
                RouterResponse<TestResult> response = Router.route(MainActivity.this, request);
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

        findViewById(R.id.wide_router_with_parameter_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterRequest<String> request = new RouterRequest.Builder<String>()
                    .parameter("this is a test parameter")
                    .process(getPackageName() + ":second")
                    .provider("SecondProcessProvider")
                    .action("SecondProcessAction")
                    .cacheStrategy(MemoryCacheStrategy.FIXED)
                    .build();
                RouterResponse<TestResult> response = Router.route(MainActivity.this, request);
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

        findViewById(R.id.remove_cache_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DefaultRouterCache cache = (DefaultRouterCache) Router.getRouterCache();
                RouterRequest key = cache.generateCacheKey(ProcessUtils.getMainProcess(v.getContext()), "MainProcessProvider", "MainProcessActionOne");
                cache.remove(key);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Process.killProcess(Process.myPid());
        System.exit(0);
    }
}
