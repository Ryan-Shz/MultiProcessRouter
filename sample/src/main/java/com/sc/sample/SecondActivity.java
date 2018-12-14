package com.sc.sample;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sc.framework.router.RouteRequest;
import com.sc.framework.router.Router;
import com.sc.framework.router.RouteResponse;
import com.sc.framework.router.MemoryCacheStrategy;
import com.sc.framework.router.ProcessUtils;

/**
 * @author shamschu
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
//                RouteRequest request = new RouteRequest.Builder()
//                        .process(ProcessUtils.getMainProcess(v.getContext()))
//                        .provider("MainProcessProvider")
//                        .action("MainProcessActionOne")
//                        .cacheStrategy(MemoryCacheStrategy.FIXED)
//                        .build();
//                RouteResponse<String> response = Router.route(v.getContext(), request);
//                if (response.isSuccess()) {
//                    Toast.makeText(SecondActivity.this, response.getResult(), Toast.LENGTH_LONG).show();
//                } else {
//                    Toast.makeText(SecondActivity.this, response.getError(), Toast.LENGTH_LONG).show();
//                }
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
