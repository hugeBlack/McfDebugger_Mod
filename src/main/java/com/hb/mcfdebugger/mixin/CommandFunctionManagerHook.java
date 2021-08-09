package com.hb.mcfdebugger.mixin;

import com.google.common.collect.Queues;
import com.hb.mcfdebugger.McfDebugger;
import com.google.common.collect.Lists;
import com.hb.mcfdebugger.mixinHelpers.SendFunctionState;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.server.function.FunctionLoader;
import net.minecraft.util.profiler.Profiler;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Objects;

/*@Mixin(CommandFunctionManager.Entry.class)
public abstract class CommandFunctionManagerHook {

    @Shadow
    boolean justLoaded;
    boolean executing = justLoaded;
    @Shadow
    @Final
    MinecraftServer server;
    @Shadow
    @Final
    ArrayDeque<CommandFunctionManager.Entry> chain = new ArrayDeque();
    @Shadow
    @Final
    List<CommandFunctionManager.Entry> pending = Lists.newArrayList();
    @Shadow
    FunctionLoader loader;

    @Shadow
    abstract int getMaxCommandChainLength();

    @Overwrite
    public int execute(CommandFunction function, ServerCommandSource source) {
        int i = this.getMaxCommandChainLength();
        if (this.executing) {
            if (this.chain.size() + this.pending.size() < i) {
                this.pending.add(new CommandFunctionManager.Entry((CommandFunctionManager) (Object) this, source, new CommandFunction.FunctionElement(function)));
            }
            return 0;
        } else {
            try {
                this.executing = true;
                int j = 0;
                CommandFunction.Element[] elements = function.getElements();

                int k;
                for (k = elements.length - 1; k >= 0; --k) {
                    this.chain.push(new CommandFunctionManager.Entry((CommandFunctionManager) (Object) this, source, elements[k]));
                }
                //modified
                SendFunctionState.send(this.loader, source, function);
                //modified
                while (!this.chain.isEmpty()) {
                    try {
                        CommandFunctionManager.Entry entry = (CommandFunctionManager.Entry) this.chain.removeFirst();
                        this.server.getProfiler().push(entry::toString);
                        //modified
                        McfDebugger.nowCommandCount = j;
                        McfDebugger.commandsLeftToMaxChainLength = i - j;
                        entry.execute(this.chain, i);
                        j = McfDebugger.nowCommandCount;
                        //modified
                        if (!this.pending.isEmpty()) {
                            List var10000 = Lists.reverse(this.pending);
                            ArrayDeque var10001 = this.chain;
                            var10000.forEach(var10001::addFirst);
                            this.pending.clear();
                        }
                    } finally {
                        this.server.getProfiler().pop();
                    }

                    ++j;
                    if (j >= i) {
                        k = j;
                        return k;
                    }
                }

                k = j;
                return k;
            } finally {
                this.chain.clear();
                this.pending.clear();
                this.executing = false;

            }
        }
    }
    @Shadow @Final ServerCommandSource source;
    @Shadow @Final int depth;
    @Shadow @Final CommandFunction.Element element;
    @Overwrite
    public void execute(CommandFunctionManager manager, Deque<CommandFunctionManager.Entry> entries, int maxChainLength, @Nullable CommandFunctionManager.Tracer tracer) {
        //modified
        SendFunctionState.send(this.loader);
        //modified
        try {
            CommandFunctionManager.Entry entry = (CommandFunctionManager.Entry) this.chain.removeFirst();
            this.server.getProfiler().push(entry::toString);
            //modified
            McfDebugger.nowCommandCount = CommandFunctionManager.;
            McfDebugger.commandsLeftToMaxChainLength = maxChainLength - j;
            this.element.execute(manager, this.source, entries, maxChainLength, this.depth, tracer);
        } catch (CommandSyntaxException var6) {
            if (tracer != null) {
                tracer.traceError(this.depth, var6.getRawMessage().getString());
            }
        } catch (Exception var7) {
            if (tracer != null) {
                tracer.traceError(this.depth, var7.getMessage());
            }
        }

    }


}
*/