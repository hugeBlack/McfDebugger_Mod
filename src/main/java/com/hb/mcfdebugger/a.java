package com.hb.mcfdebugger;

import net.minecraft.server.function.CommandFunction;

import java.lang.reflect.Field;

import static com.hb.mcfdebugger.McfDebugger.LOG;

//for debug only
public class a {
    public static void a(String cmdList){

    }
    public static void b(){
        LOG(McfDebugger.tagFunctionLeft);
        LOG(McfDebugger.stackList);
    }
}
