package com.hb.mcfdebugger.mixin;

import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ServerCommandSource.class)
public class CommandSourceHook {
    @Shadow @Final int level;
    public int fakeGetLevel(){
        return this.level;
    }
}
