package com.hb.mcfdebugger.config;
import com.google.gson.Gson;

import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.hb.mcfdebugger.McfDebugger.LOG;

final public class ConfigManager {
    private static ConfigManager cm;
    private File configFile;
    private Gson gson;
    private LinkedHashMap<String, Integer> configList;

    public static ConfigManager getConfigManager(){
        if (cm == null){
            cm = new ConfigManager();
        }
        return cm;
    }

    private ConfigManager(){
        initConfigList();
        gson = new Gson();
    }

    private void initConfigList(){
        configList = setupRulesMap();
    }

    public LinkedHashMap<String, Integer> setupRulesMap(){
        LinkedHashMap<String, Integer> m = new LinkedHashMap<>();
        m.put("port", 1453);
        m.put("timeOut", 5);
        m.put("enable", 1);
        return m;
    }

    public void passConfigFile(File f){
        this.configFile = f;
    }

    public void setupConfig(){
        loadConfig();
        LOG("Config Loaded");
    }

    public LinkedHashMap<String, Integer> loadConfig(){
        if (this.configFile.exists()){
            try (FileReader reader = new FileReader(this.configFile)){
                Iterator m =gson.fromJson(reader,new LinkedHashMap<String,Double>().getClass()).entrySet().iterator();
                while(m.hasNext()){
                    Map.Entry entry = (Map.Entry)m.next();
                    this.configList.put(entry.getKey().toString(),((Double)entry.getValue()).intValue());
                }
            } catch (IOException e) {
                LOG("Failed to parse config");
                throw new RuntimeException("Could not parse config", e);
            }
        } else {
            initConfigList();
            writeConfig();
        }

        return configList;
    }

    public void writeConfig(){
        File dir = configFile.getParentFile();

        if (!dir.exists()) {
            if (!dir.mkdirs()) {
                LOG("Failed to create the parent directory");
                throw new RuntimeException("Failed to create the parent directory");
            }
        } else if (!dir.isDirectory()) {
            LOG("Failed to create config file");
            throw new RuntimeException("The parent is not a directory");
        }

        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(configList, writer);
        } catch (IOException e) {
            LOG("Failed to save config");
            throw new RuntimeException("Could not save config file", e);
        }
    }



}
