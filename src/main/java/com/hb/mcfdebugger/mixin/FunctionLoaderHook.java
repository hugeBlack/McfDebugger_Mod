package com.hb.mcfdebugger.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.Entity;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceReloader;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.FunctionLoader;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.Tag;
import net.minecraft.tag.TagGroup;
import net.minecraft.tag.TagGroupLoader;
import net.minecraft.text.LiteralText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.profiler.Profiler;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import com.hb.mcfdebugger.mixinHelpers.FakeCommandFunctionCreate;

@Mixin(FunctionLoader.class)
public abstract class FunctionLoaderHook implements ResourceReloader {
    @Shadow @Final TagGroupLoader<CommandFunction> tagLoader;
    @Shadow abstract Optional<CommandFunction> get(Identifier id);
    private static final int PATH_PREFIX_LENGTH = "functions/".length();
    private static final int PATH_SUFFIX_LENGTH = ".mcfunction".length();
    @Shadow @Final CommandDispatcher<ServerCommandSource> commandDispatcher;
    private static final Logger LOGGER= LogManager.getLogger();
    @Shadow volatile Map<Identifier, CommandFunction> functions;
    @Shadow volatile TagGroup<CommandFunction> tags;
    @Shadow @Final int level;

    @Overwrite
    public CompletableFuture<Void> reload(ResourceReloader.Synchronizer synchronizer, ResourceManager manager, Profiler prepareProfiler, Profiler applyProfiler, Executor prepareExecutor, Executor applyExecutor) {
        CompletableFuture<Map<Identifier, Tag.Builder>> completableFuture = CompletableFuture.supplyAsync(() -> {
            return this.tagLoader.loadTags(manager);
        }, prepareExecutor);
        CompletableFuture<Map<Identifier, CompletableFuture<CommandFunction>>> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            return manager.findResources("functions", (string) -> {
                return string.endsWith(".mcfunction");
            });
        }, prepareExecutor).thenCompose((collection) -> {
            Map<Identifier, CompletableFuture<CommandFunction>> map = Maps.newHashMap();
            ServerCommandSource serverCommandSource = new ServerCommandSource(CommandOutput.DUMMY, Vec3d.ZERO, Vec2f.ZERO, (ServerWorld)null, this.level, "", LiteralText.EMPTY, (MinecraftServer)null, (Entity)null);
            Iterator var6 = collection.iterator();

            while(var6.hasNext()) {
                Identifier identifier = (Identifier)var6.next();
                String string = identifier.getPath();
                Identifier identifier2 = new Identifier(identifier.getNamespace(), string.substring(PATH_PREFIX_LENGTH, string.length() - PATH_SUFFIX_LENGTH));
                map.put(identifier2, CompletableFuture.supplyAsync(() -> {
                    List<String> list = readLines(manager, identifier);
                    //modified
                    return FakeCommandFunctionCreate.create(identifier2, this.commandDispatcher, serverCommandSource, list);
                }, prepareExecutor));
            }

            CompletableFuture<?>[] completableFutures = (CompletableFuture[])map.values().toArray(new CompletableFuture[0]);
            return CompletableFuture.allOf(completableFutures).handle((void_, throwable) -> {
                return map;
            });
        });
        CompletableFuture var10000 = completableFuture.thenCombine(completableFuture2, Pair::of);
        synchronizer.getClass();
        return var10000.thenCompose(synchronizer::whenPrepared).thenAcceptAsync((pair) -> {
            Map<Identifier, CompletableFuture<CommandFunction>> map = (Map)((Pair)pair).getSecond();
            ImmutableMap.Builder<Identifier, CommandFunction> builder = ImmutableMap.builder();
            map.forEach((identifier, completableFutureA) -> {
                completableFutureA.handle((commandFunction, throwable) -> {
                    if (throwable != null) {
                        LOGGER.error("Failed to load function {}", identifier, throwable);
                    } else {
                        builder.put(identifier, commandFunction);
                    }

                    return null;
                }).join();
            });
            this.functions = builder.build();
            this.tags = this.tagLoader.buildGroup((Map)((Pair)pair).getFirst());
        }, applyExecutor);
    }
    private static List<String> readLines(ResourceManager resourceManager, Identifier id) {
        try {
            Resource resource = resourceManager.getResource(id);
            Throwable var3 = null;

            List var4;
            try {
                var4 = IOUtils.readLines(resource.getInputStream(), StandardCharsets.UTF_8);
            } catch (Throwable var14) {
                var3 = var14;
                throw var14;
            } finally {
                if (resource != null) {
                    if (var3 != null) {
                        try {
                            resource.close();
                        } catch (Throwable var13) {
                            var3.addSuppressed(var13);
                        }
                    } else {
                        resource.close();
                    }
                }

            }

            return var4;
        } catch (IOException var16) {
            throw new CompletionException(var16);
        }
    }
}
