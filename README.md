# MultiProcessRouter
一个基本AnnotationProcessor实现的多进程路由框架。支持注解注册接口、多进程间数据交互。可快速应用于多进程模块化，所有进程间的请求为同步调用。

## Usage

### Gradle
```
dependencies {
    classpath 'com.android.tools.build:gradle:2.3.1'
    classpath 'com.neenbedankt.gradle.plugins:android-apt:1.8'
}

allprojects {
    repositories {
        jcenter()
        maven {
            url 'https://dl.bintray.com/shamschu/maven'
        }
    }
}

apply plugin: 'com.neenbedankt.android-apt'

compile 'com.sc.framework:router:1.0.0'
compile 'com.sc.framework:annotation:1.0.0'
apt "com.sc.framework:compiler:1.0.0"
```

### 创建每个进程对应的LocalRouterService

LocalRouterService为本地Service(运行在每个单独进程中)，用来进行进程间的AIDL通信，一般来说，你只要继承LocalRouterService，并且在你的AndroidMenifest中注册该Service并使用android:process指定它所在的进程名称即可。

```
public class ProcessService2 extends LocalRouterService {

}

<service
    android:name=".ProcessService2"
    android:process=":second"/>
```

### 初始化路由
在应用启动时调用:

```
Router.register(Context context, IRouterServiceRegister serviceRegister);
```
并实现IRouterServiceRegister的getServices()方法来返回每个进程对应的LocalRouterService映射列表。
例如您的应用有两个进程，一个主进程，一个名称为second的其他进程，那么调用如下所示:

```
Router.register(this, new IRouterServiceRegister() {
    @Override
    public Map<String, Class<? extends LocalRouterService>> getServices() {
        Map<String, Class<? extends LocalRouterService>> services = new HashMap<>();
        services.put(ProcessUtils.getMainProcess(TestApplication.this), ProcessService1.class);
        services.put(ProcessUtils.getMainProcess(TestApplication.this) + ProcessUtils.COLON + "second", ProcessService2.class);
        return services;
    }
});
```

### 接收路由服务初始化完成广播
Router初始化时,动态注册InitializeCompleteReceiver，并实现onRouterServiceInitCompleted方法，它会在Router服务初始化完成后被触发，你可以在你应用的闪屏页Activity的onCreate中注册，并等待通知，通知成功后再进行app的下一步操作，一旦初始化完成，你可以任意的调用其他进程的接口。

```
public class SplashActivity extends AppCompatActivity {

    private InitializeCompleteReceiver receiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        receiver = new InitializeCompleteReceiver() {
            @Override
            protected void onRouterServiceInitCompleted() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(InitializeCompleteReceiver.ACTION_ROUTER_SERVICE_COMPLETED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
```

	
### 注册Provider

#### 使用注解

@Provider(process = PROCESS_NAME)

PROCESS_NAME为Provider的进程名称。

```
@Provider(process = "com.shamschu.framework")
public class TestProvider extends RouterProvider {

    @Override
    public String getName() {
        return "TestProvider";
    }
}
```
### 注册Action
#### 使用注解
@Action(provider = YOUR_PROVIDER.class)

YOUR_PROVIDER.class为Action所属的Provider的类。

```
@Action(provider = TestProvider.class)
public class TestAction extends RouterAction<String> {

    @Override
    public RouterResponse<String> invoke(Context context, RouterRequest request) {
        return new RouterResponse.Builder<String>()
            .result("this is a router test!")
            .build();
    }

}
```

### 创建请求

```
RouterRequest request = new RouterRequest.Builder()
        .process("com.shamschu.framework") // 请求进程
        .provider("TestProvider") // 请求进程提供的接口
        .action("TestAction") // 请求接口提供的动作，
        .cacheStrategy(CacheStrategy.FIXED) // 请求缓存策略 NONE: 不缓存，每次都重新获取 FIXED: 一旦请求后则缓存,不在重新发起请求
        .build();
```

### 请求并获取结果
```
RouterResponse<String> response = Router.route(v.getContext(), request);
if (response.isSuccess()) {
    Toast.makeText(SecondActivity.this, response.getResult(), Toast.LENGTH_LONG).show();
} else {
    Toast.makeText(SecondActivity.this, response.getError(), Toast.LENGTH_LONG).show();
}
```
