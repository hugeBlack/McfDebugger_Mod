package com.hb.mcfdebugger.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerCommandSource.class)
public class CommandSourceHook {
    @Shadow @Final int level;
    public int fakeLevel;
    @Inject(method="getEntity" ,at=@At("HEAD"))
    public void fakeGetEntity(CallbackInfoReturnable ci) {
        this.fakeLevel=level;
        System.out.println(1234);
    }
}
