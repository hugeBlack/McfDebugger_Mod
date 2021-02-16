package com.hb.mcfdebugger.mixin;

import com.hb.mcfdebugger.McfDebugger;
import net.minecraft.server.command.FunctionCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Collection;
import java.util.Iterator;

@Mixin(FunctionCommand.class)
public class FunctionCommandHook {
    @Overwrite
    private static int execute(ServerCommandSource source, Collection<CommandFunction> functions) {
        int i = 0;
        String cmdList = "";
        CommandFunction commandFunction;
        McfDebugger.lastCallCmdObj = McfDebugger.lastCmdObj==null?null:McfDebugger.lastCmdObj;
        McfDebugger.tagFunctionLeft.add(functions.size());
        for (Iterator var3 = functions.iterator(); var3.hasNext(); i += source.getMinecraftServer().getCommandFunctionManager().execute(commandFunction, source.withSilent().withMaxLevel(2))) {

            commandFunction = (CommandFunction) var3.next();
            //modified
            cmdList += commandFunction.getId().toString() + (var3.hasNext() ? "," : "");
            if(!var3.hasNext()){
                McfDebugger.hbPair p = new McfDebugger.hbPair(cmdList,McfDebugger.lastCallCmdObj);
                McfDebugger.stackList.add(p);
            }
            //modified
        }

        if (functions.size() == 1) {
            source.sendFeedback(new TranslatableText("commands.function.success.single", new Object[]{i, ((CommandFunction) functions.iterator().next()).getId()}), true);
        } else {
            source.sendFeedback(new TranslatableText("commands.function.success.multiple", new Object[]{i, functions.size()}), true);
        }
        //modified
        McfDebugger.lastCallCmdObj=null;
        McfDebugger.lastCmdObj = null;
        //modified
        return i;
    }
}
