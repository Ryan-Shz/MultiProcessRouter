package com.sc.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.sc.framework.router.Router;
import com.sc.framework.router.RouterRequest;
import com.sc.framework.router.RouterResponse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.router_test_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RouterRequest request = new RouterRequest.Builder()
                    .process(getPackageName() + ":second")
                    .provider("TestProviderThree")
                    .action("TestActionThree")
                    .build();
                RouterResponse<TestResult> response = Router.route(MainActivity.this, request);
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
