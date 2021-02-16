package com.hb.mcfdebugger.config;

import com.hb.mcfdebugger.McfDebugger;
import net.minecraft.util.WorldSavePath;

public class LoadConfig {
    public static void load(){
        McfDebugger.configManager=ConfigManager.getConfigManager();
        McfDebugger.configManager.passConfigFile(McfDebugger.mcServer.getSavePath(WorldSavePath.ROOT).resolve("mcfDebugger_config.json").toFile());
        McfDebugger.configManager.setupConfig();
        McfDebugger.configList=McfDebugger.configManager.loadConfig();
        if(McfDebugger.configList.get("enable")==1){
            McfDebugger.thread.restartWsServer(McfDebugger.configList.get("port"),McfDebugger.configList.get("timeOut"));
        }else{
            McfDebugger.thread.stopWsServer();
        }
    }
}
