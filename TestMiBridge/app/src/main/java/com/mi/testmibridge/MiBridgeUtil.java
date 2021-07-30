package com.mi.testmibridge;

public class MiBridgeUtil {

    private MiBridgeUtil(){

    }

    /**
     * String-->int
     * @param value string
     * @return int
     */
    public static int parseString(String value){
        return parseString(value,-1);
    }

    /**
     * String-->int
     * @param value string
     * @param defaultValue 默认值
     * @return 结果
     */
    public static int parseString(String value,int defaultValue){
        try {
            return Integer.parseInt(value);
        }catch (Exception e){
            return defaultValue;
        }
    }
}
