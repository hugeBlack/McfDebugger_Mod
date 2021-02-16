# Minecraft函数调试器
![McFD图标](https://sm.ms/image/G9tM2LjZ48Wsvow) 
##简介
你还在为写函数的时候哪个指令没有执行而不知所措吗？<br>
你还在为调试而各种/say吗<br>
你还在为不知道哪个实体执行了function而一头雾水吗（跑
<br><br>
Minecraft Function Debugger (Mod)，简称 McFD，中文名「函数调试器(模组部分)」是一个能够为 Minecraft Java版的函数提供调试支持的Mod，你可以使用它像调试其他语言一样调试mcfunction
<br>
虽然您可以在有Websocket知识，Json读写能力的基础上仅使用本模组实现调试功能<br>
我还是建议您 :请务必配套使用 [对应的Vscode插件](https://github.com/hugeBlack/McfDebugger_Extension) 来获得最佳体验

## 修改

* 对游戏性没有任何修改，主要对函数有关的方法进行了Overwrite，虽然已经尽可能地进行最少的修改，但可能对游戏的行为产生影响。
* 本模组通过Websocket与Vscode插件通信

##指令
配置指令: /mcfDebuggerConfig 需要4级权限
* /mcfDebuggerConfig port <端口：整数> 立即修改端口并重启内置的Websocket服务器，默认1453
* /mcfDebuggerConfig enable <true/false> 立即启用/禁用内置的Websocket服务器，默认true
* /mcfDebuggerConfig timeOut <秒：整数> 设置连接超时的时间，不宜太长，默认5
* 每个世界不共享配置文件

调试器指令：/debuggerCmd
* 用于实现调试器功能，没有明显效果，不会消耗MaxChainLength

##使用
具体使用方式请移步 [Vscode插件](https://github.com/hugeBlack/McfDebugger_Extension) 部分：
