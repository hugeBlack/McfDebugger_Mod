# Minecraft Function Debugger

![McFD Banner](https://i.loli.net/2021/02/17/3lkRqAjT5hNGorJ.png)

Languages：  
English \ [简体中文](https://github.com/hugeBlack/McfDebugger_Mod/blob/master/README_zh_cn.md)

*( If you only want to download this mod, please see the [Release](https://github.com/hugeBlack/McfDebugger_Mod/releases) section on the right )*

## Intoduction

Minecraft Function Debugger (Mod Part), McFD dor short, , McFD for short, is a vscode extension that provides support for debugging Minecraft functions. You can use it to debug mcfunctions like debugging other languages.

Although you can use this mod only with sufficient knowledge of Websocket and Json, I still suggest that you use the [corresponding VSCode extension](https://github.com/hugeBlack/McfDebugger_Extension) to get full experience.

## Modification

* It made no change to gaming features. However, it overwrites some of the methods of Function system. Although we tried our best to make as minior changes as possible, it may change the behaviour of the game in some aspect.
* This mod uses Websocket to communicate with the VSCode extension.

## Commands

### Configuration command  

/mcfDebuggerConfig  

* /mcfDebuggerConfig port <port：int>  
  Change the port of Websocket Server inside and restart it (if enabled). 1453 by default.
* /mcfDebuggerConfig enable <true/false>  
  Enable/disable the Websocket Server. True by default.
* /mcfDebuggerConfig timeOut <second: int>  
  Set the time out. It should not be too large. 5 by default.
* Configurations are not shared among worlds.
* Require permission level 4.

### Debugger Command：/debuggerCmd

* It is used by the debugger to implement certain features. It has no output and don't cost MaxChainLength.
* Require permission level 2.

## Usage

Please see the [Vscode Extension](https://github.com/hugeBlack/McfDebugger_Extension) part for usage.

## Disclaimer

The VSCode Extension part reads your datapack only, so your datapack is safe definitely. However, the mod part may damage your save. Despite some tests, it could happen under extreme circumstances.

Also, since it communicate with the game with Websocket your device may be attacked by ill-intentioned people.  

 Unfortunately, we can't provide help in these cases. So I strongly recommend that you **should backup** your awesome world. AndP lease config your Firewall to defend your devide from potential attacks.

## Q&A

1. Q: Why using Fabric instead of Forge?  
   A:Because Fabric supports Snapshots quickly while Forge don't.
2. Q: Why I can't stop my game?  
   A:Check if the game is paused by a exception in VSCode. If it isn't, wait for the timeout and the game will quit automatically (that's why you should set a short timeout!). **NEVER shut down the game in that state with task manager or other means** because it may damage your save!
3. ...
