package com.hb.mcfdebugger;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.hb.mcfdebugger.commandHelpers.ReadScoreboard;
import net.minecraft.entity.Entity;
import net.minecraft.network.MessageType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.CommandBlockExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class wsCommandParser {
    public static void sendMsg(String Msg){
        McfDebugger.mcServer.getPlayerManager().broadcastChatMessage(new TranslatableText(Msg), MessageType.SYSTEM, Util.NIL_UUID);

    }
    public static boolean isInPauseList(String funNamespace, String funPath, Integer cmdIndex){
        if(wsCommandParser.pauseList==null){
            return false;
        }
        for(LinkedTreeMap<String,Object> cmd:wsCommandParser.pauseList){
            if(((Double)cmd.get("cmdIndex")).intValue()== cmdIndex && cmd.get("funNamespace").equals(funNamespace) && cmd.get("funPath").equals(funPath)){
                return true;
            }
        }
        return false;
    }
    public static List<LinkedTreeMap<String,Object>> pauseList;
    public class command{
        public String funNamespace;
        public String funPath;
        public int cmdIndex;
    }

    public static boolean inModeList(String mode){
        List<String> availableModes= Arrays.asList("byStep","none","normalDebug");
        for(String nowMode:availableModes){
            if(nowMode.equals(mode)){return true;}
        }
        return false;
    }
    public static class MsgObj{
        public String msg;
        public String type;
        public MsgObj(String msg,String msgType){
            this.msg=msg;
            this.type=msgType;
        }
    }
    public static void SendFeedBack(String msg,String msgType){
        MsgObj a= new MsgObj(msg,msgType);
        DebugThread.sendObjMsgToDebugger(a,"cmdFeedback");
    }
    public static void DebugHandler(String wsMessage){

        Gson gson=new Gson();
        Map<String,Object> commandObj = gson.fromJson(wsMessage,Map.class);
        String command="";
        try{
            command=commandObj.get("command").toString();
        }catch(Error e){
            SendFeedBack("Unknown command.","Error");
            return;
        }
        switch (command){
            case "next":
                DebugThread.nextStep=true;
                SendFeedBack("Function proceeded.","Success");
                break;
            case "setMode":
                String mode="";
                try{
                    mode=commandObj.get("mode").toString();
                }catch(Error e){
                    SendFeedBack("Unknown mode.","Error");
                    break;
                }
                if(inModeList(mode)){
                    McfDebugger.debuggerMode=mode;
                    SendFeedBack("Mode switched.","Success");
                }else{
                    SendFeedBack("Unknown mode.","Error");
                }
                break;
            case "setPauseList":
                try{
                    pauseList=(List<LinkedTreeMap<String,Object>>) commandObj.get("pauseList");
                    SendFeedBack("pauseList set.","Success");
                }catch (Error e){
                    SendFeedBack("pauseList error.","Error");
                }
                break;
            case "getScoreboard":
                try{
                    ReadScoreboard.parseAllScoreboard();
                }catch(Error e){
                    SendFeedBack("scoreboard error.","Error");
                }

                break;
            case "reload":
                CommandBlockExecutor executor = new CommandBlockExecutor() {
                    @Override
                    public ServerWorld getWorld() {
                        return McfDebugger.mcServer.getOverworld();
                    }

                    @Override
                    public void markDirty() {

                    }

                    @Override
                    public Vec3d getPos() {
                        return null;
                    }

                    @Override
                    public ServerCommandSource getSource() {
                        return new ServerCommandSource(this, new Vec3d(0,0,0), Vec2f.ZERO, this.getWorld(), 2, this.getCustomName().getString(), this.getCustomName(), this.getWorld().getServer(), (Entity)null);
                    }
                };
                executor.setCommand("reload");
                executor.execute(McfDebugger.mcServer.getOverworld());
                wsCommandParser.sendMsg("mcfdebugger.reload_command");
                SendFeedBack("reloaded.","Success");
                break;
            case "getVersion":
                DebugThread.sendObjMsgToDebugger("3","versionResult");
            default:
                SendFeedBack("Unknown command.","Error");
        }
    }
}
