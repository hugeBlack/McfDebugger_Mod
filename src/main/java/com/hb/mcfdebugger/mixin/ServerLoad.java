package com.hb.mcfdebugger.mixin;

import com.hb.mcfdebugger.McfDebugger;
import com.hb.mcfdebugger.config.LoadConfig;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.WorldSavePath;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ServerLoad {
    @Inject(method = "loadWorld", at = @At("HEAD"))
    private void serverLoaded(CallbackInfo ci)
    {
        McfDebugger.mcServer=(MinecraftServer)(Object)this;
        LoadConfig.load();
    }
}
