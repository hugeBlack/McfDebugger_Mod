package com.hb.mcfdebugger.mixin;

import com.hb.mcfdebugger.McfDebugger;
import com.google.common.collect.Lists;
import com.hb.mcfdebugger.mixinHelpers.SendFunctionState;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.CommandFunctionManager;
import net.minecraft.server.function.FunctionLoader;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.ArrayDeque;
import java.util.List;

@Mixin(CommandFunctionManager.class)
public abstract class CommandFunctionManagerHook {

    @Shadow
    boolean executing;
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
    FunctionLoader field_25333;

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
                SendFunctionState.send(this.field_25333, source, function);
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

}
