package com.hb.mcfdebugger.mixin;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Deque;

import com.hb.mcfdebugger.*;
import com.hb.mcfdebugger.mixinHelpers.SendCommandState;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CommandFunction.CommandElement.class)
public abstract class DebugHook implements CommandFunction.Element{
    CommandFunction.Element element=(CommandFunction.Element)this;
    @Shadow @Final ParseResults<ServerCommandSource> parsed;
    @Shadow
    abstract int execute(CommandFunctionManager manager, ServerCommandSource source) throws CommandSyntaxException;
    @Overwrite
    public void execute (CommandFunctionManager manager, ServerCommandSource source, Deque<CommandFunctionManager.Entry> entries, int maxChainLength, int depth, @Nullable CommandFunctionManager.Tracer tracer) throws CommandSyntaxException {
        SendCommandState.send(element,source);
        boolean isLastCmd=false;
        try {
            Field funNamespaceField = element.getClass().getDeclaredField("isLastCmd");
            isLastCmd= (Boolean) funNamespaceField.get(element);
        } catch (ReflectiveOperationException e) { }
        if(isLastCmd) {
            int i =McfDebugger.tagFunctionLeft.size()-1;
            if(i>=0){
                if(McfDebugger.tagFunctionLeft.get(i)<=1){
                    McfDebugger.stackList.remove(McfDebugger.stackList.size() - 1);
                    McfDebugger.tagFunctionLeft.remove(i);
                }else{
                    McfDebugger.tagFunctionLeft.set(i,McfDebugger.tagFunctionLeft.get(i)-1);
                }
            }
        }
        //origin
        if (tracer != null) {
            String string = this.parsed.getReader().getString();
            tracer.traceCommandStart(depth, string);
            int i = this.execute(manager, source);
            tracer.traceCommandEnd(depth, string, i);
        } else {
            this.execute(manager, source);
        }
        //origin

    }
}