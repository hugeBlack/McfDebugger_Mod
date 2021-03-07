package com.hb.mcfdebugger.mixinHelpers;

import com.hb.mcfdebugger.*;
import com.hb.mcfdebugger.config.ConfigHolder;
import net.minecraft.command.argument.Vec3ArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class SendCommandState {
    public static void send(CommandFunction.Element element, ServerCommandSource source){
        if(!ConfigHolder.debuggerMode.equals("none")) {
            String funNamespace = null;
            String funPath = null;
            Integer cmdIndex = -1;
            try {
                Field funNamespaceField = element.getClass().getDeclaredField("funNamespace");
                funNamespace = (String) funNamespaceField.get(element);
            } catch (ReflectiveOperationException e) {
            }
            try {
                Field funPathField = element.getClass().getDeclaredField("funPath");
                funPath = (String) funPathField.get(element);
            } catch (ReflectiveOperationException e) {
            }
            try {
                Field cmdIndexField = element.getClass().getDeclaredField("cmdIndex");
                cmdIndex = (Integer) cmdIndexField.get(element);
            } catch (ReflectiveOperationException e) {
            }

            if(ConfigHolder.debuggerMode.equals("byStep")|| wsCommandParser.isInPauseList(funNamespace,funPath,cmdIndex)){
                SendCmdObj sendCmdObj = new SendCmdObj(funNamespace, funPath, cmdIndex, element.toString(),true,ReadCommandSource.read(source));
                McfDebugger.lastCmdObj=sendCmdObj;
                DebugThread.sendObjMsgToDebugger(McfDebugger.stackList,"stackReport");
                DebugThread.sendObjMsgToDebugger(sendCmdObj,"commandReport");
                PauseWaiter.WaitForNext();
            }else{
                SendCmdObj sendCmdObj = new SendCmdObj(funNamespace, funPath, cmdIndex, element.toString(),false,null);
                McfDebugger.lastCmdObj=sendCmdObj;
                DebugThread.sendObjMsgToDebugger(sendCmdObj,"commandReport");
            }
        }
    }
}
