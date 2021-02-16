package com.hb.mcfdebugger.mixin;

import com.hb.mcfdebugger.DebugThread;
import com.hb.mcfdebugger.McfDebugger;
import com.hb.mcfdebugger.SendCmdObj;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import com.hb.mcfdebugger.commands.DebuggerCommand;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerCommandSource.class)
public class ServerCommandSourceHook {
    @Inject(method = "sendFeedback", at = @At("RETURN"))
    public void sendFeedback(Text message, boolean broadcastToOps, CallbackInfo ci) throws CommandSyntaxException {
        if (McfDebugger.lastCmdObj!=null && (McfDebugger.lastCmdObj.funNamespace.equals(McfDebugger.nowLoudFunNamespace) && McfDebugger.lastCmdObj.funPath.equals(McfDebugger.nowLoudFunPath) && McfDebugger.lastCmdObj.cmdIndex == McfDebugger.nowLoudIndex + 1)) {
            McfDebugger.hbPair send=new McfDebugger.hbPair("output",message.getString());
            DebugThread.sendObjMsgToDebugger(send,"loudResult");
            McfDebugger.nowLoudFunNamespace="";
            throw DebuggerCommand.LOGGER_HIT_EXCEPTION.create();
        }
        if (McfDebugger.lastCmdObj!=null && (McfDebugger.lastCmdObj.funNamespace.equals(McfDebugger.nowLogFunNamespace) && McfDebugger.lastCmdObj.funPath.equals(McfDebugger.nowLogFunPath) && McfDebugger.lastCmdObj.cmdIndex == McfDebugger.nowLogIndex + 1)) {

            McfDebugger.hbPair send=new McfDebugger.hbPair("output",new McfDebugger.hbPair(message.getString(),McfDebugger.lastCmdObj));
            DebugThread.sendObjMsgToDebugger(send,"logResult");
            McfDebugger.nowLogFunNamespace="";
        }
    }
}
