package com.hb.mcfdebugger.commands;

import com.hb.mcfdebugger.DebugThread;
import com.hb.mcfdebugger.McfDebugger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;

import static com.mojang.brigadier.arguments.IntegerArgumentType.*;

public class DebuggerConfigCommand {
    public static final SimpleCommandExceptionType INVALID_ARGUMENT = new SimpleCommandExceptionType(new TranslatableText("mcfdebugger.invalidArgument"));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("mcfDebuggerConfig").
                requires(source -> source.hasPermissionLevel(4))
                .then(CommandManager.literal("enable")
                        .then(CommandManager.argument("enable?", BoolArgumentType.bool())
                                .executes((CommandContext<ServerCommandSource> cmd) -> {
                                            boolean isEnable = BoolArgumentType.getBool(cmd, "enable?");
                                            McfDebugger.configList.put("enable", isEnable ? 1 : 0);
                                            McfDebugger.configManager.writeConfig();
                                            if (isEnable) {
                                                cmd.getSource().sendFeedback(new TranslatableText("mcfdebugger.enabled"), true);
                                                McfDebugger.thread.restartWsServer(McfDebugger.configList.get("port"), McfDebugger.configList.get("timeOut"));
                                            } else {
                                                cmd.getSource().sendFeedback(new TranslatableText("mcfdebugger.disabled"), true);
                                                McfDebugger.thread.stopWsServer();

                                            }
                                            return 1;
                                        }
                                )
                        )
                )
                .then(CommandManager.literal("port")
                        .then(CommandManager.argument("port", integer())
                                .executes(cmd -> {
                                            if (IntegerArgumentType.getInteger(cmd, "port") < 65536 && IntegerArgumentType.getInteger(cmd, "port") > 0) {
                                                McfDebugger.configList.put("port", IntegerArgumentType.getInteger(cmd, "port"));
                                                McfDebugger.configManager.writeConfig();
                                                cmd.getSource().sendFeedback(new TranslatableText("mcfdebugger.set_port", IntegerArgumentType.getInteger(cmd, "port")), true);
                                                McfDebugger.thread.restartWsServer(McfDebugger.configList.get("port"), McfDebugger.configList.get("timeOut"));
                                                return 1;
                                            }
                                            throw INVALID_ARGUMENT.create();
                                        }
                                )
                        )
                )
                .then(CommandManager.literal("timeOut")
                        .then(CommandManager.argument("timeOut", integer())
                                .executes(cmd -> {
                                            if (IntegerArgumentType.getInteger(cmd, "timeOut") > 0) {
                                                McfDebugger.configList.put("enable", IntegerArgumentType.getInteger(cmd, "timeOut"));
                                                McfDebugger.configManager.writeConfig();
                                                cmd.getSource().sendFeedback(new TranslatableText("mcfdebugger.set_time_out", IntegerArgumentType.getInteger(cmd, "timeOut")), true);
                                                DebugThread.wsserver.setConnectionLostTimeout(IntegerArgumentType.getInteger(cmd, "timeOut"));
                                                return 1;
                                            }
                                            throw INVALID_ARGUMENT.create();
                                        }
                                )
                        )
                );
        dispatcher.register(literalArgumentBuilder);
    }

    public static int getNum(int num) {
        return num;
    }

}
