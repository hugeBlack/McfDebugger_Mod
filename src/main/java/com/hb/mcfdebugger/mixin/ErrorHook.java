package com.hb.mcfdebugger.mixin;

import com.hb.mcfdebugger.*;
import com.hb.mcfdebugger.config.ConfigHolder;
import com.hb.mcfdebugger.mixinHelpers.ReadCommandSource;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(CommandFunctionManager.Entry.class)
public class ErrorHook {
    @Shadow @Final CommandFunctionManager manager;
    @Shadow @Final ServerCommandSource source;
    @Shadow @Final CommandFunction.Element element;
    @Overwrite
    public void execute(ArrayDeque<CommandFunctionManager.Entry> stack, int maxChainLength) {
        try {
            element.execute(manager, source, stack, maxChainLength);
        } catch (Throwable var4) {
            if (!ConfigHolder.debuggerMode.equals("none")) {
                String exceptionMsg = var4.toString();
                if (exceptionMsg != "") {
                    if (McfDebugger.lastCmdObj!=null && !McfDebugger.lastCmdObj.toSimple().isNext(McfDebugger.nowMuteCmd)) {
                        sendError(exceptionMsg,ConfigHolder.nonStopOnException?1:0);

                    }
                }
            }
        }
        if (McfDebugger.lastCmdObj!=null && McfDebugger.lastCmdObj.toSimple().isNext(McfDebugger.nowMuteCmd)) {
            McfDebugger.nowMuteCmd.clear();
        }
    }
    public void sendError(String message,int mode) {
        McfDebugger.lastCmdObj.exception=message;
        McfDebugger.lastCmdObj.pause=true;
        SendCmdObj sendCmdObj = new SendCmdObj(McfDebugger.lastCmdObj.funNamespace, McfDebugger.lastCmdObj.funPath, McfDebugger.lastCmdObj.cmdIndex, element.toString(),true, ReadCommandSource.read(source));
        sendCmdObj.exception=message;
        if(mode==0||McfDebugger.howeverStop){
            McfDebugger.howeverStop=false;
            DebugThread.sendObjMsgToDebugger(McfDebugger.stackList,"stackReport");
            DebugThread.sendObjMsgToDebugger(sendCmdObj,"errorCommandReport");
            PauseWaiter.WaitForNext();
        }else if (mode==1){
            DebugThread.sendObjMsgToDebugger(sendCmdObj,"nonStopErrorCommandReport");
        }
    }
}
