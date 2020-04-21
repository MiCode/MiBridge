# 小米应用加速器
## 其他语言
[English](./README.md)

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
## 接入说明
#### 1. 申请调试权限<br>
请提供以下信息，发送到邮箱xiaomi-mispeed-support@xiaomi.com, 申请接入调试权限。<br>
邮件主题：xx公司，申请小米应用加速器接入调试权限。<br>

应用包名 | 公司名称 | 申请人 | 手机型号 | MIUI版本 | IMEI号
---- | ----- | ------ | ------- | -------- | ---------
com.mi.testmibridge | Xx | 张三 | Redmi K20 Pro | MIUI 11 20.3.5 开发版 | 867252030127459
    
我们将会给您回复一个`鉴权码`，供前期接入SDK调试使用。

#### 2. 接口定义
__权限检查接口__<br>

1. ```boolean checkPermission(String pkg, int uid, String auth_key)```<br>
      __介绍：通过申请获得的鉴权码，检查应用是否有调试权限。__<br>

      参数：<br>
      *pkg* : 应用包名<br>
      *uid* : android.os.Process.myUid()<br>
      *auth_key* : 申请获得的鉴权码<br>

      返回结果：<br>
      *true* : 权限检查通过<br>
      *false* : 权限检查失败，无接口使用权限<br>

2. ```boolean checkPermission(String pkg, int uid)```<br>
      __介绍：检查应用是否有正式权限，正式权限的申请参考[3.申请正式权限]()，__<br>
      __获得正式权限后，使用此接口检查权限，无需使用上一个接口的鉴权码。__<br>

      参数：<br>
      *pkg* : 应用包名<br>
      *uid* : android.os.Process.myUid()<br>

      返回结果：<br>
      true: 权限检查通过<br>
      false: 权限检查失败，无接口使用权限<br>

__系统资源申请接口__<br>

3. ```int requestCpuHighFreq(int level, int timeoutms)```<br>
      __介绍：向系统申请cpu频率资源的接口__<br>

      参数：<br>
      *level* : 需要的cpu频率level，系统将会根据不同机型设置不同的cpu频率<br>

      Level级别 | 解释（以小米8为例）
      ---- | -----
      1 | Level 1 将会设置系统当前最小频率为系统最高频率
      2 | Level 2 将会设置系统当前最小频率为系统较高频率<br>例：小米8对应大核最低2.2GHz, 小核最低1.5GHz
      3 | Level 3 将会设置系统当前最小频率为系统正常频率以上<br>例：小米8对应大核最低1.9GHz, 小核最低1.0GHz

      __注意：请谨慎使用Level 1级别。Level 1级别使用场景数量限制为5，__<br>
      __Level 2级别使用场景数量限制为20，Level 3 不做限制。__<br>
      *timeoutms* : 申请cpu资源的持续时间。<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

4. ```int cancelCpuHighFreq()```<br>
      __介绍：取消申请的cpu频率资源__<br>

      参数：无<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

5. ```int requestThreadPriority(int req_tid, int timeoutms)```<br>
      __介绍：申请线程获得高优先级，将会优先运行在系统大核上。__<br>

      参数：<br>
      *req_tid* : 申请的线程id<br>
      *timeoutms* : 申请的时长<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

6. ```int cancelThreadPriority (int req_tid)```<br>
      __介绍：取消申请的线程优先级__<br>

      参数：<br>
      *req_tid* : 取消的线程id<br>

      返回结果：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

#### 3. 申请正式权限
请提供以下信息，发送到邮箱xiaomi-mispeed-support@xiaomi.com, 申请正式权限。<br>
邮件主题：xx公司，申请小米应用加速器正式权限。<br>
__`注意：申请正式权限，需要签订相关商务协议。`__<br>
__1. 资源接口使用场景__<br>

场景 | cpu level | thread priority | timeout (ms)
---- | ----- | ------ | -------
打开xxx页面 | 1 | 0 | 100
Xxx滑动 | 2 | 0 | 1000
场景3 | 3 | 1 | 500
场景4 | 1 | 0 | 100
场景5 | 0 | 1 |2000

__2. 性能测试__<br>

场景 | 具体测试内容 | 优化前 | 优化后 | 提升
---- | ----- | ------ | ------- | --------
进入某页面 | 进入页面耗时 | 100 ms | 80 ms | 20%
某页面滑动 | 滑动丢帧数(率) | 10% | 6% | 40%
场景3 | 测试内容3 | .. | .. |..

备注：我们也会根据您提供的场景进行性能测试。<br>
如贵公司无相关测试条件，也可以由我们帮您进行测试。<br>

__3. 功耗测试__<br>

场景 | Base | 接入后 | Diff
---- | ----- | ------ | -------
场景1 | 1000 mA | 1050 mA | 50
场景2 | .. | .. | ..

备注：我们也会根据您提供的场景进行功耗测试。<br>
如贵公司无相关测试条件，也可以由我们帮您进行测试。<br>


#### 4. 更多合作
后续将会开放更多接口<br>
requestGpuHighFreq，requestIOHighFreq，requestMemory，requestNetwork等

后续可以提供的支持<br>
为您的应用提供更多的支持（例如，卡顿打点，内存泄漏打点），提升应用的性能，提升用户的使用满意度。