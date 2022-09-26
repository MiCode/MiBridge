<div align="center">
  <p>
    <h1>
      <a href="https://github.com/MiCode/MiBridge">
        <img src="data/xiaomi_circle.svg" alt="MiBridge" />
      </a>
      <br />
      MiSpeed
    </h1>
    <h4>Powerful yet simple to use .</h4>
  </p>
  <p>
    <a >
      <img src="data/MiSpeed-1.0.0.svg" alt="Build Status" />
    </a>
    <a >
      <img src="data/docs-1.0.0-blue.svg" alt="Doc Status" />
    </a>
      <img src="data/jar-available-blueviolet.svg" alt="Jar Status" />
    </a>
    <a >
      <img src="data/license-Apache2.0-brightgreen.svg" alt="license" />
    </a>
    <br>
  </p>
</div>


# 小米应用加速器
## 其他语言
[English](./README.md)


## Index

- [我们的目的](#我们的目的)
- [简介](#简介)
- [我们的优势](#我们的优势)
- [接入说明](#接入说明)
  - [申请调试权限](#申请调试权限)
  - [接口定义](#接口定义)
  - [申请正式权限](#申请正式权限)
  - [更多合作](#更多合作)




## 我们的目的
一切为了用户，让用户得到最好的体验！
## 简介
小米应用加速器向开发者提供申请系统资源的接口，可以为开发者提升应用关键场景下的性能，提升用户使用体验。
## 我们的优势
* __*轻量级接入*__<br>
  通过JAR包接入，大小只有几kb, 不会对开发者的应用造成负担
* __*快速系统通信*__<br>
  通过Binder直接与系统层进行通信，申请系统资源，快速高效
* __*场景化定制*__<br>
  为开发者在不同的场景下提供不同级别的系统资源
## 接入说明<br>
*MiBridge.jar*<br>
&emsp;您可以引入此jar包调用小米应用加速器相关接口。<br>
*TestMiBridge*<br>
&emsp;此目录包含： <br>
&emsp;&emsp;*mibridge*: MiBridge jar包的实现代码。<br>
&emsp;&emsp;*app*: 测试MiBridge的app代码。<br>
#### 申请调试权限
请提供以下信息，发送到邮箱xiaomi-mispeed-support@xiaomi.com, 申请接入调试权限。<br>
#### 邮件主题：xx（APP名称）申请小米应用加速器接入调试权限<br>

应用包名 | 申请人 |
---- | ----- |
com.mi.testmibridge | 张三 |

我们将会为您开通调试权限．

[__支持设备版本列表__](./support_devices.md)

#### 接口定义
__权限检查接口__<br>
1. ```boolean checkDebugPermission(Context context, String pkg, int uid, String auth_key)```<br>
      __介绍：通过申请获得的鉴权码，检查应用是否有调试权限。__<br>

      参数：<br>
      *context* : 应用上下文<br>
      *pkg* : 应用包名<br>
      *uid* : android.os.Process.myUid()<br>
      *auth_key* : 上一步中申请获得的鉴权码<br>

      返回结果：<br>
      true: 权限检查通过<br>
      false: 权限检查失败，无接口使用权限<br>

2. ```boolean checkPermission(String pkg, int uid)```<br>
      __介绍：检查应用是否有权限__<br>

      参数：<br>
      *pkg* : 应用包名<br>
      *uid* : android.os.Process.myUid()<br>

      返回结果：<br>
      true: 权限检查通过<br>
      false: 权限检查失败，无接口使用权限<br>

__系统资源申请接口__<br>

3. ```int requestCpuHighFreq(int uid, int level, int timeoutms)```<br>
      __介绍：向系统申请cpu频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *level* : 需要的cpu频率level，系统将会根据不同机型设置不同的cpu频率<br>
      *timeoutms* : 申请cpu资源的持续时间<br>

      Level级别 | 解释（以小米8为例）| timeoutms
      ---- | -----|-----
      1 | Level 1 将会设置系统当前最小频率为系统最高频率 |  最长10s
      2 | Level 2 将会设置系统当前最小频率为系统较高频率<br>例：小米8对应大核最低2.2GHz, 小核最低1.5GHz |  最长5s
      3 | Level 3 将会设置系统当前最小频率为系统正常频率以上<br>例：小米8对应大核最低1.9GHz, 小核最低1.0GHz |  最长1.5s

      __注意：请谨慎使用Level 1级别。Level 1级别使用场景数量限制为5，__<br>
      __Level 2级别使用场景数量限制为20，Level 3 不做限制。__<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

4. ```int cancelCpuHighFreq(int uid)```<br>
      __介绍：取消申请cpu频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

5. ```int requestThreadPriority(int uid, int req_tid, int timeoutms)```<br>
      __介绍：申请线程获得高优先级，将会优先得到调度运行__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *req_tid* : 申请的线程id<br>
      *timeoutms* : 申请的时长<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

6. ```int cancelThreadPriority (int uid, int req_tid)```<br>
      __介绍：取消申请线程优先级的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *req_tid* : 取消的线程id<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

7. ```int requestGpuHighFreq(int uid, int level, int timeoutms)```<br>
      __介绍：向系统申请gpu频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *level* : 需要的gpu频率level，系统将会根据不同机型设置不同的gpu频率<br>
      *timeoutms* : 申请gpu资源的持续时间<br>

      Level级别 | 解释 | timeoutms
      ---- | -----|-----
      1 | Level 1 将会设置系统当前最小频率为系统最高频率 |  最长10s
      2 | Level 2 将会设置系统当前最小频率为系统较高频率 |  最长5s
      3 | Level 3 将会设置系统当前最小频率为系统正常频率以上 |  最长1.5s

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

8. ```int cancelGpuHighFreq(int uid)```<br>
      __介绍：取消申请gpu频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>

      返回结果：<br>
       0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

9. ```int requestDdrHighFreq(int uid, int level, int timeoutms)```<br>
      __介绍：向系统申请ddr频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *level* : 需要的ddr频率level，系统将会根据不同机型设置不同的ddr频率<br>
      *timeoutms* : 申请ddr资源的持续时间<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

10. ```int cancelDdrHighFreq(int uid)```<br>
      __介绍：取消申请ddr频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>

      返回结果：<br>
       0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

11. ```int requestIOPrefetch(int uid, String filePath)```<br>
      __介绍：申请IO预读取接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *filePath* : 预读取的文件路径<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

12. ```int requestBindCore(int uid, int req_tid)```<br>
      __介绍：申请线程优先大核运行接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *req_tid* : 申请的线程id<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>
__系统状态接口__<br>

13. ```int getSystemState(int uid, Context context, int type)```<br>
      __介绍：如开发者所知，在设备温度升高的时候，系统常常会为了降低温度/减缓温度升高的速度，保护硬件进行限频、限帧等限制。同时，系统续航模式切换到省电模式或超级省电的时候，系统也会为了延长续航进行限频、限帧等限制。限制后可能会导致应用出现卡顿等状况让设备使用体验不佳，因此当设备温控限频即将发生变化时，系统可调用预定义的回调函数通知三方应用，同时系统也为三方应用提供了温控level的查询接口及当前续航模式的查询接口，在温控level变化时应用可收到警报，或者应用要进行复杂操作时，可查询下用户当前的温控level及续航模式，如果温控限频严重或续航模式为省电模式/超级省电，建议应用可在保障基本操作流畅的前提下，动态调整代码执行负载和代码复杂度，配合手机系统协同优化，提高整机使用体验。__<br>
      参数：<br>
      *uid* : 调用者UID <br>
      *context* : 应用上下文 <br>
      *type* : 查询的系统状态类型，目前有两类: <br>
       - 1 : 设备壳温 <br>
       - 2 : 系统省电模式 <br>

      返回结果 : <br>
      - 当查询设备壳温时(type = 1)，返回对应温度的温控级别
        - 0: 正常范围
        - 1: 轻微限频
        - 2: 较严重限频
        - 3: 非常严重限频
        - -2 : Permission not granted!
      - 当查询设备省电模式(type = 2)，返回当前省电模式
        - 0：性能模式
        - 1：均衡模式
        - 2：省电模式
        - 3：超级省电
        - -2 : Permission not granted!

14. ```int registerThermalEventCallback(int uid, ThermalEventCallBack cb)```<br>
      __介绍：注册回调，当系统温控级别发生变化，触发回调接口onThermalLevelChanged__<br>

    ```java
    ThermalEventCallBack  cb = new ThermalEventCallBack() {
        @Override
        public void onThermalLevelChanged(int level) {
            //do somthing;
        }
    };
    ```
      参数 : <br>
      *uid* : 调用者UID<br>
      *cb* : 回调实例，须重写onThermalLevelChanged()方法 <br>
      返回结果 : <br>
      - 0 : 注册成功
      - -1 : 注册失败
      - -2 : Permission not granted!

15. ```int unRegisterThermalEventCallback(int uid, ThermalEventCallBack cb)``` <br>
      __介绍：注销回调__ <br>
      参数： <br>
      *uid* : 调用者UID <br>
      *cb* : 回调实例 <br>
      返回结果 :  <br>
      - 0 : 注销成功 <br>
      - -1 : 注销失败 <br>
      - -2 : Permission not granted! <br>


#### 申请正式权限
请提供以下信息，发送到邮箱xiaomi-mispeed-support@xiaomi.com, 申请正式权限。<br>
邮件主题：xx（APP名称）申请小米应用加速器正式权限<br>
__1. 资源接口使用场景<br>

场景 | cpu level | thread priority | timeout (ms)
---- | ----- | ------ | -------
打开xxx页面 | 1 | 0 | 100
Xxx滑动 | 2 | 0 | 1000
场景3 | 3 | 1 | 500
场景4 | 1 | 0 | 100
场景5 | 0 | 1 |2000

__2. 性能测试<br>

场景 | 具体测试内容 | 优化前 | 优化后 | 提升
---- | ----- | ------ | ------- | --------
进入某页面 | 进入页面耗时 | 100 ms | 80 ms | 20%
某页面滑动 | 滑动丢帧数(率) | 10% | 6% | 40%
场景3 | 测试内容3 | .. | .. |..

备注：我们也会根据您提供的场景进行性能测试。<br>

__3. 功耗测试<br>

场景 | Base | 接入后 | Diff
---- | ----- | ------ | -------
场景1 | 1000 mA | 1050 mA | 50
场景2 | .. | .. | ..

备注：我们也会根据您提供的场景进行功耗测试。<br>


#### 更多合作
后续将会开放更多接口<br>
requestIOHighFreq，requestMemory，requestNetwork等

后续可以提供的支持<br>
为您的应用提供更多的支持（例如，卡顿打点，内存泄漏打点），提升应用的性能，提升用户的使用满意度。
