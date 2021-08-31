package com.mi.mibridge;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import dalvik.system.PathClassLoader;

/**
 * Created by guyunjian on 2020/2/14.
 */

public class MiBridge {

    private static final String TAG = "MiBridge";
    private static final String PERFORMANCE_JAR = "/system/framework/MiuiBooster.jar";
    private static final String PERFORMANCE_CLASS = "com.miui.performance.MiuiBooster";

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

    private static Class perfClass;
    private static PathClassLoader perfClassLoader;

    private static Constructor<Class> mConstructor = null;
    private static Object mPerf = null;

    static {
        try {
            perfClassLoader = new PathClassLoader(PERFORMANCE_JAR,
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
            } catch (Exception e){
                Log.e(TAG, "cancelBindCore no exit");
            }

            try {
                //iop
                argClasses = new Class[]{int.class, String.class};
                mRequestIOPrefetchFunc = perfClass.getDeclaredMethod("requestIOPrefetch", argClasses);
            } catch (Exception e) {
                Log.e(TAG, "requestIOPrefetch no exit");
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
}
