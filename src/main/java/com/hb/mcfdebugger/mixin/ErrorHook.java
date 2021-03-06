package com.hb.mcfdebugger.mixin;

import com.hb.mcfdebugger.*;
import com.hb.mcfdebugger.config.ConfigHolder;
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
            if (!ConfigHolder.debuggerMode.equals("none")&&!ConfigHolder.nonStopOnException) {
                String exceptionMsg = var4.toString();
                if (exceptionMsg != "") {
                    if (McfDebugger.lastCmdObj!=null && !McfDebugger.lastCmdObj.toSimple().isNext(McfDebugger.nowMuteCmd)) {
                        sendError(exceptionMsg);

                    }
                }
            }
        }
        if (McfDebugger.lastCmdObj!=null && McfDebugger.lastCmdObj.toSimple().isNext(McfDebugger.nowMuteCmd)) {
            McfDebugger.nowMuteCmd.clear();
        }
    }
    public void sendError(String message) {
        McfDebugger.lastCmdObj.exception=message;
        McfDebugger.lastCmdObj.pause=true;
        Map<String,String> sourceMap =new LinkedHashMap<>();
        if(source.getEntity()!=null){
            sourceMap.put("entityName",source.getEntity().getEntityName());
            sourceMap.put("entityUuid",source.getEntity().getUuidAsString());
        }else{
            sourceMap.put("entityName","None(Server)");
            sourceMap.put("entityUuid","None");
        }
        sourceMap.put("pos" ,source.getPosition().toString());
        sourceMap.put("rotation","("+source.getRotation().y+", "+source.getRotation().x+")");
        sourceMap.put("world",source.getWorld().getRegistryKey().getValue().toString());
        try {
            Method levelMethod = source.getClass().getDeclaredMethod("fakeGetLevel");
            sourceMap.put("level", String.valueOf(levelMethod.invoke(source)));
        } catch (ReflectiveOperationException e) {
            sourceMap.put("level","Error");
        }
        sourceMap.put("commandsLeftToMaxChainLength", String.valueOf(McfDebugger.commandsLeftToMaxChainLength));
        SendCmdObj sendCmdObj = new SendCmdObj(McfDebugger.lastCmdObj.funNamespace, McfDebugger.lastCmdObj.funPath, McfDebugger.lastCmdObj.cmdIndex, element.toString(),true,sourceMap);
        sendCmdObj.exception=message;
        DebugThread.sendObjMsgToDebugger(McfDebugger.stackList,"stackReport");
        DebugThread.sendObjMsgToDebugger(sendCmdObj,"errorCommandReport");

        PauseWaiter.WaitForNext();
    }
}
