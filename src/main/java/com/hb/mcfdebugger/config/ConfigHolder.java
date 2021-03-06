package com.hb.mcfdebugger.config;

public class ConfigHolder {
    public static String debuggerMode="none";
    public static boolean isThisModDebugging=false;
    public static boolean nonStopOnException = false;
    public static void resetFeature(){
        nonStopOnException = false;
        isThisModDebugging=false;
    }
}
