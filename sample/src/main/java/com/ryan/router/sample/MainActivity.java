package com.ryan.router.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.ryan.router.RouteRequest;
import com.ryan.router.RouteResponse;
import com.ryan.router.Router;
import com.ryan.router.MemoryCacheStrategy;
import com.ryan.router.ProcessUtils;
import com.ryan.router.sample.bean.TestResult;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.local_router_request_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                runOnNewThread(new Runnable() {
                    @Override
                    public void run() {
                        RouteRequest request = new RouteRequest.Builder()
                            .process(ProcessUtils.getMainProcess(v.getContext()))
                            .provider("MainProcessProvider")
                            .action("MainProcessActionOne")
                            .cacheStrategy(MemoryCacheStrategy.FIXED)
                            .build();
                        final RouteResponse<String> response = Router.route(MainActivity.this, request);
                        if (response == null) {
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccess()) {
                                    Toast.makeText(MainActivity.this, response.getResult(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        findViewById(R.id.wide_router_without_parameter_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnNewThread(new Runnable() {
                    @Override
                    public void run() {
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
                });
            }
        });

        findViewById(R.id.wide_router_with_parameter_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnNewThread(new Runnable() {
                    @Override
                    public void run() {
                        RouteRequest<String> request = new RouteRequest.Builder<String>()
                            .parameter("this is a test parameter")
                            .process(getPackageName() + ":second")
                            .provider("SecondProcessProvider")
                            .action("SecondProcessAction")
                            .cacheStrategy(MemoryCacheStrategy.FIXED)
                            .build();
                        final RouteResponse<TestResult> response = Router.route(MainActivity.this, request);
                        if (response == null) {
                            return;
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (response.isSuccess()) {
                                    Toast.makeText(MainActivity.this, response.getResult().getRtnMessage(), Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(MainActivity.this, response.getError(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                });
            }
        });

        findViewById(R.id.open_second_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), SecondActivity.class));
            }
        });

    }


    private void runOnNewThread(Runnable runnable) {
        new Thread(runnable).start();
    }

}
