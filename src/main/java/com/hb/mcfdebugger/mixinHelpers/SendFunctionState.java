package com.hb.mcfdebugger.mixinHelpers;

import com.hb.mcfdebugger.DebugThread;
import com.hb.mcfdebugger.McfDebugger;
import net.minecraft.command.CommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.FunctionLoader;
import net.minecraft.util.Identifier;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SendFunctionState {
    public static void send(FunctionLoader f, CommandSource source, CommandFunction function) {
        Map<Identifier, CommandFunction> funMap = f.getFunctions();
        List<McfDebugger.HbCmdObj> a = funObjParse(funMap);
        DebugThread.sendObjMsgToDebugger(a, "functionList");
    }

    public static List<McfDebugger.HbCmdObj> funObjParse(Map<Identifier, CommandFunction> cmdMap) {

        List<McfDebugger.HbCmdObj> functionArray = new LinkedList<>();
        for (Map.Entry<Identifier, CommandFunction> entry : cmdMap.entrySet()) {
            McfDebugger.HbCmdObj cmdObj = new McfDebugger.HbCmdObj();
            cmdObj.funName = String.valueOf(entry.getKey());
            for (CommandFunction.Element singleCmd : entry.getValue().getElements()) {
                cmdObj.commands.add(singleCmd.toString());
            }
            functionArray.add(cmdObj);
        }
        //McfDebugger.lastFunctionMap = cmdMap;
        return functionArray;
    }
}
