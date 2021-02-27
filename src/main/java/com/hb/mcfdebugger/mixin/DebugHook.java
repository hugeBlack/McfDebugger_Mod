package com.hb.mcfdebugger.mixin;

import java.lang.reflect.Field;
import java.util.ArrayDeque;

import com.hb.mcfdebugger.*;
import com.hb.mcfdebugger.mixinHelpers.SendCommandState;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CommandFunction.CommandElement.class)
public abstract class DebugHook implements CommandFunction.Element{
    CommandFunction.Element element=(CommandFunction.Element)this;
    @Shadow @Final ParseResults<ServerCommandSource> parsed;
    @Overwrite
    public void execute (CommandFunctionManager manager, ServerCommandSource source, ArrayDeque<CommandFunctionManager.Entry> stack, int maxChainLength) throws CommandSyntaxException {
        SendCommandState.send(element,source);
        boolean isLastCmd=false;
        try {
            Field funNamespaceField = element.getClass().getDeclaredField("isLastCmd");
            isLastCmd= (Boolean) funNamespaceField.get(element);
        } catch (ReflectiveOperationException e) { }
        if(isLastCmd) {
            int i =McfDebugger.tagFunctionLeft.size()-1;
            if(McfDebugger.tagFunctionLeft.get(i)<=1){
                McfDebugger.stackList.remove(McfDebugger.stackList.size() - 1);
                McfDebugger.tagFunctionLeft.remove(i);
            }else{
                McfDebugger.tagFunctionLeft.set(i,McfDebugger.tagFunctionLeft.get(i)-1);
            }
        }
        //origin
        manager.getDispatcher().execute(new ParseResults(this.parsed.getContext().withSource(source), this.parsed.getReader(), this.parsed.getExceptions()));
        //origin

    }
}