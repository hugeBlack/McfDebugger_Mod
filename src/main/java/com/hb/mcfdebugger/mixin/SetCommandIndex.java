package com.hb.mcfdebugger.mixin;

import com.hb.mcfdebugger.McfDebugger;
import net.minecraft.server.function.CommandFunction;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CommandFunction.CommandElement.class)
public abstract class SetCommandIndex {
    public String funNamespace= McfDebugger.nowCmd.funNamespace;
    public String funPath= McfDebugger.nowCmd.funPath;
    public int cmdIndex= McfDebugger.nowCmd.cmdIndex;
    public boolean isLastCmd=McfDebugger.nowIsLastCmd;
}
