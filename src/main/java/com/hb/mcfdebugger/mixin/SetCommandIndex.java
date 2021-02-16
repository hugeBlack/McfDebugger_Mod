package com.hb.mcfdebugger.mixin;

import com.hb.mcfdebugger.McfDebugger;
import net.minecraft.server.function.CommandFunction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandFunction.CommandElement.class)
public abstract class SetCommandIndex {
    public String funNamespace= McfDebugger.nowFunNamespace;
    public String funPath= McfDebugger.nowFunPath;
    public int cmdIndex= McfDebugger.nowIndex;
    public boolean isLastCmd=McfDebugger.nowIsLastCmd;
}
