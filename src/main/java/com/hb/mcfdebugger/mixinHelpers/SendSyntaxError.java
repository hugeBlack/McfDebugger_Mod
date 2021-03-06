package com.hb.mcfdebugger.mixinHelpers;

import com.hb.mcfdebugger.DebugThread;
import com.hb.mcfdebugger.McfDebugger;

import java.util.LinkedHashMap;
import java.util.Map;

public class SendSyntaxError {
    public static void send(IllegalArgumentException e){
        Map<String,String> errorMap = new LinkedHashMap<>();
        errorMap.put("namespace",McfDebugger.nowCmd.funNamespace);
        errorMap.put("path",McfDebugger.nowCmd.funPath);
        errorMap.put("cmdIndex", String.valueOf(McfDebugger.nowCmd.cmdIndex));
        errorMap.put("errorMsg", e.getMessage());
        DebugThread.sendObjMsgToDebugger(errorMap,"functionSyntaxError");
    }
}
