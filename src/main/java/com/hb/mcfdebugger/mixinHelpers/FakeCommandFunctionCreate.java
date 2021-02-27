package com.hb.mcfdebugger.mixinHelpers;

import com.google.common.collect.Lists;
import com.hb.mcfdebugger.McfDebugger;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.util.Identifier;

import java.util.List;

public class FakeCommandFunctionCreate {
    public static CommandFunction create(Identifier id, CommandDispatcher<ServerCommandSource> commandDispatcher, ServerCommandSource serverCommandSource, List<String> list) {
        List<CommandFunction.Element> list2 = Lists.newArrayListWithCapacity(list.size());

        for(int i = 0; i < list.size(); ++i) {
            int j = i + 1;
            String string = ((String)list.get(i)).trim();
            StringReader stringReader = new StringReader(string);
            if (stringReader.canRead() && (stringReader.peek() != '#'|| string.substring(0,2).equals("#@"))) {
                if (stringReader.peek() == '/') {
                    stringReader.skip();
                    if (stringReader.peek() == '/') {
                        IllegalArgumentException e= new IllegalArgumentException("Unknown or invalid command '" + string + "' on line " + j + " (if you intended to make a comment, use '#' not '//')");
                        SendSyntaxError.send(e);
                        throw e;
                    }

                    String string2 = stringReader.readUnquotedString();
                    IllegalArgumentException e = new IllegalArgumentException("Unknown or invalid command '" + string + "' on line " + j + " (did you mean '" + string2 + "'? Do not use a preceding forwards slash.)");
                    SendSyntaxError.send(e);
                    throw e;
                }

                try {
                    //modified
                    ParseResults<ServerCommandSource> parseResults;

                    if(!string.substring(0,2).equals("#@")){
                        parseResults = commandDispatcher.parse(stringReader, serverCommandSource);
                    }else{
                        parseResults = commandDispatcher.parse(new StringReader("debuggerCmd "+string.substring(2)), serverCommandSource);
                    }                    if (parseResults.getReader().canRead()) {
                        throw CommandManager.getException(parseResults);
                    }
                    //modified
                    McfDebugger.nowFunNamespace=id.getNamespace();
                    McfDebugger.nowFunPath=id.getPath();
                    McfDebugger.nowIndex=i;
                    McfDebugger.nowIsLastCmd=(i == list.size()-1);
                    list2.add(new CommandFunction.CommandElement(parseResults));
                } catch (CommandSyntaxException var10) {
                    IllegalArgumentException e = new IllegalArgumentException("Whilst parsing command on line " + j + ": " + var10.getMessage());
                    SendSyntaxError.send(e);
                    throw e;
                }
            }
        }
        return new CommandFunction(id, (CommandFunction.Element[])list2.toArray(new CommandFunction.Element[0]));
    }
}
