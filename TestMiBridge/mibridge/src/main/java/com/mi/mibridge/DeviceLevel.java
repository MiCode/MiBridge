package com.mi.mibridge;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;

/**
 * Created by zhangxingyu on 2020/11/9.
 */
public class DeviceLevel {

    private static final String TAG = "DeviceLevel";

    private static final String PERFORMANCE_JAR = "/system/framework/MiuiBooster.jar";
    private static final String PERFORMANCE_CLASS = "com.miui.performance.DeviceLevelUtils";
    private static Method mInitDeviceLevel = null;
    private static Method mGetDeviceLevel = null;
    private static Method mGetDeviceLevelForWhole = null;
    private static Method mIsSupportPrune = null;
    private static Method mGetMiuiLiteVersion = null;
    private static Method mGetMiuiMiddleVersion = null;
    private static Class perfClass;
    private static PathClassLoader perfClassLoader;
    private static Application application;
    private static Context applicationContext;
    private static Constructor<Class> mConstructor = null;
    private static Object mPerf = null;

    public static final int DEV_STANDARD_VER = 1;

    public static int RAM;
    public static int CPU;
    public static int GPU;

    public static int LOW;
    public static int MIDDLE;
    public static int HIGH;
    public static int UNKNOWN;

    public static boolean IS_MIUI_LITE_VERSION;

    public static boolean IS_MIUI_MIDDLE_VERSION;

    public static boolean IS_MIUI_GO_VERSION;

    public static int TOTAL_RAM;

    static {
        try {
            perfClassLoader = new PathClassLoader(PERFORMANCE_JAR, ClassLoader.getSystemClassLoader());
            perfClass = perfClassLoader.loadClass(PERFORMANCE_CLASS);
            mConstructor = perfClass.getConstructor(Context.class);

            // 触发初始化deviceLevelList信息，存入settings
            mInitDeviceLevel = perfClass.getDeclaredMethod("initDeviceLevel");

            // 获取机型等级的接口
            Class[] argClasses = new Class[]{int.class, int.class};
            mGetDeviceLevel = perfClass.getDeclaredMethod("getDeviceLevel", argClasses);

            argClasses = new Class[]{int.class};
            mGetDeviceLevelForWhole = perfClass.getDeclaredMethod("getDeviceLevel", argClasses);

            // 判断是否支持MIUI性能优化策略的接口
            mIsSupportPrune = perfClass.getDeclaredMethod("isSupportPrune");

            // 获取类型定义
            RAM = getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_RAM");
            CPU = getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_CPU");
            GPU = getStaticObjectField(perfClass, "DEVICE_LEVEL_FOR_GPU");

            // 获取等级定义
            LOW = getStaticObjectField(perfClass, "LOW_DEVICE");
            MIDDLE = getStaticObjectField(perfClass, "MIDDLE_DEVICE");
            HIGH = getStaticObjectField(perfClass, "HIGH_DEVICE");
            UNKNOWN = getStaticObjectField(perfClass, "DEVICE_LEVEL_UNKNOWN");

            // 判断是否为MiuiLite
            IS_MIUI_LITE_VERSION = getStaticObjectField(perfClass, "IS_MIUI_LITE_VERSION");

            // 判断是否为MiuiGo版本
            IS_MIUI_GO_VERSION = getStaticObjectField(perfClass, "IS_MIUI_GO_VERSION");

            // 返回该设备的Ram值，单位：G
            TOTAL_RAM = getStaticObjectField(perfClass, "TOTAL_RAM");

            // 判断MiuiLite版本
            mGetMiuiLiteVersion = perfClass.getDeclaredMethod("getMiuiLiteVersion");

        } catch (Exception e) {
            Log.e(TAG, "MiDeviceLevelBridge(): Load Class Exception:" + e);
        }

        try {
            // 判断是否为MiuiMiddle
            IS_MIUI_MIDDLE_VERSION = getStaticObjectField(perfClass, "IS_MIUI_MIDDLE_VERSION");

            // MiuiMiddle版本号
            mGetMiuiMiddleVersion = perfClass.getDeclaredMethod("getMiuiMiddleVersion");

        } catch (Exception e) {
            Log.e(TAG, "DeviceLevelUtils(): newInstance Exception:" + e);
            e.printStackTrace();
        }

        if (applicationContext == null) {
            try {
                application = (Application) Class.forName("android.app.ActivityThread").getMethod("currentApplication").invoke(null, (Object[]) null);
                if (application != null) {
                    applicationContext = application.getApplicationContext();
                }
            } catch (Exception e) {
                Log.e(TAG, "android.app.ActivityThread Exception:" + e);
            }
        }

        if (applicationContext == null) {
            try {
                application = (Application) Class.forName("android.app.AppGlobals").getMethod("getInitialApplication").invoke(null, (Object[]) null);
                if (application != null) {
                    applicationContext = application.getApplicationContext();
                }
            } catch (Exception e) {
                Log.e(TAG, "android.app.AppGlobals Exception:" + e);
            }
        }

        try {
            if (mConstructor != null) {
                mPerf = mConstructor.newInstance(applicationContext);
            }
        } catch (Exception e) {
            Log.e(TAG, "DeviceLevelUtils(): newInstance Exception:" + e);
            e.printStackTrace();
        }
    }

    public DeviceLevel() {
    }

    public static void initDeviceLevel() {
        try {
            mInitDeviceLevel.invoke(mPerf);
        } catch (Exception e) {
            Log.e(TAG, "initDeviceLevel failed , e:" + e.toString());
        }
    }

    public static int getDeviceLevel(int version, int type) {
        int ret = -1;
        try {
            Object retVal = mGetDeviceLevel.invoke(mPerf, version, type);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "getDeviceLevel failed , e:" + e.toString());
        }
        return ret;
    }

    public static int getDeviceLevel(int version) {
        int ret = -1;
        try {
            Object retVal = mGetDeviceLevelForWhole.invoke(mPerf, version);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "getDeviceLevel failed , e:" + e.toString());
        }
        return ret;
    }

    public static int getMiuiLiteVersion() {
        int ret = -1;
        try {
            Object retVal = mGetMiuiLiteVersion.invoke(mPerf);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "getMiuiLiteVersion failed , e:" + e.toString());
        }
        return ret;
    }

    public static int getMiuiMiddleVersion() {
        int ret = -1;
        try {
            Object retVal = mGetMiuiMiddleVersion.invoke(mPerf);
            ret = (int) retVal;
        } catch (Exception e) {
            Log.e(TAG, "getMiuiMiddleVersion failed , e:" + e.toString());
        }
        return ret;
    }
    public static boolean isSupportPrune() {
        boolean isSupport = false;
        try {
            Object retVal = mIsSupportPrune.invoke(mPerf);
            isSupport = (boolean) retVal;
        } catch (Exception e) {
            Log.e(TAG, "isSupportPrune failed , e:" + e.toString());
        }
        return isSupport;
    }

    // 反射获取静态成员变量的值
    private static <T> T getStaticObjectField(Class<?> clazz, String field) throws Exception {
        Field declaredField = clazz.getDeclaredField(field);
        declaredField.setAccessible(true);
        return (T) declaredField.get(null);
    }
}
