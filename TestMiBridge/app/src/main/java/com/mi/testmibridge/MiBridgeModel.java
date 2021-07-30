package com.mi.testmibridge;

public class MiBridgeModel {

    // 标题
    private String mName;

    // 点击item触发的行为
    private Runnable mRunnable;

    public MiBridgeModel(String mName, Runnable mRunnable) {
        this.mName = mName;
        this.mRunnable = mRunnable;
    }

    public String getName() {
        return mName;
    }

    public Runnable getRunnable() {
        return mRunnable;
    }
}
