package com.mi.mibridge;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.xiaomi.NetworkBoost.IAIDLMiuiNetQoECallback;
import com.xiaomi.NetworkBoost.IAIDLMiuiNetSelectCallback;
import com.xiaomi.NetworkBoost.IAIDLMiuiNetworkCallback;
import com.xiaomi.NetworkBoost.IAIDLMiuiWlanQoECallback;
import com.xiaomi.NetworkBoost.NetLinkLayerQoE;
import com.xiaomi.NetworkBoost.NetworkBoostManager;
import com.xiaomi.NetworkBoost.ServiceCallback;
import com.xiaomi.NetworkBoost.Version;

import java.io.FileDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import dalvik.system.PathClassLoader;

/**
 * Created by guyunjian on 2020/2/14.
 */

public class MiBridge {

    private static final String TAG = "MiBridge";
    private static final String PERFORMANCE_JAR = "/system/framework/MiuiBooster.jar";
    private static final String PERFORMANCE_U_JAR = "/system_ext/framework/MiuiBooster.jar";
    private static final String PERFORMANCE_CLASS = "com.miui.performance.MiuiBooster";
    private static final String ITHERMALEVENTCALLBACK_CLASS = "com.miui.performance.IThermalEventCallBack";

    private static Method mCheckPermissionFunc = null;
    private static Method mCheckDebugPermissionFunc = null;
    private static Method mRequestCpuHighFunc = null;
    private static Method mCancelCpuHighFunc = null;
    private static Method mRequestThreadPriorityFunc = null;
    private static Method mCancelThreadPriorityFunc = null;
    private static Method mRequestGpuHighFunc = null;
    private static Method mCancelGpuHighFunc = null;
    private static Method mRequestIOPrefetchFunc = null;
    private static Method mRequestDdrHighFunc = null;
    private static Method mCancelDdrHighFunc = null;
    private static Method mRequestBindCoreFunc = null;
    private static Method mCancelBindCoreFunc = null;
    private static Method mGetSystemStateFunc = null;
    private static Method mRegisterThermalEventCallbackFunc = null;
    private static Method mUnRegisterThermalEventCallbackFunc = null;
    private static Method mSetDynamicRefreshRateSceneFunc = null;

    private static NetworkBoostManager sNetworkBoostManager;

    private static Class perfClass;
    private static Class IThermalEventCallBackClass = null;
    private static PathClassLoader perfClassLoader;

    private static Constructor<Class> mConstructor = null;
    private static Object mPerf = null;

    static {
        try {
            String jarPath = Build.VERSION.SDK_INT <= 33 ? PERFORMANCE_JAR : PERFORMANCE_U_JAR;
            perfClassLoader = new PathClassLoader(jarPath,
                    ClassLoader.getSystemClassLoader());
            perfClass = perfClassLoader.loadClass(PERFORMANCE_CLASS);
            mConstructor = perfClass.getConstructor();
            Class[] argClasses = new Class[]{String.class, int.class};
            try {
                mCheckPermissionFunc = perfClass.getDeclaredMethod("checkPermission", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "checkPermission no exit");
            }
            try {
                IThermalEventCallBackClass = perfClassLoader.loadClass(ITHERMALEVENTCALLBACK_CLASS);
            } catch (Exception e) {
                Log.e(TAG, "com.miui.performance.IThermalEventCallBack not exits!");
            }

            try {
                argClasses = new Class[]{Context.class, String.class, int.class, String.class};
                mCheckDebugPermissionFunc = perfClass.getDeclaredMethod("checkPermission", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "checkPermission_debug no exit");
            }

            try {
                //cpu
                argClasses = new Class[]{int.class, int.class, int.class};
                mRequestCpuHighFunc = perfClass.getDeclaredMethod("requestCpuHighFreq", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "requestCpuHighFreq no exit");
            }

            try {
                argClasses = new Class[]{int.class};
                mCancelCpuHighFunc = perfClass.getDeclaredMethod("cancelCpuHighFreq", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "cancelCpuHighFreq no exit");
            }

            try {
                //thread
                argClasses = new Class[]{int.class, int.class, int.class};
                mRequestThreadPriorityFunc = perfClass.getDeclaredMethod("requestThreadPriority", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "requestThreadPriority no exit");
            }

            try {
                argClasses = new Class[]{int.class, int.class};
                mCancelThreadPriorityFunc = perfClass.getDeclaredMethod("cancelThreadPriority", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "cancelThreadPriority no exit");
            }

            try {
                //gpu
                argClasses = new Class[]{int.class, int.class, int.class};
                mRequestGpuHighFunc = perfClass.getDeclaredMethod("requestGpuHighFreq", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "requestGpuHighFreq no exit");
            }

            try {
                argClasses = new Class[]{int.class};
                mCancelGpuHighFunc = perfClass.getDeclaredMethod("cancelGpuHighFreq", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "cancelGpuHighFreq no exit");
            }

            try {
                //Ddr
                argClasses = new Class[]{int.class, int.class, int.class};
                mRequestDdrHighFunc = perfClass.getDeclaredMethod("requestDdrHighFreq", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "requestDdrHighFreq no exit");
            }

            try {
                argClasses = new Class[]{int.class};
                mCancelDdrHighFunc = perfClass.getDeclaredMethod("cancelDdrHighFreq", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "cancelDdrHighFreq no exit");
            }

            try {
                //bindcore
                argClasses = new Class[]{int.class, int.class, int.class};
                mRequestBindCoreFunc = perfClass.getDeclaredMethod("requestBindCore", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "requestBindCore no exit");
            }

            try {
                //cancelcore
                argClasses = new Class[]{int.class, int.class};
                mCancelBindCoreFunc = perfClass.getDeclaredMethod("cancelBindCore", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "cancelBindCore no exit");
            }

            try {
                //iop
                argClasses = new Class[]{int.class, String.class};
                mRequestIOPrefetchFunc = perfClass.getDeclaredMethod("requestIOPrefetch", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "requestIOPrefetch no exit");
            }

            try {
                argClasses = new Class[]{int.class, Context.class, int.class};
                mGetSystemStateFunc = perfClass.getDeclaredMethod("getSystemState", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "getSystemState no exit");
            }

            try {
                argClasses = new Class[]{int.class, String.class, int.class};
                mSetDynamicRefreshRateSceneFunc = perfClass.getDeclaredMethod("setDynamicRefreshRateScene", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "setDynamicRefreshRateScene no exit");
            }

            try {
                argClasses = new Class[]{int.class, IThermalEventCallBackClass};
                mRegisterThermalEventCallbackFunc = perfClass.getDeclaredMethod("registerThermalEventCallback", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "registerThermalEventCallback no exit, " + e);
            }

            try {
                argClasses = new Class[]{int.class, IThermalEventCallBackClass};
                mUnRegisterThermalEventCallbackFunc = perfClass.getDeclaredMethod("unRegisterThermalEventCallback", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "UnRegisterThermalEventCallback no exit");
            }

        } catch (Exception e) {
            Log.e(TAG, "MiBridge() : Load Class Exception: " + e);
        }

        try {
            if (mConstructor != null) {
                mPerf = mConstructor.newInstance();
            }
        } catch (Exception e) {
            Log.e(TAG, "MiBridge() : newInstance Exception:" + e);
        }
    }

    public MiBridge() {

    }

    public static boolean checkPermission(String pkg, int uid) {
        boolean ret = false;
        try {
            Object retVal = mCheckPermissionFunc.invoke(mPerf, pkg, uid);
            ret = (boolean) retVal;
        } catch (Exception e) {
            Log.e(TAG, "check permission failed , e:" + e.toString());
        }
        return ret;
    }

    public static boolean checkDebugPermission(Context context, String pkg, int uid, String auth_key) {
        boolean ret = false;
        try {
            Object retVal = mCheckDebugPermissionFunc.invoke(mPerf, context, pkg, uid, auth_key);
            ret = (boolean) retVal;
        } catch (Exception e) {
            Log.e(TAG, "check debug permission failed , e:" + e.toString());
        }
        return ret;
    }

    public static int requestCpuHighFreq(int uid, int level, int timeoutms) {
        int ret = -1;
        try {
            Object retVal = mRequestCpuHighFunc.invoke(mPerf, uid, level, timeoutms);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "request cpu high failed , e:" + e.toString());
        }
        return ret;
    }

    public static int cancelCpuHighFreq(int uid) {
        int ret = -1;
        try {
            Object retVal = mCancelCpuHighFunc.invoke(mPerf, uid);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "cancel cpu high failed, e:" + e.toString());
        }
        return ret;
    }

    public static int requestThreadPriority(int uid, int req_tid, int timeoutms) {
        int ret = -1;
        try {
            Object retVal = mRequestThreadPriorityFunc.invoke(mPerf, uid, req_tid, timeoutms);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "request thread priority failed , e:" + e.toString());
        }
        return ret;
    }

    public static int cancelThreadPriority(int uid, int req_tid) {
        int ret = -1;
        try {
            Object retVal = mCancelThreadPriorityFunc.invoke(mPerf, uid, req_tid);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "cancel thread priority failed, e:" + e.toString());
        }
        return ret;
    }

    public static int requestGpuHighFreq(int uid, int level, int timeoutms) {
        int ret = -1;
        try {
            Object retVal = mRequestGpuHighFunc.invoke(mPerf, uid, level, timeoutms);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "request Gpu high failed , e:" + e.toString());
        }
        return ret;
    }

    public static int cancelGpuHighFreq(int uid) {
        int ret = -1;
        try {
            Object retVal = mCancelGpuHighFunc.invoke(mPerf, uid);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "cancel Gpu high failed, e:" + e.toString());
        }
        return ret;
    }

    public static int requestBindCore(int uid, int tid, int timeoutms) {
        int ret = -1;
        try {
            Object retVal = mRequestBindCoreFunc.invoke(mPerf, uid, tid, timeoutms);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "request BindCore failed , e:" + e.toString());
        }
        return ret;
    }

    public static int cancelBindCore(int uid, int tid) {
        int ret = -1;
        try {
            Object retVal = mCancelBindCoreFunc.invoke(mPerf, uid, tid);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "request BindCore failed , e:" + e.toString());
        }
        return ret;
    }

    public static int cancelDdrHighFreq(int uid) {
        int ret = -1;
        try {
            Object retVal = mCancelDdrHighFunc.invoke(mPerf, uid);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "cancel Ddr high failed, e:" + e.toString());
        }
        return ret;
    }

    public static int requestDdrHighFreq(int uid, int level, int timeoutms) {
        int ret = -1;
        try {
            Object retVal = mRequestDdrHighFunc.invoke(mPerf, uid, level, timeoutms);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "request Ddr high failed , e:" + e.toString());
        }
        return ret;
    }

    public static int requestIOPrefetch(int uid, String filePath) {
        int ret = -1;
        try {
            Object retVal = mRequestIOPrefetchFunc.invoke(mPerf, uid, filePath);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "request IO Prefetch failed, e:" + e.toString());
        }
        return ret;
    }

    public static int getSystemState(int uid, Context context, int type) {
        int ret = -1;
        try {
            Object retVal = mGetSystemStateFunc.invoke(mPerf, uid, context, type);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "get system state failed , e:" + e.toString());
        }
        return ret;
    }

    public static int registerThermalEventCallback(int uid, ThermalEventCallBack cb) {
        int ret = -1;
        Object Obj = null;
        try {
            Obj = cb.getProxy(IThermalEventCallBackClass);
        } catch (Exception e) {
            Log.e(TAG, "getProxy failed, e: " + e.toString());
            return ret;
        }

        try {
            Object retVal = mRegisterThermalEventCallbackFunc.invoke(mPerf, uid, Obj);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "registerThermalEventCallback failed , e:" + e.toString());
        }
        return ret;
    }


    public static int unRegisterThermalEventCallback(int uid, ThermalEventCallBack cb) {
        int ret = -1;
        Object Obj = null;
        try {
            Obj = cb.getProxy(IThermalEventCallBackClass);
        } catch (Exception e) {
            Log.e(TAG, "getProxy failed, e: " + e.toString());
            return ret;
        }
        try {
            Object retVal = mUnRegisterThermalEventCallbackFunc.invoke(mPerf, uid, Obj);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "unRegisterThermalEventCallback failed , e:" + e.toString());
        }
        return ret;
    }

    public static int setScene(int uid, String pkgName, int sceneId) {
        int ret = -1;
        try {
            Object retVal = mSetDynamicRefreshRateSceneFunc.invoke(mPerf, uid, pkgName, sceneId);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "set dynamic refresh rate failed , e:" + e.toString());
        }
        return ret;
    }

    /**
     * Network func
     * @param context Context
     * @param cb ServiceCallback
     */
    public static void initNetwork(Context context, ServiceCallback cb) {
        sNetworkBoostManager = new NetworkBoostManager(context, cb);
    }

    public static boolean bindService() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.bindService();
    }

    public static void unbindService() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return;
        }
        sNetworkBoostManager.unbindService();
    }

    public static boolean setSockPrio(int fd, int prio) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setSockPrio(fd, prio);
    }

    public static boolean setSockPrio(FileDescriptor fd, int prio) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setSockPrio(fd, prio);
    }

    public static boolean setSockPrio(Socket sock, int prio) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setSockPrio(sock, prio);
    }

    public static boolean setTCPCongestion(int fd, String cc) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setTCPCongestion(fd, cc);
    }

    public static boolean setTCPCongestion(FileDescriptor fd, String cc) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setTCPCongestion(fd, cc);
    }

    public static boolean setTCPCongestion(Socket sock, String cc) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setTCPCongestion(sock, cc);
    }

    public static boolean isSupportDualWifi() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.isSupportDualWifi();
    }

    public static boolean setSlaveWifiEnable(boolean enable){
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setSlaveWifiEnable(enable);
    }

    public static boolean connectSlaveWifi(int networkId){
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.connectSlaveWifi(networkId);
    }

    public static boolean disconnectSlaveWifi(){
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.disconnectSlaveWifi();
    }

    public static boolean setTrafficTransInterface(int fd, String bindInterface){
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setTrafficTransInterface(fd,bindInterface);
    }

    public static boolean setTrafficTransInterface(FileDescriptor fd, String bindInterface) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setTrafficTransInterface(fd,bindInterface);
    }

    public static boolean setTrafficTransInterface(Socket sock, String bindInterface) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setTrafficTransInterface(sock,bindInterface);
    }

    public static boolean registerCallback(IAIDLMiuiNetworkCallback cb) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.registerCallback(cb);
    }

    public static boolean unregisterCallback(IAIDLMiuiNetworkCallback cb) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.unregisterCallback(cb);
    }

    public static boolean activeScan(int[] channeList) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.activeScan(channeList);
    }

    public static boolean registerWifiLinkCallback(IAIDLMiuiWlanQoECallback cb) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.registerWifiLinkCallback(cb);
    }

    public static boolean abortScan() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.abortScan();
    }

    public static boolean unregisterWifiLinkCallback(IAIDLMiuiWlanQoECallback cb) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.unregisterWifiLinkCallback(cb);
    }

    public static boolean suspendBackgroundScan() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.suspendBackgroundScan();
    }

    public static boolean resumeBackgroundScan() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.resumeBackgroundScan();
    }

    public static boolean suspendWifiPowerSave() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.suspendWifiPowerSave();
    }

    public static boolean resumeWifiPowerSave() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.resumeWifiPowerSave();
    }

    public static Map<String, String> getQoEByAvailableIfaceName(String ifaceName) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return new HashMap<>();
        }
        return sNetworkBoostManager.getQoEByAvailableIfaceName(ifaceName);
    }

    public static Map<String, String> getAvailableIfaces() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return new HashMap<>();
        }
        return sNetworkBoostManager.getAvailableIfaces();
    }

    public static Map<String, String> requestAppTrafficStatistics(int type, long startTime, long endTime) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return new HashMap<>();
        }
        return sNetworkBoostManager.requestAppTrafficStatistics(type,startTime,endTime);
    }

    public static NetLinkLayerQoE getQoEByAvailableIfaceNameV1(String ifaceName) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return new NetLinkLayerQoE();
        }
        return sNetworkBoostManager.getQoEByAvailableIfaceNameV1(ifaceName);
    }

    public static boolean registerNetLinkCallback(IAIDLMiuiNetQoECallback cb, int interval) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.registerNetLinkCallback(cb,interval);
    }

    public static boolean unregisterNetLinkCallback(IAIDLMiuiNetQoECallback cb) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.unregisterNetLinkCallback(cb);
    }

    public static boolean isSupportDualCelluarData() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.isSupportDualCelluarData();
    }

    public static boolean isCelluarDSDAState() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.isCelluarDSDAState();
    }

    public static boolean setDualCelluarDataEnable(boolean enable) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.setDualCelluarDataEnable(enable);
    }

    public static boolean enableWifiSelectionOpt(IAIDLMiuiNetSelectCallback cb, int type) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.enableWifiSelectionOpt(cb, type);
    }

    public static void triggerWifiSelection() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return;
        }
        sNetworkBoostManager.triggerWifiSelection();
    }

    public static void reportBssidScore(Map<String, String> bssidScores) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return;
        }
        sNetworkBoostManager.reportBssidScore(bssidScores);
    }

    public static boolean disableWifiSelectionOpt(IAIDLMiuiNetSelectCallback cb) {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.disableWifiSelectionOpt(cb);
    }

    public static int getSDKVersion() {
        return NetworkBoostManager.getSDKVersion();
    }

    public static int getServiceVersion() {
        return NetworkBoostManager.getServiceVersion();
    }

    public static boolean isSupportMediaPlayerPolicy() {
        if (null == sNetworkBoostManager) {
            Log.e(TAG, "networkBoost not initialized, please call initNetwork method first");
            return false;
        }
        return sNetworkBoostManager.isSupportMediaPlayerPolicy();
    }

}
