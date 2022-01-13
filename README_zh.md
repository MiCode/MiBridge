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

1. ```boolean checkPermission(String pkg, int uid)```<br>
      __介绍：检查应用是否有权限__<br>

      参数：<br>
      *pkg* : 应用包名<br>
      *uid* : android.os.Process.myUid()<br>

      返回结果：<br>
      true: 权限检查通过<br>
      false: 权限检查失败，无接口使用权限<br>

__系统资源申请接口__<br>

2. ```int requestCpuHighFreq(int uid, int level, int timeoutms)```<br>
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

3. ```int cancelCpuHighFreq(int uid)```<br>
      __介绍：取消申请cpu频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

4. ```int requestThreadPriority(int uid, int req_tid, int timeoutms)```<br>
      __介绍：申请线程获得高优先级，将会优先得到调度运行__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *req_tid* : 申请的线程id<br>
      *timeoutms* : 申请的时长<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

5. ```int cancelThreadPriority (int uid, int req_tid)```<br>
      __介绍：取消申请线程优先级的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *req_tid* : 取消的线程id<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

6. ```int requestGpuHighFreq(int uid, int level, int timeoutms)```<br>
      __介绍：向系统申请gpu频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *level* : 需要的gpu频率level，系统将会根据不同机型设置不同的gpu频率<br>
      *timeoutms* : 申请gpu资源的持续时间<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

7. ```int cancelGpuHighFreq(int uid)```<br>
      __介绍：取消申请gpu频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>

      返回结果：<br>
       0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

8. ```int requestDdrHighFreq(int uid, int level, int timeoutms)```<br>
      __介绍：向系统申请ddr频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *level* : 需要的ddr频率level，系统将会根据不同机型设置不同的ddr频率<br>
      *timeoutms* : 申请ddr资源的持续时间<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

9. ```int cancelDdrHighFreq(int uid)```<br>
      __介绍：取消申请ddr频率资源的接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>

      返回结果：<br>
       0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

10. ```int requestIOPrefetch(int uid, String filePath)```<br>
      __介绍：申请IO预读取接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *filePath* : 预读取的文件路径<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

11. ```int requestBindCore(int uid, int req_tid)```<br>
      __介绍：申请线程优先大核运行接口__<br>

      参数：<br>
      *uid* : android.os.Process.myUid()<br>
      *req_tid* : 申请的线程id<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

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
