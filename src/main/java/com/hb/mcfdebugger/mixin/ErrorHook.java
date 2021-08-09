package com.hb.mcfdebugger.mixin;

import com.hb.mcfdebugger.*;
import com.hb.mcfdebugger.config.ConfigHolder;
import com.hb.mcfdebugger.mixinHelpers.ReadCommandSource;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;

@Mixin(CommandFunctionManager.Entry.class)
public class ErrorHook {
    @Shadow @Final ServerCommandSource source;
    @Shadow @Final int depth;
    @Shadow @Final CommandFunction.Element element;
    @Overwrite
    public void execute(CommandFunctionManager manager, Deque<CommandFunctionManager.Entry> entries, int maxChainLength, @Nullable CommandFunctionManager.Tracer tracer) {
        try {
            this.element.execute(manager, this.source, entries, maxChainLength, this.depth, tracer);
        } catch (CommandSyntaxException var6) {
            if (tracer != null) {
                tracer.traceError(this.depth, var6.getRawMessage().getString());
            }
            sendError(var6);
        } catch (Exception var7) {
            if (tracer != null) {
                tracer.traceError(this.depth, var7.getMessage());
            }
            sendError(var7);
        }
        if (McfDebugger.lastCmdObj!=null && McfDebugger.lastCmdObj.toSimple().isNext(McfDebugger.nowMuteCmd)) {
            McfDebugger.nowMuteCmd.clear();
        }
    }
    public void sendError(Exception exception) {
        if (!ConfigHolder.debuggerMode.equals("none")) {
            String exceptionMsg = exception.toString();
            if (exceptionMsg != "") {
                if (McfDebugger.lastCmdObj!=null && !McfDebugger.lastCmdObj.toSimple().isNext(McfDebugger.nowMuteCmd)) {
                    int mode=ConfigHolder.nonStopOnException?1:0;
                    McfDebugger.lastCmdObj.exception=exceptionMsg;
                    McfDebugger.lastCmdObj.pause=true;
                    SendCmdObj sendCmdObj = new SendCmdObj(McfDebugger.lastCmdObj.funNamespace, McfDebugger.lastCmdObj.funPath, McfDebugger.lastCmdObj.cmdIndex, element.toString(),true, ReadCommandSource.read(source));
                    sendCmdObj.exception=exceptionMsg;
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
        }

    }
}
