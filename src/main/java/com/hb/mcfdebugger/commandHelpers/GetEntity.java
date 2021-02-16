package com.hb.mcfdebugger.commandHelpers;

import com.hb.mcfdebugger.DebugThread;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.entity.Entity;
import net.minecraft.server.command.ServerCommandSource;

import java.util.*;

public class GetEntity {
    public static void get(CommandContext<ServerCommandSource> cmd) throws CommandSyntaxException{
        List<SendEntityObj> entityList = new LinkedList<>();
        Iterator var2 =EntityArgumentType.getOptionalEntities(cmd,"targets").iterator();
        while(var2.hasNext()){
            Entity entity =(Entity)var2.next();
            SendEntityObj sendEntityObj = new SendEntityObj();
            sendEntityObj.name=entity.getEntityName();
            sendEntityObj.uuid=entity.getUuidAsString();
            sendEntityObj.type=entity.getType().toString();
            sendEntityObj.detail.put("tags",entity.getScoreboardTags());
            sendEntityObj.detail.put("pos",entity.getPos().toString());
            sendEntityObj.detail.put("rotation","("+entity.getRotationClient().y+", "+entity.getRotationClient().x+")");
            entityList.add(sendEntityObj);
        }
        DebugThread.sendObjMsgToDebugger(entityList,"getEntityResult");
    }
    public static class SendEntityObj{
        public String name;
        public String uuid;
        public String type;
        public Map<String,Object> detail =new LinkedHashMap<>();
    }

}
