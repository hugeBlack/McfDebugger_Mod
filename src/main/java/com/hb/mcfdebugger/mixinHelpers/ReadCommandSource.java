package com.hb.mcfdebugger.mixinHelpers;

import com.hb.mcfdebugger.McfDebugger;
import net.minecraft.server.command.ServerCommandSource;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public class ReadCommandSource {
    public static Map<String,String> read(ServerCommandSource source){
        Map<String,String> sourceMap =new LinkedHashMap<>();
        if(source.getEntity()!=null){
            sourceMap.put("entityName",source.getEntity().getEntityName());
            sourceMap.put("entityUuid",source.getEntity().getUuidAsString());
        }else{
            sourceMap.put("entityName","None(Server)");
            sourceMap.put("entityUuid","None");
        }
        sourceMap.put("pos" ,source.getPosition().toString());
        sourceMap.put("rotation","("+source.getRotation().y+", "+source.getRotation().x+")");
        sourceMap.put("world",source.getWorld().getRegistryKey().getValue().toString());
        try {
            Method levelMethod = source.getClass().getDeclaredMethod("fakeGetLevel");
            sourceMap.put("level", String.valueOf(levelMethod.invoke(source)));
        } catch (ReflectiveOperationException e) {
            sourceMap.put("level","Error");
        }
        sourceMap.put("commandsLeftToMaxChainLength", String.valueOf(McfDebugger.commandsLeftToMaxChainLength));
        return sourceMap;
    }
}
