package com.hb.mcfdebugger.mixinHelpers;

import com.hb.mcfdebugger.DebugThread;
import com.hb.mcfdebugger.McfDebugger;

public class SendSyntaxError {
    public static void send(IllegalArgumentException e){
        DebugThread.sendObjMsgToDebugger(e.getMessage(),"functionSyntaxError");
    }
}
