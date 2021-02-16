package com.hb.mcfdebugger.mixinHelpers;

import net.minecraft.server.function.CommandFunction;

public abstract class HackedElement implements CommandFunction.Element {
        public String funNamespace;
        public String funPath;
        public int cmdIndex;
        public boolean isLastCmd;

}
