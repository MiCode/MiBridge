package com.mi.mibridge;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ThermalEventCallBack implements InvocationHandler {
    private static final String TAG = "MiBridge";
    private Object mObj = null;
    private Object mTarget = null;

    public void onThermalLevelChanged(int level){};

    public int getProxyHashCode() {
        return this.hashCode();
    }

    public Object getProxy(Class IThermalEventCallBack) {
        mTarget = IThermalEventCallBack.getInterfaces();
        mObj = Proxy.newProxyInstance(IThermalEventCallBack.getClassLoader(), new Class[] { IThermalEventCallBack }, this);
        return mObj;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        Object ret = null;

        if(mTarget == null) {
            Log.e(TAG, "getProxy fisrt!");
        }

        if(method.getName() == "onThermalLevelChanged" && args != null) {
            int level = Integer.parseInt(String.valueOf(args[0]));
            onThermalLevelChanged(level);
            return null;
        } else if(method.getName() == "getProxyHashCode") {
            ret = getProxyHashCode();
            return ret;
        }

        try {
             ret = method.invoke(mTarget, args);
        } catch (Exception e) {
            Log.e(TAG, "method invoke failed, e: " + e);
        }

        return ret;
    }
}
