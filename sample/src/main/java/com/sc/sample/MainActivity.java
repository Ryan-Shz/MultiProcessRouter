package com.sc.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sc.framework.router.Router;
import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;
import com.sc.framework.router.cache.CacheStrategy;
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
                    .provider("MainProcessProvider")
                    .action("MainProcessActionOne")
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
                    .cacheStrategy(CacheStrategy.NONE)
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
                    .cacheStrategy(CacheStrategy.FIXED)
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

    }
}
