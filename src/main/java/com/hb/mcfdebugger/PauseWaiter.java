package com.hb.mcfdebugger;

import com.hb.mcfdebugger.config.ConfigHolder;

public class PauseWaiter {
    public static long lastHeartBeatSet=0;

    public static void WaitForNext(){
        DebugThread.nextStep = false;
        while (!DebugThread.nextStep) {
            try {
                if(DebugThread.wsserver.getConnections().size()<=0){
                    ConfigHolder.debuggerMode="none";
                    break;
                }
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        DebugThread.nextStep = false;
    }
}
