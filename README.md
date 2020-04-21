# MiSpeed
## Other language versions
[简体中文](./README_zh.md)

## Our Purpose
All for users, let users get the best experience!
## Introduction
Xiaomi Application Accelerator (MiSpeed) provides developers with the interface to apply for system resources,<br>
which can improve the performance of developers in key application scenarios and enhance the user experience.
## Our Advantages
* __*Lightweight Access*__<br>
  Accessing through JAR file, the size is only a few KB, which will not burden developers' applications.
* __*Fast System Communication*__<br>
  Communicate with the system layer directly through binder, request system resources, fast and efficient.
* __*Scenario Customization*__<br>
  Provide different levels of system resources in different scenarios.
## Access Note
#### 1. Applying for debugging permission<br>
Please provide the following information and send it to xiaomi-mispeed-support@xiaomi.com to apply for debugging permission.<br>
Subject：xx Company，Applying for MiSpeed debugging permission。<br>

PackageName | CompanyName | Requestor | Device model | MIUI version | IMEI num
---- | ----- | ------ | ------- | -------- | ---------
com.mi.testmibridge | Xx | Tony | Redmi K20 Pro | MIUI 11 20.3.5 Beta | 867252030127459

We will reply you with an `authentication code` for debugging.

#### 2. APIs
__Permission apis__<br>

1. ```boolean checkPermission(String pkg, int uid, String auth_key)```<br>
      __Description：Check whether the application has debugging permission.__<br>

      parameters：<br>
      *pkg* : Package Name<br>
      *uid* : android.os.Process.myUid()<br>
      *auth_key* : `authentication code`<br>

      return value：<br>
      *true* : Permission granted<br>
      *false* : Permission not granted<br>

2. ```boolean checkPermission(String pkg, int uid)```<br>
      __Description：Check whether the application has formal permission，please refer [3.Applying for fromal permission]()__<br>
      __After obtaining the formal permission, use this interface to check the permission without using the `authentication code`.__<br>

      parameters：<br>
      *pkg* : Package Name<br>
      *uid* : android.os.Process.myUid()<br>

      return value：<br>
      true: Permission granted<br>
      false: Permission not granted<br>

__APIs for request system resouce__<br>

3. ```int requestCpuHighFreq(int level, int timeoutms)```<br>
      __Description： Reqeust CPU frequency__<br>

      parameters：<br>
      *level* : Expected cpu frequency level，system will set different CPU frequency according to different models<br>

      Level | Explanation (take Xiaomi 8 as an example)
      ---- | -----
      1 | Level 1 Set the CPU frequency to MAX
      2 | Level 2 Set the CPU frequency to higher<br>example：Xiaomi 8, Set big core at least 2.2GHz, little core at least 1.5GHz
      3 | Level 3 Set the CPU frequency to above normal<br>example：Xiaomi 8, Set big core at least 1.9GHz, little core at least 1.0GHz

      __Attention：Please using Level 1 with caution。Level 1 scenarios is limited to 5，__<br>
      __Level 2 is limited to 20，Level 3 no limits。__<br>
      *timeoutms* : duration<br>

      return value：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

4. ```int cancelCpuHighFreq()```<br>
      __Description：Cancle cpu frequency request__<br>

      parameters：None<br>

      return value：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

5. ```int requestThreadPriority(int req_tid, int timeoutms)```<br>
      __Description：Request thread for higher priority, running in big core.__<br>

      parameters：<br>
      *req_tid* : thread id<br>
      *timeoutms* : duration<br>

      return value：<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

6. ```int cancelThreadPriority (int req_tid)```<br>
      __Description：Cancel request__<br>

      parameters：<br>
      *req_tid* : thread id<br>

      return value:<br>
      0:   Success<br>
      -1:  Fail<br>
      -2:  Permission not granted!<br>

#### 3. Applying for fromal permission
Please provide the following information and send it to xiaomi-mispeed-support@xiaomi.com to apply for formal permission.<br>
Subject：xx Company，Applying for MiSpeed formal permission。<br>
__`Note：To apply for formal permission,relevant business agreements need to be signed.`__<br>
__1. APIs using scenarios__<br>

Scenarios | Cpu level | Thread priority | Timeout (ms)
---- | ----- | ------ | -------
Open xx activity | 1 | 0 | 100
Scroll in xx activity | 2 | 0 | 1000
Scenario 3 | 3 | 1 | 500
Scenario4 | 1 | 0 | 100
Scenario 5 | 0 | 1 |2000

__2. Performance Test__<br>

Scenarios | Detail test | Before | After | Diff
---- | ----- | ------ | ------- | --------
Open xx activity | the duration of open activity | 100 ms | 80 ms | 20%
Scroll | drop frames | 10% | 6% | 40%
Scenario 3 | Test 3 | .. | .. |..

Note：We will also do performance test according to the scenarios provided by you.<br>

__3. Power consumption test__<br>

Scenarios | Before | After | Diff
---- | ----- | ------ | -------
Scenario 1 | 1000 mA | 1050 mA | 50
Scenario 2 | .. | .. | ..

Note：We will also do power consumption test according to the scenarios provided by you.<br>


#### 4. More cooperation
More APIs will be supported in the future<br>
requestGpuHighFreq，requestIOHighFreq，requestMemory，requestNetwork等

Other supports that can be provided in the future<br>
Provide more support for your application (e.g. memory leak detection), improve the performance of the application, and improve the user satisfaction.
