package com.hb.mcfdebugger;

import com.hb.mcfdebugger.config.ConfigHolder;
import com.hb.mcfdebugger.config.ConfigManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.command.CommandSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.function.FunctionLoader;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;

import java.util.*;

public class McfDebugger implements ModInitializer {
	public static boolean isThisModDebugging=false;

	public static class HbCmdObj{
		public String funName;
		public List<String> commands;
		public HbCmdObj() {
			this.funName="";
			this.commands=new LinkedList<>();
		}
	}

	public static SimpleCmdObj nowCmd = new SimpleCmdObj();
	public static boolean nowIsLastCmd=false;

	public static SendCmdObj lastCmdObj;
	public static MinecraftServer mcServer;

	public static int nowCommandCount=0;
	public static int commandsLeftToMaxChainLength=0;

	public static SimpleCmdObj nowMuteCmd = new SimpleCmdObj();
	public static SimpleCmdObj nowLoudCmd = new SimpleCmdObj();
	public static SimpleCmdObj nowLogCmd = new SimpleCmdObj();

	public static List<hbPair> stackList= new LinkedList<>();
	public static List<Integer> tagFunctionLeft= new LinkedList<>();
	public static SendCmdObj lastCallCmdObj=null;
	public static class hbPair{
		public String key;
		public Object value;
		public hbPair(String key,Object value){
			this.key=key;
			this.value=value;
		}
	}

	public static ConfigManager configManager=ConfigManager.getConfigManager();
	public static LinkedHashMap<String, Integer> configList=new LinkedHashMap<>();

	public static DebugThread thread = new DebugThread();

	@Override
	public void onInitialize() {
		LOG("Hello Fabric world!");
		thread.start();

	}
	public static void LOG(Object msgObj){
		if(ConfigHolder.isThisModDebugging){
			System.out.println(msgObj.toString());
		}
	}

}
