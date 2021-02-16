package com.hb.mcfdebugger.mixin;
import com.hb.mcfdebugger.commands.*;
import com.hb.mcfdebugger.commands.DebuggerCommand;
import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.server.command.TestCommand;

@Mixin(CommandManager.class)
public abstract class regcmd
{

	@Shadow @Final private CommandDispatcher<ServerCommandSource> dispatcher;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void onRegister(CommandManager.RegistrationEnvironment arg, CallbackInfo ci) {
		DebuggerConfigCommand.register(this.dispatcher);
		DebuggerCommand.register(this.dispatcher);
		TestCommand.register(this.dispatcher);

	}


}
