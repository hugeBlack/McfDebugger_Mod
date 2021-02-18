package com.hb.mcfdebugger.commands;

import com.hb.mcfdebugger.commandHelpers.GetEntity;
import com.hb.mcfdebugger.McfDebugger;
import com.hb.mcfdebugger.commandHelpers.ReadScoreboard;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.command.argument.*;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;

public class DebuggerCommand {
    public static final SimpleCommandExceptionType LOGGER_HIT_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("MCFDEBUGGER: Logger hit."));
    public static final SimpleCommandExceptionType CONSOLE_LOG_HIT_EXCEPTION = new SimpleCommandExceptionType(new TranslatableText("MCFDEBUGGER: Logger hit."));

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        LiteralArgumentBuilder<ServerCommandSource> literalArgumentBuilder = CommandManager.literal("debuggerCmd").
                requires(source -> source.hasPermissionLevel(2))
                .then(CommandManager.literal("mute").executes((CommandContext<ServerCommandSource> cmd) -> {
                    McfDebugger.nowCommandCount--;
                    return DebuggerCommand.mute(cmd);
                }))
                .then(CommandManager.literal("getScoreboard")
                        .then(CommandManager.literal("byEntity")
                                .then(CommandManager.argument("targets", ScoreHolderArgumentType.scoreHolder()).suggests(ScoreHolderArgumentType.SUGGESTION_PROVIDER)
                                        .executes((CommandContext<ServerCommandSource> cmd) -> {
                                                    McfDebugger.nowCommandCount--;
                                                    return DebuggerCommand.execute_getScoreByEntity(cmd);

                                                }
                                        )
                                )
                        )
                        .then(CommandManager.literal("byObjective")
                                .then(CommandManager.argument("objective", ObjectiveArgumentType.objective())
                                        .executes((CommandContext<ServerCommandSource> cmd) -> {
                                                    McfDebugger.nowCommandCount--;
                                                    return DebuggerCommand.execute_getScoreByObjective(cmd);
                                                }
                                        )
                                )
                        )
                )
                .then(CommandManager.literal("getEntity")
                        .then(CommandManager.argument("targets", EntityArgumentType.entities())
                                .executes((CommandContext<ServerCommandSource> cmd) -> {
                                            McfDebugger.nowCommandCount--;
                                            return DebuggerCommand.getEntity(cmd);
                                        }
                                )
                        )

                )
                .then(CommandManager.literal("loud")
                        .executes((CommandContext<ServerCommandSource> cmd) -> {
                                    McfDebugger.nowCommandCount--;
                                    return DebuggerCommand.loud(cmd);
                                }
                        )

                )
                .then(CommandManager.literal("log")
                        .executes((CommandContext<ServerCommandSource> cmd) -> {
                                    McfDebugger.nowCommandCount--;
                                    return DebuggerCommand.log(cmd);
                                }
                        )

                )
;
        dispatcher.register(literalArgumentBuilder);
    }

    public static int execute_getScoreByEntity(CommandContext<ServerCommandSource> cmd) throws CommandSyntaxException {
        if (!McfDebugger.debuggerMode.equals("none")) {
            ReadScoreboard.parseScoreboardByEntity(cmd);
            throw LOGGER_HIT_EXCEPTION.create();
        } else {
            return 1;
        }

    }

    public static int execute_getScoreByObjective(CommandContext<ServerCommandSource> cmd) throws CommandSyntaxException {
        if (!McfDebugger.debuggerMode.equals("none")) {
            ReadScoreboard.parseScoreboardByObjective(cmd);
            throw LOGGER_HIT_EXCEPTION.create();
        } else {
            return 1;
        }
    }

    public static int getEntity(CommandContext<ServerCommandSource> cmd) throws CommandSyntaxException {
        if (!McfDebugger.debuggerMode.equals("none")) {
            GetEntity.get(cmd);
            throw LOGGER_HIT_EXCEPTION.create();
        } else {
            return 1;
        }
    }

    public static int loud(CommandContext<ServerCommandSource> cmd) throws CommandSyntaxException {
        if (!McfDebugger.debuggerMode.equals("none")) {
            McfDebugger.nowLoudFunNamespace = McfDebugger.lastCmdObj.funNamespace;
            McfDebugger.nowLoudFunPath = McfDebugger.lastCmdObj.funPath;
            McfDebugger.nowLoudIndex = McfDebugger.lastCmdObj.cmdIndex;
        }
        return 1;
    }

    public static int log(CommandContext<ServerCommandSource> cmd) throws CommandSyntaxException {
        if (!McfDebugger.debuggerMode.equals("none")) {
            McfDebugger.nowLogFunNamespace = McfDebugger.lastCmdObj.funNamespace;
            McfDebugger.nowLogFunPath = McfDebugger.lastCmdObj.funPath;
            McfDebugger.nowLogIndex = McfDebugger.lastCmdObj.cmdIndex;
        }
        return 1;
    }

    public static int mute(CommandContext<ServerCommandSource> cmd) throws CommandSyntaxException {
        if (!McfDebugger.debuggerMode.equals("none")) {
            McfDebugger.nowMuteFunNamespace = McfDebugger.lastCmdObj.funNamespace;
            McfDebugger.nowMuteFunPath = McfDebugger.lastCmdObj.funPath;
            McfDebugger.nowMuteIndex = McfDebugger.lastCmdObj.cmdIndex;
        }
        return 1;
    }
}
