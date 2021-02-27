# Minecraft函数调试器

![McFD图标](https://i.loli.net/2021/02/17/3lkRqAjT5hNGorJ.png)

语言：  
[English](https://github.com/hugeBlack/McfDebugger_Mod/blob/master/README.md) \ 简体中文

*(如果你只是想下载模组，请移步右边的[Release](https://github.com/hugeBlack/McfDebugger_Mod/releases)板块)*

## 简介

你还在为写函数的时候哪个指令没有执行而不知所措吗？  
你还在为调试而各种/say吗  
你还在为不知道哪个实体执行了function而一头雾水吗（跑

Minecraft Function Debugger (Mod)，简称 McFD，中文名「函数调试器(模组部分)」是一个能够为 Minecraft Java版的函数提供调试支持的Mod，你可以使用它像调试其他语言一样调试mcfunction

虽然您可以在有Websocket知识，Json读写能力的基础上仅使用本模组实现调试功能

我还是建议您务必配套使用 [对应的Vscode插件](https://github.com/hugeBlack/McfDebugger_Extension) 来获得最佳体验

## 修改

* 对游戏性没有任何修改，主要对函数有关的方法进行了Overwrite，虽然已经尽可能地进行最少的修改，但可能对游戏的行为产生影响。
* 本模组通过Websocket与Vscode插件通信

## 指令

配置指令: /mcfDebuggerConfig 需要4级权限

* /mcfDebuggerConfig port <端口：整数> 立即修改端口并重启内置的Websocket服务器，默认1453
* /mcfDebuggerConfig enable <true/false> 立即启用/禁用内置的Websocket服务器，默认true
* /mcfDebuggerConfig timeOut <秒：整数> 设置连接超时的时间，不宜太长，默认5
* 每个世界不共享配置文件

调试器指令：/debuggerCmd

* 用于实现调试器功能，没有明显效果，不会消耗MaxChainLength

## 使用

具体使用方式请移步 [Vscode插件](https://github.com/hugeBlack/McfDebugger_Extension) 部分.

## 免责声明

VSCode插件部分只会读取你的数据包，因此你的数据包是安全的。然而，在某些极端情况下，模组部分可能会因为某些原因损坏你的存档，尽管经经过了些许测试，但是这种情况仍有可能发生。  
并且，由于使用Webocket通信，你的设备可能被恶意入侵。  
遗憾的是，我们并不能在这些情况中提供任何帮助。
所以请请随时备份您宝贵的存档并请自行配置防火墙来保证您的设备的安全。

## Q&A

1. Q:为什么使用Fabric而不是Forge之类的?  
   A:因为Fabric光速支持快照等版本而FML更新慢。
2. Q:为什么游戏关闭不了？  
   A:检查一下你的调试器VSCode部分是不是正在暂停状态，否则超时时间一过就会自动关闭，**切记不要去杀进程，可能会导致存档损坏**
3. Q:为什么提示文字显示不正常？
   A:请安装[Fabric-Api](https://www.curseforge.com/minecraft/mc-mods/fabric-api)!
